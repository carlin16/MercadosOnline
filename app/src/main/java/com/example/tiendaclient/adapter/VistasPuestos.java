package com.example.tiendaclient.adapter;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tiendaclient.R;
import com.example.tiendaclient.models.recibido.Puesto;

import java.util.ArrayList;
import java.util.List;

public class VistasPuestos extends RecyclerView.Adapter<VistasPuestos.MultiHolder>  implements Filterable {



    List<Puesto> lst_normal;
    List<Puesto> lst_full;

    Context contex;
    FragmentManager fragmentManager;

    public VistasPuestos(List<Puesto> lst_normal) {
        this.lst_normal = lst_normal;
        lst_full=new ArrayList<>(lst_normal);

    }

    public VistasPuestos(List<Puesto> lst_normal, FragmentManager fragmentManager) {
        this.lst_normal = lst_normal;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public MultiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_puestos,parent,false);


        //RecyclerView.LayoutParams layoutParams= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // view.setLayoutParams(layoutParams);
        MultiHolder th= new MultiHolder(view);
        return th;    }

    @Override
    public void onBindViewHolder(@NonNull MultiHolder holder, int position) {
        holder.nombre.setText(""+ lst_normal.get(position).getIdVendedor());
        Glide
                .with(holder.imagen.getContext())
                .load("https://www.eltelegrafo.com.ec/media/k2/items/cache/a5bd5a3316b1f3f06df4936ac9164372_XL.jpg")
                // .override(60,60)

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

        ImageView imagen;
        TextView nombre;
        public MultiHolder(@NonNull View itemView) {
            super(itemView);
            imagen=itemView.findViewById(R.id.multi_foto);
            nombre=itemView.findViewById(R.id.multi_nombre);
        }
    }


    private Filter puestos_filter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Puesto> filtro=new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtro.addAll(lst_full);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Puesto item : lst_full) {
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
