package cz.zdrubecky.zoopraha;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cz.zdrubecky.zoopraha.manager.AnimalManager;
import cz.zdrubecky.zoopraha.model.Animal;

public class AnimalFragment extends DialogFragment {
    private static final String TAG = "AnimalFragment";
    private static final String ARG_ANIMAL_ID = "animal_id";

    private TextView mNameTextView;
    private ImageView mImageImageView;
    private TextView mClassTextView;
    private TextView mOrderTextView;
    private TextView mDescriptionTextView;
    private TextView mContinentsTextView;
    private TextView mDistributionTextView;
    private TextView mBiotopesTextView;
    private TextView mFoodTextView;
    private TextView mProportionsTextView;
    private TextView mReproductionTextView;
    private TextView mAttractionsTextView;
    private TextView mLocationTextView;
    private TextView mBreedingTextView;
    private TextView mProjectsTextView;

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
        setAttributeText(mNameTextView, mAnimal.getName() + " (" + mAnimal.getLatinName() + ")");
        mClassTextView = (TextView) v.findViewById(R.id.fragment_animal_class_textview);
        setAttributeText(mClassTextView, mAnimal.getClass() + " (" + mAnimal.getClassLatinName() + ")");
        mOrderTextView = (TextView) v.findViewById(R.id.fragment_animal_order_textview);
        setAttributeText(mOrderTextView, mAnimal.getClass() + " (" + mAnimal.getOrderLatinName() + ")");
        mDescriptionTextView = (TextView) v.findViewById(R.id.fragment_animal_description_textview);
        setAttributeText(mDescriptionTextView, mAnimal.getDescription());
        mDistributionTextView = (TextView) v.findViewById(R.id.fragment_animal_distribution_textview);
        setAttributeText(mDistributionTextView, mAnimal.getDistribution());
        mContinentsTextView = (TextView) v.findViewById(R.id.fragment_animal_continents_textview);
        setAttributeText(mContinentsTextView, mAnimal.getContinents());
        mBiotopesTextView = (TextView) v.findViewById(R.id.fragment_animal_biotopes_textview);
        setAttributeText(mBiotopesTextView, mAnimal.getBiotopesDetail());
        mFoodTextView = (TextView) v.findViewById(R.id.fragment_animal_food_textview);
        setAttributeText(mFoodTextView, mAnimal.getFoodDetail());
        mProportionsTextView = (TextView) v.findViewById(R.id.fragment_animal_proportions_textview);
        setAttributeText(mProportionsTextView, mAnimal.getProportions());
        mReproductionTextView = (TextView) v.findViewById(R.id.fragment_animal_reproduction_textview);
        setAttributeText(mReproductionTextView, mAnimal.getReproduction());
        mAttractionsTextView = (TextView) v.findViewById(R.id.fragment_animal_attractions_textview);
        setAttributeText(mAttractionsTextView, mAnimal.getProportions());
        mLocationTextView = (TextView) v.findViewById(R.id.fragment_animal_location_textview);
        setAttributeText(mLocationTextView, mAnimal.getLocation());
        mBreedingTextView = (TextView) v.findViewById(R.id.fragment_animal_breeding_textview);
        setAttributeText(mBreedingTextView, mAnimal.getBreeding());
        mProjectsTextView = (TextView) v.findViewById(R.id.fragment_animal_projects_textview);
        setAttributeText(mProjectsTextView, mAnimal.getProjects());

        return v;
    }

    // A helper method to set a text to a TextView if it's not empty - otherwise the View is hidden
    private void setAttributeText(TextView attribute, String text) {
        if (text != null && !text.equals("")) {
            attribute.setText(text);
        } else {
            LinearLayout parent = (LinearLayout) attribute.getParent();
            parent.setVisibility(View.GONE);
        }
    }
}
