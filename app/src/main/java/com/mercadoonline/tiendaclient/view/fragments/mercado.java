package com.mercadoonline.tiendaclient.view.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.mercadoonline.tiendaclient.R;
import com.mercadoonline.tiendaclient.adapter.VistaMultitienda;
import com.mercadoonline.tiendaclient.adapter.VistasMercado;
import com.mercadoonline.tiendaclient.models.recibido.ResponseCategorias;
import com.mercadoonline.tiendaclient.models.recibido.ResponseError;
import com.mercadoonline.tiendaclient.models.recibido.ResponseTiendas;
import com.mercadoonline.tiendaclient.models.recibido.ResponseVerMercado;
import com.mercadoonline.tiendaclient.service.ApiService;
import com.mercadoonline.tiendaclient.service.RetrofitCliente;
import com.mercadoonline.tiendaclient.utils.Global;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class mercado extends Fragment {

    public mercado() {
        // Required empty public constructor
    }
    LocationManager locationManager =null;

    View vista;
    RecyclerView recyclerView;
    ImageView compra;
            //icono_filtro;
    VistasMercado adapter;
    List<ResponseVerMercado> listado= new ArrayList<>();
    List<ResponseTiendas> ls_tienda= new ArrayList<>();
   // TextView TituloVista;
    VistaMultitienda adapter2;
    Retrofit retrofit;
    ApiService retrofitApi;
    EditText buscar;
    Boolean continuar=false;
    String mensaje="mercado";
    RelativeLayout RlNoFound;
    TabLayout tabCategorias;
    List<String> categorias= new ArrayList<>();

    int width  = Resources.getSystem().getDisplayMetrics().widthPixels;
    int height = Resources.getSystem().getDisplayMetrics().heightPixels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista= inflater.inflate(R.layout.fragment_mercado, container, false);
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=vista.findViewById(R.id.Recycler_mercados);
        compra=vista.findViewById(R.id.icono_buscar);
       // icono_filtro=vista.findViewById(R.id.icono_filtro);
       // icono_filtro.setVisibility(View.VISIBLE);
        //TituloVista=vista.findViewById(R.id.TituloVista);
        buscar=vista.findViewById(R.id.escribir_busqueda);
        buscar.clearFocus();
        RlNoFound=vista.findViewById(R.id.RlNoFound);
       // llamarPreferences();
        peticion_mercado();
        categorias.clear();
        click();
        if(Global.categoriasNegocios.size()>0){
            creacion_tabs();
            seleccion_tabs();
        }else{
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    creacion_tabs();
                    seleccion_tabs();
                }
            }, 500);
        }



    }


    @Override
    public void onStart() {
        if(locationManager==null)
            miUbicacion();
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        if(locationManager!=null){
            locationManager.removeUpdates(locListener);
        }



        super.onDestroyView();
    }

    private void  iniciar_recycler(){
        RlNoFound.setVisibility(View.GONE);
        adapter= new VistasMercado(listado,getFragmentManager());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void peticion_mercado(){
        //("peticion","mercado");
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi.VerMercados(Global.LoginU.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<List<ResponseVerMercado>>>() {
                    @Override
                    public void onNext(Response<List<ResponseVerMercado>> response) {


                        if(response.isSuccessful()){
                            listado.clear();
                            //("code VM",""+response.code());
                            //("respuest VM",Global.convertObjToString(response.body()));
                            listado.addAll(response.body());
                            continuar=true;

                        }else {

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
                        //("code VM","error");

                    }

                    @Override
                    public void onComplete() {

                        //("code VM","completado");
                        // adapter.notifyDataSetChanged();
                        if(getActivity()==null || isRemoving() || isDetached()){
                            //("activity","removido de la actividad mercado");
                            return;
                        }else{
                            if(continuar){
                               // if(Global.idFiltro==0)
                                    iniciar_recycler();
                            }else{
                                Toast.makeText(getActivity(),mensaje,Toast.LENGTH_LONG).show();
                            }
                        }



                    }
                });
    }

    public void filtro(String S){

        if(adapter!=null)
            adapter.getFilter().filter(S);
    }


    private void click(){
        compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"La busqueda esta opcional pero esta puesto el prototipo si pide un update $ ",Toast.LENGTH_LONG).show();
                carrito car = new carrito();
                car.id_del_fragment="frag_car";
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Contenedor_Fragments, car).addToBackStack("frag_car");
                fragmentTransaction.commit();




            }
        });


