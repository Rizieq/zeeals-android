package com.example.user.zeeals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.zeeals.adapter.MainActivity;
import com.example.user.zeeals.ResponsesAndRequest.Account_id;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.model.zGroupList;
import com.example.user.zeeals.model.zSource;
import com.example.user.zeeals.service.RetroConnection;
import com.example.user.zeeals.service.UserClient;
import com.eyalbira.loadingdots.LoadingDots;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class splashscreen extends AppCompatActivity {
    LoadingDots loadingDots;
    RetroConnection conn;
    UserClient userClient;
    List<Zlink> zlinks;
    private static final String TAG = "splashscreen";
    private ArrayList<String> uriArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSharedPreferences("TOKEN",MODE_PRIVATE).edit().clear().apply();


        uriArrayList =new ArrayList<>();
        zlinks = new ArrayList<>();
        setContentView(R.layout.activity_splashscreen);
        conn = new RetroConnection();
        userClient = conn.getConnection();
        ImageView logoZeeals = (ImageView) findViewById(R.id.splashLogo);
        loadingDots =findViewById(R.id.splashProgressBar);

        Animation splashanim = AnimationUtils.loadAnimation(this,R.anim.animation_splash);
        logoZeeals.startAnimation(splashanim);
        loadingDots.startAnimation(splashanim);

        // SET TIME SPLASH //
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(1000);
                    SharedPreferences pref = getSharedPreferences("TOKEN",MODE_PRIVATE);
                    String tokenAccess = pref.getString("TOKEN",null);
                    if(tokenAccess==null){
                        startActivity(new Intent(splashscreen.this,loginScreen.class));
                        finish();
                    }else{
                        retreiveList(tokenAccess);
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        timer.start();
    }

    public void retreiveList(String token){
        /*ACCOUNT ID HERE's STILL HARCODED SHIT */

        String mobile = "$2y$10$s6ZYEuThB8IkZ0sl1ucOOeJFYf/4DmGNbeIyB6j4l9lPpwdu41n5K";
        Account_id acid = new Account_id("3",mobile);
        Call<zGroupList> call=  userClient.links(token,acid);
        call.enqueue(new Callback<zGroupList>() {
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
                        Intent i = new Intent(splashscreen.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Toast.makeText(splashscreen.this,"Login error",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(splashscreen.this,loginScreen.class));
                    finish();
                }

            }
            @Override
            public void onFailure(@NonNull Call<zGroupList> call, @NonNull Throwable t) {
                Toast.makeText(splashscreen.this,"Connection error",Toast.LENGTH_SHORT).show();
            }
        });
    }




}
