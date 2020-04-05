package com.example.tiendaclient.view.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tiendaclient.R;
import com.example.tiendaclient.utils.Global;
import com.example.tiendaclient.view.Login;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import in.anshul.libray.PasswordEditText;

import static com.example.tiendaclient.utils.Global.RegisU;
import static com.example.tiendaclient.utils.Global.verificar_vacio;

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
    TextView IrLogin;
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
        IrLogin=vista.findViewById(R.id.ir_login);

        TINom=vista.findViewById(R.id.TINombre2);
        TIApell=vista.findViewById(R.id.TIApellido2);
        TIEma=vista.findViewById(R.id.TIEmail);
        TITel=vista.findViewById(R.id.TITelefono);
        TIUsua=vista.findViewById(R.id.TIUsuario);
        TIPassw=vista.findViewById(R.id.TIPass);
        TIRePass=vista.findViewById(R.id.TIRepass);

        Nombres.requestFocus();
        TINom.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));

    }

    private void CLick(){
        IrLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        BtnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar_campos();
                Log.e("boton registar", "se dio clic ");
            }
        });
        //Mejora en foco del TexImput
        TIApell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Apellidos.requestFocus();
            }
        });
        TINom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Nombres.requestFocus();
            }
        });
        TITel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Telefono.requestFocus();
            }
        });
        TIUsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario.requestFocus();
            }
        });
        TIPassw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pass.requestFocus();
            }
        });
        TIRePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RePass.requestFocus();
            }
        });


        //validaciones para que al seleccionar campo, el texview cambien de color
        Nombres.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    TINom.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TINom.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
        });
        Apellidos.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    TIApell.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TIApell.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
        });
        Telefono.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    TITel.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TITel.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
        });
        Email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    TIEma.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TIEma.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
        });
        Pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    TIPassw.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TIPassw.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
        });
        RePass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    TIRePass.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TIRePass.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
        });
        Usuario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    TIUsua.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TIUsua.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
        });
        //FIN validaciones para que al seleccionar campo, el texview cambien de color

    }

    private void validar_campos(){
        Log.e("VC", "estoy en validar campos ");
        if(verificar_vacio(Nombres.getText().toString())) {
            Nombres.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
        }
        else if(verificar_vacio(Apellidos.getText().toString())) {
            Apellidos.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
        }
        else if (verificar_vacio(Telefono.getText().toString())) {
            Telefono.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
        }
        else if (verificar_vacio(Email.getText().toString())) {
            Email.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
        }
        else if (verificar_vacio(Usuario.getText().toString())) {
            Usuario.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
        }
        else if(verificar_vacio(Pass.getText().toString())) {
            Pass.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
        }
        else if(tamano_texto(Pass) );

        else if(verificar_vacio(RePass.getText().toString())) RePass.requestFocus();
        else if(!Pass.getText().toString().equals(RePass.getText().toString())){
            Snackbar.make(vista, "Las contraseñas no coinciden", Snackbar.LENGTH_LONG).show();
        }
        else if (Telefono.getText().toString().substring(0, 1).equals("0")) {
            numeroTelefono = codigo_pais.getSelectedCountryCode() + Telefono.getText().toString().trim().substring(1);
            llenarDatos();
        } else {
            numeroTelefono = codigo_pais.getSelectedCountryCode() + Telefono.getText().toString().trim();
            llenarDatos();
        }

    }
    private Boolean tamano_texto( EditText texto){


        if (texto.getText().toString().length()<8) {
            Snackbar.make(vista, "Contraseña muy corta, minimo 8 digitos", Snackbar.LENGTH_LONG).show();
            //texto.setError("Contraseña corta");//esto creo q hacia caer..comente y ya no pasa
            return true;
        }
        return false;
    }
    public  void llenarDatos(){

        RegisU.setNombres(Nombres.getText().toString());
        RegisU.setApellidos(Apellidos.getText().toString());
        RegisU.setCelular(numeroTelefono);
        RegisU.setEmail(Email.getText().toString());
        RegisU.setUsuario(Usuario.getText().toString());
        RegisU.setPassword(Pass.getText().toString());
        Log.e("Llenar dts", "Se llenaron los datos en Global "+ Global.convertObjToString(RegisU));
        siguiente_paantalla();

       // Gson gson = new Gson();
       // String JPetUser= gson.toJson(User);
     //   Log.e("json",JPetUser);
      //  animacion_registro();
        //peticion_Registro(JPetUser);
    }

    private  void siguiente_paantalla(){
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.Contenedor_Registro, new registro_completar()).addToBackStack(null);
        fragmentTransaction.commit();

    }


}
