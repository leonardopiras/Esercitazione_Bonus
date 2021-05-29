package com.example.esercitazionebonus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ModificaPassword extends AppCompatActivity {

    TextView vecchiaPassword, username;
    AppSessione appSessione;
    EditText password, confermaPassword;
    String s_password, s_confermaPassword;
    Button conferma, indietro;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_password);
        appSessione = getIntent().getParcelableExtra("AppSessione");

        vecchiaPassword = findViewById(R.id.textViewPasswordVecchia);
        username = findViewById(R.id.textViewUsername);

        password = findViewById(R.id.textViewPassword);
        confermaPassword = findViewById(R.id.textViewPasswordConferma);

        conferma = findViewById(R.id.conferma);
        indietro = findViewById(R.id.indietro);

        username.setText(appSessione.actualUser.username);
        vecchiaPassword.setText(appSessione.actualUser.password);


        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_password = password.getText().toString();
                s_confermaPassword = confermaPassword.getText().toString();
                boolean errori = false;


                if (s_password.isEmpty() || s_password.equals("")) {
                    password.requestFocus();
                    password.setError("inserisci una password");
                    errori = true;
                }

                if (s_confermaPassword.isEmpty() || s_confermaPassword.equals("")) {
                    confermaPassword.requestFocus();
                    confermaPassword.setError("riscrivi la password");
                    errori = true;
                }

                if (!s_confermaPassword.equals(s_password)) {
                    confermaPassword.requestFocus();
                    confermaPassword.setError("la password non corrisponde");
                    errori = true;
                }

                if (s_password.equals(appSessione.actualUser.password)){
                    password.setError("scegli una password diversa dalla precedente");
                    password.requestFocus();
                    errori = true;
                }

                if (errori == false) {
                    appSessione.actualUser.password = s_password;
                    appSessione.getUser(appSessione.actualUser.username).password = s_password;
                    Intent showResult = new Intent(ModificaPassword.this, Home.class);
                    showResult.putExtra("AppSessione", appSessione);
                    showResult.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(showResult);
                } else {
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
                }
            }
        });

        indietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showResult = new Intent(ModificaPassword.this, Home.class);
                showResult.putExtra("AppSessione", appSessione);
                showResult.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(showResult);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent showResult = new Intent(ModificaPassword.this, Home.class);
        showResult.putExtra("AppSessione", appSessione);
        showResult.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(showResult);
    }
}