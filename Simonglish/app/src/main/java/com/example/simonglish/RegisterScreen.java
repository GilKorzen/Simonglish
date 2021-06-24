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

public class RegisterScreen extends AppCompatActivity {
    EditText email, username, password, confirmPassword;
    Button submit;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        username=findViewById(R.id.register_username);
        password=findViewById(R.id.register_password);
        confirmPassword=findViewById(R.id.register_passwordConfirmation);
        email=findViewById(R.id.register_email);


        submit=findViewById(R.id.submit_register);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(password.getText().toString().equals(confirmPassword.getText().toString()))) {
                    Toast.makeText(RegisterScreen.this, "The passwords don't match", Toast.LENGTH_LONG).show();
                }
                else if (email.getText().toString().length()<6 || countOfAt(email.getText().toString())!=1 || email.getText().charAt(0)=='@' || email.getText().charAt(email.getText().toString().length()-1)=='@')
                    Toast.makeText(RegisterScreen.this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
                else if (username.getText().toString().length()<3)
                    Toast.makeText(RegisterScreen.this, "Usernsme must contain 3 digits or more", Toast.LENGTH_LONG).show();
                else if (dataSending()) {
                    startActivity(new Intent(RegisterScreen.this, LoginScreen.class));
                    finish();
                }
                else
                    Toast.makeText(RegisterScreen.this,"your email/username is already is use",Toast.LENGTH_LONG).show();
            }
        });
        back=findViewById(R.id.returnToStart2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterScreen.this,FirstScreen.class));
                finish();
            }
        });

    }
    private boolean dataSending() {
        //connects to the server and checks if the register succeeded

        JSONObject sendingData = new JSONObject();

        try {
            sendingData.put("request","register");
            sendingData.put("email",email.getText());
            sendingData.put("password",password.getText());
            sendingData.put("username",username.getText());
            SocketStuff dataTransportTask = new SocketStuff(sendingData);
            System.out.println(sendingData);
            JSONObject received = dataTransportTask.execute().get();
            if (received.getString("response").equals("register succeeded"))
                return true;
            Log.d("db", received.toString());
            return false;

        } catch (JSONException | InterruptedException | ExecutionException e) {
            Log.e("Exception", e.toString());
            return false;
        }
    }

    private int countOfAt(String email)
    {
        //returns the amount of @ in the given String
        int count=0;
        for (int i=0;i<email.length();i++)
        {
            if (email.charAt(i)=='@')
                count++;
        }
        return count;

    }
}
