package com.example.user.zeeals.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

//import com.example.user.zeeals.adapter.AddNewGroup;
import com.example.user.zeeals.fragment.UserFragment;
import com.example.user.zeeals.ResponsesAndRequest.IconList;
import com.example.user.zeeals.fragment.menuFragment;
import com.example.user.zeeals.listener.dimLayoutListener;
import com.example.user.zeeals.service.RetroConnection;
import com.example.user.zeeals.service.UserClient;
import com.example.user.zeeals.util.CurvedBottomNavigationView;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    TabLayout tabLayout;
    TabAdapter tabAdapter;
    ViewPager viewPager;
    RetroConnection conn;
    ConstraintLayout dim;
    FloatingActionButton fab;
    FABProgressCircle fabProgressCircle;

    dimLayoutListener dimLayoutListener;
    FragmentTransaction transaction;
    boolean menuShowed,layout_inHome;
    com.example.user.zeeals.fragment.menuFragment menuFragment;
    CurvedBottomNavigationView curvedBottomNavigationView;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        curvedBottomNavigationView = findViewById(R.id.customBottomBar);
        curvedBottomNavigationView.inflateMenu(R.menu.bottom_nav);
        initView();
        receiveIcon();
        onClickController();
        fabController();
    }

    void initView(){
        menuShowed=false;
        layout_inHome=true;
        fabProgressCircle=findViewById(R.id.fabProgressCircle);
        loadFragment(new mainFragment());
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainActivity_frag_container, fragment,"homeFrag")
                    .commit();
            return true;
        }
        return false;
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
                        mainFragment.dimLayout(false);
                        rotateFab();
                    }else{
                        mainFragment mainFragment= (mainFragment) getSupportFragmentManager().findFragmentByTag("homeFrag");
                        mainFragment.dimLayout(true);
                        openMenuFragment();
                        rotateFab();
                    }
                }else{
                    fab.setEnabled(false);
                    fabProgressCircle.show();
                    UserFragment userFragment= (UserFragment)getSupportFragmentManager().findFragmentByTag("accountFrag");
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
        menuFragment = menuFragment.newInstance();
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
    }

    void onClickController(){
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int position=0;
                switch (item.getItemId()){
                    case R.id.home_nav:
                        if(!menuShowed) {
                            layout_inHome = true;
                            fab.setImageResource(R.drawable.ic_add);
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
                        fab.setImageResource(R.drawable.ic_check);
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
                    case R.id.gap:
                        if(layout_inHome){
                            if (menuShowed){
                                hideMenu();
                                mainFragment mainFragment= (mainFragment) getSupportFragmentManager().findFragmentByTag("homeFrag");
                                mainFragment.dimLayout(false);
                                rotateFab();
                            }else{
                                mainFragment mainFragment= (mainFragment) getSupportFragmentManager().findFragmentByTag("homeFrag");
                                mainFragment.dimLayout(true);
                                openMenuFragment();
                                rotateFab();
                            }
                        }else{
                            fabProgressCircle.show();
                            fab.setEnabled(false);
                            UserFragment userFragment= (UserFragment)getSupportFragmentManager().findFragmentByTag("accountFrag");
                            userFragment.save();
                        }
                        return false;
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

    public void fabProgressCancel(){
        fabProgressCircle.hide();
    }

    public void fabProgressCircleEnd(){
        fabProgressCircle.beginFinalAnimation();
        fabProgressCircle.attachListener(new FABProgressListener() {
            @Override
            public void onFABProgressAnimationEnd() {
//                Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
                fabProgressCircle.hide();
            }
        });
    }
}