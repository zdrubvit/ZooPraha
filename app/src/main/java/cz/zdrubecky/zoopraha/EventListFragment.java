package cz.zdrubecky.zoopraha;

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

import java.util.List;

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
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        // Save the view so it can be worked on even before this method finishes
        mView = view;

        mEventRecyclerView = (RecyclerView) view.findViewById(R.id.event_recycler_view);
        mEventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    public void updateUI() {
        List<Event> events = mEventManager.getEvents();

        if (events.size() > 0) {
            // If the fragment is already running, update the data in case something changed (some crime)
            if (mEventAdapter == null) {
                mEventAdapter = new EventListFragment.EventAdapter(events);
                mEventRecyclerView.setAdapter(mEventAdapter);
            } else {
                mEventAdapter.setEvents(events);
                mEventAdapter.notifyDataSetChanged();
            }
        } else {
            RelativeLayout emptyList = (RelativeLayout) mView.findViewById(R.id.event_list_empty);
            emptyList.setVisibility(View.VISIBLE);
        }
    }

    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Event mEvent;
        private TextView mNameTextView;
        private TextView mStartTextView;
        private TextView mEndTextView;

        public EventHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_event_name_textview);
            mStartTextView = (TextView) itemView.findViewById(R.id.list_item_event_start_textview);
            mEndTextView = (TextView) itemView.findViewById(R.id.list_item_event_end_textview);
        }

        public void bindEvent(Event event) {
            mEvent = event;

            mNameTextView.setText(mEvent.getName());
            mStartTextView.setText(mEvent.getStart());
            mEndTextView.setText(mEvent.getEnd());
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

            View v = inflater.inflate(R.layout.list_item_event, parent, false);

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
