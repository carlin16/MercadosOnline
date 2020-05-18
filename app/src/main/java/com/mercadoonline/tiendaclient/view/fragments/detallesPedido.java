package com.mercadoonline.tiendaclient.view.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.mercadoonline.tiendaclient.R;
import com.mercadoonline.tiendaclient.adapter.VistasDetallePedido;
import com.mercadoonline.tiendaclient.models.recibido.DetallesP;
import com.mercadoonline.tiendaclient.models.recibido.ResponseDetallesPedidos;
import com.mercadoonline.tiendaclient.models.recibido.ResponseError;
import com.mercadoonline.tiendaclient.service.ApiService;
import com.mercadoonline.tiendaclient.service.RetrofitCliente;
import com.mercadoonline.tiendaclient.utils.Global;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

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
        TextView NumeroPedido,NombreTrasnportista,PedidoTxtStatus,PedidoCelular,DetalleSubtotal,DetalleCostoEnvio,DetalleTotal,PedidoFecha,Costo_Comision,PedidoTituloNM,TituloButtonEntrega;
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
        TituloButtonEntrega=vista.findViewById(R.id.TituloButtonEntrega);
        NumeroPedido=vista.findViewById(R.id.NumeroPedido);
        NombreTrasnportista=vista.findViewById(R.id.NombreTrasnportista);



        PedidoTituloNM=vista.findViewById(R.id.PedidoTituloNM);

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
                if(Global.Modo==1){

                    peticion_estado_pedidos();
                    pDialog.show();

                } if(Global.Modo==3){
                    dialog_Whats();
                }
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




        if(Global.Modo==1){


            if(pedido.getTipo().equals("MERCADO")){
                PedidoTituloNM.setText("Transportista");

                if(pedido.getTransportista()!=null){
                    NombreTrasnportista.setText(""+pedido.getTransportista().getNombres()+" "+pedido.getTransportista().getApellidos());
                    PedidoCelular.setText(""+pedido.getTransportista().getCelular());
                }else{
                    NombreTrasnportista.setText("No asignado");
                }

            }else{
                PedidoTituloNM.setText("TIENDA");
                NombreTrasnportista.setText(pedido.getNegocio().getNombre());

            }



            DetalleSubtotal.setText("$"+pedido.getCostoVenta());
            DetalleCostoEnvio.setText("$"+Global.formatearDecimales(Double.parseDouble(""+pedido.getCostoEnvio()),2));
            DetalleTotal.setText("$"+pedido.getTotal());

        }else if(Global.Modo==2){
            PedidoTituloNM.setText("Transportista");
            if(pedido.getTransportista()!=null){
                NombreTrasnportista.setText(""+pedido.getTransportista().getNombres()+" "+pedido.getTransportista().getApellidos());
                PedidoCelular.setText(""+pedido.getTransportista().getCelular());
            }else{
                NombreTrasnportista.setText("No asignado");
            }

            DetalleSubtotal.setText("$"+pedido.getCostoVenta());
            DetalleCostoEnvio.setText("$"+pedido.getCostoEnvio());
            DetalleTotal.setText("$"+pedido.getTotal());
        }else if(Global.Modo==3){

            TituloButtonEntrega.setText("Enviar a Transportista");
            DetaEntregado.setVisibility(View.VISIBLE);
            linea_entregado.setVisibility(View.VISIBLE);
            PedidoTituloNM.setText("Cliente");
            NombreTrasnportista.setText("+"+pedido.getCliente().getNombres());
            PedidoCelular.setText(""+pedido.getCliente().getCelular());
            DetalleSubtotal.setText("$"+pedido.getCostoVenta());
            DetalleCostoEnvio.setText("$"+pedido.getCostoEnvio());
            DetalleTotal.setText("$"+pedido.getTotal());
        }

        PedidoFecha.setText(pedido.getFechaRegistro());


        if(pedido.getEstado().equals("ENTREGADA")){
            PedidoStatus.setBackground(getResources().getDrawable(R.drawable.border_estatus_purpura));
            PedidoTxtStatus.setText("Entregada");
        }

        if(pedido.getEstado().equals("IN_PROGRESS")){
            PedidoStatus.setVisibility(View.VISIBLE);
            PedidoStatus.setBackground(getResources().getDrawable(R.drawable.border_estatus_rojo));
            PedidoTxtStatus.setText("En Progreso");

            if(Global.Modo==1 ){
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
                                    for(DetallesP p:pedido.getDetalles()){

                                        if(Integer.parseInt(p.getIdVendedor())==Global.LoginU.getid() && Integer.parseInt(p.getIdPuesto())==Global.LoginU.getId_puesto()){
                                            LstPro.add(p);
                                            subTotal=Global.formatearDecimales(subTotal+Double.parseDouble(p.getSubtotal()),2);
                                        }

                                    }


                                    comision=Global.formatearDecimales((subTotal*5)/100,2);
                                }else {
                                    LstPro.addAll(pedido.getDetalles());
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
                               // Log.e("normal-->400",mensaje);

                            } catch (Exception e) {
                                //Log.e("error conversion json",""+e.getMessage());
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
    String numeroTelefono="";

    private void dialog_Whats(){
        Dialog myDialog;
        myDialog = new Dialog(getActivity());


        myDialog.setContentView(R.layout.menu_envio_datos);
        myDialog.setCancelable(true);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

      EditText numero=myDialog.findViewById(R.id.ETTelLocal);
      RelativeLayout  DetalleBtnContinuar=myDialog.findViewById(R.id.DetalleBtnContinuar);
        CountryCodePicker codigo_pais=myDialog.findViewById(R.id.CCPTiendaNueva);
        DetalleBtnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(numero.getText().length()>7){
                    if (numero.getText().toString().substring(0, 1).equals("0")) {
                        numeroTelefono = codigo_pais.getSelectedCountryCode() + numero.getText().toString().trim().substring(1);
                        llenarDatos();
                    } else {
                        numeroTelefono = codigo_pais.getSelectedCountryCode() + numero.getText().toString().trim();
                    }
                    enviaMensajeWhatsApp(pedido.getCliente().getCelular(),numeroTelefono,""+pedido.getCostoEnvio(),""+pedido.getEntrega().getLngEntrega(),""+pedido.getEntrega().getLatEntrega() );
                }

                //contacto
               // pedido.getCliente().getCelular()
               //numero
              // numero.getText().toString();
               // pedido.getEntrega().getLatEntrega()
                //pedido.getEntrega().getLngEntrega()

                Log.e("Whatsapp",""+numeroTelefono);
                myDialog.dismiss();

            }
        });

        myDialog.show();
    }

    //telSend, telContactar, costo, lonLat
    //public  LatLng nuevo=null;
    public void  enviaMensajeWhatsApp(String telInfo, String telContactar, String costoEnvio, String longUbicacion, String latUbicacion){
        try {
            //String toNumber = "+593993942225"; // contains spaces.
            String toNumber;
            toNumber = telContactar.replace("+", "").replace(" ", "");

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Contacto:  "+""+telInfo+ "\n Costo: $"+""+costoEnvio+  "\n Ubicacion: https://www.google.com/maps/search/?api=1&query="+latUbicacion+","+longUbicacion+"/");



            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setPackage("com.whatsapp");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);



        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "Whatsapp no esta instalado.", Toast.LENGTH_LONG).show();
        }
    }





}
