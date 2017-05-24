package cz.zdrubecky.zoopraha;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cz.zdrubecky.zoopraha.api.DataFetcher;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new DataFetcher().getAdoptions(null, "10", null);
    }
}
