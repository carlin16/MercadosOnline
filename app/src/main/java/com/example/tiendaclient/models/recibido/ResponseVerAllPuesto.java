
package com.example.tiendaclient.models.recibido;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseVerAllPuesto {

        @SerializedName("id")
        @Expose
        private Integer id;
    @SerializedName("codigo")
    @Expose
    private String codigo;
    @SerializedName("id_mercado")
    @Expose
    private String idMercado;
    @SerializedName("id_vendedor")
    @Expose
    private String idVendedor;
    @SerializedName("estado")
    @Expose
    private String estado;
    @SerializedName("fecha_registro")
    @Expose
    private String fechaRegistro;
    @SerializedName("fecha_actualiza")
    @Expose
    private Object fechaActualiza;
    @SerializedName("max_categorias")
    @Expose
    private String maxCategorias="";
    @SerializedName("vendedor")
    @Expose
    private Vendedor vendedor;
    @SerializedName("productos")
    @Expose
    private List<Producto> productos = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getIdMercado() {
        return idMercado;
    }

    public void setIdMercado(String idMercado) {
        this.idMercado = idMercado;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Object getFechaActualiza() {
        return fechaActualiza;
    }

    public void setFechaActualiza(Object fechaActualiza) {
        this.fechaActualiza = fechaActualiza;
    }

    public String getMaxCategorias() {
        return maxCategorias;
    }

    public void setMaxCategorias(String maxCategorias) {
        this.maxCategorias = maxCategorias;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

}
