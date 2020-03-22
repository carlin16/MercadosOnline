package com.example.tiendaclient.models.enviado;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PeticionRegistroUser {

    @SerializedName("usuario")
    @Expose
    private String usuario="CARLIN21o";
    @SerializedName("password")
    @Expose
    private String password="12345";
    @SerializedName("nombres")
    @Expose
    private String nombres="X";
    @SerializedName("apellidos")
    @Expose
    private String apellidos="A";
    @SerializedName("direccion")
    @Expose
    private String direccion="DURAN";
    @SerializedName("celular")
    @Expose
    private String celular="0993942225";
    @SerializedName("rol")
    @Expose
    private String rol="CLIENTE";
    @SerializedName("email")
    @Expose
    private String email="carls.forever@gmail.com";

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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}