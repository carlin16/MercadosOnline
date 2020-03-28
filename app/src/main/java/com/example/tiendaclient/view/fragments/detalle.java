package com.example.tiendaclient.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiendaclient.R;
import com.example.tiendaclient.adapter.VistasDetalleProductos;
import com.example.tiendaclient.models.compra.Compra;
import com.example.tiendaclient.models.compra.ProductosCompra;
import com.example.tiendaclient.models.compra.PuestosCompra;
import com.example.tiendaclient.utils.Global;
import com.example.tiendaclient.view.RegistroUsuarios;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class detalle extends Fragment {
    View vista;
    TextView DetaCancelarPeido, DetaSubtotal, DetaCostoEnvio, DetaTotal, DetaTotal2;
    RelativeLayout DetaContinuar;
    List<ProductosCompra> LstPro = new ArrayList<>();
    public String id_del_fragment;

    RecyclerView recyclerView;
    VistasDetalleProductos adapter;


    public Compra CompraNueva = new Compra();

    Double CostoEnvi = 2.00;

    public int PosicionListaArray = 0;

    public detalle() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_detalle, container, false);
        return vista;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UI();
        unir();
        llenar_Detalle();
        iniciar_recycler();
        Click();


    }

    private void UI() {

        DetaCancelarPeido = vista.findViewById(R.id.DetalleCancelarPedido);
        DetaSubtotal = vista.findViewById(R.id.DetalleSubtotal);
        DetaCostoEnvio = vista.findViewById(R.id.DetalleCostoEnvio);
        DetaTotal = vista.findViewById(R.id.DetalleTotal);
        DetaTotal2 = vista.findViewById(R.id.DetalleTotal2);
        DetaContinuar = vista.findViewById(R.id.DetalleBtnContinuar);

    }

    private void llenar_Detalle() {
        DetaSubtotal.setText("$" + CompraNueva.getTotal().toString());
        DetaCostoEnvio.setText("$" + Global.formatearDecimales(CostoEnvi,2));
        DetaTotal.setText("$" + Global.formatearDecimales((CompraNueva.getTotal() + CostoEnvi),2));
        DetaTotal2.setText("$" + Global.formatearDecimales((CompraNueva.getTotal() + CostoEnvi),2));

    }

    private void Click() {
        DetaCancelarPeido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getActivity(), "Click en cancelar pedido", Toast.LENGTH_SHORT).show();

            }
        });

        DetaContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ubica_entrega ubi = new ubica_entrega();
                ubi.id_del_fragment=id_del_fragment;
                ubi.PosicionListaArray=PosicionListaArray;

                FragmentTransaction fragmentTransaction;
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Contenedor_Fragments, ubi).addToBackStack("frag_ubi");
                fragmentTransaction.commit();

            }
        });

    }


    private void unir() {


        for (PuestosCompra p : CompraNueva.getPuestos()) {
            LstPro.addAll(p.getProductos());

        }

    }

    @Override
    public void postponeEnterTransition() {
        super.postponeEnterTransition();
    }

    private void  iniciar_recycler(){
        recyclerView=vista.findViewById(R.id.Recycler_Detalles);
        adapter= new VistasDetalleProductos(LstPro,getFragmentManager(),id_del_fragment);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}