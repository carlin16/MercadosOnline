package com.mercadoonline.tiendaclient.adapter;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mercadoonline.tiendaclient.R;
import com.mercadoonline.tiendaclient.models.compra.PromosProductos;
import com.mercadoonline.tiendaclient.models.recibido.Productos;
import com.mercadoonline.tiendaclient.utils.Global;

import java.util.ArrayList;
import java.util.List;

public class VistasProductosPromos extends RecyclerView.Adapter<VistasProductosPromos.MultiHolder>  implements Filterable {

    private OnItemClicListener itemClicListener;



    List<PromosProductos> lst_normal;
    List<PromosProductos> lst_full;

    Context contex;
    FragmentManager fragmentManager;

    VistasProductos adapter;

    public VistasProductosPromos(List<PromosProductos> lst_normal, OnItemClicListener itemClicListener) {
        this.lst_normal = lst_normal;
        this.itemClicListener = itemClicListener;
        lst_full=new ArrayList<>(lst_normal);

    }


    public VistasProductosPromos(List<PromosProductos> lst_normal) {
        this.lst_normal = lst_normal;
        lst_full=new ArrayList<>(lst_normal);

    }

    public VistasProductosPromos(List<PromosProductos> lst_normal, FragmentManager fragmentManager, Context context, OnItemClicListener itemClicListener) {
        this.lst_normal = lst_normal;
        this.itemClicListener = itemClicListener;
        lst_full=new ArrayList<>(lst_normal);

    }

    @NonNull
    @Override
    public MultiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.promo_titulo,parent,false);


        //RecyclerView.LayoutParams layoutParams= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // view.setLayoutParams(layoutParams);
        MultiHolder th= new MultiHolder(view);
        return th;    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MultiHolder holder, final int position) {
        Log.e("vista pro promos", "estoy en Holder");
        holder.nombrePromoProduct.setText(lst_normal.get(position).getNombrePromo());

        if(lst_normal.get(position).getLst_products().size()<=0){


        }else {
            holder.recyclerProductPromo.setVisibility(View.VISIBLE);
        adapter=new VistasProductos(lst_normal.get(position).getLst_products(), new VistasProductos.OnItemClicListener() {
            @Override
            public void onItemClick(final Productos product, int position) {

                Log.e("el producto es",Global.convertObjToString(product));
                itemClicListener.onItemClick(product,position);
               // if(Global.Modo==1){comprar_productos(product);}
                //else if(Global.Modo==2 || Global.Modo==3){seleccionar_producto(product,position);}
            }
        });
       /* RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);*/
        //  RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        holder.recyclerProductPromo.setLayoutManager(new LinearLayoutManager(this.contex));
        holder.recyclerProductPromo.setHasFixedSize(true);
        holder.recyclerProductPromo.setAdapter(adapter);
        }
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

        TextView nombrePromoProduct;
        RecyclerView recyclerProductPromo;
        public MultiHolder(@NonNull View itemView) {

            super(itemView);
            //  if(Global.Modo==1){

            nombrePromoProduct=itemView.findViewById(R.id.TVProdcutosPromos);
            recyclerProductPromo=itemView.findViewById(R.id.RecyclerProductosPromo);


            // }

            ///aqui instancias

        }
    }


    private Filter puestos_filter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PromosProductos> filtro=new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtro.addAll(lst_full);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (PromosProductos item : lst_full) {
                    if (item.getNombrePromo().toLowerCase().contains(filterPattern) ) {
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

        void onItemClick(Productos product, int position);
    }



}
