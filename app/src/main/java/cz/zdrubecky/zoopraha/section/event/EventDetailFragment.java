package cz.zdrubecky.zoopraha.section.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

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

        // Create and insert the event's dates
        String startDateString;
        String endDateString;
        SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", new Locale("cs"));
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd. MM. yyyy, HH:mm", new Locale("cs"));

        mStartTextView = (TextView) v.findViewById(R.id.fragment_event_start_textview);
        try {
            String startDateSuffix = ", 00:00";

            String startDate = newDateFormat.format(isoDateFormat.parse(mEvent.getStart()));

            // Check for the midnight time and remove it
            if (startDate.endsWith(startDateSuffix)) {
                startDate = startDate.replace(startDateSuffix, "");
            }

            startDateString = startDate;
        } catch (ParseException pe) {
            startDateString = getString(R.string.fragment_event_list_item_date_unknown);

            Log.e(TAG, "An exception has been thrown during the parsing of an event's date:" + pe.toString());
        }
        mStartTextView.setText(startDateString);

        mEndTextView = (TextView) v.findViewById(R.id.fragment_event_end_textview);
        try {
            String endDateSuffix = ", 23:59";

            String endDate = newDateFormat.format(isoDateFormat.parse(mEvent.getEnd()));

            if (endDate.endsWith(endDateSuffix)) {
                endDate = endDate.replace(endDateSuffix, "");
            }

            endDateString = endDate;
        } catch (ParseException pe) {
            endDateString = getString(R.string.fragment_event_list_item_date_unknown);

            Log.e(TAG, "An exception has been thrown during the parsing of an event's date:" + pe.toString());
        }
        mEndTextView.setText(endDateString);

        // Calculate the duration in hours and minutes using plural strings
        mDurationTextView = (TextView) v.findViewById(R.id.fragment_event_duration_textview);
        int hours = mEvent.getDuration() / 60;
        String hourString = getResources().getQuantityString(R.plurals.hours, hours, hours);
        int minutes = mEvent.getDuration() % 60;
        String minuteString = getResources().getQuantityString(R.plurals.minutes, minutes, minutes);
        String durationString = getString(R.string.fragment_event_duration, hourString, minuteString);
        mDurationTextView.setText(durationString);

        mDescriptionTextView = (TextView) v.findViewById(R.id.fragment_event_description_textview);
        mDescriptionTextView.setText(mEvent.getDescription());

        return v;
    }
}
