package com.example.tiendaclient.models.compra;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductosCompra {

    @SerializedName("id")
    @Expose
    private Integer idProducto;

    @SerializedName("id_vendedor")
    @Expose
    private String idVendedor;

    @SerializedName("id_cantidad")
    @Expose
    private int id_cantidad;

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
    }

    private Double Total;

    @SerializedName("id_puesto")
    @Expose
    private Integer idPuesto;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("id_categoria")
    @Expose
    private Integer idCategoria;

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(Integer idPuesto) {
        this.idPuesto = idPuesto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    @SerializedName("precio")
    @Expose
    private Double precio;

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public int getId_cantidad() {
        return id_cantidad;
    }

    public void setId_cantidad(int id_cantidad) {
        this.id_cantidad = id_cantidad;
    }
}
