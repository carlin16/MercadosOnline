package com.example.tiendaclient.view.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tiendaclient.R;
import com.example.tiendaclient.models.enviado.PeticionNuevoProducto;
import com.example.tiendaclient.models.recibido.ResponseCategorias;
import com.example.tiendaclient.models.recibido.ResponseError;
import com.example.tiendaclient.models.recibido.ResponseRegistroUser;
import com.example.tiendaclient.service.ApiService;
import com.example.tiendaclient.service.RetrofitCliente;
import com.example.tiendaclient.utils.Global;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;
import static com.example.tiendaclient.utils.Global.RegisU;
import static com.example.tiendaclient.utils.Global.verificar_vacio;

/**
 * A simple {@link Fragment} subclass.
 */
public class agregar_productos extends Fragment {
    Context context;

    Boolean continuar=false;
    int posUnidadMedida;
    String[] UnidadesM;

    View vista;
    Uri NPimagen_product;
    RelativeLayout NPRelativeImagen;
    ImageView NPImage;
    LinearLayout NP_Esconder;

    EditText ETNPNomPro , ETNPPrecio, ETNPDescrip;
    TextInputLayout TINPNomPro , TINPPrecio, TINPDescrip;
    RelativeLayout NPBTNRegistProd;
    Spinner NPCategoria, NPUnidadMed;

    Retrofit retrofit;
    ApiService retrofitApi;


    String mensaje="";
    PeticionNuevoProducto NuevoProducto= new PeticionNuevoProducto();
   ArrayList<String> listNomCategorias = new ArrayList<String>();
    List<ResponseCategorias> categoria =new ArrayList<>();
    ArrayAdapter<String> spinnerArrayAdapter;


