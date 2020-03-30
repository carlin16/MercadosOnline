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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.tiendaclient.R;
import com.example.tiendaclient.models.recibido.ResponseError;
import com.example.tiendaclient.models.recibido.ResponseRegistroUser;
import com.example.tiendaclient.models.recibido.ResponseUpdateImagen;
import com.example.tiendaclient.models.recibido.ResponseVerMercado;
import com.example.tiendaclient.service.ApiService;
import com.example.tiendaclient.service.RetrofitCliente;
import com.example.tiendaclient.utils.Global;
import com.example.tiendaclient.utils.MinMaxFilter;
import com.example.tiendaclient.view.Login;
import com.example.tiendaclient.view.Principal;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import cn.pedant.SweetAlert.SweetAlertDialog;
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


    View vista;
    String[] Roles;
    int posicionRol=0;
    Boolean cambio_pantalla=false;
    String mensaje="";
    Spinner Rol, Mercado;
    EditText Direccion , TENMer,TENPuest;
    TextInputLayout TIDir, TINomMercado, TINPuesto;
    TextView Soy, IrLogin;
    CircularProgressButton BtnRegistrar2;
    Boolean continuar=false;
    ArrayList<String> list_tiendas = new ArrayList<String>();
    ArrayAdapter<String> spinnerArrayAdapter;
List<ResponseVerMercado> mercadito =new ArrayList<>();
    SweetAlertDialog pDialog;
    ImageView image;
