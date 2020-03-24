package com.example.tiendaclient.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tiendaclient.R;
import com.example.tiendaclient.models.enviado.PeticionLoginUser;
import com.example.tiendaclient.models.recibido.ResponseError;
import com.example.tiendaclient.models.recibido.ResponseLoginUser;
import com.example.tiendaclient.models.recibido.ResponseRegistroUser;
import com.example.tiendaclient.service.ApiService;
import com.example.tiendaclient.service.RetrofitCliente;
import com.example.tiendaclient.utils.Global;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Login extends AppCompatActivity {
    Animation Rebote;
    ImageButton Flecha;
    TextView Registro;
    EditText ETLoginUser, ETLoginPass;
    TextInputLayout TILoginUse, TILoginPassW;
    String UserSave,PassSave;

    CircularProgressButton BtnLoginIngresar;

    Retrofit retrofit;
    ApiService retrofitApi;
    Dialog myDialog;

    Boolean cambio_pantalla=false;
    String mensaje="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



       // animacion_cargando();
        //UiAnima();
        UI();
        llamarPreferences();
        Click();
    }
    private void UiAnima(){
        Rebote = AnimationUtils.loadAnimation(this, R.anim.rebote);


    }
    private  void UI(){
        ETLoginUser=findViewById(R.id.LoginUser);
        ETLoginPass=findViewById(R.id.LoginPass);

        TILoginUse=findViewById(R.id.TILoginUser);
        TILoginPassW=findViewById(R.id.TILoginPass);

        //Flecha=findViewById(R.id.flecha_registro);
        //Flecha.startAnimation(Rebote);
        Registro=findViewById(R.id.registrarme_login);

        BtnLoginIngresar=findViewById(R.id.btn_ingresar_login);
    }


    private void Click(){
        SpannableString content = new SpannableString("Registrarse");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        Registro.setText(content);


        ETLoginUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    TILoginUse.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TILoginUse.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
        });

        ETLoginPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    TILoginPassW.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TILoginPassW.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
        });

        Registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), RegistroUsuarios.class);
                startActivity(intent);
                finish();
            }
        });
        BtnLoginIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // validar_campos();
                Log.e("clic", "Clic Inicio sesion");

               validar_campos();

            }
        });

        TILoginUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ETLoginUser.requestFocus();
            }
        });
        TILoginPassW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ETLoginPass.requestFocus();
            }
        });


    }

    private Boolean verificar_vacio(String texto) {
        //Todo retorno true si esta vacio
        if (TextUtils.isEmpty(texto)) {
            Snackbar.make(findViewById(android.R.id.content), "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
    private void validar_campos(){
        if(verificar_vacio(ETLoginUser.getText().toString())) ETLoginUser.requestFocus();
        else if(verificar_vacio(ETLoginPass.getText().toString())) ETLoginPass.requestFocus() ;
        else {
            llenarDatos();
        }
    }

    private  void llenarDatos(){
        PeticionLoginUser Credenciales = new PeticionLoginUser();
        Credenciales.setUsuario(ETLoginUser.getText().toString());
        Credenciales.setPassword(ETLoginPass.getText().toString());
        Gson gson = new Gson();
        String JPetCredenciales= gson.toJson(Credenciales);
        Log.e("json",JPetCredenciales);
        peticion_Login(JPetCredenciales);
    }



    private void animacion_cargando(){
        myDialog = new Dialog(this, R.style.NewDialog);
    //  myDialog.setContentView(R.layout.s);
        //myDialog.setContentView(R.layout.splash_screen);
        myDialog.show();

    }
    public void guardarPreferences(String Use, String Pass){
        SharedPreferences DtsAlmacenados= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor MyEditorDts=DtsAlmacenados.edit();
        MyEditorDts.putString("UsuarioS", Use);
        MyEditorDts.putString("PasswordS", Pass);
        MyEditorDts.apply();
        Log.e("Datos g", Use);
        Log.e("Datos g", Pass);

    }
    public void llamarPreferences(){
        SharedPreferences DtsRescatados=PreferenceManager.getDefaultSharedPreferences(this);
        UserSave=DtsRescatados.getString("UsuarioS", "Usuario");
        PassSave=DtsRescatados.getString("PasswordS", "password");
        Log.e("Datos llamados", UserSave);
        Log.e("Datos llamados", PassSave);
    }

    public void iniciar_sesion(){
        Intent intent = new Intent (getApplicationContext(), Principal.class);
        startActivity(intent);
        finish();

    }

    private void peticion_Login(String jsonConf){
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

                        Log.e("code PU",""+response.code());
                        if (response.isSuccessful()) {
                            cambio_pantalla=true;
                            Global.LoginU=response.body();
                           // mensaje=response.body().getMensaje();
                        } else if (response.code()==500) {
                            mensaje = "Internal Server Error";
                        }else{

                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson =new Gson();
                                ResponseError staff = gson.fromJson(jObjError.toString(), ResponseError.class);

                                mensaje=staff.getMensaje();

                            } catch (Exception e) {
                                Log.e("error conversion json",""+e.getMessage());
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Write whatever to want to do after delay specified (1 sec)
                                myDialog.dismiss();
                            }
                        }, 2000);


                        myDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {

                        if(cambio_pantalla){
                            iniciar_sesion();
                            Log.e("Completado","Login exitoso");
                            guardarPreferences(ETLoginUser.getText().toString(), ETLoginPass.getText().toString());
                        }else{
                            Snackbar.make(findViewById(android.R.id.content), "Ingrese credenciales correctas", Snackbar.LENGTH_LONG).show();
                        }

                    }
                });
    }


}
