package com.example.tiendaclient.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tiendaclient.R;
import com.example.tiendaclient.models.enviado.PeticionLoginUser;
import com.example.tiendaclient.models.enviado.PeticionRegistroUser;
import com.example.tiendaclient.models.recibido.ResponseLoginUser;
import com.example.tiendaclient.models.recibido.ResponseRegistroUser;
import com.example.tiendaclient.service.ApiService;
import com.example.tiendaclient.service.RetrofitCliente;
import com.example.tiendaclient.utils.Global;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

    CircularProgressButton BtnLoginIngresar;

    Retrofit retrofit;
    ApiService retrofitApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UiAnima();
        UI();
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

        Flecha=findViewById(R.id.flecha_registro);
        Flecha.startAnimation(Rebote);
        Registro=findViewById(R.id.registrarme_login);

        BtnLoginIngresar=findViewById(R.id.btn_ingresar_login);
    }


    private void Click(){
        Registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), RegistroUser.class);
                startActivityForResult(intent, 0);
                finish();
            }
        });
        BtnLoginIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar_campos();
                Log.e("clic", "Clic Inicio sesion");
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
       // peticion_Login(JPetCredenciales);
    }

    Boolean cambio=false;
    String mensaje="";

    /*private void peticion_Login(String jsonConf){
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        JsonObject convertedObject = new Gson().fromJson(jsonConf, JsonObject.class);

        disposable = (Disposable) retrofitApi.LoginUser(convertedObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<ResponseRegistroUser>>() {
                    @Override
                    public void onNext(Response<ResponseRegistroUser> responseRegistroUserResponse)
                }
    }*/




}
