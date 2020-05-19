package com.mercadoonline.tiendaclient.mapas;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mercadoonline.tiendaclient.service.ApiService2;
import com.mercadoonline.tiendaclient.utils.ConnectivityStatus;
import com.mercadoonline.tiendaclient.R;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;

//import com.example.digo_ec.R;
//import com.example.digo_ec.service.utils.ConnectivityStatus;

public class MapDialogFragment extends DialogFragment implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener {
GoogleMap gmap;
   public Uri fotito;
    LocationManager locationManager ;
    private Marker marcador;
   public LatLng nuevo=null;
    Button ok;
    OnItemOk onItemOk;

    ApiService2 retrofitApi;
    Retrofit retrofit;
    String direccion="";

    public MapDialogFragment(OnItemOk onItemOk) {
        this.onItemOk = onItemOk;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_maps, container, false);
        catchGoogleMapsException(getActivity());
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment supportMapFragment =  SupportMapFragment.newInstance();
        fm.beginTransaction().replace(R.id.mapViewRegistro, supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);


        ok= rootView.findViewById(R.id.boton_ok_ubicacion);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(locationManager!=null)
                locationManager.removeUpdates(locListener);
                onItemOk.onItemClickok(nuevo);
                dismiss();
            }
        });

        return  rootView;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.getUiSettings().setZoomControlsEnabled(true);




        if(ConnectivityStatus.isConnected(getContext()))
        {
            gmap = googleMap;
         /*   try {

                boolean success = gmap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getContext(), R.raw.style_json));

                if (!success) {
                    Log.e("TAG", "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Toast.makeText(getContext(),"Error de estilo",Toast.LENGTH_LONG).show();
            }*/


            gmap.setOnMapClickListener(this);
            gmap.getUiSettings().setZoomControlsEnabled(true);
            gmap.setMinZoomPreference(12);



           /* LatLng latLng2 = new LatLng(-2.192149, -79.890231);

            gmap.addMarker(new MarkerOptions().position(latLng2).title("Marker Carlos"));
            gmap.moveCamera(CameraUpdateFactory.newLatLng(latLng2));

*/

            gmap.moveCamera(CameraUpdateFactory.zoomTo(17));

            miUbicacion();


            gmap.setOnMarkerDragListener(this);


            if(nuevo!=null){
                Log.e("ya tengo","ubicacion");
              //  agregar_marcador(nuevo.latitude,nuevo.longitude);
            }

        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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



        try {

            boolean success = gmap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.style_json));

            if (!success) {
                Log.e("TAG", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Toast.makeText(getContext(),"Error de estilo",Toast.LENGTH_LONG).show();
        }


        if ((ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))) {

            return;
        }
        gmap.setMyLocationEnabled(true);
        gmap.getUiSettings().setMyLocationButtonEnabled(true);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.NewDialog2);

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
        Log.e("click","end");
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



    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.e("actualizar","ubicacion");
           // actualizarUbicacion(location);
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


    private void actualizarUbicacion(Location location) {
        if (location != null) {

               agregar_marcador(location.getLatitude(), location.getLongitude());
        }

    }

    private void agregar_marcador(double lat, double lng) {
      nuevo = new LatLng(lat, lng);
        CameraUpdate miubicacion = CameraUpdateFactory.newLatLngZoom(nuevo, 18);
        if (marcador != null) marcador.remove();
      //  Bitmap layout_personalizado = marcador_personalizado(getActivity(),R.drawable.adduser2,"",null,this.fotito);


       // marcador = gmap.addMarker(new MarkerOptions().position(nuevo).draggable(true).icon( BitmapDescriptorFactory.fromBitmap(layout_personalizado)));

        marcador = gmap.addMarker(new MarkerOptions().position(nuevo).draggable(true));//.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.)));
        gmap.animateCamera(miubicacion);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(nuevo));

    }


    public static Bitmap marcador_personalizado(Context context, @DrawableRes int resource, String _name, Bitmap image,Uri fotito) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_personalizado, null);
        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.add_btn);
      //  markerImage.setImageResource(resource);
      //  markerImage.setImageBitmap(image);


/*        if(fotito!=null){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), fotito);
                markerImage.setImageBitmap(bitmap);
                Log.e("use","bitmap");
                Log.e("la fotito",bitmap.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            Log.e("use","Glide");

            Glide
                    .with(context)
                    .load(fotito)
                    .error(R.drawable.adduser2)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(markerImage);

        }*/
        Glide
                .with(context)
                .load(""+fotito)
                .error(R.drawable.adduser2)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(markerImage);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //  marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }
    public interface  OnItemOk{
        void onItemClickok(LatLng position);
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




}
