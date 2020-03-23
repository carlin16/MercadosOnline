package com.example.tiendaclient.view.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tiendaclient.R;
import com.example.tiendaclient.models.recibido.ResponseRegistroUser;
import com.example.tiendaclient.service.ApiService;
import com.example.tiendaclient.service.RetrofitCliente;
import com.example.tiendaclient.utils.Global;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;
import static com.example.tiendaclient.utils.Global.RegisU;
import static com.example.tiendaclient.utils.Global.verificar_vacio;

/**
 * A simple {@link Fragment} subclass.
 */
public class resgistro_completar extends Fragment {

    Uri imagen_perfil;
    RoundedImageView Perfil;
    Dialog myDialog;
    CircularProgressButton BtnRegistrar ,datos,fotos,credenciales;
    View vista;
    String[] Roles;
    int posicionRol=0;
    Boolean cambio=false;
    String mensaje="";
    Spinner Rol;
    EditText Direccion;
    TextInputLayout TIDir;
    TextView Soy;
    CircularProgressButton BtnRegistrar2;

    Retrofit retrofit;
    ApiService retrofitApi;


    private void UI(){
        Perfil=vista.findViewById(R.id.registro_perfil);
        Roles= getResources().getStringArray(R.array.Roles);
        Rol=vista.findViewById(R.id.spn_rolUser);
        Direccion=vista.findViewById(R.id.registro_direccion);
        TIDir=vista.findViewById(R.id.TIDireccion);
        Soy=vista.findViewById(R.id.txtRol);
        BtnRegistrar2=vista.findViewById(R.id.btn_registro2);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        animacion_cargando();
        UI();
        Click();
    }
    public void Click(){
        BtnRegistrar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar_campos();
                Log.e("boton registar", "se dio clic ");
            }
        });

        TIDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Direccion.requestFocus();
            }
        });


        Perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                funcion_cortar();
            }
        });

        //validaciones para que al seleccionar campo, el texview cambien de color
        Direccion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //spn_rolUser
                if (hasFocus) {
                    TIDir.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TIDir.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
            //validaciones para que al seleccionar campo, el texview cambien de color


        });
        Rol.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (Rol.getWindowToken() != null) {
                        Rol.performClick();
                        Soy.setTextColor(Color.parseColor("#EE8813"));
                    }else Soy.setTextColor(Color.parseColor("#CCCCCC"));
                }
            }
        });

    }

    RoundedImageView preview;

    public resgistro_completar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        vista= inflater.inflate(R.layout.fragment_resgistro_completar, container, false);
        return vista;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Foto", "Entre a ver foto");
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
                .start(getContext(),this);
    }

    private void animacion_cargando(){
        myDialog = new Dialog(getActivity(), R.style.NewDialog);
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
    private void validar_campos(){
        Log.e("VC", "estoy en validar campos ");
        if(verificar_vacio(Direccion.getText().toString())) {
            Direccion.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
        }
        else if (imagen_perfil==null) {
            mensaje();
        }else {
            llenarDatos();
        }
    }
    public  void llenarDatos(){

        RegisU.setDireccion(Direccion.getText().toString());
        RegisU.setRol(Rol.getSelectedItem().toString());
        Log.e("Llenar todos dts", "Se llenaron los datos en Global "+ Global.convertObjToString(RegisU));
        Gson gson = new Gson();
        String JPetUser= gson.toJson(RegisU);
        Log.e("json",JPetUser);
         animacion_registro();
        peticion_Registro(JPetUser);
    }

    private void mensaje() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
    private void peticion_Registro(String jsonConf){
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        JsonObject convertedObject = new Gson().fromJson(jsonConf, JsonObject.class);
        //
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
                        //revisar
                        if(response.code()==201){
                            cambio=true;

                        }else{
                            animacion_errores();
                        }
                        mensaje=response.body().getMensaje();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("error",e.toString());
                        animacion_errores();

                    }

                    @Override
                    public void onComplete() {
                        Log.e("Completado","registrado");
                        Toast.makeText(getActivity(),mensaje,Toast.LENGTH_LONG).show();
                        myDialog.dismiss();

                    }
                });
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
    public void subir_foto(){

        //  Staff staff = gson.fromJson(reader, Staff.class);

        File file = new File(imagen_perfil.getPath());
        //RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imagen = MultipartBody.Part.createFormData("foto",file.getName(),requestFile);
    }


}
