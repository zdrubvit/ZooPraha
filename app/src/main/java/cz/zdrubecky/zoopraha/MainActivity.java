package cz.zdrubecky.zoopraha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cz.zdrubecky.zoopraha.database.ZooBaseHelper;
import cz.zdrubecky.zoopraha.section.adoption.AdoptionListActivity;
import cz.zdrubecky.zoopraha.section.event.EventListActivity;
import cz.zdrubecky.zoopraha.section.lexicon.LexiconMenuActivity;
import cz.zdrubecky.zoopraha.section.quiz.QuizMenuActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button mMainButtonLexicon;
    private Button mMainButtonQuiz;
    private Button mMainButtonEvents;
    private Button mMainButtonAdoptions;

    private ZooBaseHelper mZooBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getApplicationContext().deleteDatabase(ZooBaseHelper.DATABASE_NAME);

        mMainButtonLexicon = (Button) findViewById(R.id.main_button_lexicon);
        mMainButtonLexicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LexiconMenuActivity.class);

                startActivity(i);
            }
        });

        mMainButtonQuiz = (Button) findViewById(R.id.main_button_quiz);
        mMainButtonQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, QuizMenuActivity.class);

                startActivity(i);
            }
        });

        mMainButtonEvents = (Button) findViewById(R.id.main_button_events);
        mMainButtonEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, EventListActivity.class);

                startActivity(i);
            }
        });

        mMainButtonAdoptions = (Button) findViewById(R.id.main_button_adoptions);
        mMainButtonAdoptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AdoptionListActivity.class);

                startActivity(i);
            }
        });
    }
}
