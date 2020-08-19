package com.mercadoonline.tiendaclient.models.compra;

import com.mercadoonline.tiendaclient.models.recibido.Vendedor;
import com.mercadoonline.tiendaclient.utils.Global;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PuestosCompra {

    @SerializedName("vendedor") //id del usuario
    @Expose
    private Vendedor vendedor;
    @SerializedName("id")  //id del mercado para puesto
    @Expose
    private Integer id;
    @SerializedName("productos")
    @Expose
    private List<CompraProductos> productos = new ArrayList<>();


    private String codigoPuesto;

    public String getCodigoPuesto() {
        return codigoPuesto;
    }

    public void setCodigoPuesto(String codigoPuesto) {
        this.codigoPuesto = codigoPuesto;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<CompraProductos> getProductos() {
        return productos;
    }

    public void setProductos(List<CompraProductos> productos) {
        this.productos = productos;
    }



    public void agregar_compra( CompraProductos pro){

        Boolean encontre =false;
        for(CompraProductos C: productos){
            if(C.getIdProducto()==pro.getIdProducto()){
             int cantidad= C.getId_cantidad()+pro.getId_cantidad();
             C.setId_cantidad(cantidad);
             C.setTotal(Global.formatearDecimales((cantidad*C.getPrecio()),2));

                encontre=true;
            }

        }

        if(!encontre) productos.add(pro);


    }

}
