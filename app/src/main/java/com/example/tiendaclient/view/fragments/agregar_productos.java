package com.example.tiendaclient.view.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import com.example.tiendaclient.R;
import com.example.tiendaclient.models.enviado.PeticionNuevoProducto;
import com.example.tiendaclient.models.recibido.Producto;
import com.example.tiendaclient.models.recibido.ResponseCategorias;
import com.example.tiendaclient.models.recibido.ResponseError;
import com.example.tiendaclient.models.recibido.ResponseRegistarProducto;
import com.example.tiendaclient.models.recibido.ResponseUpdateImagen;
import com.example.tiendaclient.service.ApiService;
import com.example.tiendaclient.service.RetrofitCliente;
import com.example.tiendaclient.utils.Global;
import com.example.tiendaclient.view.Principal;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
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
import static com.example.tiendaclient.utils.Global.verificar_vacio;

/**
 * A simple {@link Fragment} subclass.
 */
public class agregar_productos extends Fragment {
    Context context;

    Retrofit retrofit;
    ApiService retrofitApi;
    public  Producto product;
    public int bandera=1;
    boolean cambio=false;

    Boolean continuar=false;
    int posUnidadMedida=0;
    int posCategoria=0;
    String[] UnidadesM;

    View vista;
    Uri NPimagen_product;
    RelativeLayout NPRelativeImagen;
    ImageView NPImage;
    LinearLayout NP_Esconder;

    EditText ETNPNomPro , ETNPDescrip;
    EditText  ETNPPrecio;
    TextInputLayout TINPNomPro , TINPPrecio, TINPDescrip;
    RelativeLayout NPBTNRegistProd;
    TextView TitutloAggP;

    Spinner SPCategoria ;
    Spinner SPUnidad;

    String mensaje="";
    PeticionNuevoProducto NuevoProducto= new PeticionNuevoProducto();
    List<String> listNomCategorias = new ArrayList<>();
    List<String> Unidades;
    List<ResponseCategorias> categoria =Global.categorias;

    TextView NombButton;

    SweetAlertDialog pDialog;
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
        Click();
        animacion_cargando();

