package com.example.user.zeeals;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.zeeals.model.Message;
import com.example.user.zeeals.model.Registration;
import com.example.user.zeeals.service.UserClient;
import com.example.user.zeeals.util.AppController;
import com.example.user.zeeals.util.PasswordPattern;
import com.example.user.zeeals.util.ServerAPI;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class registrasiScreen extends AppCompatActivity {
//    private static final Pattern PASSWORD_PATTERN =
//            Pattern.compile("^" +
//                    //"(?=.*[0-9])" +         //at least 1 digit
//                    //"(?=.*[a-z])" +         //at least 1 lower case letter
//                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
//                    "(?=.*[a-zA-Z])" +      //any letter
//                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
//                    "(?=\\S+$)" +           //no white spaces
//                    ".{6,}" +               //at least 6 characters
//                    "$");

    private TextView textMasuk, zeealsregist;
    private AutoCompleteTextView etEmail;
    private EditText etPassword, etConfPassword;
    private ProgressBar pbRegist;
    private Button btnDaftar;
    private static final String TAG = "registrasiScreen";
    Dialog popUpRegistrasi;

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ServerAPI.zeealseRESTAPI)
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi_screen);

        textMasuk = (TextView) findViewById(R.id.textayomasuk);
        zeealsregist = (TextView) findViewById(R.id.txtZealsRegist);
        btnDaftar = (Button) findViewById(R.id.btnDaftar);
        Typeface poppinsBold = Typeface.createFromAsset(getAssets(), "fonts/Poppins-Bold.otf");
        Typeface poppinsRegular = Typeface.createFromAsset(getAssets(), "fonts/Poppins-Regular.otf");
        zeealsregist.setTypeface(poppinsBold);

        String text = "Ayo masuk sekarang.";
        SpannableString daftar = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(registrasiScreen.this, loginScreen.class));
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

        pbRegist = (ProgressBar) findViewById(R.id.regist_progress);
        etEmail = (AutoCompleteTextView) findViewById(R.id.ETemailRegistInput);
        etPassword = (EditText) findViewById(R.id.ETpasswordRegistInput);
        etConfPassword = (EditText) findViewById(R.id.ETconfirmpasswordRegistrasiInput);

        btnDaftar.setEnabled(false);

        validateEmailPasswordConfirm();

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationProcess();
            }
        });
        popUpRegistrasi = new Dialog(this);
        popUpRegistrasi.setCancelable(false);


    }

    /// VALIDATE TEXT FIELD REGISTRATION ///
    private void validateEmailPasswordConfirm() {
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

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().isEmpty()) {
                    etPassword.setError("Field can't be empty");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() < 6) {
                    etPassword.setError("Password need a least 6 characters");
                } else {
                    etPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etConfPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = etPassword.getText().toString().trim();
                if (s.toString().equals(password)) {
                    etConfPassword.setError(null);
                    btnDaftar.setEnabled(true);
                } else {
                    etConfPassword.setError("Password not match");
                    btnDaftar.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void registrationProcess(){
        pbRegist.setVisibility(View.VISIBLE);
        btnDaftar.setVisibility(View.INVISIBLE);

        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String confirmPassword = etConfPassword.getText().toString().trim();
        final String device = "$2y$10$Ty5GIOshIus/y18kZtDR3O6V9gKPk/Rhhv40zyMYVNeOKhC0QLzz6";


        final Registration registration = new Registration(email,password,confirmPassword,device);

        Call<Message> call = userClient.registration(registration);

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, retrofit2.Response<Message> response) {
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: "+response.isSuccessful());
                    Toast.makeText(registrasiScreen.this,"Link verifikasi telah terkirim. Silahkan cek email" , Toast.LENGTH_LONG).show();
                    showPopUpRegistrasiBerhasil();
                    pbRegist.setVisibility(View.GONE);
                    btnDaftar.setVisibility(View.VISIBLE);
                }
                else {
                    Log.d(TAG, "onResponse: "+response.isSuccessful()+": Check Email & Password Format");
                    String pesan = "Registrasi yang dilakukan gagal. Email yang didaftarkan salah atau sudah pernah didaftarkan sebelumnya.";
                    showPopUpRegistrasiGagal(pesan);
                    pbRegist.setVisibility(View.GONE);
                    btnDaftar.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.d(TAG, "onResponse: "+t.toString());
                String pesan = "Koneksi anda sedang dalam masalah. pastikan koneksi anda terhubung dengan baik. silahkan coba lagi.";
                showPopUpRegistrasiGagal(pesan);
                pbRegist.setVisibility(View.GONE);
                btnDaftar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(registrasiScreen.this);

        builder.setTitle("Kembali ke halaman Login")
                .setMessage("Apakah anda yakin ingin kembali ke halaman login? ")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(registrasiScreen.this, loginScreen.class));
                    }
                })
                .setNegativeButton("Tidak", null).setCancelable(false);
        AlertDialog alertOut = builder.create();
        alertOut.show();
    }

    public void connectionError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(registrasiScreen.this);

        builder.setTitle("Koneksi Bermasalah")
                .setMessage("Koneksi bermasalah, silahkan cek kembali koneksi anda")
                .setNeutralButton("Coba Lagi",null);
        AlertDialog alertOut = builder.create();
        alertOut.show();
    }

    public void showPopUpRegistrasiBerhasil(){
        TextView title;
        TextView txt;
        Button btnClosePopUp;
        Typeface poppinBold = Typeface.createFromAsset(getAssets(),"fonts/Poppins-Bold.otf");
        Typeface poppinsRegular = Typeface.createFromAsset(getAssets(), "fonts/Poppins-Regular.otf");

        popUpRegistrasi.setContentView(R.layout.popup_registrasi_berhasil);
        title = (TextView) popUpRegistrasi.findViewById(R.id.popUpRegistrasititle);
        txt = (TextView) popUpRegistrasi.findViewById(R.id.popUpRegistrasitext);
        btnClosePopUp = (Button) popUpRegistrasi.findViewById(R.id.btnClosepopUpRegistrasi);

        title.setTypeface(poppinBold);
        txt.setTypeface(poppinsRegular);
        btnClosePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),loginScreen.class));
                popUpRegistrasi.dismiss();
            }
        });
        popUpRegistrasi.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpRegistrasi.show();
    }

    public void showPopUpRegistrasiGagal( String pesan){
        TextView title;
        TextView txt;
        Button btnClosePopUp;
        Typeface poppinBold = Typeface.createFromAsset(getAssets(),"fonts/Poppins-Bold.otf");
        Typeface poppinsRegular = Typeface.createFromAsset(getAssets(), "fonts/Poppins-Regular.otf");

        popUpRegistrasi.setContentView(R.layout.popup_registrasi_gagal);
        title = (TextView) popUpRegistrasi.findViewById(R.id.popUpRegistrasititle);
        txt = (TextView) popUpRegistrasi.findViewById(R.id.popUpRegistrasitext);
        btnClosePopUp = (Button) popUpRegistrasi.findViewById(R.id.btnClosepopUpRegistrasi);

        title.setTypeface(poppinBold);
        txt.setTypeface(poppinsRegular);
        txt.setText(pesan);
        btnClosePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),lupaPasswordScreen.class));
                popUpRegistrasi.dismiss();
            }
        });
        popUpRegistrasi.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpRegistrasi.show();
    }
}




