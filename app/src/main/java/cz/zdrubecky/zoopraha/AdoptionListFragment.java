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

import cz.zdrubecky.zoopraha.manager.AdoptionManager;
import cz.zdrubecky.zoopraha.model.Adoption;

public class AdoptionListFragment extends Fragment {
    private static final String TAG = "AdoptionListFragment";

    private View mView;
    private RecyclerView mAdoptionRecyclerView;
    private AdoptionAdapter mAdoptionAdapter;
    private AdoptionManager mAdoptionManager;

    private Callbacks mCallbacks;

    // The interface used to communicate with the parent activity
    public interface Callbacks {
        // Through this method is the activity notified of a selected list item
        void onAdoptionSelected(Adoption adoption);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;
        if (context instanceof Activity) {
            a = (Activity) context;

            // Make sure the parent activity implements the interface
            mCallbacks = (Callbacks) a;
        } else {
            Log.d(TAG, "The attaching context is not an activity.");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdoptionManager = new AdoptionManager(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list, container, false);

        // Save the view so it can be worked on even before this method finishes
        mView = view;

        mAdoptionRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdoptionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    public void updateUI() {
        List<Adoption> adoptions = mAdoptionManager.getAdoptions();

        if (adoptions.size() > 0) {
            // If the fragment is already running, update the data in case something changed (some adoption)
            if (mAdoptionAdapter == null) {
                mAdoptionAdapter = new AdoptionAdapter(adoptions);
                mAdoptionRecyclerView.setAdapter(mAdoptionAdapter);
            } else {
                mAdoptionAdapter.setAdoptions(adoptions);
                mAdoptionAdapter.notifyDataSetChanged();
            }
        } else {
            RelativeLayout emptyList = (RelativeLayout) mView.findViewById(R.id.list_empty);
            emptyList.setVisibility(View.VISIBLE);
        }
    }

    private class AdoptionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Adoption mAdoption;
        private TextView mNameTextView;
        private TextView mPriceTextView;
        private TextView mVisitTextView;

        public AdoptionHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mNameTextView = (TextView) itemView.findViewById(R.id.fragment_adoption_list_item_name_textview);
            mPriceTextView = (TextView) itemView.findViewById(R.id.fragment_adoption_list_item_price_textview);
            mVisitTextView = (TextView) itemView.findViewById(R.id.fragment_adoption_list_item_visit_textview);
        }

        public void bindAdoption(Adoption adoption) {
            mAdoption = adoption;

            mNameTextView.setText(mAdoption.getName());
            mPriceTextView.setText(Integer.toString(mAdoption.getPrice()));
            mVisitTextView.setText(mAdoption.isVisit() ? "yes" : "no");
        }

        @Override
        public void onClick(View v) {
            mCallbacks.onAdoptionSelected(mAdoption);
        }
    }

    private class AdoptionAdapter extends RecyclerView.Adapter<AdoptionHolder> {
        private List<Adoption> mAdoptions;

        public AdoptionAdapter(List<Adoption> adoptions) {
            mAdoptions = adoptions;
        }

        @Override
        public AdoptionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            View v = inflater.inflate(R.layout.fragment_adoption_list_item, parent, false);

            return new AdoptionHolder(v);
        }

        @Override
        public void onBindViewHolder(AdoptionHolder holder, int position) {
            Adoption adoption = mAdoptions.get(position);

            holder.bindAdoption(adoption);
        }

        @Override
        public int getItemCount() {
            return mAdoptions.size();
        }

        public void setAdoptions(List<Adoption> adoptions) {
            mAdoptions = adoptions;
        }
    }
}
