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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tiendaclient.R;
import com.example.tiendaclient.adapter.VistasCompraMercado;
import com.example.tiendaclient.models.compra.Compra;
import com.example.tiendaclient.utils.Global;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class carrito extends Fragment {
    View vista;
    RecyclerView recyclerView;
   VistasCompraMercado adapter;
   ImageView StockSeleccion;
   RelativeLayout RelativeVacio;
    EditText buscar;
    public String id_del_fragment;

    public carrito() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //("volvi a crear","carrito");

        vista= inflater.inflate(R.layout.fragment_carrito, container, false);
        return vista;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=vista.findViewById(R.id.Recycler_CompaMercados);
        StockSeleccion=vista.findViewById(R.id.StockSeleccion);
        RelativeVacio=vista.findViewById(R.id.RelativeVacio);


        if(Global.VerCompras.size()<1){
            RelativeVacio.setVisibility(View.VISIBLE);
          //  Toast.makeText(getActivity(),"Ningun producto Agregado a su Carrito",Toast.LENGTH_LONG ).show();

          ///  getFragmentManager().popBackStack();
        }else{
            RelativeVacio.setVisibility(View.GONE);
            iniciar_recycler();
        }
        //("volvi a crear vista","carrito");

    }

    private void  iniciar_recycler(){
        adapter= new VistasCompraMercado(Global.VerCompras,getFragmentManager(),id_del_fragment);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }



}