    public agregar_productos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_agregar_productos, container, false);
        return vista;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UI();
        validar2Decimales();
        peticion_categorias();
        Click();
    }

    private void UI(){
        UnidadesM= getResources().getStringArray(R.array.Unidades);
        ETNPNomPro=vista.findViewById(R.id.ETNPNomPro);
        ETNPPrecio=vista.findViewById(R.id.ETNPPrecio);

        ETNPDescrip=vista.findViewById(R.id.ETNPDescrip);

        TINPNomPro=vista.findViewById(R.id.TINPNomPro);
        TINPPrecio=vista.findViewById(R.id.TINPPrecio);


        TINPDescrip=vista.findViewById(R.id.TINPDescrip);


        NPBTNRegistProd=vista.findViewById(R.id.NPBTNRegistProd);

        NPUnidadMed=vista.findViewById(R.id.NPUnidadMed);
        NPCategoria=vista.findViewById(R.id.NPCategoria);

        NPImage =vista.findViewById(R.id.NPImage);
        NPRelativeImagen=vista.findViewById(R.id.NPRelativeImagen);
        NP_Esconder=vista.findViewById(R.id.NP_Esconder);
        //Cargar categorias desde consumo de API-REST
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item ,listNomCategorias);
        spinnerArrayAdapter.setDropDownViewResource( R.layout.spinner_dropdown_item);
        NPCategoria.setAdapter(spinnerArrayAdapter);


       // NPCategoria.setBackgroundColor(12);



    }


    private void llenar_subida(){
        Glide.with(this).load(NPimagen_product).apply(RequestOptions.circleCropTransform()).into(NPImage);
        NP_Esconder.setVisibility(View.GONE);
        NPImage.setVisibility(View.VISIBLE);


    }
    public void funcion_cortar() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(getContext(),this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Foto", "Entre a ver foto");
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                NPimagen_product=result.getUri();
                Log.e("obtuve imagen",""+NPimagen_product);


                llenar_subida();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("error imagen",result.getError().toString());
            }
        }
    }

    public void Click(){


        NPBTNRegistProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar_campos();
                Log.e("boton registar", "se dio clic ");
            }
        });


        NPRelativeImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                funcion_cortar();
            }
        });

        //validaciones para que al seleccionar campo, el texview cambien de color
        ETNPNomPro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //spn_rolUser
                if (hasFocus) {
                    TINPNomPro.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TINPNomPro.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
            //validaciones para que al seleccionar campo, el texview cambien de color


        });

        ETNPPrecio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //spn_rolUser
                if (hasFocus) {
                    TINPPrecio.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TINPPrecio.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
            //validaciones para que al seleccionar campo, el texview cambien de color


        });



        ETNPDescrip.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //spn_rolUser
                if (hasFocus) {
                    TINPDescrip.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TINPDescrip.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
            //validaciones para que al seleccionar campo, el texview cambien de color


        });

        NPUnidadMed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posUnidadMedida =position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });



    }

    private void validar_campos(){
        Log.e("VC", "estoy en validar campos ");
        if(verificar_vacio(ETNPNomPro.getText().toString())) {
            ETNPNomPro.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
        }else if(verificar_vacio(ETNPPrecio.getText().toString())) {
            ETNPPrecio.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
        }else if(verificar_vacio(ETNPDescrip.getText().toString())) {
            ETNPDescrip.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();

        } else if (NPimagen_product==null) {
                mensaje();
            }else {
               llenarDatos();
            }




    }

    private void mensaje() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("¡OH!");
        builder.setMessage("No ha puesto foto de producto");
        builder.setPositiveButton("Entiendo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public  void llenarDatos(){

        NuevoProducto.setNombre(ETNPNomPro.getText().toString());
        NuevoProducto.setDescripcion(ETNPDescrip.getText().toString());
        NuevoProducto.setPrecio(Double.parseDouble(ETNPPrecio.getText().toString()));
        NuevoProducto.setIdCategoria(1);
        NuevoProducto.setUnidades(UnidadesM[posUnidadMedida]);
        NuevoProducto.setIdCategoria(categoria.get(NPCategoria.getSelectedItemPosition()).getId());
        NuevoProducto.setIdPuesto(1);

        Log.e("Llenar Ctg", "los datos llenados son "+ Global.convertObjToString(NuevoProducto));
        //siguiente_paantalla();

         Gson gson = new Gson();
         String JPetUser= gson.toJson(NuevoProducto);
          Log.e("json",JPetUser);
        //  animacion_registro();
        //peticion_Registro(JPetUser);
    }
    private void validar2Decimales(){
        ETNPPrecio.setFilters(new InputFilter[]{new InputFilter() {

            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                int indexPoint = dest.toString().indexOf('.');

                if (indexPoint == -1)
                    return source;

                int decimals = dend - (indexPoint+1);
                return decimals < 2 ? source : "";
            }
        }
        });

    }

    private void peticion_categorias(){
        Log.e("peticionCategorias","estoy en categorias");
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi.VerCategorias()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<List<ResponseCategorias>>>() {
                    @Override
                    public void onNext(Response<List<ResponseCategorias>> response) {


                        if(response.isSuccessful()){

                            Log.e("code Categoria",""+response.code());
                            Log.e("respuest Categoria",Global.convertObjToString(response.body()));
                            categoria=response.body();
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
                        Log.e("code Categoria","error");

                    }

                    @Override
                    public void onComplete() {

                        Log.e("code Categoria","completado");
                        // adapter.notifyDataSetChanged();
                        if(getActivity()==null || isRemoving() || isDetached()){
                            Log.e("activity","removido de la actividad nuevo producto");
                            return;
                        }else{



                            if(continuar){
                                for (ResponseCategorias x:categoria){
                                    listNomCategorias.add(x.getNombre());
                                    spinnerArrayAdapter.notifyDataSetChanged();

                                }

                            }else{
                                Toast.makeText(getActivity(),mensaje,Toast.LENGTH_LONG).show();
                            }


                        }



                    }
                });
    }

    /*private void peticion_PegistroNP(String jsonConf){
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
                           // cambio_pantalla=true;
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
    }*/

}
