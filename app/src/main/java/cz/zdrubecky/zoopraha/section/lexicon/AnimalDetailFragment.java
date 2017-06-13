package cz.zdrubecky.zoopraha.section.lexicon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import cz.zdrubecky.zoopraha.R;
import cz.zdrubecky.zoopraha.api.DataFetcher;
import cz.zdrubecky.zoopraha.api.ImageLoader;
import cz.zdrubecky.zoopraha.manager.AnimalManager;
import cz.zdrubecky.zoopraha.model.Animal;
import cz.zdrubecky.zoopraha.model.JsonApiObject;

public class AnimalDetailFragment extends DialogFragment {
    private static final String TAG = "AnimalDetailFragment";
    private static final String ARG_ANIMAL_ID = "animal_id";
    private static final String DIALOG_IMAGE = "DialogImage";

    private View mView;
    private ImageView mImageImageView;

    private AnimalManager mAnimalManager;
    private Animal mAnimal;

    public static AnimalDetailFragment newInstance(String animalId) {
        Bundle args = new Bundle();
        // Save the ID in fragment rather than parent activity so that they can be decoupled and function independently
        args.putSerializable(ARG_ANIMAL_ID, animalId);
        
        AnimalDetailFragment fragment = new AnimalDetailFragment();
        fragment.setArguments(args);

        // Override the default capability to be displayed as a dialog, so that it acts as a standard fragment until being treated otherwise
        fragment.setShowsDialog(false);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAnimalManager = new AnimalManager(getActivity());

        String animalId = (String) getArguments().getSerializable(ARG_ANIMAL_ID);

        mAnimal = mAnimalManager.getAnimal(animalId);

        // If the animal is not in the local DB, make a request to the API
        if (mAnimal == null) {
            new SaveAnimalTask(animalId).execute();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_animal, container, false);
        
        // Save a reference to the view so it can be accessed from outside lifecycle methods
        mView = v;

        // If the fragment acts as a dialog, respond to a touch event by closing it
        if (getShowsDialog()) {
            // Use the root scroll view as a basis for catching the events
            View rootView = v.findViewById(R.id.fragment_animal_scrollview);
            if (rootView != null) {
                rootView.setOnTouchListener(new View.OnTouchListener() {
                    int mLastAction = 0;
                    int mSecondToLastAction = 0;
                    
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // Tolerate one move event after the initial down event before dismissing the dialog
                        if (event.getActionMasked() == MotionEvent.ACTION_UP
                                && (mLastAction == MotionEvent.ACTION_DOWN
                                    || (mLastAction == MotionEvent.ACTION_MOVE && mSecondToLastAction == MotionEvent.ACTION_DOWN))
                        ) {
                            dismiss();

                            return true;
                        }

                        mSecondToLastAction = mLastAction;
                        mLastAction = event.getActionMasked();

                        // Mark the touch event as "not consumed" and let the listener propagate it further
                        return false;
                    }
                });
            }
        }

        // Check if the animal object is present a fill the view with it
        if (mAnimal != null) {
            setAnimalToView();
        }
        
