package cz.zdrubecky.zoopraha.section.lexicon;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import cz.zdrubecky.zoopraha.R;
import cz.zdrubecky.zoopraha.api.ImageLoader;


public class ImageDialogFragment extends DialogFragment {
    private static final String ARG_URL = "url";

    private ImageView mImageView;

    public static ImageDialogFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);

        ImageDialogFragment fragment = new ImageDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String url = getArguments().getString(ARG_URL);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog_image, null);

        mImageView = (ImageView) view.findViewById(R.id.fragment_dialog_image_imageview);
        mImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ImageLoader.getInstance(getActivity()).loadImage(
                        url,
                        mImageView
                );

                mImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.fragment_animal_dialog_image_close, null)
                .create();
    }
}
