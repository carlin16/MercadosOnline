package com.mercadoonline.tiendaclient.models.enviado;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class WhatsApp {
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("code")
    @Expose
    private String code;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
