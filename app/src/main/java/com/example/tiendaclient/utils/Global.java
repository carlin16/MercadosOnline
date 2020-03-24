package com.example.tiendaclient.utils;

import android.text.TextUtils;

import com.example.tiendaclient.models.enviado.PeticionRegistroUser;
import com.example.tiendaclient.models.recibido.ResponseLoginUser;
import com.example.tiendaclient.models.recibido.ResponseRegistroUser;
import com.example.tiendaclient.models.recibido.ResponseUserPorID;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Global {
    public static String IdUser;

    public static PeticionRegistroUser RegisU= new PeticionRegistroUser();
    public static ResponseLoginUser LoginU= new ResponseLoginUser();
    public static ResponseRegistroUser RegisUser= new ResponseRegistroUser();

    //objeto global para los datos q se presentan en el perfil de usuario
    public static ResponseUserPorID UserGlobal=new ResponseUserPorID();



    public static String convertObjToString(Object clsObj) {
        //convert object  to string json
        String jsonSender = new Gson().toJson(clsObj, new TypeToken<Object>() {
        }.getType());
        return jsonSender;
    }

    public static Boolean verificar_vacio(String texto) {
        //Todo retorno true si esta vacio
        if (TextUtils.isEmpty(texto)) {
            return true;
        }
       return false;
    }




}
