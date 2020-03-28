package com.example.tiendaclient.view.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tiendaclient.R;
import com.example.tiendaclient.utils.ConnectivityStatus;
import com.example.tiendaclient.utils.Global;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class ubica_entrega extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener {

    TextView DetalleCancelarPedido;
    private GoogleMap mMap;
    LocationManager locationManager ;
    private Marker marcador;
    public  LatLng nuevo=null;
    public String id_del_fragment;
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
        DetalleCancelarPedido=vista.findViewById(R.id.DetalleCancelarPedido);
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

        miUbicacion();
       // mMap.setOnMarkerDragListener(this);

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
        CameraUpdate miubicacion = CameraUpdateFactory.newLatLngZoom(nuevo, 14);
        if (marcador != null) marcador.remove();
        marcador = mMap.addMarker(new MarkerOptions().position(nuevo).draggable(true));
        //marcador = gmap.addMarker(new MarkerOptions().position(nuevo).draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.animateCamera(miubicacion);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nuevo,14));


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
}
