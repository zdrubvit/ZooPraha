package cz.zdrubecky.zoopraha.section.lexicon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cz.zdrubecky.zoopraha.SingleFragmentActivity;
import cz.zdrubecky.zoopraha.api.DataFetcher;
import cz.zdrubecky.zoopraha.manager.ClassificationManager;
import cz.zdrubecky.zoopraha.manager.FilterManager;
import cz.zdrubecky.zoopraha.manager.LocationManager;
import cz.zdrubecky.zoopraha.model.Classification;
import cz.zdrubecky.zoopraha.model.Filter;
import cz.zdrubecky.zoopraha.model.JsonApiObject;
import cz.zdrubecky.zoopraha.model.Location;

public class LexiconMenuActivity extends SingleFragmentActivity {
    private static final String TAG = "LexiconMenuActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Process the data in a background task
        new SaveItemsTask().execute();
    }

    // Replace the initial loading screen with a tab layout
    private void replaceFragment() {
        Fragment fragment = new LexiconMenuFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(getFragmentContainerId(), fragment)
                .commit();
    }

    private class SaveItemsTask extends AsyncTask<Void, Void, Void> {
        public SaveItemsTask() {}

        @Override
        protected Void doInBackground(Void... voids) {
            DataFetcher dataFetcher = new DataFetcher(LexiconMenuActivity.this);
            Gson gson = new Gson();

            List<JsonApiObject> filterResponses = new ArrayList<>();
            JsonApiObject classificationsResponse;
            JsonApiObject locationResponse;

            FilterManager filterManager = new FilterManager(LexiconMenuActivity.this);
            ClassificationManager classificationManager = new ClassificationManager(LexiconMenuActivity.this);
            LocationManager locationManager = new LocationManager(LexiconMenuActivity.this);

            // Get the resources one by one and save them to the database
            filterResponses.add(dataFetcher.getBiotopes());
            filterResponses.add(dataFetcher.getContinents());
            filterResponses.add(dataFetcher.getFood());

            for (JsonApiObject response : filterResponses) {
                List<JsonApiObject.Resource> data = response.getData();

                // Iterate over the incoming objects and use them to create the filters
                for (int i = 0; i < data.size(); i++) {
                    Filter filter = gson.fromJson(data.get(i).getDocument(), Filter.class);
                    filter.setName(data.get(i).getType());
                    filterManager.addFilter(filter);
                }
            }

            filterManager.flushFilters();

            // Take care of the classifications
            classificationsResponse = dataFetcher.getClassifications(true, true, false);

            List<JsonApiObject.Resource> classificationsResponseData = classificationsResponse.getData();

            for (int i = 0; i < classificationsResponseData.size(); i++) {
                Classification classification = gson.fromJson(classificationsResponseData.get(i).getDocument(), Classification.class);
                classification.setId(classificationsResponseData.get(i).getId());
                classificationManager.addClassification(classification);
            }

            classificationManager.flushClassifications();

            // And finally the locations
            locationResponse = dataFetcher.getLocations();

            List<JsonApiObject.Resource> locationResponseData = locationResponse.getData();

            for (int i = 0; i < locationResponseData.size(); i++) {
                Location location = gson.fromJson(locationResponseData.get(i).getDocument(), Location.class);
                location.setId(locationResponseData.get(i).getId());
                locationManager.addLocation(location);
            }

            locationManager.flushLocations();

            return null;
        }

        // This method is handled by the UI thread so it can update the UI safely after all items have been updated
        @Override
        protected void onPostExecute(Void v) {
            // The DB is up-to-date, so the list can be displayed
            replaceFragment();
        }
    }
}
