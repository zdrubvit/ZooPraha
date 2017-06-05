package cz.zdrubecky.zoopraha;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cz.zdrubecky.zoopraha.manager.QuestionManager;
import cz.zdrubecky.zoopraha.model.Question;

public class QuestionFragment extends Fragment {
    private static final String TAG = "QuestionFragment";
    private static final String ARG_QUESTION_POSITION = "question_position";

    private TextView mQuestionNumberTextView;
    private TextView mScoreTextView;
    private TextView mTextTextView;
    private ImageView mImageImageView;
    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;

    private QuestionManager mQuestionManager;
    private int mQuestionPosition;
    private Question mQuestion;
    private Callbacks mCallbacks;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Put all the answers together and shuffle them randomly
        List<String> answers = new ArrayList<>(Arrays.asList(mQuestion.getIncorrectAnswers()));
        answers.add(mQuestion.getCorrectAnswer());

        Collections.shuffle(answers);

        View v = inflater.inflate(R.layout.fragment_question, container, false);

        // Fill the top part of the question view
        mQuestionNumberTextView = (TextView) v.findViewById(R.id.fragment_question_number_textview);
        String questionNumberText = getString(R.string.fragment_question_number_textview_text, mQuestionPosition + 1);
        mQuestionNumberTextView.setText(questionNumberText);

        mScoreTextView = (TextView) v.findViewById(R.id.fragment_question_score_textview);
        String scoreText = getString(
                R.string.fragment_question_score_textview_text,
                mQuestionManager.getCorrectAnswersCount(),
                mQuestionManager.getQuestionCount()
        );
        mScoreTextView.setText(scoreText);

        // Take care of displaying the question
        mTextTextView = (TextView) v.findViewById(R.id.fragment_question_text_textview);
        mTextTextView.setText(mQuestion.getText());

        if (mQuestion.getType().equals("guess_animal_image")) {
            String imageUrl = mQuestion.getImage();

//            mImageView = (ImageView) v.findViewById(R.id.fragment_question_image_imageview);
//            mImageView.setImageBitmap(image);
            // todo switch to a bitmap getter
            mTextTextView.setText(imageUrl);
        }

        // Handle the buttons with answers
        mAnswerButton1 = (Button) v.findViewById(R.id.fragment_question_button_1);
        mAnswerButton1.setText(answers.get(0));
        mAnswerButton1.setOnClickListener(questionAnswered);
        mAnswerButton2 = (Button) v.findViewById(R.id.fragment_question_button_2);
        mAnswerButton2.setText(answers.get(1));
        mAnswerButton2.setOnClickListener(questionAnswered);
        mAnswerButton3 = (Button) v.findViewById(R.id.fragment_question_button_3);
        mAnswerButton3.setText(answers.get(2));
        mAnswerButton3.setOnClickListener(questionAnswered);
        mAnswerButton4 = (Button) v.findViewById(R.id.fragment_question_button_4);
        mAnswerButton4.setText(answers.get(3));
        mAnswerButton4.setOnClickListener(questionAnswered);

        // Mark the question as "shown", since it's gonna be displayed
        mQuestion.setShown(true);

        return v;
    }

    private View.OnClickListener questionAnswered = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            String resultText;

            // Check the button's text for a correct answer
            if (mQuestion.getCorrectAnswer().equals(button.getText())) {
                mQuestion.setAnsweredCorrectly(true);
                resultText = getString(R.string.question_answered_result_correct_textview_text);
                Log.i(TAG, "Right answer selected");
            } else {
                mQuestion.setAnsweredCorrectly(false);
                resultText = getString(R.string.question_answered_result_incorrect_textview_text, mQuestion.getCorrectAnswer());
                Log.i(TAG, "Wrong answer selected");
            }

            // Remove the old view from its parent
            View oldView = getView().findViewById(R.id.fragment_question_answers);
            ViewGroup parent = (ViewGroup) oldView.getParent();
            int index = parent.indexOfChild(oldView);
            parent.removeView(oldView);

            // Add the new view based on the index of the previous, old one
            View newView = LayoutInflater.from(getActivity()).inflate(R.layout.question_answered, parent, false);

            TextView result = (TextView) newView.findViewById(R.id.question_answered_result_textview);
            result.setText(resultText);

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
        }
    };
}
