package cz.zdrubecky.zoopraha.api;

import cz.zdrubecky.zoopraha.model.Adoption;
import cz.zdrubecky.zoopraha.model.JsonApiObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface BackendService {
    @Headers({"Accept: application/vnd.api+json"})
    @GET("adoptions")
    Call<JsonApiObject<Adoption>> getAdoptions(@Query("name") String name, @Query("limit") String limit, @Query("offset") String offset);
}
