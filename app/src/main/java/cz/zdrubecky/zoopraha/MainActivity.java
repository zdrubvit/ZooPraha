package cz.zdrubecky.zoopraha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.List;

import cz.zdrubecky.zoopraha.api.DataFetcher;
import cz.zdrubecky.zoopraha.database.ZooBaseHelper;
import cz.zdrubecky.zoopraha.manager.QuestionManager;
import cz.zdrubecky.zoopraha.model.JsonApiObject;
import cz.zdrubecky.zoopraha.model.Question;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button mMainButtonLexicon;
    private Button mMainButtonAdoptions;
    private Button mMainButtonEvents;
    private Button mMainButtonQuiz;
    private Button mMainButtonMap;
    private Button mMainButtonProfile;

    private ZooBaseHelper mZooBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getApplicationContext().deleteDatabase(ZooBaseHelper.DATABASE_NAME);
//
//        DataFetcher dataFetcherAdoptions = new DataFetcher();
//        dataFetcherAdoptions.setDataFetchedListener(new DataFetcher.DataFetchedListener() {
//            @Override
//            public void onDataFetched(JsonApiObject response) {
//                Log.i(TAG, "Listener called with " + response.getMeta().getCount() + " resources.");
//                AdoptionManager manager = new AdoptionManager(MainActivity.this);
//                List<JsonApiObject.Resource> data = response.getData();
//                Gson gson = new Gson();
//
//                // todo make this threaded
//                for (int i = 0; i < data.size(); i++) {
//                    Adoption adoption = gson.fromJson(data.get(i).getDocument(), Adoption.class);
//                    adoption.setId(data.get(i).getId());
//                    manager.addAdoption(adoption);
//                }
//            }
//        });
//        dataFetcherAdoptions.getAdoptions(null, null, null);
//
//        DataFetcher dataFetcherAnimals = new DataFetcher();
//        dataFetcherAnimals.setDataFetchedListener(new DataFetcher.DataFetchedListener() {
//            @Override
//            public void onDataFetched(JsonApiObject response) {
//                Log.i(TAG, "Listener called with " + response.getMeta().getCount() + " resources.");
//                AnimalManager manager = new AnimalManager(MainActivity.this);
//                List<JsonApiObject.Resource> data = response.getData();
//                Gson gson = new Gson();
//
//                // todo make this threaded
//                for (int i = 0; i < data.size(); i++) {
//                    Animal animal = gson.fromJson(data.get(i).getDocument(), Animal.class);
//                    animal.setId(data.get(i).getId());
//                    manager.addAnimal(animal);
//                }
//            }
//        });
//        dataFetcherAnimals.getAnimals(null, null, null);
//
//        DataFetcher dataFetcherEvents = new DataFetcher();
//        dataFetcherEvents.setDataFetchedListener(new DataFetcher.DataFetchedListener() {
//            @Override
//            public void onDataFetched(JsonApiObject response) {
//                Log.i(TAG, "Listener called with " + response.getMeta().getCount() + " resources.");
//                EventManager manager = new EventManager(MainActivity.this);
//                List<JsonApiObject.Resource> data = response.getData();
//                Gson gson = new Gson();
//
//                // todo make this threaded
//                for (int i = 0; i < data.size(); i++) {
//                    Event event = gson.fromJson(data.get(i).getDocument(), Event.class);
//                    event.setId(data.get(i).getId());
//                    manager.addEvent(event);
//                }
//            }
//        });
//        dataFetcherEvents.getEvents(null, null, null);

//        DataFetcher dataFetcherClassifications = new DataFetcher();
//        dataFetcherClassifications.setDataFetchedListener(new DataFetcher.DataFetchedListener() {
//            @Override
//            public void onDataFetched(JsonApiObject response) {
//                Log.i(TAG, "Listener called with " + response.getMeta().getCount() + " resources.");
//                ClassificationManager manager = new ClassificationManager(MainActivity.this);
//                List<JsonApiObject.Resource> data = response.getData();
//                Gson gson = new Gson();
//
//                // todo make this threaded
//                for (int i = 0; i < data.size(); i++) {
//                    Classification classification = gson.fromJson(data.get(i).getDocument(), Classification.class);
//                    classification.setId(data.get(i).getId());
//                    manager.addClassification(classification);
//                }
//
//                manager.flushClassifications();
//            }
//        });
//        dataFetcherClassifications.getClassifications(true, true, true);

        mMainButtonAdoptions = (Button) findViewById(R.id.main_button_adoptions);
        mMainButtonAdoptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AdoptionListActivity.class);

                startActivity(i);
            }
        });

        mMainButtonLexicon = (Button) findViewById(R.id.main_button_lexicon);
        mMainButtonLexicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LexiconActivity.class);

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

        mMainButtonQuiz = (Button) findViewById(R.id.main_button_quiz);
        mMainButtonQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, QuizMenuActivity.class);

                startActivity(i);
            }
        });
    }
}
