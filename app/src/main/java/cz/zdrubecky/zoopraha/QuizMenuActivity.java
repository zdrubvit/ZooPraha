package cz.zdrubecky.zoopraha;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class QuizMenuActivity extends AppCompatActivity {
    private Button mQuizButtonNewGame;
    private Button mQuizButtonLeaderboard;
    private Button mQuizButtonBadges;
    private Button mQuizButtonSettings;
    private Button mQuizButtonHelp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_menu);

        mQuizButtonNewGame = (Button) findViewById(R.id.quiz_menu_new_game_button);
        mQuizButtonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuizMenuActivity.this, QuizActivity.class);

                startActivity(i);
            }
        });

        mQuizButtonLeaderboard = (Button) findViewById(R.id.quiz_menu_leaderboard_button);
        mQuizButtonBadges = (Button) findViewById(R.id.quiz_menu_badges_button);
        mQuizButtonSettings = (Button) findViewById(R.id.quiz_menu_settings_button);
        mQuizButtonHelp = (Button) findViewById(R.id.quiz_menu_help_button);
    }
}
