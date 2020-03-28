package com.example.tiendaclient.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendaclient.R;
import com.example.tiendaclient.models.compra.ProductosCompra;
import com.example.tiendaclient.view.fragments.detalle;

import java.util.ArrayList;
import java.util.List;

public class VistasDetalleProductos extends RecyclerView.Adapter<VistasDetalleProductos.Holder>  implements Filterable {

    List<ProductosCompra> lst_normal;
    List<ProductosCompra> list_full;
    FragmentManager fragmentManager;


int manejador=0;

    public VistasDetalleProductos(List<ProductosCompra> lst_normal, FragmentManager fragmentManager) {
        this.lst_normal = lst_normal;
        list_full=new ArrayList<>(lst_normal);
        this.fragmentManager = fragmentManager;
    }

    public VistasDetalleProductos(List<ProductosCompra> lst_normal) {
        this.lst_normal = lst_normal;

        list_full=new ArrayList<>(lst_normal);
    }
    public VistasDetalleProductos(List<ProductosCompra> lst_normal, int manejador) {
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
      /*  holder.nombre.setText(lst_normal.get(position).getNombre());
        String plb=" Producto";
        if(lst_normal.get(position).getCantidad()>1){
            plb=" Productos";
        }
        holder.cantidad.setText(""+lst_normal.get(position).getCantidad()+plb);
        holder.total.setText("$"+lst_normal.get(position).getTotal());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detalle deta= new detalle();
               // deta.CompraNueva=lst_normal.get(position);
               deta.PosicionListaArray=position;
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Contenedor_Fragments, deta).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
*/
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

        TextView nombre,cantidad,total;


        public Holder(@NonNull View itemView) {
            super(itemView);
            nombre=itemView.findViewById(R.id.Compra_Mercado_Nombre);
            cantidad=itemView.findViewById(R.id.Compra_Mercado_Cantidad);
            total=itemView.findViewById(R.id.Compra_Mercado_Total);
        }
    }


private Filter mercados_filter =new Filter() {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        Log.e("adapter","filtro llegar" +constraint);
        List<ProductosCompra> filtro=new ArrayList<>();

        if (constraint == null || constraint.length() == 0) {
            Log.e("adapter","filtro sin cambios");

            filtro.addAll(list_full);
        } else {
            String filterPattern = constraint.toString().toLowerCase().trim();
            Log.e("adapter","probar-->" + filterPattern);
            Log.e("adapter","tamaño lista -->" + list_full.size());

            for (ProductosCompra item : list_full) {
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


}
