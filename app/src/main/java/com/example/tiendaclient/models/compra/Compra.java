package com.example.tiendaclient.models.compra;

import com.example.tiendaclient.models.recibido.Puesto;
import com.example.tiendaclient.models.recibido.ResponseVerAllPuesto;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Compra {


    public Compra() {

    }

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("cantidad")
    @Expose
    private Integer cantidad=0;

    @SerializedName("total")
    @Expose
    private Double Total=0.0;

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

    public List<PuestosCompra> getPuestos() {
        return puestos;
    }

    public void setPuestos(List<PuestosCompra> puestos) {
        this.puestos = puestos;
    }

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
    private String latitud;

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }



    @SerializedName("longitud")
    @Expose
    private String longitud;
    @SerializedName("fecha_registro")
    @Expose
    private String fechaRegistro;
    @SerializedName("estado")
    @Expose
    private Integer estado;

    @SerializedName("puestos")
    List<PuestosCompra> puestos=new ArrayList<>();


    public void agregar_producto(PuestosCompra puestin){


        Boolean encontre =false;
        for(PuestosCompra C: puestos){

            if(C.getId()==puestin.getId()){
                C.agregar_compra(puestin.getProductos().get(0));
                encontre=true;
            }

        }

        if(!encontre) puestos.add(puestin);

    }






}
