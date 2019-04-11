package com.example.user.zeeals;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import com.example.user.zeeals.model.Login;
import com.example.user.zeeals.model.User;
import com.example.user.zeeals.service.UserClient;
import com.example.user.zeeals.util.ServerAPI;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
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

import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class loginScreen extends AppCompatActivity {

    private TextView zeealslogin, lupaPassword, textdaftar, textfblogin, textfblogout;
    private Button btnMasuk;
    private LoginButton fbLoginButton;
    private ProgressBar pbLogin;
    private AutoCompleteTextView ETemail;
    private EditText ETPassword;
    private RelativeLayout fbBtnHandle;
    private static final String TAG = "loginScreen";
    private CallbackManager callbackManager;

    private static String tokenAccess;

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ServerAPI.zeealseRESTAPI)
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);

    Dialog popUpLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


        /// ANOTHER INITIATION ///
        ETemail = findViewById(R.id.ETemailLoginInput);
        ETPassword =  findViewById(R.id.ETpasswordLoginInput);
        pbLogin = findViewById(R.id.login_progress);

        /// THIS CODE FOR SPAN "DAFTAR" ///
        textdaftar =  findViewById(R.id.textayoDaftar);
        String text = "Ayo daftar sekarang.";
        SpannableString daftar = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(loginScreen.this, registrasiScreen.class));
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

        /// THIS IS FOR SET ZEEALS FONT ///
//        Typeface poppinsBold = Typeface.createFromAsset(getAssets(), "fonts/Poppins-Bold.otf");
//        Typeface poppinsRegular = Typeface.createFromAsset(getAssets(), "fonts/Poppins-Regular.otf");
        zeealslogin =  findViewById(R.id.txtZealsLogin);
//        zeealslogin.setTypeface(poppinsBold);

        /// THIS IS FOR SPAN LUPA PASSWORD ////
        lupaPassword =  findViewById(R.id.textLupaPassword);
//        lupaPassword.setTypeface(poppinsRegular);
        String textLupa = "Lupa password?";
        SpannableString lupapass = new SpannableString(textLupa);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(loginScreen.this, MainActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.rgb(199, 149, 109));
                ds.setUnderlineText(false);
            }
        };
        lupapass.setSpan(clickableSpan1, 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        lupaPassword.setText(lupapass);
        lupaPassword.setMovementMethod(LinkMovementMethod.getInstance());


        /// THIS IS CODE FOR BUTTON SIGN IN ////
        btnMasuk =  findViewById(R.id.btnMasuk);
        validateEmailPassword(); //FOR VALIDATING EMAIL AND PASSWORD
        //btnMasuk.setEnabled(false);
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

        /// USING FOR FACEBOOK LOGIN FUNC ///
        textfblogin = findViewById(R.id.textfblogin);
        textfblogout = findViewById(R.id.textfblogout);
        fbBtnHandle = findViewById(R.id.customBtnFb);
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


    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                Toast.makeText(loginScreen.this, "Facebook: User Logged out", Toast.LENGTH_LONG).show();
                textfblogin.setVisibility(View.VISIBLE);
                textfblogout.setVisibility(View.INVISIBLE);
            } else {
                loadUserProfile(currentAccessToken);
            }
        }
    };

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

    /// VALIDATE TEXT FIELD REGISTRATION ///
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
                if (s.toString().length() < 6) {
                    ETPassword.setError("Password need a least 6 characters");
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
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true;
        }
        else{
            return false;
        }
    }
    private boolean validatePasswordClicked(){
        String password = ETPassword.getText().toString().trim();
        if (password.length()<6){
            return false;
        }else {
            return true;
        }
    }

    /// THIS FOR LOGIN FUNCTION ///
    private void loginProcess(){
        pbLogin.setVisibility(View.VISIBLE);
        btnMasuk.setVisibility(View.INVISIBLE);

        String email = ETemail.getText().toString().trim();
        String password = ETPassword.getText().toString().trim();

        final Login login = new Login(email,password);

        Call<User> call = userClient.login(login);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: "+response.isSuccessful());
                    Log.d(TAG,"onResponse: Post email + Password Berhasil");

                    tokenAccess = "Bearer " + response.body().getAccess_token();

                    getLinkAuth(tokenAccess);
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
            public void onFailure(Call<User> call, Throwable t) {
                //Toast.makeText(loginScreen.this,t.toString(),Toast.LENGTH_LONG).show();
                //wrongInput();
                Log.d(TAG,"onResponse: Something Error Login");
                pbLogin.setVisibility(View.GONE);
                btnMasuk.setVisibility(View.VISIBLE);
                String pesan = "Koneksi anda sedang dalam masalah. pastikan koneksi anda terhubung dengan baik. silahkan coba lagi.";
                showPopUpLogin(pesan);
            }
        });
    }
    /// THIS FOR LOGIN TOKEN AUTHENTICATION PROGRESS ///
    private void getLinkAuth(final String tokenAccess){
        Call<ResponseBody> call = userClient.getLink("Bearer" + tokenAccess);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    pbLogin.setVisibility(View.GONE);
                    btnMasuk.setVisibility(View.VISIBLE);
                    Log.d(TAG,"onResponse: Token Access Berhasil");
                    startActivity(new Intent(loginScreen.this, MainActivity.class).putExtra("TOKEN",tokenAccess));
                }
                else {
                    pbLogin.setVisibility(View.GONE);
                    btnMasuk.setVisibility(View.VISIBLE);

                    Toast.makeText(loginScreen.this,"Token Acceess incorrect",Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"onResponse: Token Access Salah, Cek kembali");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pbLogin.setVisibility(View.GONE);
                btnMasuk.setVisibility(View.VISIBLE);
                Log.d(TAG,"onResponse: Token Access Parsing Error");
            }
        });

    }

    /// THIS FOR ACTION IF BACK BUTTON CLICKED ON LOGIN ////
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
    // THIS FOR POP UP MESSAGE LOGIN GAGAL ///
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
        popUpLogin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpLogin.show();
    }

    public void wrongLoginInfoAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(loginScreen.this);

        builder.setTitle("Email / Password Salah")
                .setMessage("Email / password yang dimasukkan salah. Silahkan coba lagi ")
                .setNeutralButton("Coba Lagi",null);
        AlertDialog alertOut = builder.create();
        alertOut.show();
    }

    public void wrongInput(){
        AlertDialog.Builder builder = new AlertDialog.Builder(loginScreen.this);

        builder.setTitle("Email yang dimasukkan tidak sesuai")
                .setMessage("Pastikan data yang anda masukkan sesuai tipe. Silahkan coba lagi ")
                .setNeutralButton("Coba Lagi",null);
        AlertDialog alertOut = builder.create();
        alertOut.show();
    }
}




