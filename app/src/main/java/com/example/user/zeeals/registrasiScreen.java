package com.example.user.zeeals;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class registrasiScreen extends AppCompatActivity {
    private TextView login;
    private Button btnDaftar;
    private TextView daftarFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi_screen);

        login = (TextView) findViewById(R.id.textlogin);
        btnDaftar = (Button) findViewById(R.id.btnDaftar);
        daftarFont = (TextView) findViewById(R.id.textDaftarFont);
        Typeface customFont = Typeface.createFromAsset(getAssets(),"fonts/Poppins-Bold.otf");
        daftarFont.setTypeface(customFont);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registrasiScreen.this, loginScreen.class));
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(registrasiScreen.this,"Cek Listener berhasil",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(registrasiScreen.this);

        builder.setTitle("Kembali ke halaman Login")
                .setMessage("Apakah anda yakin? ")
                .setPositiveButton("Oke", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(registrasiScreen.this,loginScreen.class));
                    }
                })
                .setNegativeButton("Tidak", null).setCancelable(false);
        AlertDialog alertOut = builder.create();
        alertOut.show();
    }
}
