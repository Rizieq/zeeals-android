package com.example.user.zeeals.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

//import com.example.user.zeeals.adapter.AddNewGroup;
import com.example.user.zeeals.fragment.UserFragment;
import com.example.user.zeeals.ResponsesAndRequest.IconList;
import com.example.user.zeeals.service.RetroConnection;
import com.example.user.zeeals.service.UserClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import com.example.user.zeeals.R;
import com.example.user.zeeals.model.Zlink;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    //shared ke menu fragment
    public RecyclerAdapter_Main adapterTest;
    public List<Zlink> zLink;
    private static final String TAG = "TESTING";
    boolean fragment_in_home;
    ConstraintLayout btn_fragment_home,btn_fragment_account,bottom_nav;
    RelativeLayout mainLayout;
    View highlight_home,highlight_account;
    ImageButton btn_fragment_home2,btn_fragment_account2;
    RetroConnection conn;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment_in_home=true;
        conn = new RetroConnection();
        UserClient userClient = conn.getConnection();
        String token = getSharedPreferences("TOKEN",MODE_PRIVATE).getString("TOKEN",null);
        mainLayout= findViewById(R.id.mainLayout);
        highlight_home=findViewById(R.id.highlight_home);
        highlight_account=findViewById(R.id.highlight_account);
        highlight_account.setVisibility(View.GONE);
        Call<IconList> call = userClient.icon(token);
        call.enqueue(new Callback<IconList>() {
            @Override
            public void onResponse(Call<IconList> call, Response<IconList> response) {
                if(response.isSuccessful()){
                    ArrayList<String> x = response.body().getIconList();
                    Gson gson = new Gson();
                    String json = gson.toJson(x);
                    getSharedPreferences("ICON",MODE_PRIVATE).edit().putString("ICON",json).apply();
                }else{
                    Toast.makeText(MainActivity.this,"Failed to retrive icon list",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: failed retrieve icon list");
                }
            }

            @Override
            public void onFailure(Call<IconList> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Failed to retrive icon list",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: failed retrieve icon list");
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.mainContainer,new mainFragment()).commit();

        btn_fragment_home2=findViewById(R.id.iv_bottomNav_home);
        btn_fragment_account2=findViewById(R.id.iv_bottomNav_account);
        bottom_nav = findViewById(R.id.bottom_nav);
        btn_fragment_account2.setBackgroundColor(R.color.grey);

        btn_fragment_home = findViewById(R.id.home_nav_btn);
        btn_fragment_account = findViewById(R.id.account_nav_btn);
        btn_fragment_account.setBackgroundColor(R.color.grey);



    }

    @SuppressLint("ResourceAsColor")
    public void openMainFragment (View view){
        if(!fragment_in_home){
            Fragment show = new mainFragment();
            Fragment hide = new UserFragment();
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right,R.anim.enter_from_left,R.anim.exit_to_left)
                    .hide(hide).show(show).replace(R.id.mainContainer,show).commit();
            highlight_home.animate().translationX(0);
            btn_fragment_home2.setBackgroundColor(R.color.colorPrimary);
            btn_fragment_home.setBackgroundColor(R.color.colorPrimary);
            btn_fragment_account2.setBackgroundColor(R.color.grey);
            btn_fragment_account.setBackgroundColor(R.color.grey);
            fragment_in_home=true;
        }
    }

    @SuppressLint("ResourceAsColor")
    public void openAccountFragment(View view){
        if(fragment_in_home){
            Fragment hide = new mainFragment();
            UserFragment show = new UserFragment();
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_right,R.anim.exit_to_right)
                    .hide(hide).show(show).replace(R.id.mainContainer,show).commit();
            float x = mainLayout.getWidth()/2;

            highlight_home.animate().translationX(x);
            btn_fragment_account2.setBackgroundColor(R.color.colorPrimary);
            btn_fragment_home2.setBackgroundColor(R.color.grey);
            btn_fragment_home.setBackgroundColor(R.color.grey);
            btn_fragment_account.setBackgroundColor(R.color.colorPrimary);
            fragment_in_home=false;
        }
    }

    public void slideRight(final View view){

        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationX",0f);
        animation.setDuration(1000);
//        llDomestic.setVisibility(View.GONE);
        animation.start();

    }
    public void slideLeft(final View view, final View llDomestic){

        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationY",0f);
        animation.setDuration(100);
        llDomestic.setVisibility(View.GONE);
        animation.start();
    }


}