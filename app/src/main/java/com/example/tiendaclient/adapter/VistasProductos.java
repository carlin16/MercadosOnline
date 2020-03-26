package com.example.tiendaclient.adapter;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.example.tiendaclient.R;
import com.example.tiendaclient.models.recibido.Producto;
import com.example.tiendaclient.models.recibido.ResponseVerAllPuesto;
import com.example.tiendaclient.view.fragments.productos;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class VistasProductos extends RecyclerView.Adapter<VistasProductos.MultiHolder>  implements Filterable {



    List<Producto> lst_normal;
    List<Producto> lst_full;

    Context contex;
    FragmentManager fragmentManager;

    public VistasProductos(List<Producto> lst_normal) {
        this.lst_normal = lst_normal;
        lst_full=new ArrayList<>(lst_normal);

    }

    public VistasProductos(List<Producto> lst_normal, FragmentManager fragmentManager) {
        this.lst_normal = lst_normal;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public MultiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productos,parent,false);


        //RecyclerView.LayoutParams layoutParams= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // view.setLayoutParams(layoutParams);
        MultiHolder th= new MultiHolder(view);
        return th;    }

    @Override
    public void onBindViewHolder(@NonNull MultiHolder holder, final int position) {
       holder.nombre.setText(lst_normal.get(position).getNombre());
        holder.precio.setText("$"+lst_normal.get(position).getPrecio());
    //  aqui guia te y setea acvtualiza el response

        Glide
                .with(holder.imagen.getContext())
                .load("http://mercados-online.com/public/api/productos/"+lst_normal.get(position).getId()+"/foto")
                .placeholder(R.drawable.perfil_mercado)
                .into(holder.imagen);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Log.e("click","Tienda");




    }
});

    }

    @Override
    public int getItemCount() {
        return lst_normal.size();
    }

    @Override
    public Filter getFilter() {
        return puestos_filter;
    }

    public class MultiHolder extends RecyclerView.ViewHolder {
// instancia la scosas que faltan aqui creas lo que hay en la vista
        RoundedImageView imagen;
        TextView nombre, precio;
        public MultiHolder(@NonNull View itemView) {
            super(itemView);
            imagen=itemView.findViewById(R.id.TVPuestoFotoV);
            nombre=itemView.findViewById(R.id.TVProducNombre);
            precio=itemView.findViewById(R.id.TVProducValor);

            ///aqui instancias

        }
    }


    private Filter puestos_filter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Producto> filtro=new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtro.addAll(lst_full);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Producto item : lst_full) {
                    if (item.getFechaRegistro().toLowerCase().contains(filterPattern) ) {
                        filtro.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtro;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            lst_normal.clear();
            lst_normal.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };





    }
