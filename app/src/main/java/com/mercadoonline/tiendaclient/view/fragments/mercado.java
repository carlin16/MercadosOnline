package com.mercadoonline.tiendaclient.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mercadoonline.tiendaclient.R;
import com.mercadoonline.tiendaclient.adapter.VistasMercado;
import com.mercadoonline.tiendaclient.models.recibido.ResponseError;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class mercado extends Fragment {

    public mercado() {
        // Required empty public constructor
    }

    View vista;
    RecyclerView recyclerView;
    ImageView compra;
    VistasMercado adapter;
    List<ResponseVerMercado> listado= new ArrayList<>();
    Retrofit retrofit;
    ApiService retrofitApi;

    EditText buscar;
    Boolean continuar=false;
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
        buscar=vista.findViewById(R.id.escribir_busqueda);
        buscar.clearFocus();

        peticion_mercado();

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

}
