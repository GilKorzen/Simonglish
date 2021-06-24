package com.example.simonglish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class DifficultyDecider extends AppCompatActivity {
    Button easy, normal, hard;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_select);

        easy=findViewById(R.id.easy);
        normal=findViewById(R.id.normal);
        hard=findViewById(R.id.hard);
        back=findViewById(R.id.returnToStart);


        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(DifficultyDecider.this, MainEasyGame.class);
                intent.putExtra("email",getIntent().getExtras().getString("email"));
                startActivity(intent);
                stopService(new Intent(DifficultyDecider.this,PlayService.class));
                finish();
            }
        });
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(DifficultyDecider.this, MainNormalGame.class);
                intent.putExtra("email",getIntent().getExtras().getString("email"));
                startActivity(intent);
                stopService(new Intent(DifficultyDecider.this,PlayService.class));
                finish();
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(DifficultyDecider.this, MainHardGame.class);
                intent.putExtra("email",getIntent().getExtras().getString("email"));
                startActivity(intent);
                stopService(new Intent(DifficultyDecider.this,PlayService.class));
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(DifficultyDecider.this, MainMenu.class);
                intent.putExtra("email",getIntent().getExtras().getString("email"));
                startActivity(intent);
            }
        });

    }
}