package com.example.tiendaclient.service;


import com.example.tiendaclient.models.recibido.ResponseCategorias;
import com.example.tiendaclient.models.recibido.ResponseDetallesPedidos;
import com.example.tiendaclient.models.recibido.ResponseRegistrarPedido;
import com.example.tiendaclient.models.recibido.ResponseUserPorID;
import com.example.tiendaclient.models.recibido.ResponseLoginUser;
import com.example.tiendaclient.models.recibido.ResponseRegistroUser;
import com.example.tiendaclient.models.recibido.ResponseUpdateImagen;
import com.example.tiendaclient.models.recibido.ResponseVerAllPuesto;
import com.example.tiendaclient.models.recibido.ResponseVerMercado;
import com.example.tiendaclient.models.recibido.ResponseVerPedido;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {



    @Headers("Content-Type: application/json")
    @POST("usuarios")
    Observable<Response<ResponseRegistroUser>>RegistroUser(@Body JsonObject object);


    @Multipart
    @POST("usuarios/{user_id}/foto")
    Observable<Response<ResponseUpdateImagen>>UploadImage(@Path(value = "user_id", encoded = true) String userId,
                                                          @Part MultipartBody.Part imageFile);



    @Headers("Content-Type: application/json")
    //cambiar y pasar por parametros, en list poner la clase q cojo de pojo
    @GET("mercados?childs=si")
    Observable<Response<List<ResponseVerMercado>>>VerMercados();

    @Headers("Content-Type: application/json")
    //cambiar y pasar por parametros, en list poner la clase q cojo de pojo
    @GET("puestos")
    Observable<Response<List<ResponseVerAllPuesto>>>VerAllPuestos(@Query("childs") String ValueChild ,
                                                                  @Query("id_mercado") String ValueIdPuesto);

    /*
    @GET("/answers?order=desc&sort=activity&site=stackoverflow")
   Call<SOAnswersResponse> getAnswers(@Query("tagged") String tags);
}
//puestos?childs=si&id_mercado=1
@GET("users/{user}/repos")
  Call<List<Repo>> listRepos(@Path("user") String user);
     */
//////////////////////
    //
    @Headers("Content-Type: application/json")
    @POST("auth")
    Observable<Response<ResponseLoginUser>>LoginUser(@Body JsonObject object);


    @Headers("Content-Type: application/json")
    @GET("usuarios/{user_id}")
    Observable<Response<ResponseUserPorID>>ObtenerUsuarioporID(@Path(value = "user_id", encoded = true) String userId);









    @Headers("Content-Type: application/json")
    @POST("auth/gc_token")
    Observable<Response<String> >RegistroToken(@Body JsonObject object);


  //  ResponseVerMercado




    @Headers("Content-Type: application/json")
    //cambiar y pasar por parametros, en list poner la clase q cojo de pojo
    @GET("pedidos")
    Observable<Response<List<ResponseVerPedido>>>VerPedidosClientes(@Query("type") String type ,
                                                              @Query("id") String id);




    @Headers("Content-Type: application/json")//FULL
    @GET("pedidos/{user_id}")
    Observable<Response<ResponseDetallesPedidos>>VerDetallePedidos(@Path(value = "user_id", encoded = true) String userId, @Query("type") String type);



    @Headers("Content-Type: application/json")
    @POST("pedidos")
    Observable<Response<ResponseRegistrarPedido>>RegistrarPedidos(@Body JsonObject object);

    @Headers("Content-Type: application/json")
    //cambiar y pasar por parametros, en list poner la clase q cojo de pojo
    @GET("productos/categorias")
    Observable<Response<List<ResponseCategorias>>>VerCategorias();


    @Headers({"Content-Type: application/json", "multipart/form-data"})
    @Multipart
    @POST("productos")
    Observable<Response<ResponseUpdateImagen>>RegistrarProducto(@Path(value = "user_id", encoded = true) String userId,
                                                          @Part MultipartBody.Part imageFile);


//pedidos?type=CLIENTE&id=16



}
