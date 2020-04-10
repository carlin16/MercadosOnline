package com.example.tiendaclient.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.tiendaclient.R;
import com.example.tiendaclient.view.fragments.registro_completar;
import com.example.tiendaclient.view.fragments.registro_datos_usuario;

public class RegistroUsuarios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuarios);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Contenedor_Registro, new registro_datos_usuario()).commit();//aqui puedo instanciar un nuevo fragment
                    //.replace(R.id.Contenedor_Registro, new registro_completar()).commit();//aqui puedo instanciar un nuevo fragment

    }
}
