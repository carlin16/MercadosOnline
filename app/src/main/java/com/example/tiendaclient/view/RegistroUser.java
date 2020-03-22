package com.example.tiendaclient.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tiendaclient.R;
import com.example.tiendaclient.models.enviado.PeticionRegistroUser;
import com.example.tiendaclient.models.recibido.ResponseError;
import com.example.tiendaclient.models.recibido.ResponseRegistroUser;
import com.example.tiendaclient.service.ApiService;
import com.example.tiendaclient.service.RetrofitCliente;
import com.example.tiendaclient.utils.Global;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.regex.Pattern;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import in.anshul.libray.PasswordEditText;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistroUser extends AppCompatActivity {
    EditText Nombres, Apellidos, Direccion, Telefono,  Email,Usuario;
    TextInputLayout TINom, TIApell, TIDir, TITel, TIEma, TIUsua, TIPassw, TIRePass ;
    Dialog myDialog;
    PasswordEditText Pass, RePass;
    String[] Roles;
    int posicionRol=0;
    Boolean cambio=false;
    String mensaje="";
    Spinner Rol, TipoTienda;
    CountryCodePicker codigo_pais;
    CircularProgressButton BtnRegistrar ,datos,fotos,credenciales;
    RoundedImageView preview;
    Uri  imagen_perfil;
    RoundedImageView Perfil;
    LinearLayout ConteTipoTienda;

    String numeroTelefono;
    Retrofit retrofit;
    ApiService retrofitApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UI();
        animacion_cargando();
        Click();
        llenarDatos();
    }

    private  void UI(){
        Usuario=findViewById(R.id.registro_usuario);
        Nombres=findViewById(R.id.registro_nombre);
        Apellidos=findViewById(R.id.registro_apellido);
        Direccion=findViewById(R.id.registro_direccion);
        Telefono=findViewById(R.id.telefono_registro);
        Email=findViewById(R.id.registro_email);
        Pass=findViewById(R.id.registro_pass);
        RePass=findViewById(R.id.registro_repass);
        Rol=findViewById(R.id.spn_rolUser);
        BtnRegistrar=findViewById(R.id.btn_registro);
        codigo_pais = findViewById(R.id.ccp);
        Perfil=findViewById(R.id.registro_perfil);
        Roles= getResources().getStringArray(R.array.Roles);


        ///
        Nombres.requestFocus();
        ///

        TINom=findViewById(R.id.TINombre);
        TIApell=findViewById(R.id.TIApellido);
        TIDir=findViewById(R.id.TIDireccion);
        TIEma=findViewById(R.id.TIEmail);
        TITel=findViewById(R.id.TITelefono);
        TIUsua=findViewById(R.id.TIUsuario);
        TIPassw=findViewById(R.id.TIPass);
        TIRePass=findViewById(R.id.TIRepass);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent (this, Login.class);
        startActivityForResult(intent, 0);
        finish();
    }

    private void Click(){
        BtnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar_campos();
                Log.e("clic", "se dio clic");
            }
        });

        TIEma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email.requestFocus();
            }
        });


        TIApell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Apellidos.requestFocus();
            }
        });
        TINom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Nombres.requestFocus();
            }
        });
        TITel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Telefono.requestFocus();
            }
        });
        TIDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Direccion.requestFocus();
            }
        });
        TIUsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario.requestFocus();
            }
        });
        TIPassw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pass.requestFocus();
            }
        });
        TIRePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RePass.requestFocus();
            }
        });


        Rol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              posicionRol=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcion_cortar();

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
        if(verificar_vacio(Nombres.getText().toString())) Nombres.requestFocus();
        else if(verificar_vacio(Apellidos.getText().toString())) Apellidos.requestFocus();
        else if(verificar_vacio(Direccion.getText().toString())) Direccion.requestFocus();
        else if (verificar_vacio(Telefono.getText().toString())) Telefono.requestFocus();
        else if (verificar_vacio(Email.getText().toString())) Email.requestFocus();
        else if (verificar_vacio(Usuario.getText().toString())) Usuario.requestFocus();
        else if(verificar_vacio(Pass.getText().toString())) Pass.requestFocus();
        else if(tamaño_texto(Pass) );


        else if(verificar_vacio(RePass.getText().toString())) RePass.requestFocus();
        else if(!Pass.getText().toString().equals(RePass.getText().toString())){
            Snackbar.make(findViewById(android.R.id.content), "Las contraseñas no coinciden", Snackbar.LENGTH_LONG).show();
        }
        else if (imagen_perfil==null) mensaje();
        else if (Telefono.getText().toString().substring(0, 1).equals("0")) {
            numeroTelefono = codigo_pais.getSelectedCountryCode() + Telefono.getText().toString().trim().substring(1);
                llenarDatos();
        } else {
            numeroTelefono = codigo_pais.getSelectedCountryCode() + Telefono.getText().toString().trim();
            llenarDatos();

        }

    }

    private void animacion_cargando(){
        myDialog = new Dialog(this, R.style.NewDialog);
        myDialog.setContentView(R.layout.animacion_registo_user);
        credenciales = myDialog.findViewById(R.id.btn_credenciales);
        datos = myDialog.findViewById(R.id.btn_datos) ;
        fotos = myDialog.findViewById(R.id.btn_foto);
        preview=myDialog.findViewById(R.id.perfil_registro);
        //  myDialog = new Dialog(LoginActivity.this);
        //     myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //     myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myDialog.setCancelable(false);

    }

    private  void llenarDatos(){
        PeticionRegistroUser User = new PeticionRegistroUser();
        User.setNombres(Nombres.getText().toString());
        User.setApellidos(Apellidos.getText().toString());
        User.setCelular(numeroTelefono);
        User.setDireccion(Direccion.getText().toString());
        User.setEmail(Email.getText().toString());

        User.setUsuario(Usuario.getText().toString());
        User.setRol(Roles[posicionRol]);
        User.setPassword(Pass.getText().toString());

        PeticionRegistroUser x = new PeticionRegistroUser();
        User=x;
        Gson gson = new Gson();
        String JPetUser= gson.toJson(User);
        Log.e("json",JPetUser);
        animacion_registro();
        peticion_Registro(JPetUser);
  }

    private void peticion_Registro(String jsonConf){
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        JsonObject convertedObject = new Gson().fromJson(jsonConf, JsonObject.class);

        disposable = (Disposable) retrofitApi.RegistroUser(convertedObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<ResponseRegistroUser>>() {
                    @Override
                    public void onNext(Response<ResponseRegistroUser> response) {


                        Log.e("Respuesta codigo",""+response.code());
                        Log.e("Respuesta codigo",""+response.toString());
                        Log.e("Respuesta codigo",""+response.body());

                       // Log.e("Respuesta codigo",""+response.code());

                        Log.e("Respuesta codigo",""+Global.convertObjToString(response.body()));

                       /* if(responseRegistroUserResponse.code()==201){
                            cambio=true;
                            mensaje=responseRegistroUserResponse.body().getRespuestaU().getMensaje();
                        }else  if(responseRegistroUserResponse.code()==400){
                            Gson gson = new Gson();
                            mensaje=responseRegistroUserResponse.body().getRespuestaE().getMensaje();
                            animacion_errores();

                            *//*ResponseError staff = gson.fromJson(responseRegistroUserResponse.body().toString(), ResponseError.class);
                            mensaje=staff.getMensaje();
                           *//*
                        }else{

                        }*/
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("error",e.toString());
                        animacion_errores();

                    }

                    @Override
                    public void onComplete() {
                        Log.e("Completado","registrado");
                     //   Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();


                    }
                });
    }

    private void mensaje() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡OH!");
        builder.setMessage("No ha puesto foto de perfil");
        builder.setPositiveButton("Entiendo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    private Boolean tamaño_texto( EditText texto){


        if (texto.getText().toString().length()<8) {

            texto.setError("Contraseña corta");
            return true;
        }
        return false;
    }
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imagen_perfil=result.getUri();
                Log.e("obtuve imagen",""+imagen_perfil);


                llenar_subida();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("error imagen",result.getError().toString());
            }
        }
    }
    private void llenar_subida(){
        Glide
                .with(this)
                .load(imagen_perfil)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(125, 125)
                .fitCenter()
                .into(Perfil);
        Glide
                .with(this)
                .load(imagen_perfil)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(preview);
    }
    public void funcion_cortar() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }




    public void subir_foto(){





      //  Staff staff = gson.fromJson(reader, Staff.class);

        File file = new File(imagen_perfil.getPath());
        //RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imagen = MultipartBody.Part.createFormData("foto",file.getName(),requestFile);


    }

    private  void animacion_errores(){

        credenciales.doneLoadingAnimation(Color.parseColor("#00b347"), BitmapFactory.decodeResource(getResources(),R.drawable.login_check));
        datos.doneLoadingAnimation(Color.parseColor("#00b347"), BitmapFactory.decodeResource(getResources(),R.drawable.login_check));
        fotos.doneLoadingAnimation(Color.parseColor("#00b347"), BitmapFactory.decodeResource(getResources(),R.drawable.login_check));
    }

    private void animacion_registro(){

        myDialog.show();
        credenciales.startAnimation();
        datos.startAnimation();
        fotos.startAnimation();

    }
}
