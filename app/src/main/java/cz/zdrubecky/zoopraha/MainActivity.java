package cz.zdrubecky.zoopraha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cz.zdrubecky.zoopraha.database.ZooBaseHelper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button mMainButtonLexicon;
    private Button mMainButtonQuiz;
    private Button mMainButtonMap;
    private Button mMainButtonEvents;
    private Button mMainButtonAdoptions;
    private Button mMainButtonProfile;

    private ZooBaseHelper mZooBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getApplicationContext().deleteDatabase(ZooBaseHelper.DATABASE_NAME);

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
