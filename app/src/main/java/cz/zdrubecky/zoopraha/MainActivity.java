package cz.zdrubecky.zoopraha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import cz.zdrubecky.zoopraha.api.DataFetcher;
import cz.zdrubecky.zoopraha.manager.AdoptionsManager;
import cz.zdrubecky.zoopraha.manager.AnimalsManager;
import cz.zdrubecky.zoopraha.model.Adoption;
import cz.zdrubecky.zoopraha.model.Animal;
import cz.zdrubecky.zoopraha.model.JsonApiObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button mMainButtonLexicon;
    private Button mMainButtonAdoptions;
    private Button mMainButtonEvents;
    private Button mMainButtonQuiz;
    private Button mMainButtonMap;
    private Button mMainButtonProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DataFetcher dataFetcher = new DataFetcher();
//        dataFetcher.setDataFetchedListener(new DataFetcher.DataFetchedListener() {
//            @Override
//            public void onDataFetched(JsonApiObject response) {
//                Log.i(TAG, "Listener called with " + response.getMeta().getCount() + " resources.");
//                AdoptionsManager manager = AdoptionsManager.get(MainActivity.this);
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
//        dataFetcher.getAdoptions(null, "10", null);

//        dataFetcher.setDataFetchedListener(new DataFetcher.DataFetchedListener() {
//            @Override
//            public void onDataFetched(JsonApiObject response) {
//                Log.i(TAG, "Listener called with " + response.getMeta().getCount() + " resources.");
//                AnimalsManager manager = AnimalsManager.get(MainActivity.this);
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
//        dataFetcher.getAnimals(null, null, null);

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
