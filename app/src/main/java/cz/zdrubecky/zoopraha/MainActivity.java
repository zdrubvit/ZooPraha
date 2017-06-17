package cz.zdrubecky.zoopraha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;

import cz.zdrubecky.zoopraha.api.InternalStorageDriver;
import cz.zdrubecky.zoopraha.database.ZooBaseHelper;
import cz.zdrubecky.zoopraha.section.adoption.AdoptionListActivity;
import cz.zdrubecky.zoopraha.section.adoption.AdoptionPreferences;
import cz.zdrubecky.zoopraha.section.event.EventListActivity;
import cz.zdrubecky.zoopraha.section.lexicon.LexiconMenuActivity;
import cz.zdrubecky.zoopraha.section.lexicon.LexiconPreferences;
import cz.zdrubecky.zoopraha.section.quiz.QuizMenuActivity;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

//        getApplicationContext().deleteDatabase(ZooBaseHelper.DATABASE_NAME);
//        InternalStorageDriver.deleteProcessedResourcesFile(this);
        
        Button mainButtonLexicon = (Button) findViewById(R.id.main_button_lexicon);
        mainButtonLexicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LexiconMenuActivity.class);

                startActivity(i);
            }
        });

        Button mainButtonQuiz = (Button) findViewById(R.id.main_button_quiz);
        mainButtonQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, QuizMenuActivity.class);

                startActivity(i);
            }
        });

        Button mainButtonEvents = (Button) findViewById(R.id.main_button_events);
        mainButtonEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, EventListActivity.class);

                startActivity(i);
            }
        });

        Button mainButtonAdoptions = (Button) findViewById(R.id.main_button_adoptions);
        mainButtonAdoptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AdoptionListActivity.class);

                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reset the search queries because this is a new starting point
        LexiconPreferences.setSearchQuery(this, null);
        AdoptionPreferences.setSearchQuery(this, null);
        AdoptionPreferences.setCurrentPage(this, 1);
    }
}