//
//    private void registrationProgress() {
//        pbRegist.setVisibility(View.VISIBLE);
//        btnDaftar.setVisibility(View.INVISIBLE);
//
//        final String email = etEmail.getText().toString().trim();
//        final String password = etPassword.getText().toString().trim();
//        final String confirmPassword = etConfPassword.getText().toString().trim();
//        final String device = "$2y$10$Ty5GIOshIus/y18kZtDR3O6V9gKPk/Rhhv40zyMYVNeOKhC0QLzz6";
//
//        final Registration registration = new Registration(email,password,confirmPassword,device);
//
//        StringRequest registrasiRequest = new StringRequest(Request.Method.POST, ServerAPI.URL_REGISTRASI,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            Toast.makeText(registrasiScreen.this, "Message: " + jsonObject.getString("message"), Toast.LENGTH_LONG).show();
//                            pbRegist.setVisibility(View.GONE);
//                            btnDaftar.setVisibility(View.VISIBLE);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            pbRegist.setVisibility(View.GONE);
//                            btnDaftar.setVisibility(View.VISIBLE);
//                            Toast.makeText(registrasiScreen.this, "Message: " + e.toString(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        pbRegist.setVisibility(View.GONE);
//                        btnDaftar.setVisibility(View.VISIBLE);
//                        Toast.makeText(registrasiScreen.this, "Message: " + error.toString(), Toast.LENGTH_LONG).show();
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("email", email);
//                if (password.equals(confirmPassword)) {
//                    params.put("password", password);
//                    params.put("cpassword", confirmPassword);
//                } else {
//
//                }
//                return params;
//            }
//        };
//        AppController.getInstance().addToRequestQueue(registrasiRequest);
////    }


