
package com.mercadoonline.tiendaclient.models.enviado;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PeticionPedido {



    @SerializedName("id_vendedor")
    @Expose
    private Integer idVendedor;

    @SerializedName("tipo")
    @Expose
    private String tipo;

    @SerializedName("id_usuario")
    @Expose
    private Integer idUsuario;
    @SerializedName("id_mercado")
    @Expose
    private Integer idMercado;
    @SerializedName("id_transportista")
    @Expose
    private Integer idTransportista;
    @SerializedName("costo_venta")
    @Expose
    private Double costoVenta;
    @SerializedName("costo_envio")
    @Expose
    private Integer costoEnvio;
    @SerializedName("total")
    @Expose
    private Double total;
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
    @SerializedName("detalle")
    @Expose
    private List<Detalle> detalle = null;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdMercado() {
        return idMercado;
    }

    public void setIdMercado(Integer idMercado) {
        this.idMercado = idMercado;
    }

    public Integer getIdTransportista() {
        return idTransportista;
    }

    public void setIdTransportista(Integer idTransportista) {
        this.idTransportista = idTransportista;
    }

    public Double getCostoVenta() {
        return costoVenta;
    }

    public void setCostoVenta(Double costoVenta) {
        this.costoVenta = costoVenta;
    }

    public Integer getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(Integer costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
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

    public List<Detalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<Detalle> detalle) {
        this.detalle = detalle;
    }


    public Integer getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Integer idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
