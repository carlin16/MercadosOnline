
package com.mercadoonline.tiendaclient.models.enviado;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqRegTienda {

    @SerializedName("id_usuario")
    @Expose
    private Integer idUsuario;
    @SerializedName("telefono")
    @Expose
    private String telefono;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("direccion")
    @Expose
    private String direccion;
    @SerializedName("longitud")
    @Expose
    private String longitud;
    @SerializedName("latitud")
    @Expose
    private String latitud;
    @SerializedName("ciudad")
    @Expose
    private String ciudad;
    @SerializedName("tipo_negocio")
    @Expose
    private Integer tipoNegocio;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Integer getTipoNegocio() {
        return tipoNegocio;
    }

    public void setTipoNegocio(Integer tipoNegocio) {
        this.tipoNegocio = tipoNegocio;
    }

}
