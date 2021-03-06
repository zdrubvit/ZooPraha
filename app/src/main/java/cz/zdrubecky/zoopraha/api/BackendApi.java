package cz.zdrubecky.zoopraha.api;

import cz.zdrubecky.zoopraha.model.JsonApiObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BackendApi {
    @Headers({"Accept: application/vnd.api+json"})
    @GET("adoptions")
    Call<JsonApiObject> getAdoptions(@Query("name") String name, @Query("limit") String limit, @Query("offset") String offset);

    @Headers({"Accept: application/vnd.api+json"})
    @GET("lexicon/{id}")
    Call<JsonApiObject> getAnimal(@Path("id") String id);

    @Headers({"Accept: application/vnd.api+json"})
    @GET("lexicon")
    Call<JsonApiObject> getAnimals(
            @Query("biotope") String biotope,
            @Query("class_name") String className,
            @Query("continents") String continents,
            @Query("description") String description,
            @Query("distribution") String distribution,
            @Query("food") String food,
            @Query("limit") String limit,
            @Query("location") String location,
            @Query("name") String name,
            @Query("order_name") String orderName,
            @Query("offset") String offset
    );

    @Headers({"Accept: application/vnd.api+json"})
    @GET("biotopes")
    Call<JsonApiObject> getBiotopes();

    @Headers({"Accept: application/vnd.api+json"})
    @GET("classifications")
    Call<JsonApiObject> getClassifications(@Query("class") String className, @Query("order") String order, @Query("family") String family);

    @Headers({"Accept: application/vnd.api+json"})
    @GET("continents")
    Call<JsonApiObject> getContinents();

    @Headers({"Accept: application/vnd.api+json"})
    @GET("events")
    Call<JsonApiObject> getEvents(@Query("datetime") String datetime, @Query("limit") String limit, @Query("offset") String offset);

    @Headers({"Accept: application/vnd.api+json"})
    @GET("food")
    Call<JsonApiObject> getFood();

    @Headers({"Accept: application/vnd.api+json"})
    @GET("locations")
    Call<JsonApiObject> getLocations();

    @Headers({"Accept: application/vnd.api+json"})
    @GET("questions")
    Call<JsonApiObject> getQuestions(@Query("limit") String limit);
}
