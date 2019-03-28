package com.example.user.zeeals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class splashscreen extends AppCompatActivity {
    private ImageView logoZeeals;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

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
                    startActivity(new Intent(splashscreen.this,loginScreen.class));
                }
            }
        };
        timer.start();
    }
}
