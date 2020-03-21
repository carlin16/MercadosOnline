package com.example.tiendaclient.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tiendaclient.R;

public class Login extends AppCompatActivity {
    Animation Rebote;
    ImageButton Flecha;
    TextView Registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UiAnima();
        UI();
        Click();
    }
    private void UiAnima(){
        Rebote = AnimationUtils.loadAnimation(this, R.anim.rebote);


    }
    private  void UI(){
        Flecha=findViewById(R.id.flecha_registro);
        Flecha.startAnimation(Rebote);

        Registro=findViewById(R.id.registrarme_login);



    }


    private void Click(){
        Registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), RegistroUser.class);
                startActivityForResult(intent, 0);
                finish();
            }
        });

    }
}
