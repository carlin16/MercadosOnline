package com.example.tiendaclient.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiendaclient.R;
import com.example.tiendaclient.models.compra.Compra;
import com.example.tiendaclient.view.RegistroUsuarios;

/**
 * A simple {@link Fragment} subclass.
 */
public class detalle extends Fragment {
    View vista;
    TextView DetaCancelarPeido,DetaSubtotal, DetaCostoEnvio,DetaTotal,DetaTotal2;
    RelativeLayout DetaContinuar;

    public Compra CompraNueva=new Compra();

    Double CostoEnvi=2.00;

    public int PosicionListaArray=0;
    public detalle() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_detalle, container, false);
        return  vista;



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UI();
        llenar_Detalle();
        Click();


    }

    private void UI(){

        DetaCancelarPeido=vista.findViewById(R.id.DetalleCancelarPedido);
        DetaSubtotal=vista.findViewById(R.id.DetalleSubtotal);
        DetaCostoEnvio=vista.findViewById(R.id.DetalleCostoEnvio);
        DetaTotal=vista.findViewById(R.id.DetalleTotal);
        DetaTotal2=vista.findViewById(R.id.DetalleTotal2);
        DetaContinuar=vista.findViewById(R.id.DetalleBtnContinuar);

    }
    private  void llenar_Detalle(){
        DetaSubtotal.setText("$"+CompraNueva.getTotal().toString());
        DetaCostoEnvio.setText("$"+CostoEnvi);
        DetaTotal.setText("$"+(CompraNueva.getTotal()+CostoEnvi));
        DetaTotal2.setText("$"+(CompraNueva.getTotal()+CostoEnvi));

    }

    private  void Click(){
        DetaCancelarPeido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getActivity(), "Click en cancelar pedido", Toast.LENGTH_SHORT).show();

            }
        });

        DetaContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Contenedor_Fragments, new ubica_entrega()).addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

    }





}
