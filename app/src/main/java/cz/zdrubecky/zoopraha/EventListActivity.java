package cz.zdrubecky.zoopraha;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import cz.zdrubecky.zoopraha.api.DataFetcher;
import cz.zdrubecky.zoopraha.manager.EventManager;
import cz.zdrubecky.zoopraha.model.Event;
import cz.zdrubecky.zoopraha.model.JsonApiObject;

public class EventListActivity
        extends SingleFragmentActivity
        implements EventListFragment.Callbacks {
    
    private static final String TAG = "EventListActivity";

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

        dataFetcher.getEvents(null, null, null);
    }

    private void replaceListFragment() {
        Fragment fragment = new EventListFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(getFragmentContainerId(), fragment)
                .commit();
    }

    @Override
    protected int getLayoutResId() {
        // Choose the appropriate layout according to the device screen size using references
        return R.layout.activity_masterdetail;
    }

    public void onEventSelected(Event event) {
        // Check if there's not a split view and therefore we're not working with a tablet
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = EventDetailActivity.newIntent(this, event.getId());
            startActivity(intent);
        } else {
            Fragment eventDetail = EventDetailFragment.newInstance(event.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, eventDetail)
                    .commit();
        }
    }

    // This class is not static and therefore can block the garbage collection of its parent class, but it's useful to update the fragment from here
    private class SaveItemsTask extends AsyncTask<Void, Void, Void> {
        private EventManager mEventManager;
        private JsonApiObject mResponse;

        public SaveItemsTask(JsonApiObject response) {
            mEventManager = new EventManager(EventListActivity.this);
            mResponse = response;
        }

        // Parse the response and update the database with newly acquired data
        @Override
        protected Void doInBackground(Void... voids) {
            List<JsonApiObject.Resource> data = mResponse.getData();
            Gson gson = new Gson();

            // Iterate over the incoming objects and use them to create events
            for (int i = 0; i < data.size(); i++) {
                Event event = gson.fromJson(data.get(i).getDocument(), Event.class);
                // The ID has to be set explicitly because of its placement thanks to JSON-API standard
                event.setId(data.get(i).getId());
                mEventManager.addEvent(event);
            }

            mEventManager.flushEvents();

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
