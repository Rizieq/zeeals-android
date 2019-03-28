package com.example.user.zeeals;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.zeeals.model.ForgotPassword;
import com.example.user.zeeals.service.UserClient;
import com.example.user.zeeals.util.ServerAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class lupaPasswordScreen extends AppCompatActivity {

    private Button btnForgotPassword;
    private TextView zeealsForgot, textMasuk;
    private ProgressBar pbForgot;

    private AutoCompleteTextView etEmail;

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ServerAPI.zeealseRESTAPI)
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password_screen);

        /// THIS FOR SPAN "DAFTAR" WORD ///
        textMasuk = (TextView) findViewById(R.id.textayomasukForgot);
        String text = "Ayo masuk sekarang.";
        SpannableString daftar = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(lupaPasswordScreen.this, loginScreen.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.rgb(199, 149, 109));
                ds.setUnderlineText(false);
            }
        };
        daftar.setSpan(clickableSpan, 4, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textMasuk.setText(daftar);
        textMasuk.setMovementMethod(LinkMovementMethod.getInstance());

        /// CHANGE FONT TYPE ///
        Typeface poppinsBold = Typeface.createFromAsset(getAssets(),"fonts/Poppins-Bold.otf");
        zeealsForgot = (TextView) findViewById(R.id.txtZealsForgot);
        zeealsForgot.setTypeface(poppinsBold);

        /// BTN FORGOT / RESET WORKS ///
        btnForgotPassword = (Button) findViewById(R.id.btnForgot);
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(lupaPasswordScreen.this,"Cek Listener Berhasil",Toast.LENGTH_SHORT).show();
            }
        });

        /// ANOTHER INITIATIONS ///
        etEmail = (AutoCompleteTextView) findViewById(R.id.ETemailForgotInput);
        pbForgot = (ProgressBar) findViewById(R.id.forgot_progress);
        validateEmail();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,loginScreen.class));
    }

    /// VALIDATE TEXT FIELD REGISTRATION ///
    private void validateEmail() {
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                etEmail.setError("Field can't be empty");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    etEmail.setError(null);
                } else {
                    etEmail.setError("Please enter a valid email address");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void forgotPasswordProcess(){
        pbForgot.setVisibility(View.VISIBLE);
        btnForgotPassword.setVisibility(View.GONE);

        String email = etEmail.getText().toString().trim();

        final ForgotPassword forgotPassword = new ForgotPassword(email);

        Call<String> call = userClient.forgot(forgotPassword);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    pbForgot.setVisibility(View.INVISIBLE);
                    btnForgotPassword.setVisibility(View.VISIBLE);

                    Toast.makeText(lupaPasswordScreen.this, "Berhasil, Silahkan cek email anda", Toast.LENGTH_SHORT).show();

                }else
                    pbForgot.setVisibility(View.INVISIBLE);
                btnForgotPassword.setVisibility(View.VISIBLE);

                Toast.makeText(lupaPasswordScreen.this, "Gagal, Terjadi kesalahan", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pbForgot.setVisibility(View.INVISIBLE);
                btnForgotPassword.setVisibility(View.VISIBLE);

                Toast.makeText(lupaPasswordScreen.this, "Gagal, Terjadi kesalahan", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
