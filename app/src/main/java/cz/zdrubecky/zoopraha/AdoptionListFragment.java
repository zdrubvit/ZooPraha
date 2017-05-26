package cz.zdrubecky.zoopraha;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import cz.zdrubecky.zoopraha.model.Adoption;

public class AdoptionListFragment extends Fragment {
    private static final String TAG = "AdoptionListFragment";

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
}
