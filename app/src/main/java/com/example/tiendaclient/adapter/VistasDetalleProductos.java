package com.example.tiendaclient.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendaclient.R;
import com.example.tiendaclient.models.compra.CompraProductos;
import com.example.tiendaclient.models.compra.PuestosCompra;
import com.example.tiendaclient.utils.Global;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VistasDetalleProductos extends RecyclerView.Adapter<VistasDetalleProductos.Holder>  implements Filterable {

    List<CompraProductos> lst_normal;
    List<CompraProductos> list_full;
    FragmentManager fragmentManager;
    String id_del_fragment;
    private OnItemLongClicListener itemLongClicListener;
    private OnItemAdd itemAdd;
    private OnItemDelete itemDelete;

    public VistasDetalleProductos(List<CompraProductos> lst_normal, FragmentManager fragmentManager, String id_del_fragment, OnItemLongClicListener itemLongClicListener, OnItemAdd itemAdd, OnItemDelete itemDelete) {
        this.lst_normal = lst_normal;
        this.fragmentManager = fragmentManager;
        this.id_del_fragment = id_del_fragment;
        this.itemLongClicListener = itemLongClicListener;
        this.itemAdd = itemAdd;
        this.itemDelete = itemDelete;
    }

    public VistasDetalleProductos(List<CompraProductos> lst_normal, FragmentManager fragmentManager, String id_del_fragment, OnItemLongClicListener itemLongClicListener) {
        this.lst_normal = lst_normal;
        this.fragmentManager = fragmentManager;
        this.id_del_fragment = id_del_fragment;
        this.itemLongClicListener = itemLongClicListener;
    }

    public VistasDetalleProductos(List<CompraProductos> lst_normal, FragmentManager fragmentManager, String id_del_fragment) {
        this.lst_normal = lst_normal;
        this.fragmentManager = fragmentManager;
        this.id_del_fragment = id_del_fragment;
    }

    int manejador=0;

    public VistasDetalleProductos(List<CompraProductos> lst_normal, FragmentManager fragmentManager) {
        this.lst_normal = lst_normal;
        list_full=new ArrayList<>(lst_normal);
        this.fragmentManager = fragmentManager;
    }

    public VistasDetalleProductos(List<CompraProductos> lst_normal) {
        this.lst_normal = lst_normal;

        list_full=new ArrayList<>(lst_normal);
    }
    public VistasDetalleProductos(List<CompraProductos> lst_normal, int manejador) {
        this.lst_normal = lst_normal;

        list_full=new ArrayList<>(lst_normal);
        this.manejador=manejador;
    }

    @NonNull
    @Override
    public VistasDetalleProductos.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalle_productos,
                parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VistasDetalleProductos.Holder holder, final int position) {
       holder.Nombreproduct.setText(lst_normal.get(position).getNombre());
        String plb=" Producto";
        holder.UnidadMedida.setText(""+lst_normal.get(position).getUnidades());
        holder.CantidadProductos.setText(""+lst_normal.get(position).getId_cantidad());
        holder.SubtotalProduct.setText("$"+ Global.formatearDecimales(lst_normal.get(position).getTotal(),2));



        holder.eliminarDetalleProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ///click normal para eliminar el detalle
                itemLongClicListener.onItemClickLong(lst_normal.get(position),position);

            }
        });


       holder.agregarProducto.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               itemAdd.onItemClickLong(lst_normal.get(position),position);
           }
       });

        holder.quitarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemDelete.onItemClickLong(lst_normal.get(position),position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return lst_normal.size();
    }

    @Override
    public Filter getFilter() {
        return mercados_filter;
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


private Filter mercados_filter =new Filter() {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        Log.e("adapter","filtro llegar" +constraint);
        List<CompraProductos> filtro=new ArrayList<>();

        if (constraint == null || constraint.length() == 0) {
            Log.e("adapter","filtro sin cambios");

            filtro.addAll(list_full);
        } else {
            String filterPattern = constraint.toString().toLowerCase().trim();
            Log.e("adapter","probar-->" + filterPattern);
            Log.e("adapter","tamaño lista -->" + list_full.size());

            for (CompraProductos item : list_full) {
                Log.e("adapter","recorro" + item.getNombre());


                if (item.getNombre().toLowerCase().contains(filterPattern) ) {
                    Log.e("adapter","filtro" +item.toString());
                    filtro.add(item);
                }
            }
        }
        FilterResults results = new FilterResults();
        results.values = filtro;
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        lst_normal.clear();
        lst_normal.addAll((List) filterResults.values);
        Log.e("adapter","cambio");
        notifyDataSetChanged();
    }
};


    public  interface OnItemLongClicListener{
        void onItemClickLong(CompraProductos product, int position);
    }


    public  interface OnItemAdd{
        void onItemClickLong(CompraProductos product, int position);
    }


    public  interface OnItemDelete{
        void onItemClickLong(CompraProductos product, int position);
    }



}
