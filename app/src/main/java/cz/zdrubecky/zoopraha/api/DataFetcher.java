package cz.zdrubecky.zoopraha.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cz.zdrubecky.zoopraha.model.*;
import okhttp3.Cache;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataFetcher {
    private static final String TAG = "DataFetcher";
//    private static final String ENDPOINT = "http://10.0.2.2:3000/";
    private static final String ENDPOINT = "http://192.168.0.12:3000/";
//    private static final String ENDPOINT = "https://zoo-backend.herokuapp.com/";

    private BackendApi mService;
    private DataFetchedListener mListener;

    // The interface that listeners has to implement in order to be notified of a completed task
    public interface DataFetchedListener {
        void onDataFetched(JsonApiObject response, int statusCode, String etag);
    }

    // Here is the link to the listener
    public void setDataFetchedListener(DataFetchedListener listener) {
        mListener = listener;
    }

    public DataFetcher(Context context) {
        // The cache size in bytes
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(context.getCacheDir(), cacheSize);

        // Take control of the timeouts and provide a cache for the responses, Retrofit will handle the server's eTag itself
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .cache(cache)
                .build();

        // Create a new service instance with the provided converter and client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        mService = retrofit.create(BackendApi.class);
    }

    private class RequestCallback<T> implements Callback<T> {
        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if ( ! response.isSuccessful() && response.errorBody() != null) {
                // The error parsing has to be done explicitly, Retrofit does not convert the body at all
                try {
                    JSONObject errorBodyObject = new JSONObject(response.errorBody().string());
                    JSONArray errors = errorBodyObject.getJSONArray("errors");

                    Gson gson = new GsonBuilder().create();
                    JsonApiError jsonApiError = gson.fromJson(errors.getJSONObject(0).toString(), JsonApiError.class);

                    Log.e(TAG, "The request failed : " + jsonApiError.toString());
                } catch (JSONException | IOException e) {
                    Log.e(TAG, "An exception has been thrown during the JSON parsing of an error response.", e);
                }
            } else {
                T jsonApiObject = response.body();
                int responseStatusCode = 200;
                String responseEtag;

                // Append the response status code (mainly to utilize the 304 not modified in the listeners)
                if (response.raw().networkResponse() != null) {
                    responseStatusCode = response.raw().networkResponse().code();
                }

                // Get the ETag header and use it as a response identification later on
                Headers headers = response.headers();
                responseEtag = headers.get("ETag");

                // Notify the existing listener
                if (mListener != null) {
                    mListener.onDataFetched((JsonApiObject) jsonApiObject, responseStatusCode, responseEtag);
                }
            }
        }

        // Called when the Internet access is restricted, timeout reached etc.
        @Override
        public void onFailure(Call<T> call, Throwable t) {
            Log.e(TAG, "An exception has been thrown during the fetching of resources.", t);

            // Notify the existing listener that the data fetching somehow failed and it should deal with it
            if (mListener != null) {
                mListener.onDataFetched(null, 0, null);
            }
        }
    }

    public void getAdoptions(String name, String limit, String offset) {
        Call<JsonApiObject> call = mService.getAdoptions(name, limit, offset);

        // Put the request in a background thread
        call.enqueue(new RequestCallback<JsonApiObject>());
    }

    public JsonApiObject getAnimal(String id) {
        Call<JsonApiObject> call = mService.getAnimal(id);

        try {
            return (JsonApiObject) call.execute().body();
        } catch (IOException ioe) {
            return null;
        }
    }

    // The method's argument represents the kind of builder we want to get - all the query parameters are encapsulated in it
    public void getAnimals(LexiconQueryBuilder builder) {
        Call<JsonApiObject> call = mService.getAnimals(
                builder.getBiotope(), builder.getClassName(), builder.getContinents(), builder.getDescription(),
                builder.getDistribution(), builder.getFood(), builder.getLimit(), builder.getLocation(),
                builder.getName(), builder.getOrderName(), builder.getOffset());

        call.enqueue(new RequestCallback<JsonApiObject>());
    }

    // Some of the calls are synchronous so that they can be queued (also, there's an issue with multiple concurrent async calls and a resulting closed socket)
    public JsonApiObject getBiotopes() {
        Call<JsonApiObject> call = mService.getBiotopes();

        try {
            return (JsonApiObject) call.execute().body();
        } catch (IOException ioe) {
            return null;
        }
    }

    public JsonApiObject getClassifications(boolean getClass, boolean getOrder, boolean getFamily) {
        Call<JsonApiObject> call = mService.getClassifications(Boolean.toString(getClass), Boolean.toString(getOrder), Boolean.toString(getFamily));

        try {
            return (JsonApiObject) call.execute().body();
        } catch (IOException ioe) {
            return null;
        }
    }

    public JsonApiObject getContinents() {
        Call<JsonApiObject> call = mService.getContinents();

        try {
            return (JsonApiObject) call.execute().body();
        } catch (IOException ioe) {
            return null;
        }
    }

    public void getEvents(String datetime, String limit, String offset) {
        Call<JsonApiObject> call = mService.getEvents(datetime, limit, offset);

        call.enqueue(new RequestCallback<JsonApiObject>());
    }

    public JsonApiObject getFood() {
        Call<JsonApiObject> call = mService.getFood();

        try {
            return (JsonApiObject) call.execute().body();
        } catch (IOException ioe) {
            return null;
        }
    }

    public JsonApiObject getLocations() {
        Call<JsonApiObject> call = mService.getLocations();

        try {
            return (JsonApiObject) call.execute().body();
        } catch (IOException ioe) {
            return null;
        }
    }

    public void getQuestions(int limit) {
        String limitString = Integer.toString(limit);
        Call<JsonApiObject> call = mService.getQuestions(limitString);

        call.enqueue(new RequestCallback<JsonApiObject>());
    }
}
