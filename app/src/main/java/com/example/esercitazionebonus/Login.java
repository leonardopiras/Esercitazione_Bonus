package com.example.esercitazionebonus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    AppSessione appSessione;
    EditText usernameEditText, passwordEditText;
    Button registerButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        appSessione = getAppSessione();

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showResult = new Intent(Login.this, Registrazione.class);
                showResult.putExtra("AppSessione", appSessione);
                showResult.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(showResult);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();


                if (appSessione.getUser(username) != null) {
                    if (appSessione.getUser(username).password.equals(password)) {
                        appSessione.actualUser = appSessione.getUser(username);
                        Intent showResult = new Intent(Login.this, Home.class);
                        showResult.putExtra("AppSessione", appSessione);
                        showResult.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(showResult);
                    } else {
                        if (password.isEmpty()) {
                            passwordEditText.setError("Campo password vuoto");
                            passwordEditText.requestFocus();
                        }
                        else{
                            passwordEditText.setError("Password sbagliata");
                            passwordEditText.requestFocus();
                        }
                    }
                } else {
                    if (username.isEmpty()){
                        usernameEditText.setError("Campo username vuoto");
                        usernameEditText.requestFocus();
                    }
                    else{
                        usernameEditText.setError("Username non trovato");
                        usernameEditText.requestFocus();
                    }


                }
            }
        });
    }



    public AppSessione getAppSessione() {
        if (getIntent().getParcelableExtra("AppSessione") == null){
            return new AppSessione();
        } else {
            return getIntent().getParcelableExtra("AppSessione");
        }
    }
}