
package com.mercadoonline.tiendaclient.models.enviado;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mercadoonline.tiendaclient.models.recibido.Vendedor;

public class Detalle {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_venta")
    @Expose
    private Integer idVenta;
    @SerializedName("id_vendedor")
    @Expose
    private Integer idVendedor;
    @SerializedName("id_puesto")
    @Expose
    private Object idPuesto;
    @SerializedName("id_producto")
    @Expose
    private Integer idProducto;
    @SerializedName("precio")
    @Expose
    private Double precio;
    @SerializedName("cantidad")
    @Expose
    private Integer cantidad;
    @SerializedName("subtotal")
    @Expose
    private Double subtotal;
    @SerializedName("nombre_producto")
    @Expose
    private String nombreProducto;
    @SerializedName("unidades")
    @Expose
    private String unidades;
    @SerializedName("puesto")
    @Expose
    private Object puesto;
    @SerializedName("vendedor")
    @Expose
    private Vendedor vendedor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public Integer getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Integer idVendedor) {
        this.idVendedor = idVendedor;
    }

    public Object getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(Object idPuesto) {
        this.idPuesto = idPuesto;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getUnidades() {
        return unidades;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }

    public Object getPuesto() {
        return puesto;
    }

    public void setPuesto(Object puesto) {
        this.puesto = puesto;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

}
