package cz.zdrubecky.zoopraha;

import android.content.Intent;
import android.support.v4.app.Fragment;

import cz.zdrubecky.zoopraha.model.Event;

public class EventListActivity
        extends SingleFragmentActivity
        implements EventListFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new EventListFragment();
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
            Fragment eventDetail = EventFragment.newInstance(event.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, eventDetail)
                    .commit();
        }
    }
}
