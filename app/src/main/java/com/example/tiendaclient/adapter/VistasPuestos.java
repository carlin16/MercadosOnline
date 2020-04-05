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
import com.example.tiendaclient.utils.Global;
import com.example.tiendaclient.view.fragments.productos;
import com.google.android.gms.common.api.Response;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
        List<ResponseVerAllPuesto> filtro=new ArrayList<>();
/*

        for(ResponseVerAllPuesto res:lst_normal){
            if(res.getProductos().size()>0 ){
                filtro.add(res);
            }
        }
*/

        this.lst_normal = lst_normal;
        this.fragmentManager = fragmentManager;
        this.Mercado=Mercado;
        lst_full=new ArrayList<>(lst_normal);

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


       String url= Global.Url+"usuarios/"+lst_normal.get(position).getIdVendedor()+"/foto";
        Glide
                .with(holder.imagen.getContext())
                .load(url)
                // .override(60,60)
                .placeholder(R.drawable.placeholder_mercado)
                .error(R.drawable.placeholder_mercado)
                .into(holder.imagen);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //("click","Tienda");
        productos pro= new productos();
        pro.ls_listado=lst_normal.get(position).getProductos();
        pro.vendedor=lst_normal.get(position).getVendedor();
        pro.idPuesto=""+lst_normal.get(position).getCodigo();
        pro.ImageVendedor=Global.Url+"usuarios/"+lst_normal.get(position).getIdVendedor()+"/foto";
        pro.ID=lst_normal.get(position).getId();
        pro.categorias=""+lst_normal.get(position).getMaxCategorias();
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
        CircleImageView imagen;
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
                //("lista","es nulo o es cero");

                filtro.addAll(lst_full);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ResponseVerAllPuesto item : lst_full) {


                   String comparar= item.getMaxCategorias().toLowerCase().trim();

                    List<String> list = new ArrayList<String>(Arrays.asList(comparar.split(" , ")));
                    //("lista",Global.convertObjToString(list));


                    boolean encontre=false;
                    for(String s:list){
                        //("buscar",s+"");
                        //("buscando",""+filterPattern+"--"+s.contains(filterPattern));

                        if(s.toLowerCase().contains(filterPattern)){
                            encontre=true;
                        }

                    }

                    if(encontre){
                        filtro.add(item);
                    }
                   /* if(list.contains(filterPattern)){
                    }*/

                   /* if (item.getFechaRegistro().toLowerCase().contains(filterPattern) ) {
                        filtro.add(item);
                    }*/


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
