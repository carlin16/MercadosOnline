package com.example.tiendaclient.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.tiendaclient.R;
import com.example.tiendaclient.utils.Vista_tabs;
import com.example.tiendaclient.view.fragments.agregar_productos;
import com.example.tiendaclient.view.fragments.mercado;
import com.example.tiendaclient.view.fragments.pedido;
import com.example.tiendaclient.view.fragments.perfil_usuario;
import com.google.android.material.tabs.TabLayout;

public class Principal extends AppCompatActivity {

   public static TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        getSupportFragmentManager().beginTransaction()
                //.replace(R.id.Contenedor_Fragments, new mercado()).commit();
        .replace(R.id.Contenedor_Fragments, new agregar_productos()).commit();
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


                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Contenedor_Fragments, new mercado()).commit();
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

}
