package com.example.tiendaclient.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
//
import com.example.tiendaclient.R;
import com.example.tiendaclient.adapter.VistasPuestos;
import com.example.tiendaclient.models.recibido.Puesto;
import com.example.tiendaclient.models.recibido.ResponseVerAllPuesto;
import com.example.tiendaclient.models.recibido.ResponseVerMercado;
import com.example.tiendaclient.service.ApiService;
import com.example.tiendaclient.service.RetrofitCliente;
import com.example.tiendaclient.utils.Global;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class puestos extends Fragment {


    public List<ResponseVerAllPuesto> ls_listado= new ArrayList<>();
    public ResponseVerMercado Mercado= new ResponseVerMercado();

    public puestos() {
        // Required empty public constructor
    }

    View vista;
    RecyclerView recyclerView;
    VistasPuestos adapter;

    Retrofit retrofit;
    ApiService retrofitApi;
    TextView NombreMercado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista =  inflater.inflate(R.layout.fragment_puestos, container, false);
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NombreMercado=vista.findViewById(R.id.TVPNombreMercado);
        recyclerView= vista.findViewById(R.id.Recycler_puestos);
        NombreMercado.setText(Mercado.getNombre());



    iniciar_recycler();
    peticio_puestos();
    }




    private void  iniciar_recycler(){
        adapter=new VistasPuestos(ls_listado,getFragmentManager(),Mercado);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void peticio_puestos(){
        Log.e("peticion","de puestos");
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi.VerAllPuestos("si",""+Mercado.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<List<ResponseVerAllPuesto>>>() {
                    @Override
                    public void onNext(Response<List<ResponseVerAllPuesto>> response) {
                        ls_listado.clear();
                        Log.e("code VP",""+response.code());
                        Log.e("respuest VP", Global.convertObjToString(response.body()));
                        ls_listado.addAll(response.body());


                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("code VP","error"+e.toString());

                    }

                    @Override
                    public void onComplete() {

                        Log.e("code VP","completado");
                        adapter.notifyDataSetChanged();

                    }
                });
    }


}
