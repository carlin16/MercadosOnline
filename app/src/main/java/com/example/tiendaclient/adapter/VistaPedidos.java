package com.example.tiendaclient.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendaclient.R;
import com.example.tiendaclient.models.recibido.ResponseVerPedido;
import com.example.tiendaclient.utils.Global;
import com.example.tiendaclient.view.fragments.detalle;

import java.util.ArrayList;
import java.util.List;

public class VistaPedidos extends RecyclerView.Adapter<VistaPedidos.Holder>   {

    List<ResponseVerPedido> lst_normal;
    List<ResponseVerPedido> list_full;
    FragmentManager fragmentManager;
    Context context;
    String id_del_fragment;

    public VistaPedidos(List<ResponseVerPedido> lst_normal, FragmentManager fragmentManager, Context context) {
        this.lst_normal = lst_normal;
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    int manejador=0;

    public VistaPedidos(List<ResponseVerPedido> lst_normal, FragmentManager fragmentManager) {
        this.lst_normal = lst_normal;
        list_full=new ArrayList<>(lst_normal);
        this.fragmentManager = fragmentManager;
    }

    public VistaPedidos(List<ResponseVerPedido> lst_normal, FragmentManager fragmentManager, String id_del_fragment) {
        this.lst_normal = lst_normal;
        this.fragmentManager = fragmentManager;
        this.id_del_fragment = id_del_fragment;
    }

    public VistaPedidos(List<ResponseVerPedido> lst_normal) {
        this.lst_normal = lst_normal;

        list_full=new ArrayList<>(lst_normal);
    }
    public VistaPedidos(List<ResponseVerPedido> lst_normal, int manejador) {
        this.lst_normal = lst_normal;

        list_full=new ArrayList<>(lst_normal);
        this.manejador=manejador;
    }

    @NonNull
    @Override
    public VistaPedidos.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mispedidos,
                parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VistaPedidos.Holder holder, final int position) {
        //ENTREGADA //WAITING //IN_PROGRESS

        if(lst_normal.get(position).getEstado().equals("ENTREGADA")) holder.RellenoStatus.setBackground(context.getResources().getDrawable(R.drawable.border_estatus_purpura));
        if(lst_normal.get(position).getEstado().equals("WAITING")) holder.RellenoStatus.setBackground(context.getResources().getDrawable(R.drawable.border_estatus_naranja));
        if(lst_normal.get(position).getEstado().equals("IN_PROGRESS")) holder.RellenoStatus.setBackground(context.getResources().getDrawable(R.drawable.border_estatus_rojo));

       // holder.RellenoStatus.setBackgroundColor(R.drawable.border_estatus_purpura);
        holder.Status.setText(lst_normal.get(position).getEstado());
        holder.NumPedido.setText("Pedido # "+lst_normal.get(position).getId());
        holder.DirMercado.setText(lst_normal.get(position).getNombreMercado());
        holder.FechayHoraPedido.setText(lst_normal.get(position).getFechaRegistro());

        holder.TotalPedido.setText("$"+ Global.formatearDecimales(Double.parseDouble(lst_normal.get(position).getTotal()),2));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* detalle deta= new detalle();
               deta.CompraNueva=lst_normal.get(position);
                deta.PosicionListaArray=position;
                deta.id_del_fragment=id_del_fragment;
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Contenedor_Fragments, deta).addToBackStack("frag_deta");
                fragmentTransaction.commit();*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return lst_normal.size();
    }



    public class Holder extends RecyclerView.ViewHolder {

        TextView NumPedido,Status, DirMercado, FechayHoraPedido,TotalPedido;
        LinearLayout RellenoStatus;

        public Holder(@NonNull View itemView) {
            super(itemView);
            NumPedido=itemView.findViewById(R.id.PedidoNumero);
            Status=itemView.findViewById(R.id.PedidoTxtStatus);
            DirMercado=itemView.findViewById(R.id.PedidoDireccion);
            FechayHoraPedido=itemView.findViewById(R.id.PedidoFecha);
            TotalPedido=itemView.findViewById(R.id.PedidoTotal);
            RellenoStatus=itemView.findViewById(R.id.PedidoStatus);


        }
    }



}
