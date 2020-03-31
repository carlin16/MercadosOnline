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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tiendaclient.R;
import com.example.tiendaclient.adapter.VistaPedidos;
import com.example.tiendaclient.models.recibido.ResponseError;
import com.example.tiendaclient.models.recibido.ResponseVerPedido;
import com.example.tiendaclient.service.ApiService;
import com.example.tiendaclient.service.RetrofitCliente;
import com.example.tiendaclient.utils.Global;
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
    Boolean continuar=false;
    String mensaje="";
    RelativeLayout RelativeVacio;

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
        RelativeVacio=vista.findViewById(R.id.RelativeVacio);
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


                        if(response.isSuccessful()){
                            listado.clear();
                            Log.e("Pedido",""+response.code());
                            Log.e("Resp Pedido", Global.convertObjToString(response.body()));
                            listado.addAll(response.body());
                            continuar=true;
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



                            if(continuar){
                                if(listado.size()<1){
                                    RelativeVacio.setVisibility(View.VISIBLE);
                                    ///  getFragmentManager().popBackStack();
                                }else{
                                    RelativeVacio.setVisibility(View.GONE);
                                    iniciar_recycler();
                                }
                            }
                            else{
                                Toast.makeText(getActivity(),mensaje,Toast.LENGTH_LONG).show();
                            }





                        }



                    }
                });
    }


}