package cz.zdrubecky.zoopraha.section.lexicon;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import cz.zdrubecky.zoopraha.SingleFragmentActivity;

public class AnimalDetailActivity extends SingleFragmentActivity {
    private static final String EXTRA_ANIMAL_ID = "cz.zdrubecky.zoopraha.animal_id";

    public static Intent newIntent(Context packageContext, String animalId) {
        Intent intent = new Intent(packageContext, AnimalDetailActivity.class);
        intent.putExtra(EXTRA_ANIMAL_ID, animalId);

        return intent;
    }

    @Override
    protected Fragment createInitialFragment() {
        return AnimalDetailFragment.newInstance((String) getIntent().getSerializableExtra(EXTRA_ANIMAL_ID));
    }

    @Override
    protected Fragment createReplacementFragment() {
        return createInitialFragment();
    }
}
