package com.mercadoonline.tiendaclient.view.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mercadoonline.tiendaclient.R;
import com.mercadoonline.tiendaclient.adapter.VistaMultitienda;
import com.mercadoonline.tiendaclient.adapter.VistasMercado;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
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

    View vista;
    RecyclerView recyclerView;
    ImageView compra,icono_filtro;
    VistasMercado adapter;
    List<ResponseVerMercado> listado= new ArrayList<>();
    List<ResponseTiendas> ls_tienda= new ArrayList<>();
    TextView TituloVista;

    VistaMultitienda adapter2;



    Retrofit retrofit;
    ApiService retrofitApi;

    EditText buscar;
    Boolean continuar=false;
    Boolean filtroVista= true;
    String mensaje="mercado";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        vista= inflater.inflate(R.layout.fragment_mercado, container, false);

return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=vista.findViewById(R.id.Recycler_mercados);
        compra=vista.findViewById(R.id.icono_buscar);
        icono_filtro=vista.findViewById(R.id.icono_filtro);
        icono_filtro.setVisibility(View.VISIBLE);
        TituloVista=vista.findViewById(R.id.TituloVista);



        buscar=vista.findViewById(R.id.escribir_busqueda);
        buscar.clearFocus();
llamarPreferences();

        click();

    }

    private void  iniciar_recycler(){
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
                                if(filtroVista)
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


        icono_filtro
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // enviaMensajeWhatsApp();
                popupFiltro(v);

            }
        });






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






    private void peticion_mistiendas(){
        //("peticion","mercado");
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi.VerTiendas(""+Global.LoginU.getToken())
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
                                if(!filtroVista)
                                iniciar_recycler2();
                            }else{
                                Toast.makeText(getActivity(),mensaje,Toast.LENGTH_LONG).show();
                            }
                        }



                    }
                });
    }



    private void iniciar_recycler2(){
        adapter2=new VistaMultitienda(ls_tienda,getFragmentManager());
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
            filtroVista=true;
            TituloVista.setText("Mercados");

        }
        else{
            filtroVista=false;

            TituloVista.setText("Tiendas");

        }
        peticion_mercado();
        peticion_mistiendas();

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
                TituloVista.setText("Mercados");
                guardarPreferences("MERCADO");
                popupWindow.dismiss();

            }
        });


        btnTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciar_recycler2();
                TituloVista.setText("Tiendas");
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






    public void  enviaMensajeWhatsApp(){
try {
        String toNumber = "+593993942225"; // contains spaces.
        toNumber = toNumber.replace("+", "").replace(" ", "");

        Intent sendIntent = new Intent("android.intent.action.MAIN");
        sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Contacto:  283838 \n Costo: $2  \n Ubicacion: https://www.google.com/maps/search/?api=1&query=36.26577,-92.54324/");


        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setPackage("com.whatsapp");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);



    } catch (android.content.ActivityNotFoundException ex) {
        Toast.makeText(getActivity(), "Whatsapp no esta instalado.", Toast.LENGTH_LONG).show();
    }
    }


}
