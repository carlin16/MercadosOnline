package com.example.tiendaclient.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tiendaclient.R;
import com.example.tiendaclient.adapter.VistaPedidos;
import com.example.tiendaclient.adapter.VistasMercado;
import com.example.tiendaclient.models.recibido.ResponseVerMercado;
import com.example.tiendaclient.models.recibido.ResponseVerPedido;
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
public class pedido extends Fragment {
    View vista;

    RecyclerView recyclerView;
    VistaPedidos adapter;
    List<ResponseVerPedido> listado= new ArrayList<>();
    Retrofit retrofit;
    ApiService retrofitApi;


    public pedido() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista=inflater.inflate(R.layout.fragment_pedido, container, false);
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       recyclerView=vista.findViewById(R.id.Recycler_pedidos);
        peticion_pedidos();

    }

    private void  iniciar_recycler(){
        adapter= new VistaPedidos(listado, getFragmentManager(),getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void peticion_pedidos(){
        Log.e("Pedidos","Se consumio");
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi.VerPedidosClientes( "CLIENTE", ""+Global.LoginU.getid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<List<ResponseVerPedido>>>() {
                    @Override
                    public void onNext(Response<List<ResponseVerPedido>> response) {

                        Log.e("Pedido",""+response.code());
                        Log.e("Resp Pedido", Global.convertObjToString(response.body()));
                        listado.addAll(response.body());


                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("CodPetiEroor","error");

                    }

                    @Override
                    public void onComplete() {

                        Log.e("CodPetiEroor","completado");
                        // adapter.notifyDataSetChanged();
                        if(getActivity()==null || isRemoving() || isDetached()){
                            Log.e("activity","removido ");
                            return;
                        }else{
                            iniciar_recycler();

                        }



                    }
                });
    }


}
