package com.example.tiendaclient.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.tiendaclient.R;
import com.example.tiendaclient.view.fragments.registro_datos_usuario;
import com.example.tiendaclient.view.fragments.resgistro_completar;

public class RegistroUsuarios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuarios);
        getSupportFragmentManager().beginTransaction()
                //.replace(R.id.Contenedor_Registro, new registro_datos_usuario()).commit();//aqui puedo instanciar un nuevo fragment
                       .replace(R.id.Contenedor_Registro, new resgistro_completar()).commit();//aqui puedo instanciar un nuevo fragment

    }
}
