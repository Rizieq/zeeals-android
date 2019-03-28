package com.example.user.zeeals;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class lupaPasswordScreen extends AppCompatActivity {

    private Button btnResetPassword;
    private TextView textLogin;
    private TextView txtReset;
    private TextView txtPassword;
    private AutoCompleteTextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password_screen);

        txtPassword = (TextView) findViewById(R.id.textResetPassword1);
        txtReset = (TextView) findViewById(R.id.textResetPassword0);
        Typeface myCustFont = Typeface.createFromAsset(getAssets(),"fonts/Poppins-Bold.otf");
        txtPassword.setTypeface(myCustFont);
        txtReset.setTypeface(myCustFont);

        btnResetPassword = (Button) findViewById(R.id.btnReset);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(lupaPasswordScreen.this,"Cek Listener Berhasil",Toast.LENGTH_SHORT).show();
            }
        });

        textLogin = (TextView) findViewById(R.id.textloginReset);
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(lupaPasswordScreen.this,loginScreen.class));
                //finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,loginScreen.class));
    }
}
