package com.example.esercitazionebonus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Registrazione extends AppCompatActivity {

    AppSessione appSessione;
    EditText username, password, confermaPassword, citta, dataNascita;
    String s_username, s_password, s_confermaPassword, s_citta, s_dataNascita;

    Button registrati, indietro;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);
        appSessione = getIntent().getParcelableExtra("AppSessione");

        username = findViewById(R.id.textViewUsername);
        password = findViewById(R.id.textViewPassword);
        confermaPassword = findViewById(R.id.textViewPasswordConferma);
        citta = findViewById(R.id.textViewCitta);
        dataNascita = findViewById(R.id.textViewDataNascita);

        registrati = findViewById(R.id.registrati);
        indietro = findViewById(R.id.indietro);

        s_dataNascita = "";


        dataNascita.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    DatePickerFragment.newInstance().show(getSupportFragmentManager(), "dialog");
            }
        });



        registrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_username = username.getText().toString();
                s_password = password.getText().toString();
                s_confermaPassword = confermaPassword.getText().toString();
                s_citta = citta.getText().toString();
                boolean errori = false;

                if (s_dataNascita.equals("")) {
                    dataNascita.setError("Scegli una data");
                    dataNascita.requestFocus();
                    errori = true;
                }

                if (s_username.isEmpty() || s_username.equals("")) {
                    username.setError("inserisci uno username");
                    username.requestFocus();
                    errori = true;
                } else {
                    for(User us : appSessione.users) {
                        if (us.username.equals(s_username)){
                            errori = true;
                            username.requestFocus();
                            username.setError("username scelto non disponibile");
                        }
                    }
                }

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

                if (s_citta.isEmpty() || s_citta.equals("")) {
                    citta.requestFocus();
                    citta.setError("inserisci una città");
                    errori = true;
                }

                if (errori == false) {
                    User us = new User(s_username, s_password, s_citta, s_dataNascita, false);
                    appSessione.actualUser = us;
                    appSessione.users.add(us);
                    Intent showResult = new Intent(Registrazione.this, Home.class);
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
                Intent showResult = new Intent(Registrazione.this, Login.class);
                showResult.putExtra("AppSessione", appSessione);
                showResult.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(showResult);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (indietro != null)
            indietro.performClick();
        else super.onBackPressed();
    }

    public void doPositiveClick(Calendar date){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/YY");
        dataNascita.setText(format.format(date.getTime()));
        if (date.after(Calendar.getInstance()) || date.equals(Calendar.getInstance())) {
            dataNascita.setError("data di nascita errata");
            dataNascita.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerFragment.newInstance().show(getSupportFragmentManager(), "dialog");
                }
            });
        } else {
            dataNascita.setError(null);
        }
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(dataNascita.getApplicationWindowToken(),0);
        s_dataNascita = date.get(Calendar.DAY_OF_MONTH)+"\\"+(date.get(Calendar.MONTH)+1)+"\\"+date.get(Calendar.YEAR);
    }



}