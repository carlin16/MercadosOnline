package com.example.tiendaclient.service;


import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {



    @Headers("Content-Type: application/json")
    @POST("consultarcodkarcher")
    Observable<String > consulta_tags_sql(@Body JsonObject object);

}
