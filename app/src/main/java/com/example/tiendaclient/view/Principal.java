package com.example.tiendaclient.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.tiendaclient.R;
import com.example.tiendaclient.models.recibido.ResponseCategorias;
import com.example.tiendaclient.models.recibido.ResponseError;
import com.example.tiendaclient.service.ApiService;
import com.example.tiendaclient.service.RetrofitCliente;
import com.example.tiendaclient.utils.Global;
import com.example.tiendaclient.utils.Vista_tabs;
import com.example.tiendaclient.view.fragments.agregar_productos;
import com.example.tiendaclient.view.fragments.mercado;
import com.example.tiendaclient.view.fragments.pedido;
import com.example.tiendaclient.view.fragments.perfil_usuario;
import com.example.tiendaclient.view.fragments.productos;
import com.example.tiendaclient.view.fragments.puestos;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Principal extends AppCompatActivity {


   public static TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        if(Global.LoginU.getRol().equals("CLIENTE")){
            getSupportFragmentManager().beginTransaction()
                    //.replace(R.id.Contenedor_Fragments, new mercado()).commit();
                    .replace(R.id.Contenedor_Fragments, new mercado()).addToBackStack("frag_puestos").commit();
            Global.Modo=1;
            Log.e("Modo", "CLIENTE");

        }else if(Global.LoginU.getRol().equals("VENDEDOR")){
            productos productin= new productos();
            //productin.idPuesto=Global.LoginU.getId_puesto();
           // puestito.banderaRol=2;
            getSupportFragmentManager().beginTransaction()
                    //.replace(R.id.Contenedor_Fragments, new mercado()).commit();
                    .replace(R.id.Contenedor_Fragments, productin).commit();
            Global.Modo=2;
            peticion_categorias();
            Log.e("Modo", "VENDEDOR");
        }

       /* getSupportFragmentManager().beginTransaction()
                //.replace(R.id.Contenedor_Fragments, new mercado()).commit();
        .replace(R.id.Contenedor_Fragments, new mercado()).commit();
*/

        iniciar_tabs();
    }


    private void iniciar_tabs() {
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setCustomView(new Vista_tabs(this, R.drawable.ic_home2, R.drawable.ic_home, "Mercados")));
        tabLayout.addTab(tabLayout.newTab().setCustomView(new Vista_tabs(this, R.drawable.ic_product2, R.drawable.ic_product, "Pedidos")));
        tabLayout.addTab(tabLayout.newTab().setCustomView(new Vista_tabs(this, R.drawable.ic_user2, R.drawable.ic_user, "Perfil")));
       tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
       seleccion_tabs();
    }

    public   void cambiar_tab(int position){
        tabLayout.getTabAt(position).select();

    }


    private void seleccion_tabs(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
              //  tabLayout.getTabAt(position).select();

                elegir(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.e("seleccion ","antiguo");

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
              //  int position = tab.getPosition();
                Log.e("seleccion ","nuevo");
                elegir(tab.getPosition());
            }
        });



    }


    private void elegir(int position){
        clearFragmentBackStack();
        switch (position) {
            case 0:
                Log.e("posicion",""+position);

                if(Global.LoginU.getRol().equals("CLIENTE")){
                    getSupportFragmentManager().beginTransaction()
                            //.replace(R.id.Contenedor_Fragments, new mercado()).commit();
                            .replace(R.id.Contenedor_Fragments, new mercado()).commit();

                }else if(Global.LoginU.getRol().equals("VENDEDOR")){
                    productos productin= new productos();
                    //productin.idPuesto=Global.LoginU.getId_puesto();
                    // puestito.banderaRol=2;
                    getSupportFragmentManager().beginTransaction()
                            //.replace(R.id.Contenedor_Fragments, new mercado()).commit();
                            .replace(R.id.Contenedor_Fragments, productin).addToBackStack("frag_puestos").commit();
                }


                break;

            case 1:
                Log.e("posicion",""+position);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Contenedor_Fragments, new pedido()).commit();
                break;

            case 2:
                Log.e("posicion",""+position);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Contenedor_Fragments, new perfil_usuario()).commit();
                break;

        }

    }

    private static void removeAllFragments(FragmentManager fragmentManager) {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }


    public void clearFragmentBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        Log.e("cuantos fragments",""+fm.getBackStackEntryCount());
        for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
            fm.popBackStack();
        }
    }


    private void peticion_categorias(){
        Retrofit retrofit;
        ApiService retrofitApi;
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi.VerCategorias()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<List<ResponseCategorias>>>() {
                    @Override
                    public void onNext(Response<List<ResponseCategorias>> response) {


                        if(response.isSuccessful()){

                            Log.e("code Categoria",""+response.code());
                            Log.e("respuest Categoria",Global.convertObjToString(response.body()));
                            Global.categorias=response.body();

                        }else {

                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson =new Gson();
                                ResponseError staff = gson.fromJson(jObjError.toString(), ResponseError.class);
                                Log.e("normal-->400",staff.getMensaje());

                            } catch (Exception e) {
                                Log.e("error conversion json",""+e.getMessage());
                            }


                        }


                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("code Categoria","error");

                    }

                    @Override
                    public void onComplete() {

                        Global.llenar_categoria();
                    }
                });
    }

}
