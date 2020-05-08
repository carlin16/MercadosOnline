
package com.mercadoonline.tiendaclient.models.enviado;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PeticionLoginUser {

    @SerializedName("usuario")
    @Expose
    private String usuario;
    @SerializedName("password")
    @Expose
    private String password;

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

}