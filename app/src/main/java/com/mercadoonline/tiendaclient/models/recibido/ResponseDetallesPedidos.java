
package com.mercadoonline.tiendaclient.models.recibido;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mercadoonline.tiendaclient.models.enviado.Detalle;

public class ResponseDetallesPedidos {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_usuario")
    @Expose
    private Integer idUsuario;
    @SerializedName("id_establecimiento")
    @Expose
    private Integer idEstablecimiento;
    @SerializedName("id_transportista")
    @Expose
    private Object idTransportista;
    @SerializedName("costo_venta")
    @Expose
    private Double costoVenta;
    @SerializedName("costo_envio")
    @Expose
    private Integer costoEnvio;
    @SerializedName("total")
    @Expose
    private Double total;
    @SerializedName("forma_pago")
    @Expose
    private String formaPago;
    @SerializedName("fecha_registro")
    @Expose
    private String fechaRegistro;
    @SerializedName("fecha_actualiza")
    @Expose
    private Object fechaActualiza;
    @SerializedName("tipo")
    @Expose
    private String tipo;
    @SerializedName("estado")
    @Expose
    private String estado;
    @SerializedName("mercado")
    @Expose
    private Mercado mercado;
    @SerializedName("negocio")
    @Expose
    private Negocio negocio;
    @SerializedName("cliente")
    @Expose
    private Cliente cliente;
    @SerializedName("transportista")
    @Expose
    private Transportista transportista;
    @SerializedName("entrega")
    @Expose
    private Entrega entrega;
    @SerializedName("detalles")
    @Expose
    private List<DetallesP> detalles = null;

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

    public Integer getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(Integer idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public Object getIdTransportista() {
        return idTransportista;
    }

    public void setIdTransportista(Object idTransportista) {
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

    public List<DetallesP> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallesP> detalles) {
        this.detalles = detalles;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public Negocio getNegocio() {
        return negocio;
    }

    public void setNegocio(Negocio negocio) {
        this.negocio = negocio;
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


}