        return v;
    }
    
    private void setAnimalToView() {
        TextView nameTextView = (TextView) mView.findViewById(R.id.fragment_animal_name_textview);
        setAttributeText(nameTextView, mAnimal.getName() + " (" + mAnimal.getLatinName() + ")");

        mImageImageView = (ImageView) mView.findViewById(R.id.fragment_animal_image_imageview);
        if (!mAnimal.getImage().equals("")) {
            // If there's an image present, place it in the view
            mImageImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // Wait for the view being rendered so that its dimensions are known and Picasso can use them to resize the image
                    ImageLoader.getInstance(getActivity()).loadImage(
                            mAnimal.getImage(),
                            mImageImageView
                    );

                    mImageImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });

            // Show an enlarged image detail on click through a dialog fragment
            mImageImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    ImageDialogFragment fragment = ImageDialogFragment.newInstance(mAnimal.getImage());

                    fragment.show(manager, DIALOG_IMAGE);
                }
            });
        } else {
            // Hide the view otherwise
            mImageImageView.setVisibility(View.GONE);
        }

        TextView classTextView = (TextView) mView.findViewById(R.id.fragment_animal_class_textview);
        setAttributeText(classTextView, mAnimal.getClassName() + " (" + mAnimal.getClassLatinName() + ")");
        TextView orderTextView = (TextView) mView.findViewById(R.id.fragment_animal_order_textview);
        setAttributeText(orderTextView, mAnimal.getOrderName() + " (" + mAnimal.getOrderLatinName() + ")");
        TextView descriptionTextView = (TextView) mView.findViewById(R.id.fragment_animal_description_textview);
        setAttributeText(descriptionTextView, mAnimal.getDescription());
        TextView distributionTextView = (TextView) mView.findViewById(R.id.fragment_animal_distribution_textview);
        setAttributeText(distributionTextView, mAnimal.getDistribution());
        TextView continentsTextView = (TextView) mView.findViewById(R.id.fragment_animal_continents_textview);
        setAttributeText(continentsTextView, mAnimal.getContinents());
        TextView biotopesTextView = (TextView) mView.findViewById(R.id.fragment_animal_biotopes_textview);
        setAttributeText(biotopesTextView, mAnimal.getBiotopesDetail());
        TextView foodTextView = (TextView) mView.findViewById(R.id.fragment_animal_food_textview);
        setAttributeText(foodTextView, mAnimal.getFoodDetail());
        TextView proportionsTextView = (TextView) mView.findViewById(R.id.fragment_animal_proportions_textview);
        setAttributeText(proportionsTextView, mAnimal.getProportions());
        TextView reproductionTextView = (TextView) mView.findViewById(R.id.fragment_animal_reproduction_textview);
        setAttributeText(reproductionTextView, mAnimal.getReproduction());
        TextView attractionsTextView = (TextView) mView.findViewById(R.id.fragment_animal_attractions_textview);
        setAttributeText(attractionsTextView, mAnimal.getAttractions());
        TextView locationTextView = (TextView) mView.findViewById(R.id.fragment_animal_location_textview);
        setAttributeText(locationTextView, mAnimal.getLocation());
        TextView breedingTextView = (TextView) mView.findViewById(R.id.fragment_animal_breeding_textview);
        setAttributeText(breedingTextView, mAnimal.getBreeding());
        TextView projectsTextView = (TextView) mView.findViewById(R.id.fragment_animal_projects_textview);
        setAttributeText(projectsTextView, mAnimal.getProjects());
    }

    // A helper method to set a text to a TextView if it's non-empty - the View is hidden otherwise
    private void setAttributeText(TextView attribute, String text) {
        if (text != null && !text.equals("")) {
            attribute.setText(text);
        } else {
            LinearLayout parent = (LinearLayout) attribute.getParent();
            parent.setVisibility(View.GONE);
        }
    }

    private class SaveAnimalTask extends AsyncTask<Void, Void, Boolean> {
        private String mAnimalId;

        public SaveAnimalTask(String animalId) {
            mAnimalId = animalId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DataFetcher dataFetcher = new DataFetcher(getActivity());

            JsonApiObject response = dataFetcher.getAnimal(mAnimalId);

            if (response != null && response.getMeta().getCount() == 1) {
                Gson gson = new Gson();
                List<JsonApiObject.Resource> responseData = response.getData();

                mAnimal = gson.fromJson(responseData.get(0).getDocument(), Animal.class);
                mAnimal.setId(responseData.get(0).getId());

                mAnimalManager.addAnimal(mAnimal);
                mAnimalManager.flushAnimals();

                Log.i(TAG, "A new animal with an ID "+ mAnimal.getId() + " received and saved.");

                return true;
            }

            // If the call resulted in an error or no animal's been received, notify the UI thread
            return false;
        }

        @Override
        protected void onPostExecute(Boolean animalAdded) {
            // If the animal's been correctly imported and saved, display it
            if (animalAdded.equals(true)) {
                setAnimalToView();
            } else {
                Toast.makeText(getActivity(), getString(R.string.fragment_animal_no_animal_error_text), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
