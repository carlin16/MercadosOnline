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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tiendaclient.R;
import com.example.tiendaclient.models.recibido.Puesto;
import com.example.tiendaclient.models.recibido.ResponseVerAllPuesto;
import com.example.tiendaclient.models.recibido.ResponseVerMercado;
import com.example.tiendaclient.view.fragments.productos;
import com.google.android.gms.common.api.Response;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class VistasPuestos extends RecyclerView.Adapter<VistasPuestos.MultiHolder>  implements Filterable {



    List<ResponseVerAllPuesto> lst_normal;
    List<ResponseVerAllPuesto> lst_full;
    ResponseVerMercado Mercado=new ResponseVerMercado();
    Context contex;
    FragmentManager fragmentManager;

    public VistasPuestos(List<ResponseVerAllPuesto> lst_normal) {
        this.lst_normal = lst_normal;
        lst_full=new ArrayList<>(lst_normal);

    }

    public VistasPuestos(List<ResponseVerAllPuesto> lst_normal, FragmentManager fragmentManager, ResponseVerMercado Mercado) {
        this.lst_normal = lst_normal;
        this.fragmentManager = fragmentManager;
        this.Mercado=Mercado;
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
    public void onBindViewHolder(@NonNull MultiHolder holder, final int position) {
        holder.nombre.setText(lst_normal.get(position).getVendedor().getNombres()+" "+lst_normal.get(position).getVendedor().getApellidos());
        holder.CodigoPuesto.setText(lst_normal.get(position).getCodigo());
        holder.DescripcionPuesto.setText(lst_normal.get(position).getMaxCategorias());
    //  aqui guia te y setea acvtualiza el response

        Glide
                .with(holder.imagen.getContext())
                .load("https://images.squarespace-cdn.com/content/v1/5def38213ab4a76a02e72dac/1577824254353-Y9FOHXRD3ZAHDYIQ2Z1E/ke17ZwdGBToddI8pDm48kNiEM88mrzHRsd1mQ3bxVct7gQa3H78H3Y0txjaiv_0fDoOvxcdMmMKkDsyUqMSsMWxHk725yiiHCCLfrh8O1z4YTzHvnKhyp6Da-NYroOW3ZGjoBKy3azqku80C789l0s0XaMNjCqAzRibjnE_wBlkZ2axuMlPfqFLWy-3Tjp4nKScCHg1XF4aLsQJlo6oYbA/1x1_Foto-perfil-LinkedIn.jpg")
                // .override(60,60)

                .placeholder(R.drawable.perfil_mercado)
                .into(holder.imagen);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Log.e("click","Tienda");
        productos pro= new productos();
        pro.ls_listado=lst_normal.get(position).getProductos();
        pro.vendedor=lst_normal.get(position).getVendedor();
        pro.idPuesto=""+lst_normal.get(position).getCodigo();
        pro.categorias=lst_normal.get(position).getMaxCategorias();
        pro.Mercado=Mercado;



        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Contenedor_Fragments, pro).addToBackStack(null);
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
        return puestos_filter;
    }

    public class MultiHolder extends RecyclerView.ViewHolder {
// instancia la scosas que faltan aqui creas lo que hay en la vista
        RoundedImageView imagen;
        TextView nombre, CodigoPuesto, DescripcionPuesto;
        public MultiHolder(@NonNull View itemView) {
            super(itemView);
            imagen=itemView.findViewById(R.id.TVPuestoFotoV);
            nombre=itemView.findViewById(R.id.TVPuestoNombre);
            CodigoPuesto=itemView.findViewById(R.id.TVPuestoCodP);
            DescripcionPuesto=itemView.findViewById(R.id.TVPuestoDescripcion);

            ///aqui instancias

        }
    }


    private Filter puestos_filter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ResponseVerAllPuesto> filtro=new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtro.addAll(lst_full);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ResponseVerAllPuesto item : lst_full) {
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
