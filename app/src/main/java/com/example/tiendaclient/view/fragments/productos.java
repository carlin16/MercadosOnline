package com.example.tiendaclient.view.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.tiendaclient.R;
import com.example.tiendaclient.adapter.VistasProductos;
import com.example.tiendaclient.models.compra.Compra;
import com.example.tiendaclient.models.compra.CompraProductos;
import com.example.tiendaclient.models.compra.PuestosCompra;
import com.example.tiendaclient.models.recibido.Producto;
import com.example.tiendaclient.models.recibido.ResponseVerMercado;
import com.example.tiendaclient.models.recibido.Vendedor;
import com.example.tiendaclient.utils.Global;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class productos extends Fragment {


    public List<Producto> ls_listado= new ArrayList<>();
    public Vendedor vendedor= new Vendedor();
    public ResponseVerMercado Mercado =new ResponseVerMercado();


    TextView NombreProducto, UnidadesProd, Valorproduct,DescripProduct,Subtotal;
    ElegantNumberButton CantidadCar;
    RoundedImageView FotoProducto,FotoPuesto;
    Button AgregarCarrito;

    ImageView compra;
    EditText buscar;

    DecimalFormat df = new DecimalFormat("#.00");//formatear  a 2 decimales

    public productos() {
        // Required empty public constructor
    }

    View vista;
    RecyclerView recyclerView;
    VistasProductos adapter;
    TextView Idpuesto, NombreDueno, DescripcionPuesto, Cantidadpro;
    public String idPuesto;
    String LinkImagenP=Global.Url+"usuarios/"+idPuesto+"/foto";
    public  String categorias;
    public int ID=0;
    Dialog myDialog;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


UI();
llenarDatos();
        animacion_compra();
        iniciar_recycler();
        click();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista =inflater.inflate(R.layout.fragment_productos, container, false);
        return vista;
    }

    private void  iniciar_recycler(){
        recyclerView= vista.findViewById(R.id.Recycler_productos);
        adapter=new VistasProductos(ls_listado, new VistasProductos.OnItemClicListener() {
            @Override
            public void onItemClick(final Producto product, int position) {
                Log.e("hey producto","click");
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //
                NombreProducto.setText(product.getNombre());
                UnidadesProd.setText("Precio Unidad");
                Valorproduct.setText("$"+product.getPrecio().toString());
                Subtotal.setText("$"+product.getPrecio().toString());
                DescripProduct.setText(product.getDescripcion());
                CantidadCar.setNumber("1");

                Glide
                        .with(getActivity())
                        .load(Global.Url+"productos/"+product.getId()+"/foto")
                        .placeholder(R.drawable.placeholder_mercado)
                        .error(R.drawable.portada_mercado)
                        .into(FotoProducto);

                final double precio=product.getPrecio();
                 //final double subtotal=0;
                CantidadCar.setRange( 1,  1000);
                CantidadCar.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                    @Override
                    public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                        Log.e("Elegante btn", String.format("oldValue: %d   newValue: %d", oldValue, newValue));


                        Subtotal.setText("$"+df.format(precio*newValue));
                    }
                });

                AgregarCarrito.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Se agrego al Carrito", Toast.LENGTH_SHORT).show();
                        llenarCarrito(product);
                        myDialog.dismiss();

                    }
                });

               // FotoProducto=myDialog.findViewById(R.id.TVPuestoFotoV);
                //AgregarCarrito=myDialog.findViewById(R.id.TVCompBtnAgregarCar);
                myDialog.show();


            }
        });
       /* RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);*/

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

