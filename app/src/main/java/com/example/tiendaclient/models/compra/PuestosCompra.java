package com.example.tiendaclient.models.compra;

import com.example.tiendaclient.models.recibido.Producto;
import com.example.tiendaclient.models.recibido.Vendedor;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PuestosCompra {

    @SerializedName("vendedor")
    @Expose
    private Vendedor vendedor;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("productos")
    @Expose
    private List<ProductosCompra> productos = new ArrayList<>();


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

    public List<ProductosCompra> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductosCompra> productos) {
        this.productos = productos;
    }



    public void agregar_compra( ProductosCompra pro){

        Boolean encontre =false;
        for(ProductosCompra C: productos){

            if(C.getIdProducto()==pro.getIdProducto()){


             int cantidad= C.getId_cantidad()+pro.getId_cantidad();

             C.setId_cantidad(cantidad);
             C.setTotal(cantidad*C.getPrecio());

                encontre=true;
            }

        }

        if(!encontre) productos.add(pro);


    }

}
