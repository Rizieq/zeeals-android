package com.example.user.zeeals.adapter;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

//import com.example.user.zeeals.adapter.AddNewGroup;
import com.example.user.zeeals.responses.IconList;
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

//    private TextView profileName, profileDesc;
//    Dialog picChangePopUp, editGroupNamePopup;
//    private ImageView imgProfpic,imgBannerProfPic;
//
//    private static final int PICK_IMAGE_PROF_PIC = 100;
//    private static final int PICK_IMAGE_PROF_BANNER =101;
//    private static final int EDIT_PROFILE =102;
//    String imgType="";
//
//    boolean menuShowed;
//    FloatingActionButton fab;
//    RecyclerView recyclerViewTes;
//    FragmentTransaction transaction;
//    //groupAdapter adapter;
//    editLinkFragment editSource_Fragment;
//    menuFragment menuFragment;


    //shared ke menu fragment
    public RecyclerAdapter_Main adapterTest;
    public List<Zlink> zLink;


    public String token;
    int USER_ID;
    private static final String TAG = "TESTING";
    RetroConnection connection;
    boolean fragment_in_home;


    ConstraintLayout btn_fragment_home,btn_fragment_account,bottom_nav;

    ImageButton btn_fragment_home2,btn_fragment_account2;
    RetroConnection conn;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment_in_home=true;


//        String name = getSharedPreferences("PROFILE",MODE_PRIVATE).getString("PROFILE_NAME","Set Your Name");
//        String desc = getSharedPreferences("PROFILE",MODE_PRIVATE).getString("PROFILE_DESC","Set your description here");
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
            Fragment hide = new accountFragment();
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right,R.anim.enter_from_left,R.anim.exit_to_left)
                    .hide(hide).show(show).replace(R.id.mainContainer,show).commit();

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
            Fragment show = new accountFragment();
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_right,R.anim.exit_to_right)
                    .hide(hide).show(show).replace(R.id.mainContainer,show).commit();
            btn_fragment_account2.setBackgroundColor(R.color.colorPrimary);
            btn_fragment_home2.setBackgroundColor(R.color.grey);
            btn_fragment_home.setBackgroundColor(R.color.grey);
            btn_fragment_account.setBackgroundColor(R.color.colorPrimary);
            fragment_in_home=false;

        }
    }




//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(resultCode==RESULT_OK){
//            if(requestCode==1){
//                ArrayList<Parcelable> sourceParcel = data.getParcelableArrayListExtra("SOURCES");
//                zGroup groupParcel = data.getParcelableExtra("GROUP");
//
//                ArrayList<zSource> arraySource = new ArrayList<>();
//                for(int i =0;i<sourceParcel.size();i++){
//                    arraySource.add((zSource) sourceParcel.get(i));
//                }
//                groupParcel.setzSource(arraySource);
//                zLink.add(groupParcel);
//                adapterTest.notifyItemInserted(zLink.size()-1);
//
//            }else if (requestCode == PICK_IMAGE_PROF_PIC){
//                imgType = "Profile";
//                Uri source_uri = data.getData();
//                Uri dest_uri = Uri.fromFile(new File(getCacheDir(),"cropped"));
//
//                Crop.of(source_uri,dest_uri).asSquare().start(this);
//                imgProfpic.setImageURI(Crop.getOutput(data));
//                picChangePopUp.dismiss();
//            }else if (requestCode == PICK_IMAGE_PROF_BANNER){
//                Uri source_uri_banner = data.getData();
//                Uri dest_uri_banner = Uri.fromFile(new File(getCacheDir(),"croppedBanner"));
//                imgType = "Banner";
//                Crop.of(source_uri_banner,dest_uri_banner).withAspect(3,1).start(this);
//                imgBannerProfPic.setImageURI(Crop.getOutput(data));
//
//                picChangePopUp.dismiss();
//            }
//            else if (requestCode == Crop.REQUEST_CROP){
//                handle_crop(resultCode,data,imgType);
//            }
//        }
//        else{
//            Log.d(TAG, "onActivityResult: error bang, nga tau kenaps");
//        }
//
//    }
//
//    public void handle_crop(int code,Intent result,String imgType){
//        if (code == RESULT_OK){
//            if(imgType == "Profile"){
//                imgProfpic.setImageURI(Crop.getOutput(result));
//            }
//            else if (imgType.equals("Banner")){
//                imgBannerProfPic.setImageURI(Crop.getOutput(result));
//
//            }
//        }
//        else if (code ==Crop.RESULT_ERROR){
//            Toast.makeText(this, "Error on crop", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void openMenuFragment() {
//
//        menuFragment = menuFragment.newInstance();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        transaction = fragmentManager.beginTransaction();
////        transaction.addToBackStack(null);
//        transaction.setCustomAnimations(R.anim.animation_enter,R.anim.animation_exit_fragment,R.anim.animation_pop_enter_fragment,R.anim.animation_pop_exit_animation);
//        transaction.add(R.id.fragment_menu_container, menuFragment, "").commit();
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        //super.onBackPressed();
//        fab.show();
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle("Logout akun")
//                .setMessage("Apakah anda ingin kembali ke halaman login ?")
//                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(MainActivity.this, loginScreen.class));
//                    }
//                })
//                .setNegativeButton("Tidak", null);
//        builder.show();
//    }
//
//    public void openEditScreen(View v){
////        Intent intent = new Intent(MainActivity.this, editProfileScreen.class);
////        intent.putExtra("nama",profileName.getText().toString().trim());
////        intent.putExtra("desc",profileDesc.getText().toString().trim());
////        startActivityForResult(intent,EDIT_PROFILE);
//    }
//    public void keyboardDown(){
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//    }
//
//
////    DATA PASSING
////    @Override
////    public void passDataFromMenutoAct(zGroup group) {
////        zLink.add(group);
////        adapterTest.notifyItemInserted(zLink.size()-1);
////    }
//
//    //    public void connectAPI(){
////        Retrofit.Builder builder = new Retrofit.Builder()
////                .baseUrl(ServerAPI.group_REST_API)
////                .addConverterFactory(GsonConverterFactory.create());
////
////        Retrofit retrofit = builder.build();
////        UserClient userClient = retrofit.create(UserClient.class);
////
////
////    }


}