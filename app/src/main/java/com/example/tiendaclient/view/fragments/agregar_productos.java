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
public class agregar_productos extends Fragment {

    public agregar_productos() {
        // Required empty public constructor
    }

View vista;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_agregar_productos, container, false);
        return vista;
    }
}
