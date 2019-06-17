package com.example.user.zeeals.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

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


public class MainActivity extends AppCompatActivity{
    //shared ke menu fragment
    public RecyclerAdapter_Main adapterTest;
    public List<Zlink> zLink;
    private static final String TAG = "TESTING";
    int startingPosition=1;
    RetroConnection conn;
    ConstraintLayout dim;
    FloatingActionButton fab;

    FragmentTransaction transaction;
    boolean menuShowed,layout_inHome;
    com.example.user.zeeals.fragment.menuFragment menuFragment;
    BottomNavigationView bottomNavigationView;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        bottomNavigationView = findViewById(R.id.bottomnav);
//        bottomNavigationView.inflateMenu(R.menu.bottom_nav);
        initView();
        receiveIcon();
        onClickController();
        fabController();
    }

    void initView(){
        menuShowed=false;
        layout_inHome=true;
        loadFragment(new mainFragment());
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainActivity_frag_container, fragment,"homeFrag")
                    .commit();
        }
    }

    void fabController(){
        fab = findViewById(R.id.fab);
        dim = findViewById(R.id.semi_white_bg);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layout_inHome){
                    if (menuShowed){
                        hideMenu();
                        mainFragment mainFragment= (mainFragment) getSupportFragmentManager().findFragmentByTag("homeFrag");
                        assert mainFragment != null;
                        mainFragment.dimLayout(false);
                        rotateFab();
                    }else{
                        mainFragment mainFragment= (mainFragment) getSupportFragmentManager().findFragmentByTag("homeFrag");
                        assert mainFragment != null;
                        mainFragment.dimLayout(true);
                        openMenuFragment();
                        rotateFab();
                    }
                }else{
//                    fab.setEnabled(false);
//                    fabProgressCircle.show();
                    UserFragment userFragment= (UserFragment)getSupportFragmentManager().findFragmentByTag("accountFrag");
                    assert userFragment != null;
                    userFragment.save();
                }

            }
        });
    }

    public void rotateFab(){
        if (menuShowed){
            ViewCompat.animate(fab).
                    rotation(0.0f).
                    withLayer().
                    setDuration(1000).
                    setInterpolator(new OvershootInterpolator()).
                    start();

            menuShowed=false;
        }else{
            menuShowed=true;
            ViewCompat.animate(fab).
                    rotation(405f).
                    withLayer().
                    setDuration(1000).
                    setInterpolator(new OvershootInterpolator()).
                    start();
        }

    }

    public void openMenuFragment() {
        menuFragment = com.example.user.zeeals.fragment.menuFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom,R.anim.enter_from_bottom);
        transaction.replace(R.id.fragment_menu_container, menuFragment).show(menuFragment).commit();

    }

    void receiveIcon(){
        conn = new RetroConnection();
        UserClient userClient = conn.getConnection();
        String token = getSharedPreferences("TOKEN",MODE_PRIVATE).getString("TOKEN",null);
        Call<IconList> call = userClient.icon(token);
        call.enqueue(new Callback<IconList>() {
            @Override
            public void onResponse(@NonNull Call<IconList> call, @NonNull Response<IconList> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
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
            public void onFailure(@NonNull Call<IconList> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this,"Failed to retrive icon list",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: failed retrieve icon list");
            }
        });
    }

    void onClickController(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int position=0;
                switch (item.getItemId()){
                    case R.id.home_nav:
                        if(!layout_inHome)fab.setAnimation(shrink());
                        if(!menuShowed) {
                            layout_inHome = true;
                            fragment = new mainFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.mainActivity_frag_container, fragment, "homeFrag")
                                    .commit();
                            position = 1;
                        }else{
                            return false;
                        }
                        break;
                    case R.id.account_nav:
                        if(layout_inHome)fab.setAnimation(shrink());
                        fragment = new UserFragment();
                        layout_inHome=false;
                        if(menuShowed){
                            transaction = getSupportFragmentManager().beginTransaction();
                            transaction.hide(menuFragment);
                            transaction.commit();
                            rotateFab();
                        }
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainActivity_frag_container, fragment,"accountFrag")
                                .commit();
                        position=2;
                        break;
                }
                return loadFragment(fragment,position);
            }
        });
    }

    private boolean loadFragment(Fragment fragment, int newPosition) {

        if(fragment != null) {
            if(startingPosition > newPosition) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.mainActivity_frag_container, fragment);
                transaction.commit();
            }
            if(startingPosition < newPosition) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                transaction.replace(R.id.mainActivity_frag_container, fragment);
                transaction.commit();
            }
            startingPosition = newPosition;

            return true;
        }

        return false;
    }

    public void hideMenu(){
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom,R.anim.enter_from_bottom).replace(R.id.fragment_menu_container,menuFragment).hide(menuFragment).commit();
    }

    public void enableFab(){
        fab.setEnabled(true);
    }

    AnimationSet grow(){
        final ScaleAnimation growAnim = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.addAnimation(growAnim);
        animationSet.setDuration(200);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(layout_inHome) fab.setImageResource(R.drawable.ic_add);
                else fab.setImageResource(R.drawable.ic_check);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return animationSet;
    }

    AnimationSet shrink(){
        final ScaleAnimation shrinkAnim = new ScaleAnimation(1, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animationSet.addAnimation(shrinkAnim);
        animationSet.setDuration(200);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fab.setAnimation(grow());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animationSet;
    }
}