package com.example.tiendaclient.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tiendaclient.R;
import com.example.tiendaclient.adapter.VistasPuestos;
import com.example.tiendaclient.models.recibido.Puesto;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class puestos extends Fragment {


    public List<Puesto> ls_listado= new ArrayList<>();
    public int id=0;

    public puestos() {
        // Required empty public constructor
    }

    View vista;
    RecyclerView recyclerView;
    VistasPuestos adapter;

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
        recyclerView= vista.findViewById(R.id.Recycler_puestos);
        iniciar_recycler();

    }


    private void iniciar_recycler(){
        adapter=new VistasPuestos(ls_listado);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }


}
