package com.example.user.zeeals;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.zeeals.adapter.MainActivity;
import com.example.user.zeeals.model.Account;
import com.example.user.zeeals.model.User;
import com.example.user.zeeals.ResponsesAndRequest.Account_id;
import com.example.user.zeeals.model.Login;
import com.example.user.zeeals.model.AuthLogin;
import com.example.user.zeeals.ResponsesAndRequest.Serve;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.model.zGroupList;
import com.example.user.zeeals.model.zSource;
import com.example.user.zeeals.service.RetroConnection;
import com.example.user.zeeals.service.UserClient;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;



public class loginScreen extends AppCompatActivity {


    private TextView textfblogin;
    private TextView textfblogout;
    private Button btnMasuk;
    private LoginButton fbLoginButton;
    private ProgressBar pbLogin;
    private AutoCompleteTextView ETemail;
    private EditText ETPassword;
    private static final String TAG = "loginScreen";
    private CallbackManager callbackManager;
    private static String tokenAccess;
    RetroConnection conn;
    UserClient userClient;
    List<Zlink> zlinks;

    Dialog popUpLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conn = new RetroConnection();
        userClient = conn.getConnection();
        zlinks = new ArrayList<>();


        SharedPreferences.Editor pref = getSharedPreferences("TOKEN",MODE_PRIVATE).edit().clear();
        pref.apply();

        setContentView(R.layout.activity_login_screen);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

            ETemail = findViewById(R.id.ETemailLoginInput);
            ETPassword =  findViewById(R.id.ETpasswordLoginInput);
            pbLogin = findViewById(R.id.login_progress);

