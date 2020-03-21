package com.example.tiendaclient.service;


import com.example.tiendaclient.models.enviado.PeticionLoginUser;
import com.example.tiendaclient.models.enviado.PeticionRegistroUser;
import com.example.tiendaclient.models.recibido.ResponseLoginUser;
import com.example.tiendaclient.models.recibido.ResponseRegistroUser;
import com.example.tiendaclient.view.RegistroUser;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {



    @Headers("Content-Type: application/json")
    @POST("usuarios")
    Observable<Response<ResponseRegistroUser> >RegistroUser(@Body JsonObject object);

    @Headers("Content-Type: application/json")
    @POST("authr")
    Observable<Response<ResponseLoginUser> >LoginUser(@Body JsonObject object);


    @Multipart
    @Headers("Content-Type: application/json")
    @POST("usuarios/{user_id}/foto")
    Observable<String>UploadImage(@Path(value = "user_id", encoded = true) String userId,
                                  @Part MultipartBody.Part imageFile);



    @Headers("Content-Type: application/json")
    @POST("auth/gc_token")
    Observable<Response<String> >RegistroToken(@Body JsonObject object);




}
