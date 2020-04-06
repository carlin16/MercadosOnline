
package com.example.tiendaclient.models.recibido;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetallesP {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_venta")
    @Expose
    private String idVenta;
    @SerializedName("id_vendedor")
    @Expose
    private String idVendedor;
    @SerializedName("id_puesto")
    @Expose
    private String idPuesto;
    @SerializedName("id_producto")
    @Expose
    private String idProducto;
    @SerializedName("precio")
    @Expose
    private String precio;
    @SerializedName("cantidad")
    @Expose
    private String cantidad;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("nombre_producto")
    @Expose
    private String nombreProducto;
    @SerializedName("puesto")
    @Expose
    private PuestoDetalle puesto;
    @SerializedName("vendedor")
    @Expose
    private Vendedor vendedor;
    @SerializedName("unidades")
    @Expose
    private Object unidades;

    public Object getUnidades() {
        return unidades;
    }

    public void setUnidades(Object unidades) {
        this.unidades = unidades;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(String idPuesto) {
        this.idPuesto = idPuesto;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public PuestoDetalle getPuesto() {
        return puesto;
    }

    public void setPuesto(PuestoDetalle puesto) {
        this.puesto = puesto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }


    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

}
