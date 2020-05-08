package com.mercadoonline.tiendaclient.utils;

import android.text.TextUtils;

import com.mercadoonline.tiendaclient.models.compra.Compra;
import com.mercadoonline.tiendaclient.models.enviado.PeticionRegistroUser;
import com.mercadoonline.tiendaclient.models.recibido.ResponseCategorias;
import com.mercadoonline.tiendaclient.models.recibido.ResponseLoginUser;
import com.mercadoonline.tiendaclient.models.recibido.ResponseRegistroUser;
import com.mercadoonline.tiendaclient.models.recibido.ResponseUserPorID;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Global {
    public static String IdUser;
    public static PeticionRegistroUser RegisU= new PeticionRegistroUser();
    public static ResponseLoginUser LoginU= new ResponseLoginUser();
    public static ResponseRegistroUser RegisUser= new ResponseRegistroUser();
    //objeto global para los datos q se presentan en el perfil de usuario
    public static ResponseUserPorID UserGlobal=new ResponseUserPorID();
    public static String Url="http://mercados-online.com/api/";
    public static  List<ResponseCategorias> categorias = new ArrayList<>();
    public static  List<String> Nombres_Categoria = new ArrayList<>();
    public static List<Compra> VerCompras= new ArrayList<>();

public static void llenarToken(){
    LoginU.setToken("Bearer "+ LoginU.getToken());
}

    public static void limpiar(){
        RegisU= new PeticionRegistroUser();
        LoginU= new ResponseLoginUser();
        RegisUser= new ResponseRegistroUser();
        UserGlobal=new ResponseUserPorID();
        categorias = new ArrayList<>();
        Nombres_Categoria = new ArrayList<>();
        VerCompras= new ArrayList<>();

    }



    public static void llenar_categoria(){
        Nombres_Categoria.clear();
        for (ResponseCategorias x:categorias){
            Nombres_Categoria.add(x.getNombre());
        }


    }

    public static int Modo;





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



    public static void Agregar_Carrito(Compra nueva){

            Boolean encontre =false;
        for(Compra C: VerCompras){

            if(C.getId()==nueva.getId()){

              C.setCantidad(C.getCantidad()+nueva.getCantidad());
              C.setTotal(formatearDecimales(C.getTotal()+nueva.getTotal(),2));
                //("totalGlobal" , "------------->"+C.getTotal());
                C.agregar_producto(nueva.getPuestos().get(0));
                encontre=true;
            }

        }

        if(!encontre)
        VerCompras.add(nueva);


        //("lista",convertObjToString(VerCompras));

    }

    public static Double formatearDecimales(Double numero, Integer numeroDecimales) {
        return Math.round(numero * Math.pow(10, numeroDecimales)) / Math.pow(10, numeroDecimales);
    }


    public static String ucFirst(String str) {
        if (str.isEmpty()) {
            return str;
        } else {
            return Character.toUpperCase(str.charAt(0)) + str.substring(1);
        }
    }


    public static String PrimeraMayusculaNP(String texto){

        String[] datos = texto.toLowerCase().split(" ");
String Completo="";
        for(String item : datos)
        {

            Completo=Completo +" "+ Global.convierte(item);
        }

        String x=Completo.substring(1);
        return x;
    }


    public static String convierte(String string) {
        if (string == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        StringTokenizer st = new StringTokenizer(string," ");
        while (st.hasMoreElements()) {
            String ne = (String)st.nextElement();
            if (ne.length()>0) {
                builder.append(ne.substring(0, 1).toUpperCase());
                builder.append(ne.substring(1).toLowerCase()); //agregado
                builder.append(' ');
            }
        }
        return builder.toString();
    }
}
