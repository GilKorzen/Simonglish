package com.example.simonglish;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FirstScreen extends  AppCompatActivity {
    Button toLogin, toRegister, toGame;
    Intent serviceIntent, intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        startService(new Intent(FirstScreen.this,PlayService.class));



        toLogin = findViewById(R.id.log_in);
        toRegister = findViewById(R.id.register);
        toGame = findViewById(R.id.guest);

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    startActivity(new Intent(FirstScreen.this, LoginScreen.class));


                } catch (Exception e) {
                    Log.d("", e.toString());
                }
            }
        });
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(FirstScreen.this, RegisterScreen.class));
                } catch (Exception e) {
                    Log.d("", e.toString());
                }
            }
        });
        toGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    intent=new Intent(FirstScreen.this, MainMenu.class);
                    intent.putExtra("email","");
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d("", e.toString());
                }
            }
        });
    }

}

