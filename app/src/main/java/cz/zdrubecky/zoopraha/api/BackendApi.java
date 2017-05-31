package cz.zdrubecky.zoopraha.api;

import cz.zdrubecky.zoopraha.model.JsonApiObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface BackendApi {
    @Headers({"Accept: application/vnd.api+json"})
    @GET("adoptions")
    Call<JsonApiObject> getAdoptions(@Query("name") String name, @Query("limit") String limit, @Query("offset") String offset);

    @Headers({"Accept: application/vnd.api+json"})
    @GET("classifications")
    Call<JsonApiObject> getClassifications(@Query("class") String className, @Query("order") String order, @Query("family") String family);

    @Headers({"Accept: application/vnd.api+json"})
    @GET("events")
    Call<JsonApiObject> getEvents(@Query("datetime") String datetime, @Query("limit") String limit, @Query("offset") String offset);

    @Headers({"Accept: application/vnd.api+json"})
    @GET("lexicon")
    Call<JsonApiObject> getAnimals(@Query("name") String name, @Query("limit") String limit, @Query("offset") String offset);
}
