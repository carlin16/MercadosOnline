package com.example.tiendaclient.utils;

import com.example.tiendaclient.models.recibido.ResponseLoginUser;
import com.example.tiendaclient.models.recibido.ResponseRegistroUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Global {

    public static ResponseRegistroUser RegisU= new ResponseRegistroUser();
    public static ResponseLoginUser LoginU= new ResponseLoginUser();


    public static String convertObjToString(Object clsObj) {
        //convert object  to string json
        String jsonSender = new Gson().toJson(clsObj, new TypeToken<Object>() {
        }.getType());
        return jsonSender;
    }






}