//    Gson gson = new GsonBuilder()
//            .setLenient()
//            .create();
//
//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl(ServerAPI.URL_POSTLOGIN)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build();


//    private void loginProgress(){
//        final String email = ETemail.getText().toString().trim();
//        final String password = ETPassword.getText().toString().trim();
//
//        pbLogin.setVisibility(View.VISIBLE);
//        btnMasuk.setVisibility(View.INVISIBLE);
//
//        StringRequest requestLogin = new StringRequest(Request.Method.POST, ServerAPI.URL_POSTLOGIN,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String accessToken = jsonObject.getString("access_token");
//                            String tokenType = jsonObject.getString("token_type");
//                            Toast.makeText(loginScreen.this,"message: "+accessToken,Toast.LENGTH_SHORT).show();
//                            pbLogin.setVisibility(View.GONE);
//                            btnMasuk.setVisibility(View.VISIBLE);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            pbLogin.setVisibility(View.GONE);
//                            btnMasuk.setVisibility(View.VISIBLE);
//                            Toast.makeText(loginScreen.this,"message: "+ e.toString(),Toast.LENGTH_SHORT);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        pbLogin.setVisibility(View.GONE);
//                        btnMasuk.setVisibility(View.VISIBLE);
//                        Toast.makeText(loginScreen.this,"message: " +error.toString(),Toast.LENGTH_SHORT).show();
//                    }
//                }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<>();
//                params.put("email",email);
//                params.put("password",password);
//                return params;
//            }
//        };
//        AppController.getInstance().addToRequestQueue(requestLogin);
//    }
//
//    private void loginTokenParse(String tokenAccess, String tokenType){
//        JsonObjectRequest requestToken = new JsonObjectRequest(Request.Method.GET, ServerAPI.URL_GETTOKEN, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try{
//                            JSONArray jsonArray = response.getJSONArray("Authorization");
//
//                            for (int i=0;i<jsonArray.length();i++){
//                                JSONObject Authorization = jsonArray.getJSONObject(i);
//                                Toast.makeText(loginScreen.this,"message "+ Authorization.getString("id"),Toast.LENGTH_SHORT).show();
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(loginScreen.this,"message "+e.toString(),Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(loginScreen.this,"message "+error.toString(),Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }



