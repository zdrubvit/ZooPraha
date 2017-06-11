package cz.zdrubecky.zoopraha.section.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cz.zdrubecky.zoopraha.R;
import cz.zdrubecky.zoopraha.manager.QuestionManager;
import cz.zdrubecky.zoopraha.manager.QuizResultManager;
import cz.zdrubecky.zoopraha.model.QuizResult;

public class QuizResultActivity extends AppCompatActivity {
    private QuestionManager mQuestionManager;
    private QuizResultManager mQuizResultManager;

    private TextView mCorrectAnswersTextView;
    private TextView mIncorrectAnswersTextView;
    private TextView mTotalTimeTextView;
    private TextView mScoreTextView;
    private Button mNewGameButton;
    private Button mLeaderboardButton;
    private Button mMenuButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        mQuestionManager = QuestionManager.get(this);
        mQuizResultManager = new QuizResultManager(this);

        mCorrectAnswersTextView = (TextView) findViewById(R.id.quiz_result_correct_answers_textview);
        String correctAnswersText = getString(
                R.string.quiz_result_correct_answers_textview_text,
                mQuestionManager.getCorrectAnswersCount(),
                mQuestionManager.getQuestionCount()
        );
        mCorrectAnswersTextView.setText(correctAnswersText);

        mIncorrectAnswersTextView = (TextView) findViewById(R.id.quiz_result_incorrect_answers_textview);
        String incorrectAnswersText = getString(
                R.string.quiz_result_incorrect_answers_textview_text,
                mQuestionManager.getIncorrectAnswersCount(),
                mQuestionManager.getQuestionCount()
        );
        mIncorrectAnswersTextView.setText(incorrectAnswersText);

        mTotalTimeTextView = (TextView) findViewById(R.id.quiz_result_total_time_textview);
        String totalTimeText = getString(R.string.quiz_result_total_time_textview_text, mQuestionManager.getTotalTime());
        mTotalTimeTextView.setText(totalTimeText);

        mScoreTextView = (TextView) findViewById(R.id.quiz_result_score_textview);
        Double questionValue = 100.0 / mQuestionManager.getQuestionCount();
        Double score = questionValue * mQuestionManager.getCorrectAnswersCount();
        String scoreText = getString(R.string.quiz_result_score_textview_text, score.intValue(), 100);
        mScoreTextView.setText(scoreText);

        QuizResult quizResult = new QuizResult();
        quizResult.setDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        quizResult.setName(QuizPreferences.getUserName(this));
        quizResult.setScore(score.intValue());
        quizResult.setQuestionTime(QuizPreferences.getQuestionTime(this));
        quizResult.setTotalTime(mQuestionManager.getTotalTime());
        quizResult.setQuestionCount(mQuestionManager.getQuestionCount());
        quizResult.setCorrectAnswerCount(mQuestionManager.getCorrectAnswersCount());
        mQuizResultManager.addQuizResult(quizResult);

        mNewGameButton = (Button) findViewById(R.id.quiz_result_new_game_button);
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuizResultActivity.this, QuizActivity.class);

                startActivity(i);
            }
        });

        mLeaderboardButton = (Button) findViewById(R.id.quiz_result_leaderboard_button);
        mLeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuizResultActivity.this, QuizResultListActivity.class);

                startActivity(i);
            }
        });

        mMenuButton = (Button) findViewById(R.id.quiz_result_menu_button);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuizResultActivity.this, QuizMenuActivity.class);

                startActivity(i);
            }
        });
    }

    // Move up to the activity's parent when the back button is pressed
    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}
