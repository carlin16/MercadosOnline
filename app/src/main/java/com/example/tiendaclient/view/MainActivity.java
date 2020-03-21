package com.example.tiendaclient.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.tiendaclient.R;
import com.google.android.material.snackbar.Snackbar;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.regex.Pattern;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import in.anshul.libray.PasswordEditText;

public class MainActivity extends AppCompatActivity {
    EditText Nombres, Apellidos, Direccion, Telefono,  Email;
    PasswordEditText Pass, RePass;
    Spinner Rol, TipoTienda;
    CountryCodePicker codigo_pais;
    CircularProgressButton BtnRegistrar;
    Uri  imagen_perfil;
    LinearLayout ConteTipoTienda;

    String numeroTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UI();
        Click();
    }

    private  void UI(){
        Nombres=findViewById(R.id.registro_nombre);
        Apellidos=findViewById(R.id.registro_apellido);
        Direccion=findViewById(R.id.registro_direccion);
        Telefono=findViewById(R.id.telefono_registro);
        Email=findViewById(R.id.registro_email);
        Pass=findViewById(R.id.registro_pass);
        RePass=findViewById(R.id.registro_repass);
        Rol=findViewById(R.id.spn_rolUser);
        TipoTienda=findViewById(R.id.spn_tipoTienda);
        BtnRegistrar=findViewById(R.id.btn_registro);
        codigo_pais = findViewById(R.id.ccp);
        ConteTipoTienda=findViewById(R.id.Contenedor_TipoTienda);


    }

    private void Click(){
        BtnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar_campos();
                Log.e("clic", "se dio clic");
            }
        });
        Rol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1){
                    ConteTipoTienda.setVisibility(View.VISIBLE);
                    Log.e("click","visible");

                }else {
                    ConteTipoTienda.setVisibility(View.GONE);
                    Log.e("click","oculto");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private Boolean verificar_vacio(String texto) {


        //Todo retorno true si esta vacio
        if (TextUtils.isEmpty(texto)) {
            Snackbar.make(findViewById(android.R.id.content), "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
            return true;
        }


        return false;
    }
    private void validar_campos(){
        if(verificar_vacio(Nombres.getText().toString())) Nombres.requestFocus();
        else if(verificar_vacio(Apellidos.getText().toString())) Apellidos.requestFocus();
        else if(verificar_vacio(Direccion.getText().toString())) Direccion.requestFocus();
        else if (verificar_vacio(Telefono.getText().toString())) Telefono.requestFocus();
        else if (verificar_vacio(Email.getText().toString())) Email.requestFocus();
        else if(validarEmail(Email.getText().toString()));
        else if(verificar_vacio(Pass.getText().toString())) Pass.requestFocus();
        else if(tamaño_texto(Pass) );


        else if(verificar_vacio(RePass.getText().toString())) RePass.requestFocus();

        else if(!Pass.getText().toString().equals(RePass.getText().toString())){
            Snackbar.make(findViewById(android.R.id.content), "Las contraseñas no coinciden", Snackbar.LENGTH_LONG).show();
        }
        else if (imagen_perfil != null) mensaje();//dejar en ==null
        else if (Telefono.getText().toString().substring(0, 1).equals("0")) {
            numeroTelefono = codigo_pais.getSelectedCountryCode() + Telefono.getText().toString().trim().substring(1);
        } else {
            numeroTelefono = codigo_pais.getSelectedCountryCode() + Telefono.getText().toString().trim();

        }






    }

    private void mensaje() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡OH!");


        builder.setMessage("No ha puesto foto de perfil");
        builder.setPositiveButton("Entiendo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();

    }
    private Boolean tamaño_texto( EditText texto){


        if (texto.getText().toString().length()<8) {

            texto.setError("Contraseña corta");
            return true;
        }
        return false;
    }
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }




}
