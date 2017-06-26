package cz.zdrubecky.zoopraha.section.lexicon;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cz.zdrubecky.zoopraha.R;
import cz.zdrubecky.zoopraha.api.ImageLoader;
import cz.zdrubecky.zoopraha.manager.AnimalManager;
import cz.zdrubecky.zoopraha.model.Animal;

public class LexiconListFragment extends Fragment {
    private static final String TAG = "AnimalListFragment";

    private View mView;
    private RecyclerView mAnimalRecyclerView;
    private AnimalAdapter mAnimalAdapter;
    private AnimalManager mAnimalManager;

    private Callbacks mCallbacks;

    // The interface used to communicate with the parent activity
    public interface Callbacks {
        // Through this method is the activity notified of a selected list item
        void onAnimalSelected(Animal animal);
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

        mAnimalManager = new AnimalManager(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list, container, false);

        // Save the view so it can be worked on even before this method finishes
        mView = view;

        // Get the recyclerview, provide it with a layout manager and setup the pre-fetching options
        mAnimalRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAnimalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAnimalRecyclerView.setHasFixedSize(true);
        mAnimalRecyclerView.setItemViewCacheSize(20);
        mAnimalRecyclerView.setDrawingCacheEnabled(true);

        updateUI();

        return view;
    }

    public void updateUI() {
        // Filter the animals according to the supplied key, shared by the lexicon menu activity
        List<Animal> animals;

        String searchQuery = LexiconPreferences.getSearchQuery(getActivity());

        // Check if there's a search query present
        if (searchQuery != null) {
            ArrayList<String> whereArgs = new ArrayList<>();
            whereArgs.add(LexiconPreferences.getFilterValue(getActivity()));

            animals = mAnimalManager.searchAnimals(
                    mAnimalManager.createWhereClauseFromFilter(LexiconPreferences.getFilterKey(getActivity())),
                    whereArgs,
                    searchQuery
            );
        } else {
            animals = mAnimalManager.getAnimals(
                    mAnimalManager.createWhereClauseFromFilter(LexiconPreferences.getFilterKey(getActivity())),
                    new String[]{LexiconPreferences.getFilterValue(getActivity())}
            );
        }

        if (animals.size() > 0) {
            // If the fragment is already running, update the data in case something changed (some animal)
            if (mAnimalAdapter == null) {
                mAnimalAdapter = new AnimalAdapter(animals);
                mAnimalRecyclerView.setAdapter(mAnimalAdapter);
            } else {
                mAnimalAdapter.setAnimals(animals);
                mAnimalAdapter.notifyDataSetChanged();
            }
        } else {
            RelativeLayout emptyList = (RelativeLayout) mView.findViewById(R.id.list_empty);
            emptyList.setVisibility(View.VISIBLE);
        }
    }

    private class AnimalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Animal mAnimal;
        private ImageView mImageImageView;
        private TextView mNameTextView;
        private TextView mTaxonomyTextView;

        public AnimalHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mImageImageView = (ImageView) itemView.findViewById(R.id.fragment_lexicon_list_item_image_imageview);
            mNameTextView = (TextView) itemView.findViewById(R.id.fragment_lexicon_list_item_name_textview);
            mTaxonomyTextView = (TextView) itemView.findViewById(R.id.fragment_lexicon_list_item_taxonomy_textview);
        }

        public void bindAnimal(Animal animal) {
            mAnimal = animal;

            // If there's an image present, place it in the view
            if (!mAnimal.getImage().equals("")) {
                if (mImageImageView.getMeasuredHeight() != 0) {
                    // The initial view have all been rendered and are now recycled, so the dimensions are already known
                    ImageLoader.getInstance(getActivity()).loadImage(
                            mAnimal.getImage(),
                            mImageImageView
                    );
                } else {
                    mImageImageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            // Wait for the view being rendered so that its dimensions are known and Picasso can use them to resize the image
                            ImageLoader.getInstance(getActivity()).loadImage(
                                    mAnimal.getImage(),
                                    mImageImageView
                            );

                            // Remove the listener
                            mImageImageView.removeOnLayoutChangeListener(this);
                        }
                    });
                }
            } else {
                mImageImageView.setImageResource(R.mipmap.image_placeholder);
            }

            mNameTextView.setText(mAnimal.getName());
            mTaxonomyTextView.setText(mAnimal.getClassName() + " -> " + mAnimal.getOrderName());
        }

        @Override
        public void onClick(View v) {
            mCallbacks.onAnimalSelected(mAnimal);
        }
    }

    private class AnimalAdapter extends RecyclerView.Adapter<AnimalHolder> {
        private List<Animal> mAnimals;

        public AnimalAdapter(List<Animal> animals) {
            mAnimals = animals;
        }

        @Override
        public AnimalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            View v = inflater.inflate(R.layout.fragment_lexicon_list_item, parent, false);

            return new AnimalHolder(v);
        }

        @Override
        public void onBindViewHolder(AnimalHolder holder, int position) {
            Animal animal = mAnimals.get(position);

            holder.bindAnimal(animal);
        }

        @Override
        public int getItemCount() {
            return mAnimals.size();
        }

        public void setAnimals(List<Animal> animals) {
            mAnimals = animals;
        }
    }
}
