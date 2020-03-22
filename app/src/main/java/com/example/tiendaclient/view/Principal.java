package com.example.tiendaclient.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.tiendaclient.R;
import com.example.tiendaclient.view.fragments.mercado;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Contenedor_Fragments, new mercado()).commit();
    }
}
