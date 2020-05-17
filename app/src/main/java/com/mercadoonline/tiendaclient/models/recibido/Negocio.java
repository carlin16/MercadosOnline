package com.mercadoonline.tiendaclient.models.recibido;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Negocio {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("direccion")
    @Expose
    private String direccion;
    @SerializedName("creditos_totales")
    @Expose
    private String creditosTotales;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCreditosTotales() {
        return creditosTotales;
    }

    public void setCreditosTotales(String creditosTotales) {
        this.creditosTotales = creditosTotales;
    }
}
