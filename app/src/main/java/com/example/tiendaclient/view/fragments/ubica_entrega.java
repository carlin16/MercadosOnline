package com.example.tiendaclient.view.fragments;

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
import androidx.fragment.app.FragmentManager;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiendaclient.R;
import com.example.tiendaclient.models.ApiMaps.DatosDireccion;
import com.example.tiendaclient.models.compra.CompraProductos;
import com.example.tiendaclient.models.enviado.Detalle;
import com.example.tiendaclient.models.enviado.PeticionPedido;
import com.example.tiendaclient.models.recibido.ResponseError;
import com.example.tiendaclient.models.recibido.ResponseRegistrarPedido;
import com.example.tiendaclient.service.ApiService;
import com.example.tiendaclient.service.ApiService2;
import com.example.tiendaclient.service.RetrofitCliente;
import com.example.tiendaclient.service.RetrofitclienteMaps;
import com.example.tiendaclient.utils.ConnectivityStatus;
import com.example.tiendaclient.utils.Global;
import com.example.tiendaclient.view.Principal;
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
    String mensaje;
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
                 Log.e("posicion",""+PosicionListaArray);
                 getFragmentManager().popBackStack(id_del_fragment,0);
            }
        });
    }

    private void Registrar(){
        PeticionPedido pedido= new PeticionPedido();
        pedido.setCostoEnvio(2);
        pedido.setCostoVenta(Global.VerCompras.get(PosicionListaArray).getTotal());
        pedido.setTotal(Total_precio);
        pedido.setIdUsuario(Global.LoginU.getid());
        pedido.setIdMercado(Global.VerCompras.get(PosicionListaArray).getId());
        pedido.setIdTransportista(0);
        pedido.setLatEntrega(""+nuevo.latitude);
        pedido.setLngEntrega(""+nuevo.longitude);
        pedido.setDireccionEntrega(direccion);
        pedido.setCelularContacto(Global.LoginU.getCelular());

List<Detalle> pro= new ArrayList<>();
        for(CompraProductos p:productos){

            Detalle detproduc= new Detalle();


            detproduc.setIdProducto(p.getIdProducto());
            detproduc.setIdVendedor(Integer.parseInt(p.getIdVendedor()));
            detproduc.setCantidad(p.getId_cantidad());


            pro.add(detproduc);


        }

        pedido.setDetalle(pro);
        Gson gson = new Gson();
        String JSONPedido= gson.toJson(pedido);
        Log.e("json",JSONPedido);
        pDialog.show();
        peticion_Registrar(JSONPedido);

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
        Log.e("click","click");
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


    private void agregar_marcador(double lat, double lng) {
        nuevo = new LatLng(lat, lng);
        //CameraUpdate miubicacion = CameraUpdateFactory.newLatLngZoom(nuevo, 14);
        if (marcador != null) marcador.remove();
        marcador = mMap.addMarker(new MarkerOptions().position(nuevo).draggable(true));
        //marcador = gmap.addMarker(new MarkerOptions().position(nuevo).draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
       // mMap.animateCamera(miubicacion);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nuevo,mMap.getCameraPosition().zoom));
        consulta_ubicacion();
        //direccion();

    }

    private void actualizarUbicacion(Location location) {
        if (location != null) {

            agregar_marcador(location.getLatitude(), location.getLongitude());
        }

    }


    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.e("actualizar","ubicacion");
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
        disposable = retrofitApi.traerGeo(true, lat, "AIzaSyBrqlSYGXnmHVsp8yw90cnmi7GvGgjxB50")
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
                                Log.e("Carlin:", "---" + s);


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
                       /* Log.e("Ciudadela", "-" + tienda.getCiudadela());
                        Log.e("Provincia", tienda.getProvincia());
                        Log.e("Calle", "-" + tienda.getCalle());
                        Log.e("Ciudad", tienda.getCiudad());
                        Log.e("Pais", tienda.getPais());*/

                        UbicacionDireccion.setText(direccion);

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
            Log.e("addess","-"+address);
            Log.e("city","-"+city);
            Log.e("state","-"+state);
            Log.e("country","-"+country);
            Log.e("postalCode","-"+postalCode);
            Log.e("lugar","-"+knownName);
            Log.e("lugar","-"+knownName2);
            Log.e("lugar","-"+knownName3);
            Log.e("lugar","-"+knownName4);
            Log.e("lugar","-"+knownName5);
            direccion=address;
            UbicacionDireccion.setText(direccion);
           }else{

               direccion="Direccion No Especificada ";
               UbicacionDireccion.setText(direccion);
           }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("lugar","-"+e.toString());

        }


    }
    private void peticion_Registrar(String jsonConf){
        Retrofit retrofit = RetrofitCliente.getInstance();
       ApiService retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        JsonObject convertedObject = new Gson().fromJson(jsonConf, JsonObject.class);

        disposable = (Disposable) retrofitApi.RegistrarPedidos(convertedObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<ResponseRegistrarPedido>>() {
                    @Override
                    public void onNext(Response<ResponseRegistrarPedido> response) {

                        if(response.isSuccessful()){

                            Log.e("code VM",""+response.code());
                            Log.e("respuest VM",Global.convertObjToString(response.body()));
                          mensaje="Pedido Registrado";
                         correcto=true;
                        }else  if (response.code()==500){
                            mensaje="500 Internal Server Error";

                        }else{
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

                    }

                    @Override
                    public void onComplete() {
                        pDialog.dismiss();
                        Toast.makeText(getActivity(),mensaje,Toast.LENGTH_LONG).show();
                        if(correcto){
                            Global.VerCompras.remove(PosicionListaArray);
                            //getFragmentManager().popBackStack(id_del_fragment,0);
                            //redireccion a pedidos
                           // clearFragmentBackStack();
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.Contenedor_Fragments, new pedido()).commit();
                            ((Principal) getActivity()).clearFragmentBackStack();

                            ((Principal) getActivity()).cambiar_tab(1);

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
/*    public void clearFragmentBackStack() {
        FragmentManager fm = getFragmentManager();
        Log.e("cuantos fragments",""+fm.getBackStackEntryCount());
        for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
            fm.popBackStack();
        }
    }*/
}
