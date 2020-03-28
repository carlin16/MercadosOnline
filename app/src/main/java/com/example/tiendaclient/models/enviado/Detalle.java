
package com.example.tiendaclient.models.enviado;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detalle {

    @SerializedName("id_vendedor")
    @Expose
    private Integer idVendedor;
    @SerializedName("id_producto")
    @Expose
    private Integer idProducto;
    @SerializedName("cantidad")
    @Expose
    private Integer cantidad;

    public Integer getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Integer idVendedor) {
        this.idVendedor = idVendedor;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

}
