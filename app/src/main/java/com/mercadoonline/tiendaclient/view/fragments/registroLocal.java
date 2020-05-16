package com.mercadoonline.tiendaclient.view.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.mercadoonline.tiendaclient.R;
import com.mercadoonline.tiendaclient.mapas.MapDialogFragment;
import com.mercadoonline.tiendaclient.models.ApiMaps.DatosDireccion;
import com.mercadoonline.tiendaclient.service.ApiService2;
import com.mercadoonline.tiendaclient.service.RetrofitclienteMaps;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.List;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;
import static com.mercadoonline.tiendaclient.utils.Global.verificar_vacio;


/**
 * A simple {@link Fragment} subclass.
 */
public class registroLocal extends Fragment {

    Uri imagenNegocio; //Imagen q va a ser agregada al registro

    View vista;
    TextInputLayout TINomNegocio, TITelNegocio;
    ImageView ImgNegoio;
    EditText ETNomNegocio, ETTelNegocio;
    LinearLayout ContenedorUbicame, ContenedorVerDir;
    CircularProgressButton BtnRegisNegoio;
    TextView TVPresentDir;

    MapDialogFragment map;
    //LatLng NuevaUbicacion = null;
    String calle = "Dirección no especifica";

    ApiService2 retrofitApi;
    Retrofit retrofit;
    String direccion="";

    LocationManager locationManager ;
    private Marker marcador;
    public  LatLng NuevaUbicacion =null;

    public registroLocal() {
        // Required empty public constructor
    }

    private void UI(){
        TINomNegocio= vista.findViewById(R.id.TINombreNegocio);
        TITelNegocio=vista.findViewById(R.id.TITelefonoNegocio);
        ETNomNegocio=vista.findViewById(R.id.ETNombreNegocio);
        ETTelNegocio=vista.findViewById(R.id.ETTelefonoNegocio);
        ContenedorUbicame=vista.findViewById(R.id.contenedorUbicame);
        BtnRegisNegoio=vista.findViewById(R.id.BtnRegisNegocio);
        ImgNegoio=vista.findViewById(R.id.imageIconoNeg);
        TVPresentDir=vista.findViewById(R.id.TVVerDir);
        ContenedorVerDir=vista.findViewById(R.id.layout_direcciónNegocio);

        map = new MapDialogFragment(new MapDialogFragment.OnItemOk() {
            @Override
            public void onItemClickok(LatLng position) {

                NuevaUbicacion = position;

                if (NuevaUbicacion != null) {

                    ContenedorUbicame.setBackground(getResources().getDrawable(R.drawable.border_verde));
                    TVPresentDir.setText(calle);
                    ContenedorVerDir.setVisibility(View.VISIBLE);
                    consulta_ubicacion();
                   /* new AsyncTask<Void, Void, Boolean>() {


                        @Override
                        protected Boolean doInBackground( Void... voids ) {
                            //Do things...
                               direccion();
                               consulta();

                            return true;
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
                            super.onPostExecute(aBoolean);
                            if(aBoolean){
                                presentacion_dirección.setText(calle);

                            }
                        }
                    }.execute();
*/
                    //  presentacion_dirección.setText(calle);  esto esta en layout direccion

                }
                Log.e("obtengo", "position");
            }
        });

    }

    public void CLick(){
        ContenedorUbicame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imagenNegocio != null)
                    Log.e("imgane", imagenNegocio.toString());
                    map.fotito = imagenNegocio;
       // Log.e("Se cargo fotito", map.fotito.toString());
                if (NuevaUbicacion != null) {
                    Log.e("cambio", "ubicacion");
                    map.nuevo = NuevaUbicacion;
                }


