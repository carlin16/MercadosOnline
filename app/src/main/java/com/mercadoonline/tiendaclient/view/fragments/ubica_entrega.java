package com.mercadoonline.tiendaclient.view.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mercadoonline.tiendaclient.R;
import com.mercadoonline.tiendaclient.models.ApiMaps.DatosDireccion;
import com.mercadoonline.tiendaclient.models.compra.CompraProductos;
import com.mercadoonline.tiendaclient.models.compra.PuestosCompra;
import com.mercadoonline.tiendaclient.models.enviado.Detalle;
import com.mercadoonline.tiendaclient.models.enviado.PeticionPedido;
import com.mercadoonline.tiendaclient.models.recibido.ResponseError;
import com.mercadoonline.tiendaclient.models.recibido.ResponseRegistrarPedido;
import com.mercadoonline.tiendaclient.service.ApiService;
import com.mercadoonline.tiendaclient.service.ApiService2;
import com.mercadoonline.tiendaclient.service.RetrofitCliente;
import com.mercadoonline.tiendaclient.service.RetrofitclienteMaps;
import com.mercadoonline.tiendaclient.utils.ConnectivityStatus;
import com.mercadoonline.tiendaclient.utils.Global;
import com.mercadoonline.tiendaclient.view.Principal;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ubica_entrega extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener {
    ApiService2 retrofitApi;
    Retrofit retrofit;
    String direccion="";
    String mensajeWhatsappTransportista;

    SweetAlertDialog pDialog;
    TextView DetalleCancelarPedido,DetalleTotalUbica,UbicacionDireccion;
    RelativeLayout UbicaBtnContinuar;
    private GoogleMap mMap;
    LocationManager locationManager ;
    private Marker marcador;
    public  LatLng nuevo=null;
    public List<CompraProductos> productos =new ArrayList<>();
    public String id_del_fragment;
    public Double Total_precio;
    String mensaje="ubicacion";
    Boolean correcto=false;
    public int PosicionListaArray = 0;
    public ubica_entrega() {
        // Required empty public constructor
    }


        View vista;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        catchGoogleMapsException(getActivity());
        vista= inflater.inflate(R.layout.fragment_ubica_entrega, container, false);
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.Maps);
        mapFragment.getMapAsync(this);

        animacion_cargando();
        DetalleCancelarPedido=vista.findViewById(R.id.DetalleCancelarPedido);
        DetalleTotalUbica=vista.findViewById(R.id.DetalleTotalUbica);
        UbicacionDireccion=vista.findViewById(R.id.UbicacionDireccion);
        UbicaBtnContinuar=vista.findViewById(R.id.UbicaBtnContinuar);

        DetalleTotalUbica.setText("$"+Total_precio);

        UbicaBtnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(nuevo!=null)
                    Registrar();
                else
                    Toast.makeText(getActivity(),"Por favor seleccione la ubicación de entrega",Toast.LENGTH_LONG).show();


            }
        });


        DetalleCancelarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //    FragmentManager.popBackStack(id_del_fragment, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                 Global.VerCompras.remove(PosicionListaArray);
                 //("posicion",""+PosicionListaArray);
                 getFragmentManager().popBackStack(id_del_fragment,0);
            }
        });
    }

    private void Registrar(){
        PeticionPedido pedido= new PeticionPedido();
        String tipoNegocio="";

        //paraTodos

        List<Detalle> pro= new ArrayList<>();
        for(CompraProductos p:productos){

            Detalle detproduc= new Detalle();
            detproduc.setIdProducto(p.getIdProducto());
            detproduc.setIdVendedor(Integer.parseInt(p.getIdVendedor()));
            detproduc.setCantidad(p.getId_cantidad());
            pro.add(detproduc);
        }
        pedido.setDetalle(pro);
        pedido.setIdUsuario(Global.LoginU.getid());
        pedido.setLatEntrega(""+nuevo.latitude);
        pedido.setLngEntrega(""+nuevo.longitude);
        pedido.setDireccionEntrega(direccion);
        pedido.setCelularContacto(Global.LoginU.getCelular());
        pedido.setCostoVenta(Global.VerCompras.get(PosicionListaArray).getTotal());

        if(Global.VerCompras.get(PosicionListaArray).getTipoCarro()==0) {
            tipoNegocio = "MERCADO";
            pedido.setTipo(tipoNegocio);
            pedido.setIdMercado(Global.VerCompras.get(PosicionListaArray).getId());
            pedido.setIdTransportista(0);//ojo  hasta qye se llene la BD con transportistas
            pedido.setCostoEnvio((int)Global.costoEnvio);
            pedido.setTotal(Total_precio);

            ///API WHATSS APP

            mensajeWhatsappTransportista="MERCADO:"+Global.VerCompras.get(PosicionListaArray).getNombre()+
                    "\nDirección:"+Global.VerCompras.get(PosicionListaArray).getDireccion();
            for(PuestosCompra puesto: Global.VerCompras.get(PosicionListaArray).getPuestos()){
                mensajeWhatsappTransportista=mensajeWhatsappTransportista+"\n";
                mensajeWhatsappTransportista=mensajeWhatsappTransportista+"PUESTO:"+puesto.getCodigoPuesto();
                mensajeWhatsappTransportista=mensajeWhatsappTransportista+"------------------------ \n";
                for(CompraProductos productos:puesto.getProductos()){
                    mensajeWhatsappTransportista=mensajeWhatsappTransportista+"Producto:"+productos.getNombre();
                    mensajeWhatsappTransportista=mensajeWhatsappTransportista+"\n";
                    mensajeWhatsappTransportista=mensajeWhatsappTransportista+"Cantidad:"+productos.getId_cantidad();
                    mensajeWhatsappTransportista=mensajeWhatsappTransportista+"\n";
                    mensajeWhatsappTransportista=mensajeWhatsappTransportista+"\n";

                }
                mensajeWhatsappTransportista=mensajeWhatsappTransportista+"\n";

            }
            mensajeWhatsappTransportista=mensajeWhatsappTransportista+"\nUbicacion: https://www.google.com/maps/search/?api=1&query="+Global.VerCompras.get(PosicionListaArray).getLatitud()+","+Global.VerCompras.get(PosicionListaArray).getLongitud()+"\nTelefono: "+Global.LoginU.getCelular()+"\nNombres del Cliente: "+Global.LoginU.getNombres();
            Log.e("Whastapp",mensajeWhatsappTransportista);

        }

        if(Global.VerCompras.get(PosicionListaArray).getTipoCarro()==1) {
            tipoNegocio="NEGOCIO";
            pedido.setTipo(tipoNegocio);
            pedido.setIdNegocio(Global.VerCompras.get(PosicionListaArray).getId());
            pedido.setIdVendedor(Global.VerCompras.get(PosicionListaArray).getIdUsuario());
            pedido.setCostoEnvio((int)Global.costoEnvio);
            pedido.setTotal(Total_precio);
           // pedido.setTotal(Global.VerCompras.get(PosicionListaArray).getTotal()+2);
            //pedido.setTotal(Global.VerCompras.get(PosicionListaArray).getTotal());
        }




        Log.e("---1---",Global.convertObjToString(Global.VerCompras.get(PosicionListaArray)));



    //    "Contacto:+"+""+telInfo+"\nTotal a Cobrar: $"+""+costoEnvio+  "\nUbicacion: https://www.google.com/maps/search/?api=1&query="+latUbicacion+","+longUbicacion+"\nNombre Negocio: "+nombreNegocio+"\nNombres del Cliente: "+nombreApellidoCliente









        //0 puesto
        //1negocio


        Gson gson = new Gson();
        String JSONPedido= gson.toJson(pedido);


        //("json",JSONPedido);
     //   pDialog.show();
        Log.e("------",JSONPedido);
       // peticion_Registrar(JSONPedido);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if(ConnectivityStatus.isConnected(getActivity())){


        if ((ActivityCompat.checkSelfPermission(getContext(),android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                (ActivityCompat.checkSelfPermission(getContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
       // mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(this);


            miUbicacion();
       //mMap.setOnMarkerDragListener(this);

        }else{


        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("¡Alerta!");
        builder.setMessage("Revise su conexión a internet");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();        }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        }

    }


    @Override
    public void onMapClick(LatLng latLng) {
        //("click","click");
        agregar_marcador(latLng.latitude,latLng.longitude);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        marker.hideInfoWindow();

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        nuevo= marker.getPosition();
    }

    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(nuevo==null)
            actualizarUbicacion(location);


        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0, locListener, Looper.getMainLooper());

        // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100,0,locListener);
    }

    Boolean move =true;
    private void agregar_marcador(double lat, double lng) {
        nuevo = new LatLng(lat, lng);
        //CameraUpdate miubicacion = CameraUpdateFactory.newLatLngZoom(nuevo, 14);
        if (marcador != null) marcador.remove();
        marcador = mMap.addMarker(new MarkerOptions().position(nuevo).draggable(true));
        //marcador = gmap.addMarker(new MarkerOptions().position(nuevo).draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
       // mMap.animateCamera(miubicacion);
        if(move){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nuevo,18));
            move=false;
        }
        else
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nuevo,mMap.getCameraPosition().zoom));


        if(getActivity()==null || isRemoving() || isDetached()){
            //("activity","removido de la actividad mercado");
            return;
        }else{
            consulta_ubicacion();
        }
    }

    private void actualizarUbicacion(Location location) {
        if (location != null) {

            agregar_marcador(location.getLatitude(), location.getLongitude());
        }

    }


    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //("actualizar","ubicacion");
             actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    private void consulta_ubicacion() {
        retrofit = RetrofitclienteMaps.getInstance();
        retrofitApi = retrofit.create(ApiService2.class);
        Disposable disposable;
        String lat = "" + nuevo.latitude + "," + nuevo.longitude;

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
                                if(getActivity()==null || isRemoving() || isDetached()){
                                    //("activity","removido de la actividad mercado");
                                    return;
                                }else{
                                    direccion();
                                }

                            }
                        } else direccion();
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
                        if(getActivity()==null || isRemoving() || isDetached()){
                            //("activity","removido de la actividad mercado");
                            return;
                        }else{
                            if(direccion.length()>3)
                                UbicaBtnContinuar.setVisibility(View.VISIBLE);
                            UbicacionDireccion.setText(direccion);

                        }
                    }
                });

    }

    private void direccion(){
        Geocoder geocoder= new Geocoder(getActivity());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(nuevo.latitude, nuevo.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
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
            UbicacionDireccion.setText(direccion);
           }else{

               direccion="Direccion No Especificada ";
               UbicacionDireccion.setText(direccion);
           }

        } catch (IOException e) {
            e.printStackTrace();
            //("lugar","-"+e.toString());

        }


    }
    private void peticion_Registrar(String jsonConf){

        Log.e("el pedido es",jsonConf);
        Retrofit retrofit = RetrofitCliente.getInstance();
       ApiService retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        JsonObject convertedObject = new Gson().fromJson(jsonConf, JsonObject.class);

        disposable = (Disposable) retrofitApi.RegistrarPedidos(convertedObject,Global.LoginU.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<ResponseRegistrarPedido>>() {
                    @Override
                    public void onNext(Response<ResponseRegistrarPedido> response) {

                        if(response.isSuccessful()){

                            //("code VM",""+response.code());
                            //("respuest VM",Global.convertObjToString(response.body()));
                          mensaje="Pedido Registrado";
                         correcto=true;
                        }else  if (response.code()==500){
                            mensaje="Ocurrio un error Inesperado";

                        }else{
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
                        if(correcto){
                            Global.VerCompras.remove(PosicionListaArray);
                            //getFragmentManager().popBackStack(id_del_fragment,0);
                            //redireccion a pedidos
                           // clearFragmentBackStack();
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.Contenedor_Fragments, new pedido()).commit();

                            ((Principal) getActivity()).clearFragmentBackStack();
                            ((Principal) getActivity()).cambiar_tab(1);
                            Toast.makeText(getActivity(),mensaje,Toast.LENGTH_LONG).show();


                        }else{
                            if(mensaje.length()<2)
                            Toast.makeText(getActivity(),"Ocurrio un Error de Conexi",Toast.LENGTH_LONG).show();
                            else{
                                Toast.makeText(getActivity(),mensaje,Toast.LENGTH_LONG).show();

                            }

                        }


                    }
                });
    }


    private void animacion_cargando(){
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.col_naranja))));
        pDialog.setTitleText("Registrando");
        pDialog.setCancelable(false);



    }

    public static void catchGoogleMapsException(final Context context)
    {
        final Thread.UncaughtExceptionHandler defaultHandler =
                Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(
                (@NonNull final Thread thread, @NonNull final Throwable ex) ->
                {
                    if (thread.getName().contains("ZoomTableManager"))
                    {
                        new File(context.getFilesDir(), "ZoomTables.data").delete();
                        Log.e("Maps Bug 154855417", "Caught exception and deleted ZoomTables.data");
                    }
                    else{

                        Toast.makeText(context,"Se recomienda volver y intentar de nuevo",Toast.LENGTH_LONG).show();
                        Log.e("Maps","debo presentar el mensaje");
                        if (defaultHandler!=null)
                            defaultHandler.uncaughtException(thread, ex);
                        else
                            throw new RuntimeException(
                                    "No default uncaught exception handler.", ex);
                    }

                });
    }
/*    public void clearFragmentBackStack() {
        FragmentManager fm = getFragmentManager();
        //("cuantos fragments",""+fm.getBackStackEntryCount());
        for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
            fm.popBackStack();
        }
    }*/
}
