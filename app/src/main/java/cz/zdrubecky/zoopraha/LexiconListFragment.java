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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cz.zdrubecky.zoopraha.api.DataFetcher;
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

        mAnimalRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAnimalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    public void updateUI() {
        List<Animal> animals = mAnimalManager.getAnimals();

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

            if (!mAnimal.getImage().equals("")) {
                DataFetcher.loadImage(
                        getActivity(),
                        mAnimal.getImage(),
                        R.mipmap.image_placeholder,
                        R.mipmap.image_broken,
                        mImageImageView,
                        getString(R.string.datafetcher_error_loading_image)
                );
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
