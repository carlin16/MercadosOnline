
package com.example.tiendaclient.models.recibido;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseVerMercado {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("codigo_mercado")
    @Expose
    private String codigoMercado;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("direccion")
    @Expose
    private String direccion;
    @SerializedName("ciudad")
    @Expose
    private String ciudad;
    @SerializedName("latitud")
    @Expose
    private Object latitud;
    @SerializedName("longitud")
    @Expose
    private Object longitud;
    @SerializedName("fecha_registro")
    @Expose
    private String fechaRegistro;
    @SerializedName("estado")
    @Expose
    private String estado;
    @SerializedName("url_imagen")
    @Expose
    private String urlImagen;
    @SerializedName("puestos")
    @Expose
    private List<Puesto> puestos = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoMercado() {
        return codigoMercado;
    }

    public void setCodigoMercado(String codigoMercado) {
        this.codigoMercado = codigoMercado;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Object getLatitud() {
        return latitud;
    }

    public void setLatitud(Object latitud) {
        this.latitud = latitud;
    }

    public Object getLongitud() {
        return longitud;
    }

    public void setLongitud(Object longitud) {
        this.longitud = longitud;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public List<Puesto> getPuestos() {
        return puestos;
    }

    public void setPuestos(List<Puesto> puestos) {
        this.puestos = puestos;
    }
}
