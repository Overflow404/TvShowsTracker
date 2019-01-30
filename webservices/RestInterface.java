package com.example.unamed.mvc.webservices;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestInterface {
    @GET("/3/discover/tv")
    Call<FirstApiItemListWrapper> discoverTv(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("/3/search/tv")
    Call<FirstApiSearchNameWrapper> searchSerie(@Query("api_key") String apiKey, @Query("query") String name);
}
