package com.mercadoonline.tiendaclient.models.recibido;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseUserPorID {

    @SerializedName("id")
    @Expose
    private Integer id=0;
    @SerializedName("usuario")
    @Expose
    private String usuario="";
    @SerializedName("password")
    @Expose
    private String password="";
    @SerializedName("auth_token")
    @Expose
    private String authToken="";
    @SerializedName("email")
    @Expose
    private String email="";
    @SerializedName("nombres")
    @Expose
    private String nombres="";
    @SerializedName("apellidos")
    @Expose
    private String apellidos="";
    @SerializedName("direccion")
    @Expose
    private String direccion="";
    @SerializedName("celular")
    @Expose
    private String celular="";
    @SerializedName("gc_token")
    @Expose
    private Object gcToken="";
    @SerializedName("fecha_registro")
    @Expose
    private String fechaRegistro="";
    @SerializedName("estado")
    @Expose
    private Integer estado=1;
    @SerializedName("rol")
    @Expose
    private String rol="";
    @SerializedName("imagen_perfil")
    @Expose
    private String imagenPerfil="";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Object getGcToken() {
        return gcToken;
    }

    public void setGcToken(Object gcToken) {
        this.gcToken = gcToken;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

}