/*        icono_filtro
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        // enviaMensajeWhatsApp();
                        popupFiltro(v);

                    }
                });*/






        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                filtro(editable.toString());









                //  filter(editable.toString());

            }
        });
    }






    private void peticion_mistiendas(String id_catBuscar){
        //("peticion","mercado");
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        Log.e("ubi para consulta", Global.latitudCliente+" "+Global.longitudCliente);
        disposable = (Disposable) retrofitApi.VerTiendas(Global.latitudCliente,Global.longitudCliente,id_catBuscar,""+Global.LoginU.getToken())

       // disposable = (Disposable) retrofitApi.VerTiendas("-2.1875211","-79.8946906",""+1,""+Global.LoginU.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<List<ResponseTiendas>>>() {
                    @Override
                    public void onNext(Response<List<ResponseTiendas>> response) {


                        if(response.isSuccessful()){

                            ls_tienda.clear();
                            //("code VM",""+response.code());
                            //("respuest VM",Global.convertObjToString(response.body()));
                            ls_tienda.addAll(response.body());
                            continuar=true;

                        }else {

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
                        //("code VM","error");

                    }

                    @Override
                    public void onComplete() {

                        if(getActivity()==null || isRemoving() || isDetached()){
                            //("activity","removido de la actividad mercado");
                            return;
                        }else{
                            if(continuar){
                                //if(Global.idFiltro==1)
                                    iniciar_recycler2();
                            }else{
                                Toast.makeText(getActivity(),mensaje,Toast.LENGTH_LONG).show();
                            }
                        }



                    }
                });
    }



    private void iniciar_recycler2(){

        if(ls_tienda.size()>0)
            RlNoFound.setVisibility(View.GONE);
        else
            RlNoFound.setVisibility(View.VISIBLE);

        adapter2=new VistaMultitienda(ls_tienda,getFragmentManager(),getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter2);
    }


    public void guardarPreferences(String filtro){
        SharedPreferences DtsAlmacenados= getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor MyEditorDts=DtsAlmacenados.edit();
        MyEditorDts.putString("FiltroS", filtro);
        MyEditorDts.commit();//devuelve un booleano,hasta que se guarda todo
        MyEditorDts.apply();
    }


    public void llamarPreferences(){
        SharedPreferences DtsRescatados=getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String filtro=DtsRescatados.getString("FiltroS", "MERCADO");
        //Log.e("")
        if(filtro.equals("MERCADO")){
            Global.idFiltro=0;
            //TituloVista.setText("Mercados");

        }
        else{
            Global.idFiltro=1;

           // TituloVista.setText("Tiendas");

        }
        peticion_mercado();
      //  peticion_mistiendas();

    }




    private void popupFiltro(View v){

        LayoutInflater layoutInflater
                = (LayoutInflater)getActivity()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.item_opciones, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,true);





        TextView btnMercado = popupView.findViewById(R.id.filtro_mercado);

        TextView btnTienda = popupView.findViewById(R.id.filtro_tiendas);


        btnMercado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciar_recycler();
                //TituloVista.setText("Mercados");
                Global.idFiltro=0;
                guardarPreferences("MERCADO");
                popupWindow.dismiss();

            }
        });


        btnTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciar_recycler2();
               // TituloVista.setText("Tiendas");
                Global.idFiltro=1;
                guardarPreferences("TIENDA");
                popupWindow.dismiss();
            }
        });


        // popupWindow.showAsDropDown(v);
        popupWindow.showAsDropDown(v,-300,-65);
        popupWindow.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

    }



    private void obtnerUbicacion() {
        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e("falllo","al rscatar ubicaacion");
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        double latitude = location.getLatitude();
        double longitud = location.getLongitude();
        Log.e("ubicacion",""+latitude);
        Log.e("ubicacion",""+longitud);
        //

    }

    //telSend, telContactar, costo, lonLat
    //public  LatLng nuevo=null;
    public void  enviaMensajeWhatsApp(String telInfo, String telContactar, Double costoEnvio, String longUbicacion, String latUbicacion){
        try {
            //String toNumber = "+593993942225"; // contains spaces.
            String toNumber;
            toNumber = telContactar.replace("+", "").replace(" ", "");

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Contacto:  "+""+telInfo+ "\n Costo: $"+""+costoEnvio+  "\n Ubicacion: https://www.google.com/maps/search/?api=1&query="+longUbicacion+","+latUbicacion+"/");



            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setPackage("com.whatsapp");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);



        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "Whatsapp no esta instalado.", Toast.LENGTH_LONG).show();
        }
    }




    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 40000, 5, locListener, Looper.getMainLooper());
        actualizarUbicacion(location);
        // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100,0,locListener);
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




    private void actualizarUbicacion(Location location) {
        if (location != null) {
            Global.latitudCliente=""+location.getLatitude();
            Global.longitudCliente=""+location.getLongitude();

            Log.e("prueba",Global.latitudCliente);
            Log.e("prueba",Global.longitudCliente);

        }

    }
    private void creacion_tabs(){
        tabCategorias=vista.findViewById(R.id.catTab);
        categorias.add("Mercados");
        for(ResponseCategorias rcatg: Global.categoriasNegocios){
                categorias.add(rcatg.getNombre());
        }

        for (String cat:categorias){

            tabCategorias.addTab (tabCategorias.newTab (). setText (cat));
        }

        if(categorias.size()>3){
            tabCategorias.setTabMode(TabLayout.MODE_SCROLLABLE);

        }else{
            tabCategorias.setTabMode(TabLayout.MODE_FIXED);
        }
    }

    private void seleccion_tabs(){
        tabCategorias.getTabAt(0).select();
        tabCategorias.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                //  tabLayout.getTabAt(position).select();
                Log.e("pos tab", ""+position);
              //  elegir(tab.getPosition());
                if(position>0){
                    peticion_mistiendas(""+position);
                    Global.idFiltro=1;
                }else{
                    iniciar_recycler();
                    Global.idFiltro=0;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //("seleccion ","antiguo");

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //  int position = tab.getPosition();
                //("seleccion ","nuevo");
             //   elegir(tab.getPosition());
            }
        });



    }

}
