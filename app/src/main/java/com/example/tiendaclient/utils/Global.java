package com.example.tiendaclient.utils;

import android.text.TextUtils;

import com.example.tiendaclient.models.recibido.ResponseLoginUser;
import com.example.tiendaclient.models.recibido.ResponseRegistroUser;
import com.google.android.material.snackbar.Snackbar;
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

    private Boolean verificar_vacio(String texto) {


        //Todo retorno true si esta vacio
        if (TextUtils.isEmpty(texto)) {

            return true;
        }


        return false;
    }




}
