package cz.zdrubecky.zoopraha.section.event;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.zdrubecky.zoopraha.R;
import cz.zdrubecky.zoopraha.manager.EventManager;
import cz.zdrubecky.zoopraha.model.Event;

public class EventListFragment extends Fragment {
    private static final String TAG = "EventListFragment";
    
    private Callbacks mCallbacks;
    private EventManager mEventManager;
    private RecyclerView mEventRecyclerView;
    private View mView;
    private EventAdapter mEventAdapter;
    
    public interface Callbacks {
        void onEventSelected(Event event);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;
        if (context instanceof Activity) {
            a = (Activity) context;

            // Make sure the parent activity implements the interface
            mCallbacks = (EventListFragment.Callbacks) a;
        } else {
            Log.d(TAG, "The attaching context is not an activity.");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEventManager = new EventManager(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list, container, false);

        // Save the view so it can be worked on even before this method finishes
        mView = view;

        mEventRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mEventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    public void updateUI() {
        List<Event> events = mEventManager.getEvents();

        if (events.size() > 0) {
            // If the fragment is already running, update the data in case something changed (some event)
            if (mEventAdapter == null) {
                mEventAdapter = new EventListFragment.EventAdapter(events);
                mEventRecyclerView.setAdapter(mEventAdapter);
            } else {
                mEventAdapter.setEvents(events);
                mEventAdapter.notifyDataSetChanged();
            }
        } else {
            RelativeLayout emptyList = (RelativeLayout) mView.findViewById(R.id.list_empty);
            emptyList.setVisibility(View.VISIBLE);
        }
    }

    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Event mEvent;
        private TextView mNameTextView;
        private TextView mDatesTextView;

        public EventHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mNameTextView = (TextView) itemView.findViewById(R.id.fragment_event_list_item_name_textview);
            mDatesTextView = (TextView) itemView.findViewById(R.id.fragment_event_list_item_dates_textview);
        }

        public void bindEvent(Event event) {
            mEvent = event;

            mNameTextView.setText(mEvent.getName());

            // Create and insert the event's date
            String dateString;
            SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", new Locale("cs"));
            SimpleDateFormat newDateFormat = new SimpleDateFormat("d. M. yyyy, HH:mm", new Locale("cs"));

            try {
                String suffix = ", 00:00";
                String startDate = newDateFormat.format(isoDateFormat.parse(mEvent.getStart()));

                // Check for the midnight time and remove it
                if (startDate.endsWith(suffix)) {
                    startDate = startDate.replace(suffix, "");
                }

                dateString = getString(R.string.fragment_event_list_item_date, startDate);
            } catch (ParseException pe) {
                dateString = getString(R.string.fragment_event_list_item_date_unknown);

                Log.e(TAG, "An exception has been thrown during the parsing of an event's date:" + pe.toString());
            }

            mDatesTextView.setText(dateString);
        }

        @Override
        public void onClick(View v) {
            mCallbacks.onEventSelected(mEvent);
        }
    }

    private class EventAdapter extends RecyclerView.Adapter<EventListFragment.EventHolder> {
        private List<Event> mEvents;

        public EventAdapter(List<Event> events) {
            mEvents = events;
        }

        @Override
        public EventListFragment.EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            View v = inflater.inflate(R.layout.fragment_event_list_item, parent, false);

            return new EventListFragment.EventHolder(v);
        }

        @Override
        public void onBindViewHolder(EventListFragment.EventHolder holder, int position) {
            Event event = mEvents.get(position);

            holder.bindEvent(event);
        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }

        public void setEvents(List<Event> events) {
            mEvents = events;
        }
    }
}
