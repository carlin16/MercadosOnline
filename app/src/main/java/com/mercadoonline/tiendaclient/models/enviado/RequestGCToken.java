
package com.mercadoonline.tiendaclient.models.enviado;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestGCToken {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("id_usuario")
    @Expose
    private Integer idUsuario;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

}
