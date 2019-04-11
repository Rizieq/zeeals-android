package com.example.user.zeeals.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.user.zeeals.fragment.editSourceFragment;
import com.example.user.zeeals.loginScreen;
import com.example.user.zeeals.model.Message;
import com.example.user.zeeals.model.zGroupList;
import com.example.user.zeeals.service.UserClient;
import com.example.user.zeeals.util.ServerAPI;
import com.soundcloud.android.crop.Crop;

import java.io.File;
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
    String imgType="";

    boolean menuShowed;
    FloatingActionButton fab;
    RecyclerView recyclerViewTes;
    FragmentTransaction transaction;
    //groupAdapter adapter;
    editSourceFragment editSource_Fragment;
    menuFragment menuFragment;
    RecyclerAdapterTest adapterTest;
    List<Zlink> zLink;
    Context contextr;

    String token;
    private static final String TAG = "TESTING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //TOKEN
        token = getIntent().getStringExtra("TOKEN");


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
        zLink = new ArrayList<>();
        zSource zSource1 = new zSource(0,"Twitter","twitter.com/eldirohmanur",0);
        zSource zSource2 = new zSource(1,"Facebook","facebook.com/eldirohmanur",0);
        zSource zSource3 = new zSource(0,"Telkom","telyu.com/eldirohmanur",1);
        zSource zSource4 = new zSource(1,"KONG","kongs.com/eldirohmanur",1);
        zSource zSource5 = new zSource(2,"HohoHehe","HohoHehe.com/eldirohmanur",1);

        ArrayList<zSource> zSourceList1 = new ArrayList<>();
        zSourceList1.add(zSource1); zSourceList1.add(zSource2);
        ArrayList<zSource> zSourceList2 = new ArrayList<>();
        zSourceList2.add(zSource3); zSourceList2.add(zSource4); zSourceList2.add(zSource5);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(ServerAPI.zeealseRESTAPI)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        UserClient userClient = retrofit.create(UserClient.class);

        Call<zGroupList> call = userClient.showGroup(token);
        call.enqueue(new Callback<zGroupList>() {
            @Override
            public void onResponse(Call<zGroupList> call, retrofit2.Response<zGroupList> response) {
                if (response.isSuccessful()) {
                    ArrayList<zGroup> responseGroup = response.body().getGroupList();
                    for(zGroup g : responseGroup){
                        zGroup g1=new zGroup(g.getId(),g.getUrl_id(),g.getOrientation(),g.getTitle(),g.getIcon(),g.getPosition(),g.getStatus(),g.getCreated_at(),g.getCreated_at());
                        zLink.add(g1);
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    adapterTest = new RecyclerAdapterTest(recyclerViewTes,zLink,MainActivity.this.findViewById(R.id.snackbar_container),token,MainActivity.this);
                    recyclerViewTes.setAdapter(adapterTest);
                    recyclerViewTes.setLayoutManager(layoutManager);

                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapterTest,MainActivity.this));
                    itemTouchHelper.attachToRecyclerView(recyclerViewTes);
                }
                else {

                }
            }
            @Override
            public void onFailure(Call<zGroupList> call, Throwable t) {

            }
        });




        zGroup zGroup1 = new zGroup(0,zSourceList1,"Social");
        zGroup zGroup2 = new zGroup(1,zSourceList2,"Work");
        zGroup zGroup3 = new zGroup(2,"Tes");

//        zLink.add(zGroup1);
//        zLink.add(zGroup2);
//        zLink.add(zGroup3);




//        Z-VERSION  END ---------------------------

        /// FOR PROFILE UI ABOVE SEPARATOR//
        profileName =  findViewById(R.id.profileName);
        profileDesc =  findViewById(R.id.profileDesc);
        profileDesc.setMovementMethod(new ScrollingMovementMethod());
        imgProfpic =  findViewById(R.id.profilePicture);
        imgBannerProfPic =  findViewById(R.id.profileBanner);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            String nama = bundle.getString("name");
            String desc = bundle.getString("desc");

            profileName.setText(nama);
            profileDesc.setText(desc);
        }

        picChangePopUp = new Dialog(this);
        editGroupNamePopup = new Dialog(this);
        View tb =  findViewById(R.id.menu_appbar);
        tb.bringToFront();
        RelativeLayout btnBack = tb.findViewById(R.id.btnBack);
        RelativeLayout btnEdit =  tb.findViewById(R.id.btnEditPofile);
        btnBack.setVisibility(View.GONE);
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
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                ArrayList<Parcelable> sourceParcel = data.getParcelableArrayListExtra("SOURCES");
                zGroup groupParcel = data.getParcelableExtra("GROUP");

                ArrayList<zSource> arraySource = new ArrayList<>();
                for(int i =0;i<sourceParcel.size();i++){
                    arraySource.add((zSource) sourceParcel.get(i));
                }
                groupParcel.setzSource(arraySource);

                zLink.add(groupParcel);
                adapterTest.notifyItemInserted(zLink.size()-1);
            }
            if(requestCode==RESULT_CANCELED){
                Log.d(TAG, "onActivityResult: CANCELED");
            }
        }

        else if (resultCode == RESULT_OK){
            if (requestCode == PICK_IMAGE_PROF_PIC){
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
        AlertDialog alertShow = builder.show();
    }

    public void openEditScreen(View v){
        Intent intent = new Intent(MainActivity.this, editProfileScreen.class);
        intent.putExtra("nama",profileName.getText().toString().trim());
        intent.putExtra("desc",profileDesc.getText().toString().trim());
        startActivity(intent);
    }
    public void keyboardDown(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void connectAPI(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(ServerAPI.group_REST_API)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        UserClient userClient = retrofit.create(UserClient.class);


    }


}