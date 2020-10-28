package com.mercadoonline.tiendaclient.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mercadoonline.tiendaclient.R;
import com.mercadoonline.tiendaclient.mapas.MapDialogFragment;
import com.mercadoonline.tiendaclient.models.enviado.PeticionLoginUser;
import com.mercadoonline.tiendaclient.models.enviado.RequestGCToken;
import com.mercadoonline.tiendaclient.models.recibido.ResponseCategorias;
import com.mercadoonline.tiendaclient.models.recibido.ResponseError;
import com.mercadoonline.tiendaclient.models.recibido.ResponseLoginUser;
import com.mercadoonline.tiendaclient.models.recibido.ResponseUpdateImagen;
import com.mercadoonline.tiendaclient.models.recibido.ResponseUserPorID;
import com.mercadoonline.tiendaclient.service.ApiService;
import com.mercadoonline.tiendaclient.service.RetrofitCliente;
import com.mercadoonline.tiendaclient.utils.Aplicacion;
import com.mercadoonline.tiendaclient.utils.Global;
import com.mercadoonline.tiendaclient.utils.Vista_tabs;
import com.mercadoonline.tiendaclient.view.fragments.mercado;
import com.mercadoonline.tiendaclient.view.fragments.pedido;
import com.mercadoonline.tiendaclient.view.fragments.perfil_usuario;
import com.mercadoonline.tiendaclient.view.fragments.productos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mercadoonline.tiendaclient.view.fragments.visualizadorTiendasa;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Principal extends AppCompatActivity {

    final Handler handler = new Handler();

    public static TabLayout tabLayout;
    RequestGCToken ReqGcToken= new RequestGCToken();
boolean noti=false;
int position=0;

    SweetAlertDialog dialog_permisos;
    SweetAlertDialog dialog_manual;
    MapDialogFragment map;
    public  LatLng NuevaUbicacion =null;

    //List<ResponseCategorias> CatTien = new ArrayList<>();
    ArrayList<String> list_categorias = new ArrayList<String>();
    ArrayAdapter<String> spinnerArrayAdapter;
    Retrofit retrofit2;
    ApiService retrofitApi2;
    Boolean continuar = false;
    String mensaje = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null ){

            Log.e("noti","traje noti");
            if(llamarPreferences()){
                Log.e("preferencias","traje");

                position=1;

                noti=true;
            }
            else{
                Log.e("preferencias","no tengo");
                Intent intent2 = new Intent (getApplicationContext(), Login.class);
                startActivity(intent2);
                finish();
            }
        }else{

        }


        if(Global.LoginU.getRol()!=null){

            if(Global.LoginU.getRol().equals(Global.ROLCLIENTE) ){
                if(!noti){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.Contenedor_Fragments, new mercado()).commit();
                            //.replace(R.id.Contenedor_Fragments, new categorias()).commit();
                }

                Global.Modo=1;
                //("Modo", "CLIENTE");
                peticion_categoriasTiendas();
            }else if(Global.LoginU.getRol().equals(Global.ROLMERCADO)  ){

                if(!noti){
                    productos productin= new productos();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.Contenedor_Fragments, productin).commit();
                }
                Global.Modo=2;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Write whatever to want to do after delay specified (1 sec)
                        Log.e("cate", "debe llamarse las categorias");
                        peticion_categorias();
                       // peticion_categoriasTiendas();

                    }
                }, 2000);
                //("Modo", "VENDEDOR");
            }else  if(Global.LoginU.getRol().equals(Global.ROLTENDERO)){
                if(!noti){
                    visualizadorTiendasa tiendas= new visualizadorTiendasa();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.Contenedor_Fragments, tiendas).commit();
                }
                Global.Modo=3;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Write whatever to want to do after delay specified (1 sec)
                        Log.e("cate", "debe llamarse las categorias");
                        peticion_categorias();
                       // peticion_categoriasTiendas();

                    }
                }, 2000);
            }
            iniciar_tabs();
            if(noti){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Write whatever to want to do after delay specified (1 sec)
                        elegir(position);
                        cambiar_tab(position);
                    }
                }, 1500);

            }

            generar_token();
        }else{

            SharedPreferences DtsAlmacenados= getSharedPreferences("login", Context.MODE_PRIVATE);
            DtsAlmacenados.edit().clear().commit();
            Global.limpiar();
            Intent intent2 = new Intent (this, Login.class);
            Toast.makeText(this,"Inicie Sesion Nuevamente",Toast.LENGTH_SHORT).show();
            startActivity(intent2);
            finish();

        }
        validar_permisos();

       /* getSupportFragmentManager().beginTransaction()
                //.replace(R.id.Contenedor_Fragments, new mercado()).commit();
        .replace(R.id.Contenedor_Fragments, new mercado()).commit();
*/
       /* iniciar_tabs();
        elegir(position);*/
        peticion_Transportistas();
    }



    private void iniciar_tabs() {
        tabLayout = findViewById(R.id.tab_layout);
        if(Global.Modo==1 )
        tabLayout.addTab(tabLayout.newTab().setCustomView(new Vista_tabs(this, R.drawable.ic_home2, R.drawable.ic_home, "Mercados")));//CLIENTE
        if(Global.Modo==2) tabLayout.addTab(tabLayout.newTab().setCustomView(new Vista_tabs(this, R.drawable.ic_home2, R.drawable.ic_home, "Mi Puesto")));//VENDEDOR
        if(Global.Modo==3) tabLayout.addTab(tabLayout.newTab().setCustomView(new Vista_tabs(this, R.drawable.ic_home2, R.drawable.ic_home, "Mi Tienda")));///TENDERO
        tabLayout.addTab(tabLayout.newTab().setCustomView(new Vista_tabs(this, R.drawable.ic_product2, R.drawable.ic_product, "Pedidos")));
        tabLayout.addTab(tabLayout.newTab().setCustomView(new Vista_tabs(this, R.drawable.ic_user2, R.drawable.ic_user, "Perfil")));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        seleccion_tabs();
    }

    public   void cambiar_tab(int position){
        tabLayout.getTabAt(position).select();

    }


    private void seleccion_tabs(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                //  tabLayout.getTabAt(position).select();

                elegir(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //("seleccion ","antiguo");

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //  int position = tab.getPosition();
                //("seleccion ","nuevo");
                elegir(tab.getPosition());
            }
        });



    }


    private void elegir(int position){
       // if(position==0)
        Global.idFiltro=0;
        clearFragmentBackStack();
        switch (position) {
            case 0:
                //("posicion",""+position);

                if(Global.LoginU.getRol().equals(Global.ROLCLIENTE)){
                    getSupportFragmentManager().beginTransaction()
                            //.replace(R.id.Contenedor_Fragments, new mercado()).commit();
                            .replace(R.id.Contenedor_Fragments, new mercado()).commit();

                }else if(Global.LoginU.getRol().equals(Global.ROLMERCADO)){
                    productos productin= new productos();
                    //productin.idPuesto=Global.LoginU.getId_puesto();
                    // puestito.banderaRol=2;
                    getSupportFragmentManager().beginTransaction()
                            //.replace(R.id.Contenedor_Fragments, new mercado()).commit();
                            .replace(R.id.Contenedor_Fragments, productin).commit();
                }else if(Global.LoginU.getRol().equals(Global.ROLTENDERO)){
                    visualizadorTiendasa tiendas= new visualizadorTiendasa();
                    //productin.idPuesto=Global.LoginU.getId_puesto();
                    // puestito.banderaRol=2;
                    getSupportFragmentManager().beginTransaction()
                            //.replace(R.id.Contenedor_Fragments, new mercado()).commit();
                            .replace(R.id.Contenedor_Fragments, tiendas).commit();
                }


                break;

            case 1:
                //("posicion",""+position);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Contenedor_Fragments, new pedido()).commit();
                break;

            case 2:
                //("posicion",""+position);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Contenedor_Fragments, new perfil_usuario()).commit();
                break;

        }

    }

    private static void removeAllFragments(FragmentManager fragmentManager) {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }


    public void clearFragmentBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        //("cuantos fragments",""+fm.getBackStackEntryCount());
        for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
            fm.popBackStack();
        }
    }


    private void peticion_categorias(){
        Retrofit retrofit;
        ApiService retrofitApi;
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi.VerCategorias(Global.LoginU.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<List<ResponseCategorias>>>() {
                    @Override
                    public void onNext(Response<List<ResponseCategorias>> response) {


                        if(response.isSuccessful()){

                            //("code Categoria",""+response.code());
                            //("respuest Categoria",Global.convertObjToString(response.body()));
                            Global.categorias=response.body();

                        }else {

                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson =new Gson();
                                ResponseError staff = gson.fromJson(jObjError.toString(), ResponseError.class);
                                //("normal-->400",staff.getMensaje());

                            } catch (Exception e) {
                                //("error conversion json",""+e.getMessage());
                            }


                        }


                    }
                    @Override
                    public void onError(Throwable e) {
                        //("code Categoria","error");

                    }

                    @Override
                    public void onComplete() {

                        Global.llenar_categoria();
                    }
                });
    }

    private void generar_tokenNotis(String Token){
        ReqGcToken.setIdUsuario(Global.LoginU.getid());
        ReqGcToken.setToken(Token);

        Gson gson = new Gson();
        String JPTok= gson.toJson(ReqGcToken);
        //("json",JPTok);
        peticion_RegistrarToken(JPTok);

    }

    private void peticion_RegistrarToken(String jsonConf){
        Retrofit retrofit;
        ApiService retrofitApi;
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        JsonObject convertedObject = new Gson().fromJson(jsonConf, JsonObject.class);

        Disposable disposable;
        disposable = (Disposable) retrofitApi.RegistrarGCToken(convertedObject,Global.LoginU.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<ResponseUpdateImagen>>() {
                    @Override
                    public void onNext(Response<ResponseUpdateImagen> response) {


                        if(response.isSuccessful()){

                            //("GC token",""+response.code());
                            //("GC token","ok");


                        }else {

                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson =new Gson();
                                ResponseError staff = gson.fromJson(jObjError.toString(), ResponseError.class);
                                //("normal-->400",staff.getMensaje());

                            } catch (Exception e) {
                                //("error conversion json",""+e.getMessage());
                            }


                        }


                    }
                    @Override
                    public void onError(Throwable e) {
                        //("code Categoria","error");

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void generar_token(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //("falla", "getInstanceId failed"+task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        generar_tokenNotis(token);

                        // Toast.makeText(Login.this,token,Toast.LENGTH_LONG).show();

                        // Log and toast
                        //("token", token);
                    }
                });

    }


    public Boolean llamarPreferences(){
        SharedPreferences DtsRescatados= getSharedPreferences("login", Context.MODE_PRIVATE);
     String    UserSave=DtsRescatados.getString("UsuarioS", "Usuario");
        String  PassSave=DtsRescatados.getString("PasswordS", "Password");
        String  Modo=DtsRescatados.getString("ModoS", "Password");
        Log.e("MODO rescatado", Modo);

        String  Token=DtsRescatados.getString("TokenS", "Token");
        Log.e("traje",Token);
        Log.e("Usuario",UserSave);
        Aplicacion.mFirebaseCrashlytics.setUserId(UserSave);
        if(!UserSave.equals("USUARIO")){

            Global.LoginU.setRol(Modo);
            Global.LoginU.setToken(Token);
            Global.llenarToken();
            Log.e("token generado",""+Global.LoginU.getToken());

            PeticionLoginUser Credenciales = new PeticionLoginUser();
            Credenciales.setUsuario(UserSave);
            Credenciales.setPassword(PassSave);
            Gson gson = new Gson();
            String JPetCredenciales= gson.toJson(Credenciales);
            //("json",JPetCredenciales);
            peticion_Login(JPetCredenciales,Credenciales);

            return true;
        }
            return false;
    }


    private void peticion_Login(String jsonConf, PeticionLoginUser Credenciales ){
        ApiService retrofitApi;
        Retrofit retrofit;
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        JsonObject convertedObject = new Gson().fromJson(jsonConf, JsonObject.class);

        disposable = (Disposable) retrofitApi.LoginUser(convertedObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<ResponseLoginUser>>() {
                    @Override
                    public void onNext(Response<ResponseLoginUser> response) {

                        //("code PU",""+response.code());
                        if (response.isSuccessful()) {
                            Global.LoginU=response.body();
                            // mensaje=response.body().getMensaje();
                        } else if (response.code()==500) {
                            // myDialog.dismiss();
                        }else{

                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson =new Gson();
                                ResponseError staff = gson.fromJson(jObjError.toString(), ResponseError.class);


                            } catch (Exception e) {
                                //("error conversion json",""+e.getMessage());
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        //myDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        guardarPreferences(Credenciales.getUsuario(),Credenciales.getPassword(),Global.LoginU.getRol(),Global.LoginU.getToken());
                        Global.llenarToken();

                    }
                });
    }

    public void guardarPreferences(String Use, String Pass,String Modo,String Token){


        SharedPreferences DtsAlmacenados= getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor MyEditorDts=DtsAlmacenados.edit();
        MyEditorDts.putString("UsuarioS", Use);
        MyEditorDts.putString("PasswordS", Pass);
        MyEditorDts.putString("ModoS", Modo);
        MyEditorDts.putString("TokenS", Token);
        Log.e("guardar",Token);

        MyEditorDts.commit();//devuelve un booleano,hasta que se guarda todo

        MyEditorDts.apply();
        //("Datos g", Use);
        //("Datos g", Pass);

    }

    private boolean validar_permisos() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //("tengo permisos", "bien");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //Todo no tiene permisos y le sale para dar
                cargarDialogoRecomendacion();
                //("dialogo", "recomendacion");
            } else {
                //Todo no tiene permisos por que los nego y puso no volver a presentar asi que  mandamos de nuevo  a pedir y entrara
                //Todo a permisos manual
                //  requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                //("dialogo", "else");
            }
            return false;
        }
        return true;
    }
    //Todo un dialog que recomienda por que activar los permisos

    private void cargarDialogoRecomendacion() {

        dialog_permisos = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog_permisos.setTitleText("Permisos Desactivados");
        dialog_permisos.setContentText("Active los permisos de \n ubicación.");
        dialog_permisos.setConfirmText("OK");
        dialog_permisos.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    requestPermissions
                            (new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
                dialog_permisos.dismissWithAnimation();
            }
        });


        dialog_permisos.setCancelable(false);
        dialog_permisos.show();
    }

    private void solicitarPermisosManual() {
        dialog_manual = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        dialog_manual.setTitleText("Permisos Desactivados");
        dialog_manual.setContentText("Configure los permisos de forma manual para el correcto funcionamiento \n de la App");
        dialog_manual.setConfirmText("OK");
        dialog_manual.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
               // Uri uri = Uri.fromParts("package",this , null);
                Uri uri=Uri.fromParts("package", ":", null);
                intent.setData(uri);
                startActivity(intent);

                dialog_manual.dismissWithAnimation();
            }
        });


        dialog_manual.setCancelable(false);
        dialog_manual.show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length == 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //("permisos ", "defecto");

                //map.show(getFragmentManager(), null);
                // ir_ubicacion();

            } else {
                //("permisos", "manual");

                solicitarPermisosManual();
            }
        }

    }

    private void peticion_categoriasTiendas() {
        //("peticion","mercado");
        retrofit2 = RetrofitCliente.getInstance();
        retrofitApi2 = retrofit2.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi2.TraerCategoriasTiendas(Global.LoginU.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<List<ResponseCategorias>>>() {
                    @Override
                    public void onNext(Response<List<ResponseCategorias>> response) {


                        if (response.isSuccessful()) {

                            //("code VM",""+response.code());
                            //("respuest VM",Global.convertObjToString(response.body()));
                            Global.categoriasNegocios.clear();
                            Global.categoriasNegocios.addAll(response.body());
                           Log.e("cat negocios",Global.convertObjToString(Global.categoriasNegocios));
                        } else {

                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson = new Gson();
                                ResponseError staff = gson.fromJson(jObjError.toString(), ResponseError.class);
                                mensaje = staff.getMensaje();
                                //("normal-->400",mensaje);

                            } catch (Exception e) {
                                //("error conversion json",""+e.getMessage());
                            }


                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        //("code VM","error");

                    }

                    @Override
                    public void onComplete() {




                    }
                });
    }


    private void peticion_Transportistas(){
        Retrofit retrofit;
        ApiService retrofitApi;
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        //JsonObject convertedObject = new Gson().fromJson(jsonConf, JsonObject.class);

        disposable = (Disposable) retrofitApi.ObtenerTransportistas("TRANSPORTISTA",Global.LoginU.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<List<ResponseUserPorID>>>() {
                    @Override
                    public void onNext(Response<List<ResponseUserPorID>> response) {

                        //("code PU",""+response.code());
                        if (response.isSuccessful()) {
                            Global.Transportistas=response.body();
                            Log.e("transportistas", ""+ Global.Transportistas.size());
                        } else  if (response.code()==500) {
                            mensaje = "Internal Server Error";
                        } else{
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson =new Gson();
                                ResponseError staff = gson.fromJson(jObjError.toString(), ResponseError.class);
                                mensaje=staff.getMensaje();
                                //("normal-->400",mensaje);

                            } catch (Exception e) {
                                //("error conversion json",""+e.getMessage());
                            }

                        }
                    }
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        //("Completado","Se cargo usuario");



                    }
                });
    }



}
