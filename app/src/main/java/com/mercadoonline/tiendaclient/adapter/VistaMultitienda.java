package com.mercadoonline.tiendaclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mercadoonline.tiendaclient.R;
import com.mercadoonline.tiendaclient.models.recibido.ResponseTiendas;
import com.mercadoonline.tiendaclient.utils.Global;
import com.mercadoonline.tiendaclient.view.fragments.productos;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VistaMultitienda extends RecyclerView.Adapter<VistaMultitienda.Holder>  implements Filterable {

    List<ResponseTiendas> lst_normal;
    List<ResponseTiendas> list_full;
    FragmentManager fragmentManager;
    Context context;
    int manejador=0;

    public VistaMultitienda(List<ResponseTiendas> lst_normal, FragmentManager fragmentManager,Context context) {
     /*   List<ResponseTiendas> filtro= new ArrayList<>();
        for(ResponseTiendas m :lst_normal){
            if(m.getPuestos().size()>0){
                filtro.add(m);
            }
        }
        this.lst_normal = filtro;
        list_full=new ArrayList<>(lst_normal);
*/
this.context=context;
        this.lst_normal = lst_normal;
        list_full=new ArrayList<>(lst_normal);
        this.fragmentManager = fragmentManager;
    }

    public VistaMultitienda(List<ResponseTiendas> lst_normal) {
        this.lst_normal = lst_normal;

        list_full=new ArrayList<>(lst_normal);
    }
    public VistaMultitienda(List<ResponseTiendas> lst_normal, int manejador) {
        this.lst_normal = lst_normal;

        list_full=new ArrayList<>(lst_normal);
        this.manejador=manejador;
    }

    @NonNull
    @Override
    public VistaMultitienda.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tiendas,
                parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VistaMultitienda.Holder holder, final int position) {
        String EstadoTienda="";
        if(Global.Modo==3){
            holder.estadoTienda.setVisibility(View.VISIBLE);
            if(lst_normal.get(position).getEstado()==0)  {
                EstadoTienda="E";
                holder.estadoTienda.setText(EstadoTienda);
                holder.estadoTienda.setBackground(context.getResources().getDrawable(R.color.col_rojo));
                //holder.RellenoStatus.setBackground(context.getResources().getDrawable(R.drawable.border_estatus_purpura));
            }
            if(lst_normal.get(position).getEstado()==1)  {
                EstadoTienda="A";
                holder.estadoTienda.setText(EstadoTienda);
                holder.estadoTienda.setBackground(context.getResources().getDrawable(R.color.col_verde_correa));
            }
            if(lst_normal.get(position).getEstado()==2)  {
                EstadoTienda="I";
                holder.estadoTienda.setText(EstadoTienda);
                holder.estadoTienda.setBackground(context.getResources().getDrawable(R.color.col_rojo));
            }
            holder.ContenedorCreditos.setVisibility(View.VISIBLE);
            holder.creditos_en_tienda.setText(""+lst_normal.get(position).getCreditosTotales().toUpperCase());

        }


        //Tienda currentItem = lst_normal.get(position);
        holder.tienda_nombre.setText(""+lst_normal.get(position).getNombre().toUpperCase());
        holder.tienda_direccion.setText(""+lst_normal.get(position).getDireccion().toUpperCase());


        String url= Global.UrlImagen+lst_normal.get(position).getUrlImagen();
        Glide
                .with(holder.tienda_portada.getContext())
                .load(url)
               // .diskCacheStrategy(DiskCacheStrategy.ALL)
                .diskCacheStrategy(DiskCacheStrategy.NONE )
                .centerCrop()
                .placeholder(R.drawable.placeholder_mercado)
                .error(R.drawable.placeholder_mercado)
                .into(holder.tienda_portada);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productos pro= new productos();
                pro.tienda=lst_normal.get(position);
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
        return mercados_filter;
    }

    public class Holder extends RecyclerView.ViewHolder {

        RoundedImageView tienda_portada;
        CircleImageView tienda_perfil;
        TextView tienda_nombre,tienda_direccion, estadoTienda, creditos_en_tienda;
        LinearLayout ContenedorCreditos;


        public Holder(@NonNull View itemView) {
            super(itemView);
            tienda_portada =itemView.findViewById(R.id.mercado_portada);
            tienda_perfil =itemView.findViewById(R.id.mercado_perfil);
            tienda_nombre =itemView.findViewById(R.id.mercado_nombre);
            tienda_direccion=itemView.findViewById(R.id.mercado_direccion);
            creditos_en_tienda=itemView.findViewById(R.id.TVValorCredito);
            estadoTienda=itemView.findViewById(R.id.TVEstadoTienda);
            ContenedorCreditos=itemView.findViewById(R.id.ContenedorCreditos);


        }
    }


    private Filter mercados_filter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            //("adapter","filtro llegar" +constraint);
            List<ResponseTiendas> filtro=new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                //("adapter","filtro sin cambios");

                filtro.addAll(list_full);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                //("adapter","probar-->" + filterPattern);
                //("adapter","tamaño lista -->" + list_full.size());

                for (ResponseTiendas item : list_full) {
                    //("adapter","recorro" + item.getNombre());


                    if (item.getNombre().toLowerCase().contains(filterPattern) ) {
                        //("adapter","filtro" +item.toString());
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
            //("adapter","cambio");
            notifyDataSetChanged();
        }
    };


}
