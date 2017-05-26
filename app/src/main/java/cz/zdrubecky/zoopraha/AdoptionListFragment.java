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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cz.zdrubecky.zoopraha.manager.AdoptionsManager;
import cz.zdrubecky.zoopraha.model.Adoption;

public class AdoptionListFragment extends Fragment {
    private static final String TAG = "AdoptionListFragment";

    private Callbacks mCallbacks;
    private View mView;
    private RecyclerView mAdoptionRecyclerView;
    private AdoptionAdapter mAdoptionAdapter;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adoption_list, container, false);

        // Save the view so it can be worked on even before this method finishes
        mView = view;

        mAdoptionRecyclerView = (RecyclerView) view.findViewById(R.id.adoption_recycler_view);
        mAdoptionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    public void updateUI() {
        AdoptionsManager adoptionsManager = AdoptionsManager.get(getActivity());
        List<Adoption> adoptions = adoptionsManager.getAdoptions();

        if (adoptions.size() > 0) {
            // If the fragment is already running, update the data in case something changed (some crime)
            if (mAdoptionAdapter == null) {
                mAdoptionAdapter = new AdoptionAdapter(adoptions);
                mAdoptionRecyclerView.setAdapter(mAdoptionAdapter);
            } else {
                mAdoptionAdapter.setAdoptions(adoptions);
                mAdoptionAdapter.notifyDataSetChanged();
            }
        } else {
            RelativeLayout emptyList = (RelativeLayout) mView.findViewById(R.id.adoption_list_empty);
            emptyList.setVisibility(View.VISIBLE);
        }
    }

    private class AdoptionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Adoption mAdoption;
        private TextView mNameTextView;
        private TextView mPriceTextView;
        private TextView mVisitTetView;

        public AdoptionHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_adoption_name_textview);
            mPriceTextView = (TextView) itemView.findViewById(R.id.list_item_adoption_price_textview);
            mVisitTetView = (TextView) itemView.findViewById(R.id.list_item_adoption_visit_textview);
        }

        public void bindAdoption(Adoption adoption) {
            mAdoption = adoption;

            mNameTextView.setText(mAdoption.getName());
            mPriceTextView.setText(Integer.toString(mAdoption.getPrice()));
            mVisitTetView.setText(mAdoption.isVisit() ? "yes" : "no");
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

            View v = inflater.inflate(R.layout.list_item_adoption, parent, false);

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
