package com.mercadoonline.tiendaclient.models.compra;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Compra {
    public Compra() {
    }
    @SerializedName("id")
    @Expose
    private Integer id;//Mercado o Tiendas

    public int getTipoCarro() {
        return tipoCarro;
    }

    public void setTipoCarro(int tipoCarro) {
        this.tipoCarro = tipoCarro;
    }

    private int tipoCarro;
    @SerializedName("cantidad")
    @Expose
    private Integer cantidad=0;
    public Double getTotal() {
        return Total;
    }
    public void setTotal(Double total) {
        Total = total;
    }
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


    private String latitud;

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }




    private String longitud;


    @SerializedName("puestos")
    List<PuestosCompra> puestos=new ArrayList<>();


    public void agregar_producto(PuestosCompra puestin){


        Boolean encontre =false;
        for(PuestosCompra C: puestos){

            if(C.getId()==puestin.getId()){

                for(CompraProductos pro:puestin.getProductos())
                C.agregar_compra(pro);
                encontre=true;
            }

        }

        if(!encontre) puestos.add(puestin);

    }






}
