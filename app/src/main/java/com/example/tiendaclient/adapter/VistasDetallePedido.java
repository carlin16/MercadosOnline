package com.example.tiendaclient.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendaclient.R;
import com.example.tiendaclient.models.compra.CompraProductos;
import com.example.tiendaclient.models.recibido.DetallesP;
import com.example.tiendaclient.utils.Global;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalle_productos,
                parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VistasDetallePedido.Holder holder, final int position) {
       holder.Nombreproduct.setText(lst_normal.get(position).getNombreProducto());
        String plb=" Producto";
        holder.UnidadMedida.setText(plb);
        holder.CantidadProductos.setText(""+lst_normal.get(position).getCantidad());
        holder.SubtotalProduct.setText("$"+lst_normal.get(position).getSubtotal());

        holder.Contenedor_EliminarDetallePro.setVisibility(View.GONE);
        holder.agregarProducto.setVisibility(View.GONE);
        holder.quitarProducto.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return lst_normal.size();
    }



    public class Holder extends RecyclerView.ViewHolder {

        TextView Nombreproduct,UnidadMedida, CantidadProductos,SubtotalProduct;
        CircleImageView eliminarDetalleProducto,quitarProducto,agregarProducto;
        RelativeLayout Contenedor_EliminarDetallePro;

        public Holder(@NonNull View itemView) {
            super(itemView);
            Nombreproduct=itemView.findViewById(R.id.DPNombrePro);
            UnidadMedida=itemView.findViewById(R.id.DPUnidadMedida);
            CantidadProductos=itemView.findViewById(R.id.DPCantidadPro);
            SubtotalProduct=itemView.findViewById(R.id.DPSubtotalProduc);

            eliminarDetalleProducto=itemView.findViewById(R.id.eliminarDetalleProducto);
            Contenedor_EliminarDetallePro=itemView.findViewById(R.id.Contenedor_EliminarDetallePro);

            agregarProducto=itemView.findViewById(R.id.agregarProducto);
            quitarProducto=itemView.findViewById(R.id.quitarProducto);


        }
    }





    public  interface OnItemLongClicListener{
        void onItemClickLong(CompraProductos product, int position);
    }


}
