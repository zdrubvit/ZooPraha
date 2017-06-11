package cz.zdrubecky.zoopraha.section.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.zdrubecky.zoopraha.R;
import cz.zdrubecky.zoopraha.manager.EventManager;
import cz.zdrubecky.zoopraha.model.Event;

public class EventDetailFragment extends Fragment {
    private static final String TAG = "EventDetailFragment";
    private static final String ARG_EVENT_ID = "event_id";

    private TextView mStartTextView;
    private TextView mEndTextView;
    private TextView mDurationTextView;
    private TextView mDescriptionTextView;
    private TextView mNameTextView;

    private EventManager mEventManager;
    private Event mEvent;

    public static EventDetailFragment newInstance(String eventId) {
        Bundle args = new Bundle();
        // Save the ID in fragment rather than parent activity so that they can be decoupled and function independently
        args.putSerializable(ARG_EVENT_ID, eventId);
        
        EventDetailFragment fragment = new EventDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEventManager = new EventManager(getActivity());

        String eventId = (String) getArguments().getSerializable(ARG_EVENT_ID);

        mEvent = mEventManager.getEvent(eventId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event, container, false);

        mNameTextView = (TextView) v.findViewById(R.id.fragment_event_name_textview);
        mNameTextView.setText(mEvent.getName());
        mStartTextView = (TextView) v.findViewById(R.id.fragment_event_start_textview);
        mStartTextView.setText(mEvent.getStart());
        mEndTextView = (TextView) v.findViewById(R.id.fragment_event_end_textview);
        mEndTextView.setText(mEvent.getEnd());
        mDurationTextView = (TextView) v.findViewById(R.id.fragment_event_duration_textview);
        mDurationTextView.setText(Integer.toString(mEvent.getDuration()));
        mDescriptionTextView = (TextView) v.findViewById(R.id.fragment_event_description_textview);
        mDescriptionTextView.setText(mEvent.getDescription());

        return v;
    }
}
