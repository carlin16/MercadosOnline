package com.mercadoonline.tiendaclient.models.recibido;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class ResponseTiendas {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_usuario")
    @Expose
    private Integer idUsuario;
    @SerializedName("nombre")
    @Expose
    private String nombre="";
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("direccion")
    @Expose
    private String direccion="";
    @SerializedName("ciudad")
    @Expose
    private String ciudad;
    @SerializedName("latitud")
    @Expose
    private String latitud;
    @SerializedName("longitud")
    @Expose
    private String longitud;
    @SerializedName("telefono")
    @Expose
    private Object telefono;
    @SerializedName("creditos_totales")
    @Expose
    private String creditosTotales;

    @SerializedName("tipo_negocio")
    @Expose
    private Integer tipoNegocio;
    @SerializedName("url_imagen")
    @Expose
    private String urlImagen;
    @SerializedName("estado")
    @Expose
    private Integer estado;
    @SerializedName("fecha_registro")
    @Expose
    private String fechaRegistro;
    @SerializedName("fecha_actualiza")
    @Expose
    private Object fechaActualiza;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public Object getTelefono() {
        return telefono;
    }

    public void setTelefono(Object telefono) {
        this.telefono = telefono;
    }

    public String getCreditosTotales() {
        return creditosTotales;
    }

    public void setCreditosTotales(String creditosTotales) {
        this.creditosTotales = creditosTotales;
    }



    public Integer getTipoNegocio() {
        return tipoNegocio;
    }

    public void setTipoNegocio(Integer tipoNegocio) {
        this.tipoNegocio = tipoNegocio;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
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
}
