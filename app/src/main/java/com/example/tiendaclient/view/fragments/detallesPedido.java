package com.example.tiendaclient.view.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiendaclient.R;
import com.example.tiendaclient.adapter.VistasDetallePedido;
import com.example.tiendaclient.models.enviado.Detalle;
import com.example.tiendaclient.models.recibido.DetallesP;
import com.example.tiendaclient.models.recibido.ResponseDetallesPedidos;
import com.example.tiendaclient.models.recibido.ResponseError;
import com.example.tiendaclient.models.recibido.ResponseVerPedido;
import com.example.tiendaclient.service.ApiService;
import com.example.tiendaclient.service.RetrofitCliente;
import com.example.tiendaclient.utils.Global;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;


public class detallesPedido extends Fragment {

    List<DetallesP> LstPro = new ArrayList<>();
    ResponseDetallesPedidos pedido = new ResponseDetallesPedidos();
    Retrofit retrofit;
    public String id_pedido;
    ApiService retrofitApi;
    Boolean continuar=false;
    SweetAlertDialog pDialog;
View linea_entregado ;
    String mensaje="detalle pedidos";
    RecyclerView recyclerView;
    VistasDetallePedido adapter;
    TextView NumeroPedido,NombreTrasnportista,PedidoTxtStatus,PedidoCelular,DetalleSubtotal,DetalleCostoEnvio,DetalleTotal,PedidoFecha,Costo_Comision;
        LinearLayout PedidoStatus;
        RelativeLayout DetaEntregado;
        RoundedImageView atras_detalle_pedido;
        View vista;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_detalles_pedido, container, false);
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        animacion_cargando();
        UI();
        peticion_pedidos();
    }

    private void UI(){

        PedidoFecha=vista.findViewById(R.id.PedidoFecha);

        NumeroPedido=vista.findViewById(R.id.NumeroPedido);
        NombreTrasnportista=vista.findViewById(R.id.NombreTrasnportista);
        PedidoTxtStatus=vista.findViewById(R.id.PedidoTxtStatus);
        PedidoCelular=vista.findViewById(R.id.PedidoCelular);
        DetalleSubtotal=vista.findViewById(R.id.DetalleSubtotal);
        DetalleCostoEnvio=vista.findViewById(R.id.DetalleCostoEnvio);
        DetalleTotal=vista.findViewById(R.id.DetalleTotal);
        PedidoStatus=vista.findViewById(R.id.PedidoStatus);
        DetaEntregado=vista.findViewById(R.id.DetaEntregado);

        linea_entregado=vista.findViewById(R.id.linea_entregado);
        Costo_Comision=vista.findViewById(R.id.Costo_Comision);

            if(Global.Modo==2){
                Costo_Comision.setText("Comisión");
            }

        DetaEntregado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                peticion_estado_pedidos();
                pDialog.show();
            }
        });

        atras_detalle_pedido=vista.findViewById(R.id.atras_detalle_pedido);
        atras_detalle_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }



    Double subTotal=0.0;
    Double comision=0.0;

    private void llenarDatos(){

        NumeroPedido.setText("Pedido # "+pedido.getId().toString());

        if(pedido.getTransportista()!=null){
            NombreTrasnportista.setText(""+pedido.getTransportista().getNombres()+" "+pedido.getTransportista().getApellidos());
            PedidoCelular.setText(""+pedido.getTransportista().getCelular());
        }else{
              NombreTrasnportista.setText("No asignado");
        }


        if(Global.Modo==1){
            DetalleSubtotal.setText("$"+pedido.getCostoVenta());
            DetalleCostoEnvio.setText("$"+Global.formatearDecimales(Double.parseDouble(pedido.getCostoEnvio()),2));
            DetalleTotal.setText("$"+pedido.getTotal());

        }else{
            ///cambiar
            DetalleSubtotal.setText("$"+subTotal);
            DetalleCostoEnvio.setText("$"+comision);
            DetalleTotal.setText("$"+Global.formatearDecimales(subTotal-comision,2));
        }

        PedidoFecha.setText(pedido.getFechaRegistro());


        if(pedido.getEstado().equals("ENTREGADA")){
            PedidoStatus.setBackground(getResources().getDrawable(R.drawable.border_estatus_purpura));
            PedidoTxtStatus.setText("Entregada");
        }

        if(pedido.getEstado().equals("IN_PROGRESS")){
            PedidoStatus.setVisibility(View.VISIBLE);
            PedidoStatus.setBackground(getResources().getDrawable(R.drawable.border_estatus_rojo));
            PedidoTxtStatus.setText("En Progresso");

            if(Global.Modo==1){
                DetaEntregado.setVisibility(View.VISIBLE);
                linea_entregado.setVisibility(View.VISIBLE);
            }


        }
        if(pedido.getEstado().equals("WAITING")){
            PedidoStatus.setBackground(getResources().getDrawable(R.drawable.border_estatus_naranja));
            PedidoTxtStatus.setText("En Espera");

        }


    }

    private void  iniciar_recycler(){
        recyclerView=vista.findViewById(R.id.Recycler_Detalles);
        adapter= new VistasDetallePedido(LstPro);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    private void peticion_pedidos(){
        //("Pedidos","Detalles");
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi.VerDetallePedidos( id_pedido, "FULL",Global.LoginU.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<ResponseDetallesPedidos>>() {
                    @Override
                    public void onNext(Response<ResponseDetallesPedidos> response) {


                        if(response.isSuccessful()){
                            pedido=response.body();
                            //("Detalles",Global.convertObjToString(pedido));
                            continuar=true;
                        }else{
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson =new Gson();
                                ResponseError staff = gson.fromJson(jObjError.toString(), ResponseError.class);
                                mensaje=staff.getMensaje();
                                //("normal-->400",mensaje);

                            } catch (Exception e) {
                                //("error conversion json",""+e.getMessage());
                            }


                        }



                    }
                    @Override
                    public void onError(Throwable e) {
                        //("CodPetiEroor",""+e.toString());

                    }

                    @Override
                    public void onComplete() {

                        //("CodPetiEroor","completado");
                        // adapter.notifyDataSetChanged();
                        if(getActivity()==null || isRemoving() || isDetached()){
                            //("activity","removido ");
                            return;
                        }else{



                            if(continuar){

                                if(Global.Modo==2){
                                    for(DetallesP p:pedido.getDetallesPS()){

                                        if(Integer.parseInt(p.getIdVendedor())==Global.LoginU.getid() && Integer.parseInt(p.getIdPuesto())==Global.LoginU.getId_puesto()){
                                            LstPro.add(p);
                                            subTotal=Global.formatearDecimales(subTotal+Double.parseDouble(p.getSubtotal()),2);
                                        }

                                    }


                                    comision=Global.formatearDecimales((subTotal*5)/100,2);
                                }else {
                                    LstPro.addAll(pedido.getDetallesPS());
                                }
                                iniciar_recycler();
                                llenarDatos();

                                Global.convertObjToString(pedido);
                            }
                            else{
                                Toast.makeText(getActivity(),mensaje,Toast.LENGTH_LONG).show();
                            }





                        }



                    }
                });
    }

    private void peticion_estado_pedidos(){
        Log.e("Pedidos","Detalles");
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi.ActualizarPedido( id_pedido,Global.LoginU.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<ResponseError>>() {
                    @Override
                    public void onNext(Response<ResponseError> response) {


                        if(response.isSuccessful()){

                        }else{
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson =new Gson();
                                ResponseError staff = gson.fromJson(jObjError.toString(), ResponseError.class);
                                mensaje=staff.getMensaje();
                                Log.e("normal-->400",mensaje);

                            } catch (Exception e) {
                                Log.e("error conversion json",""+e.getMessage());
                            }


                        }



                    }
                    @Override
                    public void onError(Throwable e) {
                        pDialog.dismiss();

                    }

                    @Override
                    public void onComplete() {
                        pDialog.dismiss();
                        getFragmentManager().popBackStack();
                    }
                });
    }


    private void animacion_cargando(){
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.col_naranja))));
        pDialog.setTitleText("Loading..");
        pDialog.setCancelable(false);

    }
}
