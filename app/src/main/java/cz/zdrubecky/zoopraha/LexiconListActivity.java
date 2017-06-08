package cz.zdrubecky.zoopraha;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import cz.zdrubecky.zoopraha.api.DataFetcher;
import cz.zdrubecky.zoopraha.manager.AnimalManager;
import cz.zdrubecky.zoopraha.model.Animal;
import cz.zdrubecky.zoopraha.model.JsonApiObject;

public class LexiconListActivity
        extends SingleFragmentActivity
        implements LexiconListFragment.Callbacks {

    private static final String TAG = "LexiconListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataFetcher dataFetcher = new DataFetcher();
        dataFetcher.setDataFetchedListener(new DataFetcher.DataFetchedListener() {
            @Override
            public void onDataFetched(JsonApiObject response) {
                Log.i(TAG, "API response listener called with " + response.getMeta().getCount() + " resource objects.");
                
                // Hand the response handling over to a background thread and avoid blocking the UI thread
                new SaveItemsTask(response).execute();
            }
        });

        dataFetcher.getAnimals(null, null, null);
    }

    private void replaceListFragment() {
        Fragment fragment = new LexiconListFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(getFragmentContainerId(), fragment)
                .commit();
    }

    @Override
    protected int getLayoutResId() {
        // Choose the appropriate layout according to the device screen size using references
        return R.layout.activity_masterdetail;
    }

    public void onAnimalSelected(Animal animal) {
        if (animal.getId() == null) {
            Toast.makeText(this, R.string.adoption_no_detail_toast, Toast.LENGTH_SHORT).show();

            return;
        }

        // Check if there's not a split view and therefore we're not working with a tablet
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = AnimalDetailActivity.newIntent(this, animal.getId());
            startActivity(intent);
        } else {
            Fragment animalDetail = AnimalFragment.newInstance(animal.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, animalDetail)
                    .commit();
        }
    }

    // This class is not static and therefore can block the garbage collection of its parent class, but it's useful to update the fragment from here
    private class SaveItemsTask extends AsyncTask<Void, Void, Void> {
        private AnimalManager mAnimalManager;
        private JsonApiObject mResponse;

        public SaveItemsTask(JsonApiObject response) {
            mAnimalManager = new AnimalManager(LexiconListActivity.this);
            mResponse = response;
        }

        // Parse the response and update the database with newly acquired data
        @Override
        protected Void doInBackground(Void... voids) {
            List<JsonApiObject.Resource> data = mResponse.getData();
            Gson gson = new Gson();

            // Iterate over the incoming objects and use them to create animals
            for (int i = 0; i < data.size(); i++) {
                Animal animal = gson.fromJson(data.get(i).getDocument(), Animal.class);
                // The ID has to be set explicitly because of its placement thanks to JSON-API standard
                animal.setId(data.get(i).getId());
                mAnimalManager.addAnimal(animal);
            }

            mAnimalManager.flushAnimals();

            // Don't return anything, there's no need
            return null;
        }

        // This method is handled by the UI thread so it can update the UI safely after all items have been updated
        @Override
        protected void onPostExecute(Void v) {
            replaceListFragment();
        }
    }
}
