package com.example.user.zeeals.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.user.zeeals.adapter.AddNewGroup;
import com.example.user.zeeals.editProfileScreen;
import com.example.user.zeeals.fragment.addGroupFragment;
import com.example.user.zeeals.fragment.editSourceFragment;
import com.example.user.zeeals.loginScreen;
import com.example.user.zeeals.model.Message;
import com.example.user.zeeals.model.zGroupList;
import com.example.user.zeeals.service.RetroConnection;
import com.example.user.zeeals.service.UserClient;
import com.example.user.zeeals.util.ServerAPI;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.user.zeeals.R;
import com.example.user.zeeals.fragment.menuFragment;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.model.zSource;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private TextView profileName, profileDesc;
    Dialog picChangePopUp, editGroupNamePopup;
    private ImageView imgProfpic,imgBannerProfPic;

    private static final int PICK_IMAGE_PROF_PIC = 100;
    private static final int PICK_IMAGE_PROF_BANNER =101;
    private static final int EDIT_PROFILE =102;
    String imgType="";

    boolean menuShowed;
    FloatingActionButton fab;
    RecyclerView recyclerViewTes;
    FragmentTransaction transaction;
    //groupAdapter adapter;
    editSourceFragment editSource_Fragment;
    menuFragment menuFragment;


    //shared ke menu fragment
    public RecyclerAdapterTest adapterTest;
    public List<Zlink> zLink;
    public UserClient userClient;
    public Retrofit.Builder builder;

    public String token;
    int USER_ID;
    private static final String TAG = "TESTING";
    RetroConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String name = getSharedPreferences("PROFILE",MODE_PRIVATE).getString("PROFILE_NAME","Set Your Name");
        String desc = getSharedPreferences("PROFILE",MODE_PRIVATE).getString("PROFILE_DESC","Set your description here");


        //Connection
        connection = new RetroConnection();

        //TOKEN
        token = getSharedPreferences("TOKEN",MODE_PRIVATE).getString("TOKEN",null);



        recyclerViewTes = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuShowed){
                    transaction = getSupportFragmentManager().beginTransaction();
//                    transaction.commit();
                    transaction.setCustomAnimations(R.anim.animation_enter,R.anim.animation_exit_fragment);
                    transaction.hide(menuFragment);
                    transaction.commit();
                    rotateFab();
                }else{
                    openMenuFragment();
                    rotateFab();
                }

            }
        });

