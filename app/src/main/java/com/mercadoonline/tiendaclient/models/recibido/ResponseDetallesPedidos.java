
package com.mercadoonline.tiendaclient.models.recibido;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDetallesPedidos {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_usuario")
    @Expose
    private String idUsuario;
    @SerializedName("id_mercado")
    @Expose
    private String idMercado;
    @SerializedName("id_transportista")
    @Expose
    private String idTransportista;
    @SerializedName("costo_venta")
    @Expose
    private String costoVenta;
    @SerializedName("costo_envio")
    @Expose
    private String costoEnvio;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("forma_pago")
    @Expose
    private String formaPago;
    @SerializedName("fecha_registro")
    @Expose
    private String fechaRegistro;
    @SerializedName("fecha_actualiza")
    @Expose
    private Object fechaActualiza;
    @SerializedName("estado")
    @Expose
    private String estado;
    @SerializedName("mercado")
    @Expose
    private Mercado mercado;
    @SerializedName("cliente")
    @Expose
    private Cliente cliente;
    @SerializedName("transportista")
    @Expose
    private Transportista transportista = new Transportista();
    @SerializedName("entrega")
    @Expose
    private Entrega entrega;
    @SerializedName("detalles")
    @Expose
    private List<DetallesP> detallesPS = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdMercado() {
        return idMercado;
    }

    public void setIdMercado(String idMercado) {
        this.idMercado = idMercado;
    }

    public String getIdTransportista() {
        return idTransportista;
    }

    public void setIdTransportista(String idTransportista) {
        this.idTransportista = idTransportista;
    }

    public String getCostoVenta() {
        return costoVenta;
    }

    public void setCostoVenta(String costoVenta) {
        this.costoVenta = costoVenta;
    }

    public String getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(String costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Mercado getMercado() {
        return mercado;
    }

    public void setMercado(Mercado mercado) {
        this.mercado = mercado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Transportista getTransportista() {
        return transportista;
    }

    public void setTransportista(Transportista transportista) {
        this.transportista = transportista;
    }

    public Entrega getEntrega() {
        return entrega;
    }

    public void setEntrega(Entrega entrega) {
        this.entrega = entrega;
    }

    public List<DetallesP> getDetallesPS() {
        return detallesPS;
    }

    public void setDetallesPS(List<DetallesP> detallesPS) {
        this.detallesPS = detallesPS;
    }

}
