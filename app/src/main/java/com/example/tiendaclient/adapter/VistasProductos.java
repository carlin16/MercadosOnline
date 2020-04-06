package com.example.tiendaclient.adapter;

import android.annotation.SuppressLint;
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
import com.example.tiendaclient.models.recibido.Producto;
import com.example.tiendaclient.models.recibido.ResponseVerAllPuesto;
import com.example.tiendaclient.utils.Global;
import com.example.tiendaclient.view.fragments.productos;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class VistasProductos extends RecyclerView.Adapter<VistasProductos.MultiHolder>  implements Filterable {

    private OnItemClicListener itemClicListener;



    List<Producto> lst_normal;
    List<Producto> lst_full;

    Context contex;
    FragmentManager fragmentManager;

    public VistasProductos(List<Producto> lst_normal, OnItemClicListener itemClicListener) {
        this.lst_normal = lst_normal;
        this.itemClicListener = itemClicListener;
        lst_full=new ArrayList<>(lst_normal);

    }


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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MultiHolder holder, final int position) {
        Log.e("vista produ", "estoy viendo uno");
       holder.nombre.setText(lst_normal.get(position).getNombre());
        DecimalFormat f = new DecimalFormat("##.00");
        if(Double.parseDouble(lst_normal.get(position).getPrecio())<1.0){
            holder.precio.setText("$0"+f.format(Double.parseDouble(lst_normal.get(position).getPrecio())));
            Log.e("Presentacion produ", "estoy aqui en < 0");
        }else{
            holder.precio.setText("$"+f.format(Double.parseDouble(lst_normal.get(position).getPrecio())));
        }
        //holder.precio.setText("$"+f.format(Double.parseDouble(lst_normal.get(position).getPrecio())));
       // holder.precio.setText("$"+lst_normal.get(position).getPrecio());
        holder.Unidades.setText(""+lst_normal.get(position).getUnidades());
    //  aqui guia te y setea acvtualiza el response

        String url=Global.Url+"productos/"+lst_normal.get(position).getId()+"/foto";
       Glide
                .with(holder.imagen.getContext())
                .load(url)
                .placeholder(R.drawable.ic_place_productos)
               .diskCacheStrategy(DiskCacheStrategy.NONE )
               .error(R.drawable.ic_place_productos)
               . skipMemoryCache(true)
                .into(holder.imagen);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       // //("click","Tienda");
        itemClicListener.onItemClick(lst_normal.get(position),position);



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
        ImageView imagen;
        TextView nombre, precio,Unidades;
        public MultiHolder(@NonNull View itemView) {

            super(itemView);
          //  if(Global.Modo==1){
                imagen=itemView.findViewById(R.id.TVPuestoFotoV);
                nombre=itemView.findViewById(R.id.TVProducNombre);
                precio=itemView.findViewById(R.id.TVProducValor);
             Unidades=itemView.findViewById(R.id.TVProducUnidades);

            // }


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
                    if (item.getNombre().toLowerCase().contains(filterPattern) ) {
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

    public  interface OnItemClicListener{

        void onItemClick(Producto product, int position);
    }



    }
