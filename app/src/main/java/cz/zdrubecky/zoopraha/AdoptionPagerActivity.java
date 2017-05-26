package cz.zdrubecky.zoopraha;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import cz.zdrubecky.zoopraha.manager.AdoptionsManager;
import cz.zdrubecky.zoopraha.model.Adoption;

public class AdoptionPagerActivity extends AppCompatActivity {
    private static final String EXTRA_ADOPTION_ID = "cz.zdrubecky.zoopraha.adoption_id";

    private ViewPager mViewPager;
    private List<Adoption> mAdoptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption_pager);

        mViewPager = (ViewPager) findViewById(R.id.activity_adoption_pager_view_pager);
        mAdoptions = AdoptionsManager.get(this).getAdoptions();

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Set the adapter with the newly created fragment manager
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Adoption adoption = mAdoptions.get(position);

                return AdoptionFragment.newInstance(adoption.getId());
            }

            @Override
            public int getCount() {
                return mAdoptions.size();
            }
        });

        // Take the ID from an incoming intent and use it to get the current item + set it inside the pager
        String adoptionId = (String) getIntent().getSerializableExtra(EXTRA_ADOPTION_ID);
        for (int i = 0; i < mAdoptions.size(); i++) {
            if (mAdoptions.get(i).getId() == adoptionId) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    public static Intent newIntent(Context packageContext, String adoptionId) {
        Intent intent = new Intent(packageContext, AdoptionPagerActivity.class);
        intent.putExtra(EXTRA_ADOPTION_ID, adoptionId);

        return intent;
    }
}
