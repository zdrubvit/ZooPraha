package cz.zdrubecky.zoopraha;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cz.zdrubecky.zoopraha.manager.AdoptionsManager;
import cz.zdrubecky.zoopraha.model.Adoption;

public class AdoptionFragment extends Fragment {
    private static final String TAG = "CrimeFragment";
    private static final String ARG_ADOPTION_ID = "adoption_id";

    private Adoption mAdoption;

    public static AdoptionFragment newInstance(String adoptionId) {
        Bundle args = new Bundle();
        // Save the ID in fragment rather than parent activity so that they can be decoupled and function independently
        args.putSerializable(ARG_ADOPTION_ID, adoptionId);
        
        AdoptionFragment fragment = new AdoptionFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String adoptionId = (String) getArguments().getSerializable(ARG_ADOPTION_ID);

        // Due to the JAVA's pointers passed by value, the crime is now kept in a lab and can be modified from outside
        mAdoption = AdoptionsManager.get(getActivity()).getAnimal(adoptionId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_animal, container, false);

        return v;
    }
}