LinearLayout contenedor_mercado;
    Retrofit retrofit;
    ApiService retrofitApi;

    int posRol=0;


    private void UI(){
        Roles= getResources().getStringArray(R.array.Roles);
        Rol=vista.findViewById(R.id.spn_rolUser);
        image=vista.findViewById(R.id.image);
        Direccion=vista.findViewById(R.id.registro_direccion);
        TIDir=vista.findViewById(R.id.TIDireccion);
        Soy=vista.findViewById(R.id.txtRol);
        BtnRegistrar2=vista.findViewById(R.id.btn_registro2);
        IrLogin=vista.findViewById(R.id.ir_login2);
        Mercado=vista.findViewById(R.id.spn_Mercado);
        contenedor_mercado=vista.findViewById(R.id.contenedor_mercado);
        TINPuesto=vista.findViewById(R.id.TINPuesto);


        TENPuest=vista.findViewById(R.id.registro_NumPuesto);
        TENPuest.setFilters( new InputFilter[]{ new MinMaxFilter( "1" , "5" )}) ;

        TIDir.setVisibility(View.VISIBLE);
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,list_tiendas);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        Mercado.setAdapter(spinnerArrayAdapter);


    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        animacion_cargando();
        UI();
        peticion_mercado();
        Click();
    }
    public void Click(){
        IrLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
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

        TINPuesto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TENPuest.requestFocus();
            }
        });



        image.setOnClickListener(new View.OnClickListener() {
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

        TENPuest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //spn_rolUser
                if (hasFocus) {
                    TINPuesto.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TINPuesto.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
            //validaciones para que al seleccionar campo, el texview cambien de color


        });


        Rol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    posRol=position;
                switch (position) {
                    case 0:
                        //Toast.makeText(parent.getContext(), "Spinner item 1!", Toast.LENGTH_SHORT).show();
                      //  TIDir.setVisibility(View.VISIBLE);

                        contenedor_mercado.setVisibility(View.GONE);

                        break;
                    case 1:
                        contenedor_mercado.setVisibility(View.VISIBLE);
                       // TIDir.setVisibility(View.GONE);
                       // Toast.makeText(parent.getContext(), "Spinner item 2!", Toast.LENGTH_SHORT).show();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

    }


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


        Glide.with(this).load(imagen_perfil).apply(RequestOptions.circleCropTransform()).into(image);


    }
    public void funcion_cortar() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(getContext(),this);
    }

    private void animacion_cargando(){
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.col_naranja))));
        pDialog.setTitleText("Registrando");
        pDialog.setCancelable(false);



    }
    private void validar_campos(){
        Log.e("VC", "estoy en validar campos ");




        switch (posRol) {
            case 0:
                if(verificar_vacio(Direccion.getText().toString())) {
                    Direccion.requestFocus();
                    Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
                  } else if (imagen_perfil==null) {
                        mensaje();
                    }else {
                        llenarDatos();
                    }



                break;
            case 1:

                if(verificar_vacio(Direccion.getText().toString())) {
                    Direccion.requestFocus();
                    Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
                }  if(verificar_vacio(TENPuest.getText().toString())) {
                    TENPuest.requestFocus();
                    Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
                }else if (imagen_perfil==null) {
                        mensaje();
                    }else {
                        llenarDatos();
                    }

                break;

        }


    }
    public  void llenarDatos(){
        RegisU.setRol(Roles[posRol]);
        RegisU.setDireccion(Direccion.getText().toString());


        if(posRol>0){
            RegisU.setIdMercado(mercadito.get(Mercado.getSelectedItemPosition()).getId());
            RegisU.setPuesto(TENPuest.getText().toString());
        }


        Log.e("Llenar todos dts", "Se llenaron los datos en Global "+ Global.convertObjToString(RegisU));
        Gson gson = new Gson();
        String JPetUser= gson.toJson(RegisU);
        Log.e("json",JPetUser);
        pDialog.show();
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

        disposable = (Disposable) retrofitApi.RegistroUser(convertedObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<ResponseRegistroUser>>() {
                    @Override
                    public void onNext(Response<ResponseRegistroUser> response) {

                        Log.e("code PU",""+response.code());
                        if (response.isSuccessful()) {
                            cambio_pantalla=true;
                            Global.RegisUser=response.body();
                            Global.LoginU.setid(response.body().getId());
                            mensaje=response.body().getMensaje();
                        } else {

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
                        pDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        Log.e("Completado","registrado");
                        if(!cambio_pantalla){

                            Snackbar.make(vista,""+mensaje, Snackbar.LENGTH_LONG).show();
                           pDialog.dismiss();
                        }else{
                            subir_foto();
                        }



                    }
                });
    }



    public void subir_foto(){

        File file = new File(imagen_perfil.getPath());
        //RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imagen = MultipartBody.Part.createFormData("foto",file.getName(),requestFile);
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi.UploadImage(""+Global.RegisUser.getId(),imagen)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<ResponseUpdateImagen>>() {
                    @Override
                    public void onNext(Response<ResponseUpdateImagen> response) {

                        if (response.isSuccessful()) {
                            cambio_pantalla =true;
                            mensaje=response.body().getMensaje();
                            Log.e("normal",mensaje);
                        } else  if (response.code()==500) {
                            mensaje = "Internal Server Error";
                        } else{

                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson =new Gson();
                                ResponseError staff = gson.fromJson(jObjError.toString(), ResponseError.class);
                                mensaje=staff.getMensaje();
                                Log.e("normal-->400",mensaje);

                            } catch (Exception e) {
                                Log.e("error conversion json",""+e.getMessage());
                            }
                            iniciar_sesion();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                                iniciar_sesion();
                                pDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        Log.e("Completado foto","registrado");

                     iniciar_sesion();
                                pDialog.dismiss();
                    }
                });


    }
    private void iniciar_sesion(){
        Intent intent = new Intent (getActivity(), Principal.class);
        startActivity(intent);
        getActivity().finish();
    }




    private void peticion_mercado(){
        Log.e("peticion","mercado");
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi.VerMercados()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<List<ResponseVerMercado>>>() {
                    @Override
                    public void onNext(Response<List<ResponseVerMercado>> response) {


                        if(response.isSuccessful()){

                            Log.e("code VM",""+response.code());
                            Log.e("respuest VM",Global.convertObjToString(response.body()));
                            mercadito=response.body();
                            continuar=true;

                        }else {

                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson =new Gson();
                                ResponseError staff = gson.fromJson(jObjError.toString(), ResponseError.class);
                                mensaje=staff.getMensaje();
                                Log.e("normal-->400",mensaje);

                            } catch (Exception e) {
                                Log.e("error conversion json",""+e.getMessage());
                            }


                        }


                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("code VM","error");

                    }

                    @Override
                    public void onComplete() {

                        Log.e("code VM","completado");
                        // adapter.notifyDataSetChanged();
                        if(getActivity()==null || isRemoving() || isDetached()){
                            Log.e("activity","removido de la actividad mercado");
                            return;
                        }else{



                            if(continuar){
                                for (ResponseVerMercado x:mercadito){
                                    list_tiendas.add(x.getNombre());
                                    spinnerArrayAdapter.notifyDataSetChanged();

                                }

                            }else{
                                Toast.makeText(getActivity(),mensaje,Toast.LENGTH_LONG).show();
                            }


                        }



                    }
                });
    }


}
