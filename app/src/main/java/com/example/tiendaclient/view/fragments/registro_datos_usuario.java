package com.example.tiendaclient.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tiendaclient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class registro_datos_usuario extends Fragment {

    public registro_datos_usuario() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registro_datos_usuario, container, false);
    }
}