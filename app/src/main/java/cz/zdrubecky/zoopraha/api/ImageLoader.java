package cz.zdrubecky.zoopraha.api;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import cz.zdrubecky.zoopraha.R;

public class ImageLoader {
    private static final String TAG = "ImageLoader";

    private static ImageLoader sInstance = null;

    private Context mContext;
    private Picasso mPicasso;

    public static ImageLoader getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ImageLoader(context.getApplicationContext());
        }

        return sInstance;
    }

    private ImageLoader(Context context) {
        mContext = context;

        // Beware the secure protocol or any other http redirection, Picasso can't cope with that and has to use a different downloader
        mPicasso = new Picasso.Builder(context)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.i(TAG, "An exception has been thrown during the image loading: " + exception.toString());
                    }
                })
                .downloader(new OkHttpDownloader(context))
                .build();
    }

    public void loadImage(String url, ImageView view) {
        // todo "cannot reset" error in some images
        // todo replace fit with resize to prevent stretching
        mPicasso.load(url)
                .placeholder(R.mipmap.image_placeholder)
                .error(R.mipmap.image_broken)
                .fit()
                .into(view, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {}

                    @Override
                    public void onError() {
                        Toast.makeText(mContext, R.string.datafetcher_error_loading_image, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
