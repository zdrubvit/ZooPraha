package cz.zdrubecky.zoopraha;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    // Keeps track of the activity's visibility
    protected boolean isResumed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(getFragmentContainerId());

        // If there's not a fragment present yet, create the initial one and add it to the manager
        if (fragment == null) {
            fragment = createInitialFragment();

            fm.beginTransaction()
                .add(getFragmentContainerId(), fragment)
                .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        isResumed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        isResumed = false;
    }

    // Get the ID of the layout that includes fragment containers
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    // Get the ID of the fragment's View
    @IdRes
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }

    // Move up to the activity's parent when the back button is pressed
    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    // Get the specific initial fragment, the default one is an empty loading screen
    protected Fragment createInitialFragment() {
        return new LoadingScreenFragment();
    };

    // Replace the current fragment with a new one
    protected void replaceFragment() {
        // Only make the operation when the activity is in front, because it's usually called from a background thread and the UI might've changed since
        if (isResumed) {
            Fragment fragment = createReplacementFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(getFragmentContainerId(), fragment)
                    .commit();
        }
    }

    // Return a specific fragment that takes the spotlight - it has to be implemented for the replacement to work
    protected abstract Fragment createReplacementFragment();
}
