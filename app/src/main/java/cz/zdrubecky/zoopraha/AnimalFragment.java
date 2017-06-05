package cz.zdrubecky.zoopraha;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.zdrubecky.zoopraha.manager.AnimalManager;
import cz.zdrubecky.zoopraha.model.Animal;

public class AnimalFragment extends DialogFragment {
    private static final String TAG = "AnimalFragment";
    private static final String ARG_ANIMAL_ID = "animal_id";

    private TextView mNameTextView;
    private TextView mDescriptionTextView;
    private TextView mReproductionTextView;

    private AnimalManager mAnimalManager;
    private Animal mAnimal;

    public static AnimalFragment newInstance(String animalId) {
        Bundle args = new Bundle();
        // Save the ID in fragment rather than parent activity so that they can be decoupled and function independently
        args.putSerializable(ARG_ANIMAL_ID, animalId);
        
        AnimalFragment fragment = new AnimalFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAnimalManager = new AnimalManager(getActivity());

        String animalId = (String) getArguments().getSerializable(ARG_ANIMAL_ID);

        // todo check for the animal in the db first, then download it - everything in the manager
        mAnimal = mAnimalManager.getAnimal(animalId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_animal, container, false);

        mNameTextView = (TextView) v.findViewById(R.id.fragment_animal_name_textview);
        mNameTextView.setText(mAnimal.getName());
        mDescriptionTextView = (TextView) v.findViewById(R.id.fragment_animal_description_textview);
        mDescriptionTextView.setText(mAnimal.getDescription());
        mReproductionTextView = (TextView) v.findViewById(R.id.fragment_animal_reproduction_textview);
        mReproductionTextView.setText(mAnimal.getReproduction());

        return v;
    }
}
