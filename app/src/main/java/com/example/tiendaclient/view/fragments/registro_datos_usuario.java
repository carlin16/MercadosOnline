package com.example.tiendaclient.view.fragments;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.tiendaclient.R;
import com.google.android.material.textfield.TextInputLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import in.anshul.libray.PasswordEditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class registro_datos_usuario extends Fragment {

    public registro_datos_usuario() {
        // Required empty public constructor


    }

   View vista;
    EditText Nombres, Apellidos, Direccion, Telefono,  Email,Usuario;
    TextInputLayout TINom, TIApell, TIDir, TITel, TIEma, TIUsua, TIPassw, TIRePass ;
    Dialog myDialog;
    PasswordEditText Pass, RePass;
    String[] Roles;
    int posicionRol=0;
    Boolean cambio=false;
    String mensaje="";
    CountryCodePicker codigo_pais;
    CircularProgressButton BtnRegistrar ,datos,fotos,credenciales;

    String numeroTelefono;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_registro_datos_usuario, container, false);
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // vista.findViewById();
        UI();
        CLick();
    }

    public void UI(){
        Usuario=vista.findViewById(R.id.registro_usuario);
        Nombres=vista.findViewById(R.id.registro_nombre);
        Apellidos=vista.findViewById(R.id.registro_apellido);
        Telefono=vista.findViewById(R.id.telefono_registro);
        Email=vista.findViewById(R.id.registro_email);
        Pass=vista.findViewById(R.id.registro_pass);
        RePass=vista.findViewById(R.id.registro_repass);
        BtnRegistrar=vista.findViewById(R.id.btn_registro);
        codigo_pais = vista.findViewById(R.id.ccp);
        ///
        Nombres.requestFocus();
        ///

        TINom=vista.findViewById(R.id.TINombre);
        TIApell=vista.findViewById(R.id.TIApellido);
        TIEma=vista.findViewById(R.id.TIEmail);
        TITel=vista.findViewById(R.id.TITelefono);
        TIUsua=vista.findViewById(R.id.TIUsuario);
        TIPassw=vista.findViewById(R.id.TIPass);
        TIRePass=vista.findViewById(R.id.TIRepass);

    }

    private void CLick(){
        Nombres.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    TINom.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TINom.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                    //
                }
            }
        });

    }

}
