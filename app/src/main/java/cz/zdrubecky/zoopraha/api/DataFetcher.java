package cz.zdrubecky.zoopraha.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cz.zdrubecky.zoopraha.model.*;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataFetcher {
    private static final String TAG = "DataFetcher";
//    private static final String ENDPOINT = "http://10.0.2.2:3000/";
    private static final String ENDPOINT = "http://10.0.12.226:3000/";

    private BackendApi mService;
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
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
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

                // Notify the existing listener
                if (mListener != null) {
                    mListener.onDataFetched((JsonApiObject) jsonApiObject);
                }
            }
        }

        // Called when the Internet access is restricted, timeout reached etc.
        @Override
        public void onFailure(Call<T> call, Throwable t) {
            Log.e(TAG, "An exception has been thrown during the fetching of resources.", t);
        }
    }

    public void getAdoptions(String name, String limit, String offset) {
        Call<JsonApiObject> call = mService.getAdoptions(name, limit, offset);

        // Put the request in a background thread
        call.enqueue(new RequestCallback<JsonApiObject>());
    }

    public void getAnimals(String name, String limit, String offset) {
        Call<JsonApiObject> call = mService.getAnimals(name, limit, offset);

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

    public void getQuestions(String limit) {
        Call<JsonApiObject> call = mService.getQuestions(limit);

        call.enqueue(new RequestCallback<JsonApiObject>());
    }
}
