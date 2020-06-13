package com.mercadoonline.tiendaclient.view.fragments;

import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.mercadoonline.tiendaclient.R;

import static com.mercadoonline.tiendaclient.view.Principal.tabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class categorias extends Fragment {

    View vista;
    TabLayout tabCategorias;



    public categorias() {
        // Required empty public constructor
    }
    int width  = Resources.getSystem().getDisplayMetrics().widthPixels;
    int height = Resources.getSystem().getDisplayMetrics().heightPixels;


    private  void UI(){
        Log.e("ancho y alto son> ", ""+width+" "+height);
        tabCategorias=vista.findViewById(R.id.catTab);
        tabCategorias.addTab (tabCategorias.newTab (). setText ("Tabaaaaaa 1"));
        tabCategorias.addTab (tabCategorias.newTab (). setText ("Tabaaaaa 2"));
        tabCategorias.addTab (tabCategorias.newTab (). setText ("Tabaaaaa 3"));
        tabCategorias.addTab (tabCategorias.newTab (). setText ("Tabaaaaaa 4"));
       // tabCategorias.addTab (tabCategorias.newTab (). setText ("Tabaaaaa 5"));
       // tabCategorias.addTab (tabCategorias.newTab (). setText ("Tabaaaaa 6"));
        Log.e("el tip cat es>",""+tabCategorias.getTabMode());
        int mode=1;
        float myTabLayoutSize = 360;
        int tamTab=tabCategorias.getWidth();
        Log.e("el ancho del tab es> ", ""+tamTab);
        if (width >= myTabLayoutSize ){
            tabCategorias.setTabMode(TabLayout.MODE_FIXED);
            tabCategorias.setTabGravity(TabLayout.GRAVITY_FILL);
            Log.e("modo", "fijo");
        } else {
            tabCategorias.setTabMode(TabLayout.MODE_SCROLLABLE);
            Log.e("modo", "scorlable");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        vista= inflater.inflate(R.layout.fragment_categorias, container, false);
        return  vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UI();
    }
}
