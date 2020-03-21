package com.example.tiendaclient.service;


import com.example.tiendaclient.models.enviado.PeticionLoginUser;
import com.example.tiendaclient.models.enviado.PeticionRegistroUser;
import com.example.tiendaclient.models.recibido.ResponseLoginUser;
import com.example.tiendaclient.models.recibido.ResponseRegistroUser;
import com.example.tiendaclient.view.RegistroUser;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {



    @Headers("Content-Type: application/json")
    @POST("usuarios")
    Observable<Response<ResponseRegistroUser> >RegistroUser(@Body JsonObject object);




    @Headers("Content-Type: application/json")
    @POST("authr")
    Observable<Response<ResponseLoginUser> >LoginUser(@Body JsonObject object);
}
