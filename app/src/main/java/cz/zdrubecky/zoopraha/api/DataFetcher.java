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

    private BackendService service;

    public DataFetcher() {
        // I can pass here a gson instance with config
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(BackendService.class);
    }

    public void getAdoptions(String name, String limit, String offset) {
        Call<JsonApiObject<Adoption>> call = service.getAdoptions(name, limit, offset);

        call.enqueue(new Callback<JsonApiObject<Adoption>>() {
            @Override
            public void onResponse(Call<JsonApiObject<Adoption>> call, Response<JsonApiObject<Adoption>> response) {
                if ( ! response.isSuccessful() && response.errorBody() != null) {
                    JsonApiError jsonApiError;

                    try {
                        JSONObject errorBodyObject = new JSONObject(response.errorBody().string());
                        JSONArray errors = errorBodyObject.getJSONArray("errors");

                        Gson gson = new GsonBuilder().create();
                        jsonApiError = gson.fromJson(errors.getJSONObject(0).toString(), JsonApiError.class);

                        Log.e(TAG, "The request failed : " + jsonApiError.toString());
                    } catch (JSONException je) {
                        Log.e(TAG, "An exception has been thrown during the JSON parsing of an error response.", je);
                    } catch (IOException ioe) {
                        Log.e(TAG, "An exception has been thrown during the JSON parsing of an error response.", ioe);
                    }
                } else {
                    JsonApiObject<Adoption> jsonApiObject = response.body();
                }
            }

            @Override
            public void onFailure(Call<JsonApiObject<Adoption>> call, Throwable t) {
                Log.e(TAG, "An exception has been thrown during the fetching of Adoptions.", t);
            }
        });

        Log.i(TAG, "Adoptions received from the backend API.");
    }
}
