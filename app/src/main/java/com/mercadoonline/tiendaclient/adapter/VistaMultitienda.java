package com.mercadoonline.tiendaclient.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import com.mercadoonline.tiendaclient.R;
import com.mercadoonline.tiendaclient.models.recibido.Puesto;

import java.util.List;

public class VistaMultitienda extends RecyclerView.Adapter<VistaMultitienda.MultiHolder> {


    //Puesto
    List<Puesto> lstvistas_Puesto;
    Context contex;
    FragmentManager fragmentManager;

    public VistaMultitienda(List<Puesto> lstvistas_Puesto) {
        this.lstvistas_Puesto = lstvistas_Puesto;
    }

    public VistaMultitienda(List<Puesto> lstvistas_Puesto, FragmentManager fragmentManager) {
        this.lstvistas_Puesto = lstvistas_Puesto;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public MultiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tiendas,parent,false);


        //RecyclerView.LayoutParams layoutParams= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // view.setLayoutParams(layoutParams);
        MultiHolder th= new MultiHolder(view);
        return th;    }

    @Override
    public void onBindViewHolder(@NonNull MultiHolder holder, int position) {
        holder.nombre.setText(""+lstvistas_Puesto.get(position).getCodigo());
      //  Glide
              //  .with(holder.imagen.getContext())
               // .load(""+lstvistas_Puesto.get(position).getImagen())
                // .override(60,60)

                //   .centerCrop()
           //     .placeholder(R.drawable.user_imagen)
          //      .into(holder.imagen);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click","Puesto");
            }
        });

    }

    @Override
    public int getItemCount() {
        return lstvistas_Puesto.size();
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
}
