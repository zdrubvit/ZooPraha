package cz.zdrubecky.zoopraha;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cz.zdrubecky.zoopraha.api.DataFetcher;
import cz.zdrubecky.zoopraha.manager.ClassificationManager;
import cz.zdrubecky.zoopraha.manager.FilterManager;
import cz.zdrubecky.zoopraha.model.Classification;
import cz.zdrubecky.zoopraha.model.Filter;
import cz.zdrubecky.zoopraha.model.JsonApiObject;

public class LexiconActivity extends SingleFragmentActivity {
    private static final String TAG = "LexiconActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Process the data in a background task
        new SaveItemsTask().execute();
    }

    private void replaceFragment() {
        Fragment fragment = new LexiconFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(getFragmentContainerId(), fragment)
                .commit();
    }

    private class SaveItemsTask extends AsyncTask<Void, Void, Void> {
        public SaveItemsTask() {}

        @Override
        protected Void doInBackground(Void... voids) {
            DataFetcher dataFetcher = new DataFetcher();
            Gson gson = new Gson();
            List<JsonApiObject> filterResponses = new ArrayList<>();
            JsonApiObject classificationsResponse;
            FilterManager filterManager = new FilterManager(LexiconActivity.this);
            ClassificationManager classificationManager = new ClassificationManager(LexiconActivity.this);

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
            
            classificationsResponse = dataFetcher.getClassifications(true, true, false);

            List<JsonApiObject.Resource> classificationsResponseData = classificationsResponse.getData();

            // Iterate over the incoming objects and use them to create the filters
            for (int i = 0; i < classificationsResponseData.size(); i++) {
                Classification classification = gson.fromJson(classificationsResponseData.get(i).getDocument(), Classification.class);
                classification.setId(classificationsResponseData.get(i).getId());
                classificationManager.addClassification(classification);
            }

            classificationManager.flushClassifications();

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
