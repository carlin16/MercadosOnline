package com.mercadoonline.tiendaclient.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.mercadoonline.tiendaclient.R;
import com.mercadoonline.tiendaclient.view.fragments.agregar_productos;
import com.mercadoonline.tiendaclient.view.fragments.categorias;
import com.mercadoonline.tiendaclient.view.fragments.registro_datos_usuario;

public class RegistroUsuarios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuarios);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        getSupportFragmentManager().beginTransaction()
                 .replace(R.id.Contenedor_Registro, new registro_datos_usuario()).commit();//aqui puedo instanciar un nuevo fragment
                    //.replace(R.id.Contenedor_Registro, new agregar_productos()).commit();//aqui puedo instanciar un nuevo fragment

    }
}
