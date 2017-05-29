package cz.zdrubecky.zoopraha.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.zdrubecky.zoopraha.model.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataFetcher {
    private static final String TAG = "DataFetcher";
    private static final String ENDPOINT = "http://10.0.2.2:3000/";

    private BackendService mService;
    private DataFetchedListener mListener;

    // The interface that listeners has to implement in order to be notified of a completed task
    public interface DataFetchedListener {
        void onDataFetched(JsonApiObject response);
    }

    // Here is the link to the listener
    public void setDataFetchedListener(DataFetchedListener listener) {
        mListener = listener;
    }

    public DataFetcher() {
        // I can pass here a gson instance with config
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(BackendService.class);
    }

    public void getAdoptions(String name, String limit, String offset) {
        Call<JsonApiObject<Adoption>> call = mService.getAdoptions(name, limit, offset);

        // Put the request in a background thread
        call.enqueue(new Callback<JsonApiObject<Adoption>>() {
            @Override
            public void onResponse(Call<JsonApiObject<Adoption>> call, Response<JsonApiObject<Adoption>> response) {
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
                    JsonApiObject<Adoption> jsonApiObject = response.body();

                    Log.i(TAG, "The server responded with " + jsonApiObject.getMeta().getCount() + " resources.");

                    // Notify the existing listener
                    if (mListener != null) {
                        mListener.onDataFetched(jsonApiObject);
                    }
                }
            }

            // Called when the Internet access is restricted, timeout reached etc.
            @Override
            public void onFailure(Call<JsonApiObject<Adoption>> call, Throwable t) {
                Log.e(TAG, "An exception has been thrown during the fetching of Adoptions.", t);
            }
        });

        Log.i(TAG, "Adoptions received from the backend API.");
    }

    public void getAnimals(String name, String limit, String offset) {
        Call<JsonApiObject<Animal>> call = mService.getAnimals(name, limit, offset);

        // Put the request in a background thread
        call.enqueue(new Callback<JsonApiObject<Animal>>() {
            @Override
            public void onResponse(Call<JsonApiObject<Animal>> call, Response<JsonApiObject<Animal>> response) {
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
                    JsonApiObject<Animal> jsonApiObject = response.body();

                    Log.i(TAG, "The server responded with " + jsonApiObject.getMeta().getCount() + " resources.");

                    // Notify the existing listener
                    if (mListener != null) {
                        mListener.onDataFetched(jsonApiObject);
                    }
                }
            }

            // Called when the Internet access is restricted, timeout reached etc.
            @Override
            public void onFailure(Call<JsonApiObject<Animal>> call, Throwable t) {
                Log.e(TAG, "An exception has been thrown during the fetching of Animals.", t);
            }
        });

        Log.i(TAG, "Animals received from the backend API.");
    }
}
