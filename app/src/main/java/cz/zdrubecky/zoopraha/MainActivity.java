package cz.zdrubecky.zoopraha;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cz.zdrubecky.zoopraha.api.DataFetcher;
import cz.zdrubecky.zoopraha.model.Adoption;
import cz.zdrubecky.zoopraha.model.JsonApiObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataFetcher dataFetcher = new DataFetcher();
        dataFetcher.setDataFetchedListener(new DataFetcher.DataFetchedListener() {
            @Override
            public void onDataFetched(JsonApiObject response) {
                Log.i(TAG, "Listener called with " + response.getMeta().getCount() + " resources.");
            }
        });
        dataFetcher.getAdoptions(null, "10", null);
    }
}
