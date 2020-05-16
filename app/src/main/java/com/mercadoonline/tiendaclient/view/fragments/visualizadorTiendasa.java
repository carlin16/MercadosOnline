package com.mercadoonline.tiendaclient.view.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.mercadoonline.tiendaclient.R;
//import com.mercadoonline.tiendaclient.adapter.multitienda_adapter;
import com.mercadoonline.tiendaclient.adapter.VistaMultitienda;
import com.mercadoonline.tiendaclient.models.recibido.Puesto;
import com.mercadoonline.tiendaclient.models.recibido.ResponseError;
import com.mercadoonline.tiendaclient.models.recibido.ResponseNombresMercado;
import com.mercadoonline.tiendaclient.models.recibido.ResponseTiendas;
import com.mercadoonline.tiendaclient.service.ApiService;
import com.mercadoonline.tiendaclient.service.RetrofitCliente;
import com.mercadoonline.tiendaclient.utils.Global;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */


public class visualizadorTiendasa extends Fragment {

    List<ResponseTiendas> ls_tienda= new ArrayList<>();
    ResponseTiendas puestin =new ResponseTiendas();
    ImageView compra;
    Retrofit retrofit;
    ApiService retrofitApi;
    CircleImageView agregar;
    View vista;
    Boolean continuar=false;
    String mensaje="mis tiendas";
    EditText buscar;
    VistaMultitienda  adapter;

    RecyclerView recyclerView;

    public visualizadorTiendasa() {
        // Required empty public constructor
    }
    void UI(){
        agregar= vista.findViewById(R.id.agregar_tiendaBAR);
        agregar.setVisibility(View.VISIBLE);
        recyclerView=vista.findViewById(R.id.recycler_view_multitienda);
        compra=vista.findViewById(R.id.icono_buscar);
        compra.setVisibility(View.GONE);

        buscar=vista.findViewById(R.id.escribir_busqueda);
        buscar.clearFocus();
    }
    void Click(){
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registroLocal local = new registroLocal();
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Contenedor_Fragments, local).addToBackStack("frag_regisLocal");
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         vista= inflater.inflate(R.layout.fragment_visualizador_tiendasa, container, false);
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UI();
        peticion_mistiendas();
        Click();
    }






    private void iniciar_recycler(){
       adapter=new VistaMultitienda(ls_tienda);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }


    private void peticion_mistiendas(){
        //("peticion","mercado");
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi.VerMisTiendas(""+Global.LoginU.getid(),"TIENDERO",""+Global.LoginU.getToken())
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
}