//        Z-VERSION



        userClient = connection.getConnection();

        /// FOR PROFILE UI ABOVE SEPARATOR//
        profileName =  findViewById(R.id.profileName);
        profileDesc =  findViewById(R.id.profileDesc);
        profileDesc.setMovementMethod(new ScrollingMovementMethod());
        imgProfpic =  findViewById(R.id.profilePicture);
        imgBannerProfPic =  findViewById(R.id.profileBanner);

        Bundle bundle = getIntent().getExtras();
        profileName.setText(name);
        profileDesc.setText(desc);

        picChangePopUp = new Dialog(this);
        editGroupNamePopup = new Dialog(this);
        View tb =  findViewById(R.id.menu_appbar);
        tb.bringToFront();
        RelativeLayout btnBack = tb.findViewById(R.id.btnBack);
        RelativeLayout btnEdit =  tb.findViewById(R.id.btnEditPofile);
        btnBack.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String groupJSON = getSharedPreferences("TOKEN",MODE_PRIVATE).getString("GROUPLIST",null);
        Type listType = new TypeToken<List<zGroup>>(){}.getType();
        zLink = new ArrayList<>();
        zLink = new Gson().fromJson(groupJSON,listType);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        adapterTest = new RecyclerAdapterTest(recyclerViewTes,zLink,MainActivity.this.findViewById(R.id.snackbar_container),token,MainActivity.this);
        recyclerViewTes.setAdapter(adapterTest);
        recyclerViewTes.setLayoutManager(layoutManager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapterTest,MainActivity.this));
        itemTouchHelper.attachToRecyclerView(recyclerViewTes);
        adapterTest.notifyDataSetChanged();
    }

    public void rotateFab(){
        if (menuShowed){
            final OvershootInterpolator interpolator = new OvershootInterpolator();
            ViewCompat.animate(fab).
                    rotation(0.0f).
                    withLayer().
                    setDuration(1000).
                    setInterpolator(interpolator).
                    start();

            menuShowed=false;
        }else{
            menuShowed=true;
            final OvershootInterpolator interpolator = new OvershootInterpolator();
            ViewCompat.animate(fab).
                    rotation(405f).
                    withLayer().
                    setDuration(1000).
                    setInterpolator(interpolator).
                    start();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            if(requestCode==1){
                ArrayList<Parcelable> sourceParcel = data.getParcelableArrayListExtra("SOURCES");
                zGroup groupParcel = data.getParcelableExtra("GROUP");

                ArrayList<zSource> arraySource = new ArrayList<>();
                for(int i =0;i<sourceParcel.size();i++){
                    arraySource.add((zSource) sourceParcel.get(i));
                }
                groupParcel.setzSource(arraySource);
                zLink.add(groupParcel);
                adapterTest.notifyItemInserted(zLink.size()-1);

            }else if (requestCode == PICK_IMAGE_PROF_PIC){
                imgType = "Profile";
                Uri source_uri = data.getData();
                Uri dest_uri = Uri.fromFile(new File(getCacheDir(),"cropped"));

                Crop.of(source_uri,dest_uri).asSquare().start(this);
                imgProfpic.setImageURI(Crop.getOutput(data));
                picChangePopUp.dismiss();
            }else if (requestCode == PICK_IMAGE_PROF_BANNER){
                Uri source_uri_banner = data.getData();
                Uri dest_uri_banner = Uri.fromFile(new File(getCacheDir(),"croppedBanner"));
                imgType = "Banner";
                Crop.of(source_uri_banner,dest_uri_banner).withAspect(3,1).start(this);
                imgBannerProfPic.setImageURI(Crop.getOutput(data));

                picChangePopUp.dismiss();
            }
            else if (requestCode == Crop.REQUEST_CROP){
                handle_crop(resultCode,data,imgType);
            }
        }
        else{
            Log.d(TAG, "onActivityResult: error bang, nga tau kenaps");
        }

    }

    public void handle_crop(int code,Intent result,String imgType){
        if (code == RESULT_OK){
            if(imgType == "Profile"){
                imgProfpic.setImageURI(Crop.getOutput(result));
            }
            else if (imgType.equals("Banner")){
                imgBannerProfPic.setImageURI(Crop.getOutput(result));

            }
        }
        else if (code ==Crop.RESULT_ERROR){
            Toast.makeText(this, "Error on crop", Toast.LENGTH_SHORT).show();
        }
    }

    public void openMenuFragment() {

        menuFragment = menuFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
//        transaction.addToBackStack(null);
        transaction.setCustomAnimations(R.anim.animation_enter,R.anim.animation_exit_fragment,R.anim.animation_pop_enter_fragment,R.anim.animation_pop_exit_animation);
        transaction.add(R.id.fragment_menu_container, menuFragment, "").commit();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        fab.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Logout akun")
                .setMessage("Apakah anda ingin kembali ke halaman login ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MainActivity.this, loginScreen.class));
                    }
                })
                .setNegativeButton("Tidak", null);
        builder.show();
    }

    public void openEditScreen(View v){
        Intent intent = new Intent(MainActivity.this, editProfileScreen.class);
        intent.putExtra("nama",profileName.getText().toString().trim());
        intent.putExtra("desc",profileDesc.getText().toString().trim());
        startActivityForResult(intent,EDIT_PROFILE);
    }
    public void keyboardDown(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }


//    DATA PASSING
//    @Override
//    public void passDataFromMenutoAct(zGroup group) {
//        zLink.add(group);
//        adapterTest.notifyItemInserted(zLink.size()-1);
//    }

    //    public void connectAPI(){
//        Retrofit.Builder builder = new Retrofit.Builder()
//                .baseUrl(ServerAPI.group_REST_API)
//                .addConverterFactory(GsonConverterFactory.create());
//
//        Retrofit retrofit = builder.build();
//        UserClient userClient = retrofit.create(UserClient.class);
//
//
//    }


}