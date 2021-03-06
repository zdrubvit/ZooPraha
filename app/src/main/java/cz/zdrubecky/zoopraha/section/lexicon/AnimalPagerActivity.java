package cz.zdrubecky.zoopraha.section.lexicon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cz.zdrubecky.zoopraha.R;
import cz.zdrubecky.zoopraha.manager.AnimalManager;
import cz.zdrubecky.zoopraha.model.Animal;

public class AnimalPagerActivity extends AppCompatActivity {
    private static final String EXTRA_ANIMAL_ID = "cz.zdrubecky.zoopraha.animal_id";

    private ViewPager mViewPager;
    private List<Animal> mAnimals;
    private AnimalManager mAnimalManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_pager);

        mAnimalManager = new AnimalManager(this);

        mViewPager = (ViewPager) findViewById(R.id.activity_animal_pager_view_pager);

        String searchQuery = LexiconPreferences.getSearchQuery(this);

        // Check if there's a search query present
        if (searchQuery != null) {
            ArrayList<String> whereArgs = new ArrayList<>();
            whereArgs.add(LexiconPreferences.getFilterValue(this));

            mAnimals = mAnimalManager.searchAnimals(
                    mAnimalManager.createWhereClauseFromFilter(LexiconPreferences.getFilterKey(this)),
                    whereArgs,
                    searchQuery
            );
        } else {
            mAnimals = mAnimalManager.getAnimals(
                    mAnimalManager.createWhereClauseFromFilter(LexiconPreferences.getFilterKey(this)),
                    new String[]{LexiconPreferences.getFilterValue(this)}
            );
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Set the adapter with the newly created fragment manager
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Animal animal = mAnimals.get(position);

                return AnimalDetailFragment.newInstance(animal.getId());
            }

            @Override
            public int getCount() {
                return mAnimals.size();
            }
        });

        // Take the ID from an incoming intent and use it to get the current item + set it inside the pager
        String animalId = (String) getIntent().getSerializableExtra(EXTRA_ANIMAL_ID);
        for (int i = 0; i < mAnimals.size(); i++) {
            if (mAnimals.get(i).getId().equals(animalId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    public static Intent newIntent(Context packageContext, String animalId) {
        Intent intent = new Intent(packageContext, AnimalPagerActivity.class);
        intent.putExtra(EXTRA_ANIMAL_ID, animalId);

        return intent;
    }
}