                map.show(getFragmentManager(), null);


            }
        });

        BtnRegisNegoio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar_campos();

                //("boton registar", "se dio clic ");
            }
        });
        //Evento click para cortar o seleccionar imagen
        ImgNegoio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                funcion_cortar();
            }
        });



        //validaciones para que al seleccionar campo, el texview cambien de color
        ETNomNegocio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //spn_rolUser
                if (hasFocus) {

                    TINomNegocio.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TINomNegocio.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
            //validaciones para que al seleccionar campo, el texview cambien de color


        });
        ETTelNegocio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //spn_rolUser
                if (hasFocus) {

                    TITelNegocio.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TITelNegocio.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
            //validaciones para que al seleccionar campo, el texview cambien de color


        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_registro_local, container, false);
        return  vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //animacion_cargando();
        UI();
      //  peticion_mercado();
        CLick();
    }

    private void validar_campos(){
        if(verificar_vacio(ETNomNegocio.getText().toString())) {
            ETNomNegocio.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
        }  if(verificar_vacio(ETTelNegocio.getText().toString())) {
            ETTelNegocio.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
        }else if (imagenNegocio==null) {
            mensaje();
        }else if(direccion.length()<2){
            Snackbar.make(vista, "Escoja su direccion en el Mapa", Snackbar.LENGTH_LONG).show();
        }
        else {
            Snackbar.make(vista, "Deberia registrar", Snackbar.LENGTH_LONG).show();
            //llenarDatos();
        }

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
                imagenNegocio =result.getUri();
                //("obtuve imagen",""+imagen_perfil);


                llenar_subida();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                //("error imagen",result.getError().toString());
            }


        }
    }

    private void llenar_subida(){


        Glide.with(this).load(imagenNegocio).apply(RequestOptions.circleCropTransform()).into(ImgNegoio);


    }
    //mensaje.. de que debe llenar la imagen obligatorio
    private void mensaje() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("¡OH!");
        builder.setMessage("No ha puesto foto de su negocio");
        builder.setPositiveButton("Entiendo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void consulta_ubicacion() {
        retrofit = RetrofitclienteMaps.getInstance();
        retrofitApi = retrofit.create(ApiService2.class);
        Disposable disposable;
        String lat = "" + NuevaUbicacion.latitude + "," + NuevaUbicacion.longitude;

        //Logger.addLogAdapter(new AndroidLogAdapter());
        disposable = retrofitApi.traerGeo(true, lat, getString(R.string.google_maps_keyM))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<DatosDireccion>>() {
                    @Override
                    public void onNext(Response<DatosDireccion> respuesta) {

                        if(respuesta.isSuccessful()){
                            try {
                                if(respuesta.body().getResults().size()>0)
                                    direccion = respuesta.body().getResults().get(0).getFormattedAddress();
                            }catch (Exception e){

                                direccion();
                            }

                        } else direccion();



                       /* //todo cambio
                        for (AddressComponent address : respuesta.getResults().get(0).getAddressComponents()) {


                            for (String s : address.getTypes()) {
                                //("Carlin:", "---" + s);


                                datos_ubicacion(s, address.getLongName());
                            }


                            if (address.getTypes().contains(ApiString.CIUDADELA3) || address.getTypes().contains(ApiString.CIUDADELA4)) {
                                tienda.setCiudad(address.getLongName());
                            } else if (address.getTypes().contains(ApiString.CIUDADELA) || address.getTypes().contains(ApiString.CIUDADELA2)) {
                                tienda.setCiudad(address.getLongName());
                            }


                        }
*/

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                       /* //("Ciudadela", "-" + tienda.getCiudadela());
                        //("Provincia", tienda.getProvincia());
                        //("Calle", "-" + tienda.getCalle());
                        //("Ciudad", tienda.getCiudad());
                        //("Pais", tienda.getPais());*/
                        if(direccion.length()>3)
                            Log.e("la direc",""+direccion);
                            //UbicaBtnContinuar.setVisibility(View.VISIBLE);
                        TVPresentDir.setText(direccion);

                    }
                });

    }

    private void direccion(){
        Geocoder geocoder= new Geocoder(getActivity());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(NuevaUbicacion.latitude, NuevaUbicacion.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if(addresses.size()>0){

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                String knownName2 = addresses.get(0).getPremises();
                String knownName3 = addresses.get(0).getSubAdminArea();
                String knownName4 = addresses.get(0).getSubLocality();
                String knownName5 = addresses.get(0).getSubThoroughfare();
                //("addess","-"+address);
                //("city","-"+city);
                //("state","-"+state);
                //("country","-"+country);
                //("postalCode","-"+postalCode);
                //("lugar","-"+knownName);
                //("lugar","-"+knownName2);
                //("lugar","-"+knownName3);
                //("lugar","-"+knownName4);
                //("lugar","-"+knownName5);
                direccion=address;
                TVPresentDir.setText(direccion);
            }else{

                direccion="Direccion No Especificada ";
                TVPresentDir.setText(direccion);
            }
            Log.e("la direc 2",""+direccion);


        } catch (IOException e) {
            e.printStackTrace();
            //("lugar","-"+e.toString());

        }


    }
}
