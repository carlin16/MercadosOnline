package com.mercadoonline.tiendaclient.service;

import com.google.gson.JsonObject;
import com.mercadoonline.tiendaclient.models.recibido.ResponseRegistroUser;
import com.mercadoonline.tiendaclient.models.recibido.ResponseWhatsApp;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService3 {


    @Headers("Content-Type: application/json")
    @POST("ws/send")
    Observable<Response<ResponseWhatsApp>> MensajeWhastApp(@Body JsonObject object);


}
