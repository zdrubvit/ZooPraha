package cz.zdrubecky.zoopraha;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {
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

    // Get the ID of the layout that includes fragment containers
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }
    
    @IdRes
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }

    // Move up to the activity's parent when the back button is pressed
    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    // Get the specific fragment - all the children have to implement this method
    protected abstract Fragment createInitialFragment();
}