private void llenarDatos(){

    NombreDueno.setText(vendedor.getNombres()+" "+vendedor.getApellidos());

    Cantidadpro.setText(""+(ls_listado.size()));

    Idpuesto.setText(idPuesto);
    Glide
            .with(getActivity())
            .load(LinkImagenP)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.placeholder_perfil)
            .error(R.drawable.placeholder_perfil)
            .fitCenter()
            .into(FotoPuesto);
}
    private void UI(){


        Idpuesto= vista.findViewById(R.id.TVProPuesto);
        NombreDueno= vista.findViewById(R.id.TVPuestoNombre);
        DescripcionPuesto= vista.findViewById(R.id.TVPuestoDescripcion);
        Cantidadpro= vista.findViewById(R.id.TVProCantidad);
        FotoPuesto= vista.findViewById(R.id.TVPuestoFotoV);
        DescripcionPuesto.setText(categorias);
        compra=vista.findViewById(R.id.icono_buscar);
        buscar=vista.findViewById(R.id.escribir_busqueda);

    }

    private void animacion_compra(){
        myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.dialog_compra);
        myDialog.setCancelable(true);

      NombreProducto=myDialog.findViewById(R.id.TVProducNombre);
        UnidadesProd=myDialog.findViewById(R.id.TVProducUnidades);
        Valorproduct=myDialog.findViewById(R.id.TVProducValor);
        DescripProduct=myDialog.findViewById(R.id.TVCompDescripcion);
        CantidadCar=myDialog.findViewById(R.id.TVCompCantidad);
        Subtotal=myDialog.findViewById(R.id.TVCompSubtotal);
        FotoProducto=myDialog.findViewById(R.id.TVPuestoFotoV);
        AgregarCarrito=myDialog.findViewById(R.id.TVCompBtnAgregarCar);


    }
private void llenarCarrito(Producto product){
    Compra nuevoC = new Compra();
    List<PuestosCompra> pues = new ArrayList<>();
    PuestosCompra PuestComp = new PuestosCompra();
    List<CompraProductos> lstprod=new ArrayList<>();
    CompraProductos prod= new CompraProductos();

    prod.setNombre(product.getNombre());
    prod.setDescripcion(product.getDescripcion());
    prod.setId_cantidad(Integer.parseInt(CantidadCar.getNumber()));
    prod.setIdCategoria(product.getIdCategoria());
    prod.setIdProducto(product.getId());
    prod.setIdPuesto(product.getIdPuesto());
    prod.setIdVendedor(vendedor.getId().toString());
    prod.setPrecio(product.getPrecio());
    Double total= prod.getId_cantidad()*product.getPrecio();

    String f=df.format(total);
    try {
        total=DecimalFormat.getNumberInstance().parse(f).doubleValue();

        Log.e("total" , "------------->"+total);
    } catch (ParseException e) {
        e.printStackTrace();
    }

    prod.setTotal(total);

    lstprod.add(prod);

    PuestComp.setId(ID);
    PuestComp.setVendedor(vendedor);
    PuestComp.setProductos(lstprod);

    pues.add(PuestComp);

    nuevoC.setPuestos(pues);
    nuevoC.setCantidad(Integer.parseInt(CantidadCar.getNumber()));
    nuevoC.setCiudad(Mercado.getCiudad());
    nuevoC.setCodigoMercado(Mercado.getCodigoMercado());
    nuevoC.setDescripcion(Mercado.getDescripcion());
    nuevoC.setDireccion(Mercado.getDireccion());
    nuevoC.setEstado(Integer.parseInt(Mercado.getEstado()));
    nuevoC.setFechaRegistro(Mercado.getFechaRegistro());
    nuevoC.setId(Mercado.getId());
    // nuevoC.setLatitud(""+Mercado.getLatitud().toString());
    // nuevoC.setLongitud(""+Mercado.getLatitud().toString());
    nuevoC.setNombre(Mercado.getNombre());
    nuevoC.setTotal(total);
    Global.Agregar_Carrito(nuevoC);

}

    private void click(){

        compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"La busqueda esta opcional pero esta puesto el prototipo si pide un update $ ",Toast.LENGTH_LONG).show();

                carrito car = new carrito();
                car.id_del_fragment="frag_car";
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Contenedor_Fragments, car).addToBackStack("frag_car");
                fragmentTransaction.commit();


            }
        });



       buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                filtro(editable.toString());









                //  filter(editable.toString());

            }
        });

    }

    public void filtro(String S){
        if(ls_listado.size()>0)
            adapter.getFilter().filter(S);
    }


}
