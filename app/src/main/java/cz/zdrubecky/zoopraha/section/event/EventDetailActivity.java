package cz.zdrubecky.zoopraha.section.event;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import cz.zdrubecky.zoopraha.SingleFragmentActivity;

public class EventDetailActivity extends SingleFragmentActivity {
    private static final String ARG_EVENT_ID = "event_id";
    private static final String EXTRA_EVENT_ID = "cz.zdrubecky.zoopraha.event_id";

    public static Intent newIntent(Context packageContext, String eventId) {
        Intent intent = new Intent(packageContext, EventDetailActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);

        return intent;
    }

    @Override
    protected Fragment createInitialFragment() {
        return EventDetailFragment.newInstance((String) getIntent().getSerializableExtra(EXTRA_EVENT_ID));
    }
}
