package com.example.simonglish;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class LoginScreen extends AppCompatActivity {
    EditText email, password;
    Intent intent;
    Button submit;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        password=findViewById(R.id.login_password);
        email=findViewById(R.id.login_email);
        submit=findViewById(R.id.submit_login);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataSending()) {
                    intent = new Intent(LoginScreen.this, MainMenu.class);
                    intent.putExtra("email",email.getText().toString());
                    startActivity(intent);
                    finish();
                }

                else
                    Toast.makeText(LoginScreen.this,"The email or the password are incorrect",Toast.LENGTH_LONG).show();
            }
        });

        back=findViewById(R.id.returnToStart);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginScreen.this,FirstScreen.class));
                finish();
            }
        });



    }
    private boolean dataSending() {
        //connects to the server and checks if the login succeeded

        JSONObject sendingData = new JSONObject();

        try {
            sendingData.put("request","login");
            sendingData.put("email",email.getText());
            sendingData.put("password",password.getText());
            SocketStuff dataTransportTask = new SocketStuff(sendingData);
            System.out.println(sendingData);
            JSONObject received = dataTransportTask.execute().get();
            if (received.getString("response").equals("login succeeded"))
                return true;
            Log.d("db", received.toString());
            return false;

        } catch (JSONException | InterruptedException | ExecutionException e) {
            Log.e("Exception", e.toString());
            return false;
        }
    }
}
