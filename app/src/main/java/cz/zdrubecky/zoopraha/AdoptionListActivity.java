package cz.zdrubecky.zoopraha;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import cz.zdrubecky.zoopraha.model.Adoption;

public class AdoptionListActivity
        extends SingleFragmentActivity
        implements AdoptionListFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new AdoptionListFragment();
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
