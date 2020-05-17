
package com.mercadoonline.tiendaclient.models.recibido;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Producto {


    @SerializedName("stock")
    @Expose
    private Integer stock;

    @SerializedName("fuente")
    @Expose
    private Integer fuente;

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_puesto")
    @Expose
    private String idPuesto;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("id_categoria")
    @Expose
    private String idCategoria;
    @SerializedName("precio")
    @Expose
    private String precio;
    @SerializedName("url_imagen")
    @Expose
    private Object urlImagen;
    @SerializedName("estado")
    @Expose
    private String estado;
    @SerializedName("fecha_registro")
    @Expose
    private String fechaRegistro;
    @SerializedName("fecha_actualiza")
    @Expose
    private Object fechaActualiza;
    @SerializedName("unidades")
    @Expose
    private Object unidades;
    @SerializedName("nombre_categoria")
    @Expose
    private String nombreCategoria;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(String idPuesto) {
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

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public Object getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(Object urlImagen) {
        this.urlImagen = urlImagen;
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

    public Object getUnidades() {
        return unidades;
    }

    public void setUnidades(Object unidades) {
        this.unidades = unidades;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getFuente() {
        return fuente;
    }

    public void setFuente(Integer fuente) {
        this.fuente = fuente;
    }
}
