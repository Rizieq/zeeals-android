package com.example.user.zeeals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.zeeals.adapter.MainActivity;
import com.example.user.zeeals.ResponsesAndRequest.Account_id;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.model.zGroupList;
import com.example.user.zeeals.model.zSource;
import com.example.user.zeeals.security.AesCipher;
import com.example.user.zeeals.service.RetroConnection;
import com.example.user.zeeals.service.UserClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class splashscreen extends AppCompatActivity {
    private ImageView logoZeeals;
    private ProgressBar pb;
    RetroConnection conn;
    UserClient userClient;
    List<Zlink> zlinks;
    AesCipher ch;
    private static final String TAG = "splashscreen";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getSharedPreferences("TOKEN",MODE_PRIVATE).edit().clear().apply();



        zlinks = new ArrayList<>();
        setContentView(R.layout.activity_splashscreen);
        conn = new RetroConnection();
        userClient = conn.getConnection();
        logoZeeals = (ImageView) findViewById(R.id.splashLogo);
        pb = (ProgressBar) findViewById(R.id.splashProgressBar);

        Animation splashanim = AnimationUtils.loadAnimation(this,R.anim.animation_splash);
        logoZeeals.startAnimation(splashanim);
        pb.startAnimation(splashanim);

        // SET TIME SPLASH //
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    SharedPreferences pref = getSharedPreferences("TOKEN",MODE_PRIVATE);
                    String tokenAccess = pref.getString("TOKEN",null);
                    if(tokenAccess==null){
                        startActivity(new Intent(splashscreen.this,loginScreen.class));
                        finish();

                    }else{
                        retreiveList(tokenAccess);
                    }

                }
            }
        };
        timer.start();
    }

    public void retreiveList(String token){
        /*ACCOUNT ID HERE's STILL HARCODED SHIT */
        Account_id acid = new Account_id("1");
        Call<zGroupList> call=  userClient.links(token,acid);
        call.enqueue(new Callback<zGroupList>() {
            @Override
            public void onResponse(Call<zGroupList> call, Response<zGroupList> response) {
                if (response.isSuccessful()) {
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
                    Intent i = new Intent(splashscreen.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(splashscreen.this,"Login error",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(splashscreen.this,loginScreen.class));
                    finish();
                }

            }
            @Override
            public void onFailure(Call<zGroupList> call, Throwable t) {
                Toast.makeText(splashscreen.this,"Connection error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
