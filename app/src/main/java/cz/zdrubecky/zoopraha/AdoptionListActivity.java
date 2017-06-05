package cz.zdrubecky.zoopraha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import cz.zdrubecky.zoopraha.api.DataFetcher;
import cz.zdrubecky.zoopraha.manager.AdoptionManager;
import cz.zdrubecky.zoopraha.model.Adoption;
import cz.zdrubecky.zoopraha.model.JsonApiObject;

public class AdoptionListActivity
        extends SingleFragmentActivity
        implements AdoptionListFragment.Callbacks {

    private static final String TAG = "AdoptionListActivity";

    private AdoptionManager mAdoptionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdoptionManager = new AdoptionManager(this);

        DataFetcher dataFetcherAdoptions = new DataFetcher();
        dataFetcherAdoptions.setDataFetchedListener(new DataFetcher.DataFetchedListener() {
            @Override
            public void onDataFetched(JsonApiObject response) {
                Log.i(TAG, "Listener called with " + response.getMeta().getCount() + " resources.");
                List<JsonApiObject.Resource> data = response.getData();
                Gson gson = new Gson();

                // todo make this threaded
                for (int i = 0; i < data.size(); i++) {
                    Adoption adoption = gson.fromJson(data.get(i).getDocument(), Adoption.class);
                    adoption.setId(data.get(i).getId());
                    mAdoptionManager.addAdoption(adoption);
                }

                // All the adoptions have been served - display them
                replaceListFragment();
            }
        });

        dataFetcherAdoptions.getAdoptions(null, null, null);
    }

    @Override
    protected Fragment createInitialFragment() {
        return new LoadingScreenFragment();
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
        if (adoption.getLexiconId() == null) {
            Toast.makeText(this, R.string.adoption_no_detail_toast, Toast.LENGTH_SHORT).show();

            return;
        }

        // Check if there's not a split view and therefore we're not working with a tablet
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = AnimalDetailActivity.newIntent(this, adoption.getLexiconId());
            startActivity(intent);
        } else {
            Fragment animalDetail = AnimalFragment.newInstance(adoption.getLexiconId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, animalDetail)
                    .commit();
        }
    }
}
