package com.example.esercitazionebonus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {

    AppSessione appSessione;
    Button gestisciUtenti, logout, modificaPassword;
    TextView welcomeText, activityTitle;
    Chip chipAdmin;
    CircleImageView profilePhoto;

    TextView username, password, citta, dataNascita;

    Uri imageUri;


    private static final int PICK_IMAGE = 1;
    private static final int SHOOT_PHOTO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        appSessione = getIntent().getParcelableExtra("AppSessione");

        gestisciUtenti = findViewById(R.id.usersManagmentButton);
        logout = findViewById(R.id.logoutButton);
        modificaPassword = findViewById(R.id.changePasswordButton);
        welcomeText = findViewById(R.id.welcomeText);
        activityTitle = findViewById(R.id.activityTitle);
        chipAdmin = findViewById(R.id.chipAdmin);
        profilePhoto = findViewById(R.id.profilePhoto);

        username = findViewById(R.id.textViewUsername);
        password = findViewById(R.id.textViewPassword);
        citta = findViewById(R.id.textViewCitta);
        dataNascita = findViewById(R.id.textViewDataNascita);

        welcomeText.setText(appSessione.actualUser.username);
        username.setText(appSessione.actualUser.username);
        password.setText(appSessione.actualUser.password);
        citta.setText(appSessione.actualUser.city);
        dataNascita.setText(appSessione.actualUser.birthday);


        if (appSessione.actualUser.isAdmin) {
            gestisciUtenti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent showResult = new Intent(Home.this, GestisciUtenti.class);
                    showResult.putExtra("AppSessione", appSessione);
                    showResult.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(showResult);
                }
            });
        } else {
            chipAdmin.setVisibility(View.GONE);
            gestisciUtenti.setVisibility(View.GONE);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appSessione.actualUser = null;
                Intent showResult = new Intent(Home.this, Login.class);
                showResult.putExtra("AppSessione", appSessione);
                showResult.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(showResult);
            }
        });

        modificaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showResult = new Intent(Home.this, ModificaPassword.class);
                showResult.putExtra("AppSessione", appSessione);
                showResult.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(showResult);
            }
        });

        if (appSessione.actualUser.photoUri.isEmpty())
            profilePhoto.setImageResource(R.drawable.aggiorna_foto_profilo);
        else
            profilePhoto.setImageURI(Uri.parse(appSessione.actualUser.photoUri));
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    @Override
    public void onBackPressed() {
        logout.performClick();
    }

    public void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setTitle("Add image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (items[i].equals("Gallery")){
                    Intent gallery = new Intent();
                    gallery.setType("image/*");
                    gallery.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(gallery, "Select picture"), PICK_IMAGE);

                } else if (items[i].equals("Camera")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, SHOOT_PHOTO);
                }
            }
        });
        AlertDialog alertDialogObject = builder.create();

        ListView listView=alertDialogObject.getListView();
        //listView.setDivider(ResourcesCompat.getDrawable(getResources(), R.., null)); // set color
        listView.setDividerHeight(2); // set height
        alertDialogObject.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (resultCode == RESULT_OK && requestCode != SHOOT_PHOTO) {
                appSessione = (AppSessione) getIntent().getParcelableExtra("AppSessione");
            }
        }

        if(requestCode == SHOOT_PHOTO){
            assert data != null;
            bitmap = (Bitmap)data.getExtras().get("data");
        }

        if (bitmap != null) { // Se abbiamo aggiunto una foto
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("profilePhotos", Context.MODE_PRIVATE);
            File pngImageInMemory = new File(directory, appSessione.actualUser + ".png");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(pngImageInMemory);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            appSessione.actualUser.photoUri = pngImageInMemory.toString();
            appSessione.getUser(appSessione.actualUser.username).photoUri = pngImageInMemory.toString();

            profilePhoto.setImageURI(Uri.parse(appSessione.actualUser.photoUri));
        } else {
            int i = 5;
            // Se si decide di non scattare nessuna foto
        }

    }

}