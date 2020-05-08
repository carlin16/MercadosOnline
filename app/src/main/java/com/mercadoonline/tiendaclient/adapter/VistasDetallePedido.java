package com.mercadoonline.tiendaclient.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mercadoonline.tiendaclient.R;
import com.mercadoonline.tiendaclient.models.compra.CompraProductos;
import com.mercadoonline.tiendaclient.models.recibido.DetallesP;

import java.util.ArrayList;
import java.util.List;

public class VistasDetallePedido extends RecyclerView.Adapter<VistasDetallePedido.Holder>  {

    List<DetallesP> lst_normal;
    List<DetallesP> list_full;
    FragmentManager fragmentManager;
    String id_del_fragment;




    public VistasDetallePedido(List<DetallesP> lst_normal, FragmentManager fragmentManager, String id_del_fragment) {




        this.lst_normal = lst_normal;
        this.fragmentManager = fragmentManager;
        this.id_del_fragment = id_del_fragment;
    }

    int manejador=0;

    public VistasDetallePedido(List<DetallesP> lst_normal, FragmentManager fragmentManager) {
        this.lst_normal = lst_normal;
        list_full=new ArrayList<>(lst_normal);
        this.fragmentManager = fragmentManager;
    }

    public VistasDetallePedido(List<DetallesP> lst_normal) {
        this.lst_normal = lst_normal;

        list_full=new ArrayList<>(lst_normal);
    }
    public VistasDetallePedido(List<DetallesP> lst_normal, int manejador) {
        this.lst_normal = lst_normal;

        list_full=new ArrayList<>(lst_normal);
        this.manejador=manejador;
    }

    @NonNull
    @Override
    public VistasDetallePedido.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalle_pedidos,
                parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VistasDetallePedido.Holder holder, final int position) {
       holder.Nombreproduct.setText(lst_normal.get(position).getNombreProducto());
       holder.UnidadMedida.setText(""+lst_normal.get(position).getUnidades());
       holder.CantidadProductos.setText(""+lst_normal.get(position).getCantidad());
       holder.SubtotalProduct.setText("$"+lst_normal.get(position).getSubtotal());

    }

    @Override
    public int getItemCount() {
        return lst_normal.size();
    }



    public class Holder extends RecyclerView.ViewHolder {

        TextView Nombreproduct,UnidadMedida, CantidadProductos,SubtotalProduct;
        RelativeLayout Contenedor_EliminarDetallePro;

        public Holder(@NonNull View itemView) {
            super(itemView);
            Nombreproduct=itemView.findViewById(R.id.DPNombrePro);
            UnidadMedida=itemView.findViewById(R.id.DPUnidadMedida);
            CantidadProductos=itemView.findViewById(R.id.DPCantidadPro);
            SubtotalProduct=itemView.findViewById(R.id.DPSubtotalProduc);



        }
    }





    public  interface OnItemLongClicListener{
        void onItemClickLong(CompraProductos product, int position);
    }


}
