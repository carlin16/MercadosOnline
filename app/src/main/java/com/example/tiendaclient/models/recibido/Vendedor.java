
package com.example.tiendaclient.models.recibido;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vendedor {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nombres")
    @Expose
    private String nombres;
    @SerializedName("apellidos")
    @Expose
    private String apellidos;
    @SerializedName("imagen_perfil")
    @Expose
    private Object imagenPerfil;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Object getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(Object imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

}
