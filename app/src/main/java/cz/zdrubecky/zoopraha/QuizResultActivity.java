package cz.zdrubecky.zoopraha;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cz.zdrubecky.zoopraha.manager.QuestionManager;

public class QuizResultActivity extends AppCompatActivity {
    private QuestionManager mQuestionManager;

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
        String scoreText = getString(R.string.quiz_result_score_textview_text, 0);
        mScoreTextView.setText(scoreText);

        mNewGameButton = (Button) findViewById(R.id.quiz_result_new_game_button);
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuizResultActivity.this, QuizActivity.class);

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
}