            /* THIS CODE FOR SPAN "DAFTAR" */
        TextView textdaftar = findViewById(R.id.textayoDaftar);
            String text = "Ayo daftar sekarang.";
            SpannableString daftar = new SpannableString(text);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    startActivity(new Intent(loginScreen.this, registrasiScreen.class));
                    finish();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.rgb(199, 149, 109));
                    ds.setUnderlineText(false);
                }
            };
            daftar.setSpan(clickableSpan, 4, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textdaftar.setText(daftar);
            textdaftar.setMovementMethod(LinkMovementMethod.getInstance());



            /* THIS IS FOR SPAN LUPA PASSWORD */
        TextView lupaPassword = findViewById(R.id.textLupaPassword);
            String textLupa = "Lupa password?";
            SpannableString lupapass = new SpannableString(textLupa);
            ClickableSpan clickableSpan1 = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    startActivity(new Intent(loginScreen.this, MainActivity.class));
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.rgb(199, 149, 109));
                    ds.setUnderlineText(false);
                }
            };
            lupapass.setSpan(clickableSpan1, 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            lupaPassword.setText(lupapass);
            lupaPassword.setMovementMethod(LinkMovementMethod.getInstance());


            /* THIS IS CODE FOR BUTTON SIGN IN */
            btnMasuk =  findViewById(R.id.btnMasuk);
            validateEmailPassword(); //FOR VALIDATING EMAIL AND PASSWORD
            btnMasuk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateEmailClicked() && validatePasswordClicked()){
                        loginProcess();
                    }
                    else {
                        showPopUpLogin("Format email atau password yang dimasukkan tidak sesuai. Pastikan anda telah memasukkan email dan password yang sesuai");
                    }
                }
            });

            popUpLogin = new Dialog(this);

            /* USING FOR FACEBOOK LOGIN FUNC */
            textfblogin = findViewById(R.id.textfblogin);
            textfblogout = findViewById(R.id.textfblogout);
        RelativeLayout fbBtnHandle = findViewById(R.id.customBtnFb);
            fbLoginButton =  findViewById(R.id.btnFacebookLogin);
            fbBtnHandle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fbLoginButton.performClick();
                }
            });
            LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
            callbackManager = CallbackManager.Factory.create();
            fbLoginButton.setReadPermissions(Arrays.asList("email","public_profile"));
            checkLoginStatus();

            fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Toast.makeText(loginScreen.this,"NOTE THIS",Toast.LENGTH_LONG).show();

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void loadUserProfile(AccessToken newAccesToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccesToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String id = object.getString("id");
                    String email = object.getString("email");
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String full_name = first_name+ " " +last_name;

                    String image_url = "https://graph.facebook.com/"+id+"/picture?type=normal";

                    Toast.makeText(getApplicationContext(),"Facebook: User Logged in as "+ full_name,Toast.LENGTH_LONG).show();
                    textfblogin.setVisibility(View.INVISIBLE);
                    textfblogout.setVisibility(View.VISIBLE);
//                    loginProcess();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name, last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    }

    /* VALIDATE TEXT FIELD REGISTRATION */
    private void validateEmailPassword() {
        ETemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ETemail.setError("Field can't be empty");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    ETemail.setError(null);
                } else {
                    ETemail.setError("Please enter a valid email address");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ETPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().isEmpty()) {
                    ETPassword.setError("Field can't be empty");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() < 8) {
                    ETPassword.setError("Password need a least 8 characters");
                    //btnMasuk.setEnabled(false);

                } else {
                    ETPassword.setError(null);
                    //btnMasuk.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean validateEmailClicked(){
        String email = ETemail.getText().toString().trim();
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean validatePasswordClicked(){
        String password = ETPassword.getText().toString().trim();
        return password.length() >= 8;
    }

    /* THIS FOR LOGIN FUNCTION */
    private void loginProcess(){
        pbLogin.setVisibility(View.VISIBLE);
        btnMasuk.setVisibility(View.INVISIBLE);

        String email = ETemail.getText().toString().trim();
        String password = ETPassword.getText().toString().trim();

        final Login login = new Login(email,password);

        Call<AuthLogin> call = userClient.login(login);

        call.enqueue(new Callback<AuthLogin>() {
            @Override
            public void onResponse(@NonNull Call<AuthLogin> call, @NonNull retrofit2.Response<AuthLogin> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG,"onResponse: Post email + Password Berhasil");
                    assert response.body() != null;
                    Serve serve = response.body().getServe();
                    tokenAccess = "Bearer " + serve.getAccess_token();



                    Gson gson = new Gson();
                    List<Account> account = serve.getUser().getAccount();
                    User user = serve.getUser();
                    String json_account = gson.toJson(account);
                    String json_user= gson.toJson(user);

                    getSharedPreferences("ACCOUNT",MODE_PRIVATE).edit().putString("USER",json_user).apply();
                    getSharedPreferences("TOKEN",MODE_PRIVATE).edit().putString("TOKEN",tokenAccess).apply();
                    getSharedPreferences("ACCOUNT",MODE_PRIVATE).edit().putString("ACCOUNT",json_account).apply();



                    retreiveList(tokenAccess,account.get(0).getAccountId().toString());
//                    getLinkAuth(tokenAccess);
                }
                else {
                    Log.d(TAG,"onResponse: Password Login salah");
                    //Toast.makeText(loginScreen.this,"Password incorrect",Toast.LENGTH_SHORT).show();
                    pbLogin.setVisibility(View.GONE);
                    btnMasuk.setVisibility(View.VISIBLE);
                    //wrongLoginInfoAlert();
                    String pesan = "Email atau password yang anda masukkan tidak sesuai";
                    showPopUpLogin(pesan);
                }
            }
            @Override
            public void onFailure(@NonNull Call<AuthLogin> call, @NonNull Throwable t) {
                //Toast.makeText(loginScreen.this,t.toString(),Toast.LENGTH_LONG).show();
                //wrongInput();
                pbLogin.setVisibility(View.GONE);
                btnMasuk.setVisibility(View.VISIBLE);
                String pesan = "Koneksi anda sedang dalam masalah. pastikan koneksi anda terhubung dengan baik. silahkan coba lagi.";
                showPopUpLogin(pesan);
            }
        });
    }

    public void retreiveList(String token, String id){
        Account_id acid = new Account_id(id);
        Call<zGroupList> call=  userClient.links(token,acid);
        call.enqueue(new Callback<zGroupList>() {
            @SuppressLint("ShowToast")
            @Override
            public void onResponse(@NonNull Call<zGroupList> call, @NonNull Response<zGroupList> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getServe() != null) {
                        List<zGroup> responseGroup = response.body().getServe();
                        for (zGroup g : responseGroup) {
                            boolean haveChild=true;
                            List<zSource> child = g.getChildLink();
                            if(g.getChildLink().isEmpty()){
                                haveChild=false;
                            }
                            zGroup g1 = new zGroup(haveChild,g.getGroupLinkId(),g.getAccountId(),g.getUnicode(),g.getTitle(),g.getOrientation(),g.getStatus(),g.getUpdatedAt(),g.getUncategorized(),child);
                            zlinks.add(g1);
                        }
                        Gson gson = new Gson();
                        String json = gson.toJson(zlinks);
                        SharedPreferences.Editor pref = getSharedPreferences("TOKEN", MODE_PRIVATE).edit().putString("GROUPLIST", json);
                        pref.apply();
                    }
                    Intent i = new Intent(loginScreen.this, MainActivity.class).putExtra("TOKEN", tokenAccess);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } else {
                    Toast.makeText(loginScreen.this, "Connection error", Toast.LENGTH_SHORT);
                }

            }
            @Override
            public void onFailure(@NonNull Call<zGroupList> call, @NonNull Throwable t) {

            }
        });
    }



    /* THIS FOR ACTION IF BACK BUTTON CLICKED ON LOGIN */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(loginScreen.this);

        builder.setTitle("Keluar Aplikasi")
                .setMessage("Apakah anda yakin keluar dari Zeeals? ")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("Tidak", null).setCancelable(false);
        AlertDialog alertOut = builder.create();
        alertOut.show();

    }

    /* THIS FOR POP UP MESSAGE LOGIN GAGAL */
    public void showPopUpLogin(String pesan){
        TextView title;
        TextView text;
        Button btnClose;

        popUpLogin.setContentView(R.layout.popup_login_gagal);
        title =  popUpLogin.findViewById(R.id.popUpRegistrasititle);
        text =  popUpLogin.findViewById(R.id.popUpRegistrasitext);
        btnClose =  popUpLogin.findViewById(R.id.btnClosepopUpRegistrasi);

        Typeface poppinsBold = Typeface.createFromAsset(getAssets(),"fonts/Poppins-Bold.otf");
        Typeface poppinsRegular = Typeface.createFromAsset(getAssets(),"fonts/Poppins-Regular.otf");
        title.setTypeface(poppinsBold);
        text.setTypeface(poppinsRegular);
        text.setText(pesan);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpLogin.dismiss();
            }
        });
        Objects.requireNonNull(popUpLogin.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpLogin.show();
    }
}





