package com.mercadoonline.tiendaclient.service;


import com.mercadoonline.tiendaclient.utils.Global;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCliente {

    public static Retrofit newInstance;

    public static Retrofit getInstance(){

    if(newInstance==null)  {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
              //  .writeTimeout(3, TimeUnit.MINUTES) // write timeout
               // .connectTimeout(3, TimeUnit.MINUTES)
                //.readTimeout(3, TimeUnit.MINUTES)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        newInstance= new Retrofit.Builder()
                .client(client)
               // .baseUrl("http://mercados-online.com/public/api/")
                .baseUrl(Global.Url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
        return newInstance;

    }

}
