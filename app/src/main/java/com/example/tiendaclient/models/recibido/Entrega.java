
package com.example.tiendaclient.models.recibido;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Entrega {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_pedido")
    @Expose
    private String idPedido;
    @SerializedName("direccion_entrega")
    @Expose
    private String direccionEntrega;
    @SerializedName("celular_contacto")
    @Expose
    private String celularContacto;
    @SerializedName("lat_entrega")
    @Expose
    private String latEntrega;
    @SerializedName("lng_entrega")
    @Expose
    private String lngEntrega;
    @SerializedName("lat_transportista")
    @Expose
    private Object latTransportista;
    @SerializedName("lng_transportista")
    @Expose
    private Object lngTransportista;
    @SerializedName("estado")
    @Expose
    private String estado;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getCelularContacto() {
        return celularContacto;
    }

    public void setCelularContacto(String celularContacto) {
        this.celularContacto = celularContacto;
    }

    public String getLatEntrega() {
        return latEntrega;
    }

    public void setLatEntrega(String latEntrega) {
        this.latEntrega = latEntrega;
    }

    public String getLngEntrega() {
        return lngEntrega;
    }

    public void setLngEntrega(String lngEntrega) {
        this.lngEntrega = lngEntrega;
    }

    public Object getLatTransportista() {
        return latTransportista;
    }

    public void setLatTransportista(Object latTransportista) {
        this.latTransportista = latTransportista;
    }

    public Object getLngTransportista() {
        return lngTransportista;
    }

    public void setLngTransportista(Object lngTransportista) {
        this.lngTransportista = lngTransportista;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
