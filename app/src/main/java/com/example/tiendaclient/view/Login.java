package com.example.tiendaclient.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.example.tiendaclient.R;

public class Login extends AppCompatActivity {
    Animation Rebote;
    ImageButton Flecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UiAnima();
        UI();
    }
    private void UiAnima(){
        Rebote = AnimationUtils.loadAnimation(this, R.anim.rebote);


    }
    private  void UI(){
        Flecha=findViewById(R.id.flecha_registro);
        Flecha.startAnimation(Rebote);
    }
}
