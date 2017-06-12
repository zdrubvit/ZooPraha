package cz.zdrubecky.zoopraha.section.adoption;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import cz.zdrubecky.zoopraha.section.lexicon.AnimalDetailActivity;
import cz.zdrubecky.zoopraha.section.lexicon.AnimalDetailFragment;
import cz.zdrubecky.zoopraha.R;
import cz.zdrubecky.zoopraha.SingleFragmentActivity;
import cz.zdrubecky.zoopraha.api.DataFetcher;
import cz.zdrubecky.zoopraha.manager.AdoptionManager;
import cz.zdrubecky.zoopraha.model.Adoption;
import cz.zdrubecky.zoopraha.model.JsonApiObject;

public class AdoptionListActivity
        extends SingleFragmentActivity
        implements AdoptionListFragment.Callbacks {

    private static final String TAG = "AdoptionListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataFetcher dataFetcher = new DataFetcher(this);
        dataFetcher.setDataFetchedListener(new DataFetcher.DataFetchedListener() {
            @Override
            public void onDataFetched(JsonApiObject response, int statusCode) {
                Log.i(TAG, "API response listener called with " + response.getMeta().getCount() + " Adoption resource objects.");
                response.setStatus(statusCode);
                
                // Hand the response handling over to a background thread and avoid blocking the UI thread
                new SaveItemsTask(response).execute();
            }
        });

        dataFetcher.getAdoptions(null, null, null);
    }

    private void replaceListFragment() {
        Fragment fragment = new AdoptionListFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(getFragmentContainerId(), fragment)
                .commit();
    }

    @Override
    protected int getLayoutResId() {
        // Choose the appropriate layout according to the device screen size using references
        return R.layout.activity_masterdetail;
    }

    public void onAdoptionSelected(Adoption adoption) {
        if (adoption.getLexiconId().equals("")) {
            Toast.makeText(this, R.string.adoption_no_detail_toast, Toast.LENGTH_SHORT).show();

            return;
        }

        // Check if there's not a split view and therefore we're not working with a tablet
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = AnimalDetailActivity.newIntent(this, adoption.getLexiconId());
            startActivity(intent);
        } else {
            Fragment animalDetail = AnimalDetailFragment.newInstance(adoption.getLexiconId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, animalDetail)
                    .commit();
        }
    }

    // This class is not static and therefore can block the garbage collection of its parent class, but it's useful to update the fragment from here
    private class SaveItemsTask extends AsyncTask<Void, Void, Void> {
        private AdoptionManager mAdoptionManager;
        private JsonApiObject mResponse;

        public SaveItemsTask(JsonApiObject response) {
            mAdoptionManager = new AdoptionManager(AdoptionListActivity.this);
            mResponse = response;
        }

        // Parse the response and update the database with newly acquired data
        @Override
        protected Void doInBackground(Void... voids) {
            if (mResponse.getStatus() != 304) {
                List<JsonApiObject.Resource> data = mResponse.getData();
                Gson gson = new Gson();

                // Iterate over the incoming objects and use them to create adoptions
                for (int i = 0; i < data.size(); i++) {
                    Adoption adoption = gson.fromJson(data.get(i).getDocument(), Adoption.class);
                    // The ID has to be set explicitly because of its placement thanks to JSON-API standard
                    adoption.setId(data.get(i).getId());
                    mAdoptionManager.addAdoption(adoption);
                }

                mAdoptionManager.flushAdoptions();

                Log.i(TAG, data.size() + " Adoption objects added to the database.");
            }

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
