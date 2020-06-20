package com.mercadoonline.tiendaclient.models.compra;

import com.mercadoonline.tiendaclient.models.recibido.Productos;

import java.util.ArrayList;
import java.util.List;

public class PromosProductos {
    public String getNombrePromo() {
        return nombrePromo;
    }

    public void setNombrePromo(String nombrePromo) {
        this.nombrePromo = nombrePromo;
    }

    public List<Productos> getLst_products() {
        return lst_products;
    }

    public void setLst_products(List<Productos> lst_products) {
        this.lst_products = lst_products;
    }

    String nombrePromo;
    List<Productos> lst_products= new ArrayList<>();
}
