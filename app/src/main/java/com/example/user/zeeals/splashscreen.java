package com.example.user.zeeals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.user.zeeals.adapter.MainActivity;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.model.zGroupList;
import com.example.user.zeeals.service.RetroConnection;
import com.example.user.zeeals.service.UserClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class splashscreen extends AppCompatActivity {
    private ImageView logoZeeals;
    private ProgressBar pb;
    RetroConnection conn;
    UserClient userClient;
    List<Zlink> zlinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zlinks = new ArrayList<>();
        setContentView(R.layout.activity_splashscreen);
        conn = new RetroConnection();
        userClient = conn.getConnection();
//        SharedPreferences.Editor pref = getSharedPreferences("TOKEN",MODE_PRIVATE).edit().clear();
//        pref.apply();
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

                    }else{
                        retreiveList(tokenAccess);
                    }

                }
            }
        };
        timer.start();
    }

    public void retreiveList(String token){
        Call<zGroupList> call=  userClient.showGroup(token);
        call.enqueue(new Callback<zGroupList>() {
            @Override
            public void onResponse(Call<zGroupList> call, retrofit2.Response<zGroupList> response) {
                if (response.isSuccessful()) {

                    ArrayList<zGroup> responseGroup = response.body().getGroupList();
                    for(zGroup g : responseGroup){
                        zGroup g1=new zGroup(g.getGroup_link_id(),g.getAccount_id(),g.getOrientation(),g.getTitle(),g.getIcon(),g.getPosition(),g.getStatus(),g.getCreated_at(),g.getCreated_at());
                        zlinks.add(g1);
                    }
                    Gson gson = new Gson();
                    String json = gson.toJson(zlinks);
                    SharedPreferences.Editor pref = getSharedPreferences("TOKEN",MODE_PRIVATE).edit().putString("GROUPLIST",json);
                    pref.apply();
                    startActivity(new Intent(splashscreen.this, MainActivity.class));

                }
                else {
                    Log.d("SPLASH SCREEN", "onResponse: nga tau error, pengen beli trek");
                }
            }
            @Override
            public void onFailure(@NonNull Call<zGroupList> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
