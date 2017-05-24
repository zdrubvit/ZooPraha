package cz.zdrubecky.zoopraha.api;

import cz.zdrubecky.zoopraha.model.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BackendService {
    @GET("adoptions")
    Call<Adoption> getAdoptions(@Query("name") String name, @Query("limit") String limit, @Query("offset") String offset);
}
