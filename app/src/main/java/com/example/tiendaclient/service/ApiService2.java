package com.example.tiendaclient.service;



import com.example.tiendaclient.models.ApiMaps.DatosDireccion;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService2 {


/*
//TODO ES UN METODO POST NO GET
    @GET("json?")
    Call<DatosDireccion> traerGeo(@Query("sensor") boolean sensor, @Query("latlng") String latlong, @Query("key") String Key);
*/


    @POST("geocode/json?")
    Observable<Response<DatosDireccion>> traerGeo(@Query("sensor") boolean sensor, @Query("latlng") String latlong, @Query("key") String Key);


/*

    @POST("directions/json?")
    Observable<Rutas> DireccionRutas(@Query("origin") String latlongOrigen, @Query("destination") String latlongDestino, @Query("key") String Key);
*/

}
