package cz.zdrubecky.zoopraha.section.lexicon;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import cz.zdrubecky.zoopraha.R;
import cz.zdrubecky.zoopraha.SingleFragmentActivity;
import cz.zdrubecky.zoopraha.api.DataFetcher;
import cz.zdrubecky.zoopraha.manager.AnimalManager;
import cz.zdrubecky.zoopraha.model.Animal;
import cz.zdrubecky.zoopraha.model.JsonApiObject;
import cz.zdrubecky.zoopraha.model.LexiconQueryBuilder;

public class LexiconListActivity
        extends SingleFragmentActivity
        implements LexiconListFragment.Callbacks {

    private static final String TAG = "LexiconListActivity";
    private static final String EXTRA_FILTER_KEY = "cz.zdrubecky.zoopraha.filter_key";
    private static final String EXTRA_FILTER_VALUE = "cz.zdrubecky.zoopraha.filter_value";
    private static final String KEY_FILTER_KEY = "filter_key";
    private static final String KEY_FILTER_VALUE = "filter_value";

    private Pair<String, String> mFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check for the saved state first, then try and get the filter from an intent and finally from the shared preferences
        if (savedInstanceState != null) {
            mFilter = Pair.create(savedInstanceState.getString(KEY_FILTER_KEY), savedInstanceState.getString(KEY_FILTER_VALUE));
        } else if (getIntent().getStringExtra(EXTRA_FILTER_KEY) != null) {
            mFilter = Pair.create(getIntent().getStringExtra(EXTRA_FILTER_KEY), getIntent().getStringExtra(EXTRA_FILTER_VALUE));
        } else {
            mFilter = Pair.create(LexiconPreferences.getFilterKey(this), LexiconPreferences.getFilterValue(this));
        }

        // Create the builder object with the relevant query parameters set
        LexiconQueryBuilder builder = createLexiconQueryBuilderObject();

        DataFetcher dataFetcher = new DataFetcher();
        dataFetcher.setDataFetchedListener(new DataFetcher.DataFetchedListener() {
            @Override
            public void onDataFetched(JsonApiObject response) {
                Log.i(TAG, "API response listener called with " + response.getMeta().getCount() + " resource objects.");
                
                // Hand the response handling over to a background thread and avoid blocking the UI thread
                new SaveItemsTask(response).execute();
            }
        });

        dataFetcher.getAnimals(builder);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_FILTER_KEY, mFilter.first);
        outState.putString(KEY_FILTER_VALUE, mFilter.second);
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

    public static Intent newIntent(Context context, String filterKey, String filterValue) {
        Intent i = new Intent(context, LexiconListActivity.class);
        i.putExtra(EXTRA_FILTER_KEY, filterKey);
        i.putExtra(EXTRA_FILTER_VALUE, filterValue);

        return i;
    }

    public void onAnimalSelected(Animal animal) {
        // Check if there's not a split view and therefore we're not working with a tablet
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = AnimalPagerActivity.newIntent(this, animal.getId());
            startActivity(intent);
        } else {
            Fragment animalDetail = AnimalDetailFragment.newInstance(animal.getId());

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

    private LexiconQueryBuilder createLexiconQueryBuilderObject() {
        LexiconQueryBuilder builder = new LexiconQueryBuilder();

        // The intent is always there, but the right extras might not
        if (mFilter.first != null) {
            // "Switch" the query type and call the appropriate builder method (the filter names correspond to the API documentation)
            if (mFilter.first.equals("biotopes")) {
                builder.setBiotope(mFilter.second);
            } else if (mFilter.first.equals("class_name")) {
                builder.setClassName(mFilter.second);
            } else if (mFilter.first.equals("continents")) {
                builder.setContinents(mFilter.second);
            } else if (mFilter.first.equals("description")) {
                builder.setDescription(mFilter.second);
            } else if (mFilter.first.equals("distribution")) {
                builder.setDistribution(mFilter.second);
            } else if (mFilter.first.equals("food")) {
                builder.setFood(mFilter.second);
            } else if (mFilter.first.equals("location")) {
                builder.setLocation(mFilter.second);
            } else if (mFilter.first.equals("name")) {
                builder.setName(mFilter.second);
            } else if (mFilter.first.equals("order_name")) {
                builder.setOrderName(mFilter.second);
            }

            // Save the filter for later, when the activity is recreated
            LexiconPreferences.setFilterKey(this, mFilter.first);
            LexiconPreferences.setFilterValue(this, mFilter.second);
        }

        return builder;
    }
}
