package cz.zdrubecky.zoopraha.section.quiz;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cz.zdrubecky.zoopraha.R;
import cz.zdrubecky.zoopraha.api.ImageLoader;
import cz.zdrubecky.zoopraha.manager.QuestionManager;
import cz.zdrubecky.zoopraha.model.Question;
import cz.zdrubecky.zoopraha.section.lexicon.AnimalDetailFragment;

public class QuestionFragment extends Fragment {
    private static final String TAG = "QuestionFragment";
    private static final String ARG_QUESTION_POSITION = "question_position";
    private static final String ANIMAL_DETAIL_FRAGMENT = "AnimalDetailFragment";
    private static final String KEY_TIME_TO_FINISH = "time_to_finish";

    private ProgressBar mTimerProgressBar;
    private View mView;

    private QuestionManager mQuestionManager;
    private int mQuestionPosition;
    private Question mQuestion;
    private Callbacks mCallbacks;
    private QuestionCountDownTimer mQuestionCountDownTimer;
    private int mTimeToFinish;

    public static QuestionFragment newInstance(int questionPosition) {
        Bundle args = new Bundle();
        args.putInt(ARG_QUESTION_POSITION, questionPosition);

        QuestionFragment fragment = new QuestionFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public interface Callbacks {
        // Through this method is the parent activity notified when the question has been answered
        void onQuestionAnswered(Question question);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;
        if (context instanceof Activity) {
            a = (Activity) context;

            // Make sure the parent activity implements the interface
            mCallbacks = (QuestionFragment.Callbacks) a;
        } else {
            Log.d(TAG, "The attaching context is not an activity.");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mQuestionManager = QuestionManager.get(getActivity());

        mQuestionPosition = getArguments().getInt(ARG_QUESTION_POSITION);

        mQuestion = mQuestionManager.getQuestion(mQuestionPosition);

        // Check whether the fragment is being recreated after a config change and set its state accordingly
        if (savedInstanceState != null) {
            mTimeToFinish = savedInstanceState.getInt(KEY_TIME_TO_FINISH);
        } else {
            mTimeToFinish = QuizPreferences.getQuestionTime(getActivity());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question, container, false);
        // Remember the content view so it can be modified from the class's inner methods
        mView = v;

        // Fill the top part of the question view
        TextView questionNumberTextView = (TextView) v.findViewById(R.id.fragment_question_number_textview);
        String questionNumberText = getString(R.string.fragment_question_number_textview_text, mQuestionPosition + 1);
        questionNumberTextView.setText(questionNumberText);

        updateScoreText();

        renderQuestion();

        // Check if the question was already answered and if so, draw the result instead of asking the user to answer again
        if (mQuestion.isAnswered()) {
            replaceAnswers(mQuestion.isAnsweredCorrectly() ? getString(R.string.question_answered_result_correct_textview_text) : getString(R.string.question_answered_result_incorrect_textview_text, mQuestion.getCorrectAnswer()));
        } else {
            renderAnswers();
        }

        return v;
    }

    // A separate method for rendering the progress bar, the question and answers
    private void renderQuestion() {
        mTimerProgressBar = (ProgressBar) mView.findViewById(R.id.fragment_question_timer_progressbar);
        // Set the layout listener to wait for the layout pass so that the timer doesn't start before the view is completely rendered
        mTimerProgressBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Set the default values for the progress bar
                mTimerProgressBar.setMax(QuizPreferences.getQuestionTime(getActivity()));
                mTimerProgressBar.setProgress(mTimeToFinish);

                // The View is now visible, start the timer (set the interval at half a second to be able to display the last tick)
                // ...also, set the time one second above the actual number to seem more fluent
                long mTimeToFinishMillis = (mTimeToFinish + 1) * 1000;

                mQuestionCountDownTimer = new QuestionCountDownTimer(mTimeToFinishMillis, 500);
                mQuestionCountDownTimer.start();

                // Remove the listener, it's no longer needed
                mTimerProgressBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        // Take care of displaying the question
        TextView textTextView = (TextView) mView.findViewById(R.id.fragment_question_text_textview);
        textTextView.setText(mQuestion.getText());

        final ImageView imageImageView = (ImageView) mView.findViewById(R.id.fragment_question_image_imageview);
        if (mQuestion.getType().equals("guess_animal_image")) {
            // If there's an image present, place it in the view
            imageImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // Wait for the view being rendered so that its dimensions are known and Picasso can use them to resize the image
                    ImageLoader.getInstance(getActivity()).loadImage(
                            mQuestion.getImage(),
                            imageImageView
                    );

                    imageImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        } else {
            imageImageView.setVisibility(View.GONE);
        }

        // Mark the question as "shown", since it's gonna be displayed
        mQuestion.setShown(true);
    }

    // A separate method to render the answers
    private void renderAnswers() {
        // Put all the answers together and shuffle them randomly
        List<String> answers = new ArrayList<>(Arrays.asList(mQuestion.getIncorrectAnswers()));
        answers.add(mQuestion.getCorrectAnswer());

        Collections.shuffle(answers);

        // Handle the buttons with answers
        Button answerButton1 = (Button) mView.findViewById(R.id.fragment_question_button_1);
        answerButton1.setText(answers.get(0));
        answerButton1.setOnClickListener(questionAnswered);
        Button answerButton2 = (Button) mView.findViewById(R.id.fragment_question_button_2);
        answerButton2.setText(answers.get(1));
        answerButton2.setOnClickListener(questionAnswered);
        Button answerButton3 = (Button) mView.findViewById(R.id.fragment_question_button_3);
        answerButton3.setText(answers.get(2));
        answerButton3.setOnClickListener(questionAnswered);
        Button answerButton4 = (Button) mView.findViewById(R.id.fragment_question_button_4);
        answerButton4.setText(answers.get(3));
        answerButton4.setOnClickListener(questionAnswered);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Check if the counter hasn't been cancelled before
        if (mQuestionCountDownTimer != null) {
            mQuestionCountDownTimer.cancel();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_TIME_TO_FINISH, mTimeToFinish);
    }

    // Replace the answers with a new layout when the guessing part is finished
    private void replaceAnswers(String resultText) {
        // Remove the old view from its parent
        View oldView = mView.findViewById(R.id.fragment_question_answers);

        // If the View's no longer there, do nothing
        if (oldView == null) {
            return;
        }

        ViewGroup parent = (ViewGroup) oldView.getParent();
        int index = parent.indexOfChild(oldView);
        parent.removeView(oldView);

        // Add the new view based on the index of the previous, old one
        View newView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_question_answered, parent, false);

        TextView result = (TextView) newView.findViewById(R.id.question_answered_result_textview);
        result.setText(resultText);

        // Let the user flag the question as "wrong" through a dialog
        final ImageView flag = (ImageView) newView.findViewById(R.id.question_answered_flag_imageview);
        flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialog(flag);
            }
        });

        // Provide the user with the ability to see the answer animal's detail
        final Button animalDetail = (Button) newView.findViewById(R.id.question_answered_animal_detail_button);
        animalDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                final AnimalDetailFragment animalDetailFragment = AnimalDetailFragment.newInstance(mQuestion.getAnswerObjectId());

                // Use the fragment's dialog ability and display the overlay with an animal's detail
                animalDetailFragment.show(manager, ANIMAL_DETAIL_FRAGMENT);

            }
        });

        Button nextQuestion = (Button) newView.findViewById(R.id.question_answered_next_question_button);
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onQuestionAnswered(mQuestion);
            }
        });

        if (mQuestionPosition + 1 == mQuestionManager.getQuestionCount()) {
            nextQuestion.setText(getString(R.string.question_answered_next_question_button_finish_text));
        }

        parent.addView(newView, index);

        // Set the time it took to answer and mark the question as "answered" so that it's not shown more than once during device config changes
        if (!mQuestion.isAnswered() && mTimerProgressBar != null) {
            mQuestion.setTimeToAnswer(mTimerProgressBar.getMax() - mTimerProgressBar.getProgress());
            mQuestion.setAnswered(true);
        }
    }

    // A listener for all the answer buttons, fired whenever user selects one of them
    private View.OnClickListener questionAnswered = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Stop the timer immediately
            mQuestionCountDownTimer.cancel();

            Button button = (Button) v;
            String resultText;

            // Check the button's text for a correct answer
            if (mQuestion.getCorrectAnswer().equals(button.getText())) {
                mQuestion.setAnsweredCorrectly(true);
                resultText = getString(R.string.question_answered_result_correct_textview_text);
                updateScoreText();
            } else {
                mQuestion.setAnsweredCorrectly(false);
                resultText = getString(R.string.question_answered_result_incorrect_textview_text, mQuestion.getCorrectAnswer());
            }

            replaceAnswers(resultText);
        }
    };

    // Update the TextView with quiz score to reflect the number of correct answers
    private void updateScoreText() {
        TextView scoreTextView = (TextView) mView.findViewById(R.id.fragment_question_score_textview);
        String scoreText = getString(
                R.string.fragment_question_score_textview_text,
                mQuestionManager.getCorrectAnswersCount(),
                mQuestionManager.getQuestionCount()
        );
        scoreTextView.setText(scoreText);
    }

    // Creates the dialog window for the user to confirm his intention to flag a question as invalid
    private void createAlertDialog(final ImageView flag) {
        AlertDialog.Builder builder;

        // Create the builder's instance based on the current OS version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }

        builder.setTitle(R.string.question_answered_flag_alert_title)
            .setMessage(R.string.question_answered_flag_alert_text)
            .setPositiveButton(R.string.question_answered_flag_alert_yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Set the question as flagged, hide the flag image and inform the user
                    mQuestion.setFlagged(true);

                    flag.setVisibility(View.GONE);

                    Toast.makeText(getActivity(), R.string.question_answered_flag_alert_flagged, Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton(R.string.question_answered_flag_alert_no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mQuestion.setFlagged(false);
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    // Counter class, updating the progress bar with each iteration
    private class QuestionCountDownTimer extends CountDownTimer {
        public QuestionCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) (millisUntilFinished / 1000);

            // Update the member variable in case the current state is gonna be saved
            mTimeToFinish = progress;

            // Update the progress with a lower value
            mTimerProgressBar.setProgress(progress);
        }

        @Override
        public void onFinish() {
            // Set the timeout text and prevent the user from answering the question anymore
            String resultText = getString(R.string.question_answered_result_timeout_textview_text, mQuestion.getCorrectAnswer());

            mQuestion.setAnsweredCorrectly(false);

            replaceAnswers(resultText);
        }
    }
}
