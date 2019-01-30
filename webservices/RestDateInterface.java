package com.example.unamed.mvc.webservices;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestDateInterface {
    @GET("api/show-details")
    Call<SecondApiDateWrapper> dateSerie(@Query("q") String query);
}