        if(bandera==2){
            llenar_edicion();
            elegir_categoria();
            NombButton.setText("Terminar Edición");
            TitutloAggP.setText("Editar Producto");
        }



    }

    private void UI(){


        NombButton=vista.findViewById(R.id.NombButton);

        SPUnidad=vista.findViewById(R.id.SPUnidad);

        UnidadesM= getResources().getStringArray(R.array.Unidades);
        ETNPNomPro=vista.findViewById(R.id.ETNPNomPro);
        ETNPPrecio=vista.findViewById(R.id.ETNPPrecio);

        //Make sure that Decimals is set as false if a custom Separator is used

        ETNPPrecio.addTextChangedListener(moneda);

        //  ETNPPrecio.setText(d);

        ETNPDescrip=vista.findViewById(R.id.ETNPDescrip);

        TINPNomPro=vista.findViewById(R.id.TINPNomPro);
        TINPPrecio=vista.findViewById(R.id.TINPPrecio);
        TitutloAggP=vista.findViewById(R.id.TitutloAggP);


        TINPDescrip=vista.findViewById(R.id.TINPDescrip);


        NPBTNRegistProd=vista.findViewById(R.id.NPBTNRegistProd);

        SPCategoria =vista.findViewById(R.id.NPCategoria);

        NPImage =vista.findViewById(R.id.NPImage);
        NPRelativeImagen=vista.findViewById(R.id.NPRelativeImagen);
        NP_Esconder=vista.findViewById(R.id.NP_Esconder);
        //Cargar categorias desde consumo de API-REST
        /**/
        Unidades = Arrays.asList( UnidadesM );






        ArrayAdapter<String> spinnerArrayAdapter;
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.spinner_item2,Unidades);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        SPUnidad.setAdapter(spinnerArrayAdapter);
        SPUnidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               /* if (position == spinnerArrayAdapter.getCount()) {
                   ((TextView) view).setTextColor(ContextCompat.getColor(getActivity(), R.color.col_gris));
                } else {
                    ((TextView) view).setTextColor(ContextCompat.getColor(getActivity(), R.color.col_negrosolida));*/
                    posUnidadMedida=position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> spinnerArrayAdapte2;
        spinnerArrayAdapte2 = new ArrayAdapter<>(getActivity(),R.layout.spinner_item2,Global.Nombres_Categoria);
        spinnerArrayAdapte2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        SPCategoria.setAdapter(spinnerArrayAdapte2);
        SPCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               /* if (position == spinnerArrayAdapter.getCount()) {
                   ((TextView) view).setTextColor(ContextCompat.getColor(getActivity(), R.color.col_gris));
                } else {
                    ((TextView) view).setTextColor(ContextCompat.getColor(getActivity(), R.color.col_negrosolida));*/
                posCategoria=position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }



    private void llenar_edicion(){
        String url=Global.Url+"productos/"+product.getId()+"/foto";
        Glide.with(this).load(url).placeholder(R.drawable.ic_place_productos).error(R.drawable.ic_place_productos).fitCenter().into(NPImage);
        NP_Esconder.setVisibility(View.GONE);
        NPImage.setVisibility(View.VISIBLE);
        ETNPNomPro.setText(product.getNombre());
        ETNPDescrip.setText(product.getDescripcion());

        DecimalFormat f = new DecimalFormat("##.00");
        ETNPPrecio.setText("$"+f.format(Double.parseDouble(product.getPrecio())));
        elegir_categoria();

    }



    private void elegir_categoria(){

        // SPCategoria.selectItemByIndex(Global.Nombres_Categoria.indexOf());
       SPUnidad.setSelection(Unidades.indexOf(product.getUnidades()));


        int indice=0;
        for(ResponseCategorias c : Global.categorias){
            if(c.getId()==Integer.parseInt(product.getIdCategoria())){

                indice=Global.categorias.indexOf(c);
            }
        }


        SPCategoria.setSelection(indice);
        //   SpUnidad.selectItemByIndex(indice);

    }




    private void llenar_subida(){
        Glide.with(this).load(NPimagen_product).into(NPImage);
        NP_Esconder.setVisibility(View.GONE);
        NPImage.setVisibility(View.VISIBLE);


    }
    public void funcion_cortar() {
        CropImage.activity()
                .setAspectRatio(4, 4)
                .setFixAspectRatio(true)
                .start(getContext(),this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //("Foto", "Entre a ver foto");
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                NPimagen_product=result.getUri();
                //("obtuve imagen",""+NPimagen_product);
                cambio=true;


                llenar_subida();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                //("error imagen",result.getError().toString());
            }
        }
    }

    public void Click(){


        NPBTNRegistProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    double cleanOutput = ETNPPrecio.getCleanDoubleValue();
               validar_campos();

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




    }

    private void validar_campos(){
        //("VC", "estoy en validar campos ");
        if(verificar_vacio(ETNPNomPro.getText().toString())) {
            ETNPNomPro.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
            return;
        }else if(verificar_vacio(ETNPPrecio.getText().toString())) {
            ETNPPrecio.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
            return;
        }else if(verificar_vacio(ETNPDescrip.getText().toString())) {
            ETNPDescrip.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
            return;
        } else if(posUnidadMedida<0){
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
            return;
        }else if(posCategoria<0){
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();

            return;
        }



        if(bandera==2){
            llenarDatos();

        }else{

            if (NPimagen_product==null) {
                mensaje();
            }else {
                llenarDatos();
            }
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
        Double precio = Double.parseDouble(ETNPPrecio.getText().toString().replace("$" ,""));
        NuevoProducto.setPrecio(precio);
        NuevoProducto.setUnidades(UnidadesM[posUnidadMedida]);
        NuevoProducto.setIdCategoria(categoria.get(posCategoria).getId());
        NuevoProducto.setIdPuesto(Global.LoginU.getId_puesto());
        //("Llenar Ctg", "los datos llenados son "+ Global.convertObjToString(NuevoProducto));
        //siguiente_paantalla();
        Gson gson = new Gson();
        String JPetProducto= gson.toJson(NuevoProducto);
        //("json",JPetProducto);
        //  animacion_registro();
        //  subir_ProductoConImagen(JPetProducto);
        pDialog.show();
        if(bandera==1){

            subir_ProductoConImagen(JPetProducto);
        }else{
            peticion_EdicionProduct(JPetProducto, product.getId().toString());
///
///funcion editar que es un put para los datos
            // y un post para subir imagen
            //tienes que crear esos dos api rest
        }


    }



    private void peticion_EdicionProduct(String jsonConf, String id_product){
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        JsonObject convertedObject = new Gson().fromJson(jsonConf, JsonObject.class);

        disposable = (Disposable) retrofitApi.EditarProducto(id_product, convertedObject,Global.LoginU.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<ResponseRegistarProducto>>() {
                    @Override
                    public void onNext(Response<ResponseRegistarProducto> response) {

                        //("code PU",""+response.code());
                        if (response.isSuccessful()) {
                            // cambio_pantalla=true;
                            // Global.RegisUser=response.body();
                            // Global.LoginU.setid(response.body().getId());
                            mensaje=response.body().getMensaje();
                        } else {

                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson =new Gson();
                                ResponseError staff = gson.fromJson(jObjError.toString(), ResponseError.class);
                                mensaje=staff.getMensaje();

                            } catch (Exception e) {
                                //("error conversion json",""+e.getMessage());
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        pDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        //("edicion","exito");

                        if(cambio)
                        cambiar_fotoProdct(product.getId().toString());
                        else {
                            pDialog.dismiss();
                            getFragmentManager().popBackStack();
                        }
                    }
                });
    }



    public void subir_ProductoConImagen(String jsonConf){

        File file = new File(NPimagen_product.getPath());
        RequestBody payload = RequestBody.create(MediaType.parse("text/plain"),jsonConf);

        //RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imagen = MultipartBody.Part.createFormData("foto",file.getName(),requestFile);


        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi.RegistrarProducto(imagen, payload,Global.LoginU.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<ResponseRegistarProducto>>() {
                    @Override
                    public void onNext(Response<ResponseRegistarProducto> response) {

                        if (response.isSuccessful()) {
                            // cambio_pantalla =true;
                            mensaje=response.body().getMensaje();
                            //("normal",mensaje);
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

                        pDialog.dismiss();

                        //("Completado foto","registrado");
                        ((Principal) getActivity()).cambiar_tab(0);

                    }
                });


    }

    private void animacion_cargando(){
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.col_naranja))));
        pDialog.setTitleText("Registrando");
        pDialog.setCancelable(false);



    }
    TextWatcher moneda = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!s.toString().matches("^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {
                String userInput = "" + s.toString().replaceAll("[^\\d]", "");
                StringBuilder cashAmountBuilder = new StringBuilder(userInput);

                while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                    cashAmountBuilder.deleteCharAt(0);
                }
                while (cashAmountBuilder.length() < 3) {
                    cashAmountBuilder.insert(0, '0');
                }
                cashAmountBuilder.insert(cashAmountBuilder.length() - 2, '.');

                ETNPPrecio.removeTextChangedListener(this);
                ETNPPrecio.setText(cashAmountBuilder.toString());

                ETNPPrecio.setTextKeepState("$" + cashAmountBuilder.toString());
                Selection.setSelection(ETNPPrecio.getText(), cashAmountBuilder.toString().length() + 1);

                ETNPPrecio.addTextChangedListener(this);
            }
        }
    };


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), inImage, UUID.randomUUID().toString() + ".png", "drawing");
        return Uri.parse(path);
    }

    public void cambiar_fotoProdct(String idProducto){
        File file = new File(NPimagen_product.getPath());

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imagen = MultipartBody.Part.createFormData("foto",file.getName(),requestFile);
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi.CambiarFoto(idProducto, imagen,Global.LoginU.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<ResponseRegistarProducto>>() {
                    @Override
                    public void onNext(Response<ResponseRegistarProducto> response) {

                        if (response.isSuccessful()) {

                            mensaje=response.body().getMensaje();
                            //("normal",mensaje);
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
                        //("foto","cambiada");
                        pDialog.dismiss();
                        getFragmentManager().popBackStack();

                    }
                });


    }



}
