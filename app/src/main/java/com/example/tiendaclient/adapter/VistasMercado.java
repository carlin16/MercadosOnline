package com.example.tiendaclient.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tiendaclient.R;
import com.example.tiendaclient.models.recibido.ResponseVerMercado;
import com.example.tiendaclient.utils.Global;
import com.example.tiendaclient.view.fragments.puestos;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VistasMercado extends RecyclerView.Adapter<VistasMercado.Holder>  implements Filterable {

    List<ResponseVerMercado> lst_normal;
    List<ResponseVerMercado> list_full;
    FragmentManager fragmentManager;

int manejador=0;

    public VistasMercado(List<ResponseVerMercado> lst_normal, FragmentManager fragmentManager) {
        this.lst_normal = lst_normal;
        list_full=new ArrayList<>(lst_normal);
        this.fragmentManager = fragmentManager;
    }

    public VistasMercado(List<ResponseVerMercado> lst_normal) {
        this.lst_normal = lst_normal;

        list_full=new ArrayList<>(lst_normal);
    }
    public VistasMercado(List<ResponseVerMercado> lst_normal, int manejador) {
        this.lst_normal = lst_normal;

        list_full=new ArrayList<>(lst_normal);
        this.manejador=manejador;
    }

    @NonNull
    @Override
    public VistasMercado.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mercados,
                parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VistasMercado.Holder holder, final int position) {

        //Tienda currentItem = lst_normal.get(position);
        holder.mercado_nombre.setText(lst_normal.get(position).getNombre());
        holder.mercado_descripcion.setText(lst_normal.get(position).getDescripcion());
        holder.mercado_direccion.setText(lst_normal.get(position).getDireccion());

        String url= Global.Url+"mercados/"+lst_normal.get(position).getId()+"/foto";
        Glide
                .with(holder.mercado_portada.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.adduser2)
                .into(holder.mercado_portada);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                puestos pue= new puestos();

                pue.Mercado=lst_normal.get(position);


                FragmentTransaction fragmentTransaction;
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Contenedor_Fragments, pue).addToBackStack(null);
                fragmentTransaction.commit();
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

        ImageView mercado_portada;
        CircleImageView mercado_perfil;
        TextView mercado_nombre,mercado_ciudad,mercado_direccion,mercado_descripcion;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mercado_portada=itemView.findViewById(R.id.mercado_portada);
            mercado_perfil=itemView.findViewById(R.id.mercado_perfil);
            mercado_nombre=itemView.findViewById(R.id.mercado_nombre);
            mercado_ciudad=itemView.findViewById(R.id.mercado_ciudad);
            mercado_direccion=itemView.findViewById(R.id.mercado_direccion);
            mercado_descripcion=itemView.findViewById(R.id.mercado_descripcion);


        }
    }


private Filter mercados_filter =new Filter() {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        Log.e("adapter","filtro llegar" +constraint);
        List<ResponseVerMercado> filtro=new ArrayList<>();

        if (constraint == null || constraint.length() == 0) {
            Log.e("adapter","filtro sin cambios");

            filtro.addAll(list_full);
        } else {
            String filterPattern = constraint.toString().toLowerCase().trim();
            Log.e("adapter","probar-->" + filterPattern);
            Log.e("adapter","tamaño lista -->" + list_full.size());

            for (ResponseVerMercado item : list_full) {
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
