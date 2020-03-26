package com.example.tiendaclient.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tiendaclient.R;
import com.example.tiendaclient.adapter.VistasProductos;
import com.example.tiendaclient.adapter.VistasPuestos;
import com.example.tiendaclient.models.recibido.Producto;
import com.example.tiendaclient.models.recibido.ResponseVerAllPuesto;
import com.example.tiendaclient.models.recibido.Vendedor;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class productos extends Fragment {


    public List<Producto> ls_listado= new ArrayList<>();
    public Vendedor vendedor= new Vendedor();


    public productos() {
        // Required empty public constructor
    }

    View vista;
    RecyclerView recyclerView;
    VistasProductos adapter;
    TextView Idpuesto, NombreDueno, DescripcionPuesto, Cantidadpro;
    public String idPuesto;
    public  String categorias;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView= vista.findViewById(R.id.Recycler_productos);
        Idpuesto= vista.findViewById(R.id.TVProPuesto);
        Idpuesto.setText(idPuesto);

        NombreDueno= vista.findViewById(R.id.TVPuestoNombre);
        DescripcionPuesto= vista.findViewById(R.id.TVPuestoDescripcion);
        Cantidadpro= vista.findViewById(R.id.TVProCantidad);

        NombreDueno.setText(vendedor.getNombres()+" "+vendedor.getApellidos());
        DescripcionPuesto.setText(categorias);
        Cantidadpro.setText(""+(ls_listado.size()));


        iniciar_recycler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista =inflater.inflate(R.layout.fragment_productos, container, false);
        return vista;
    }

    private void  iniciar_recycler(){
        adapter=new VistasProductos(ls_listado);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    private void UI(){



    }



}
