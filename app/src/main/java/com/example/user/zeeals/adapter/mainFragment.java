package com.example.user.zeeals.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.zeeals.R;
import com.example.user.zeeals.editProfileScreen;
import com.example.user.zeeals.fragment.editSourceFragment;
import com.example.user.zeeals.fragment.menuFragment;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.service.RetroConnection;
import com.example.user.zeeals.service.UserClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class mainFragment extends Fragment {
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
    String name,desc;
    RelativeLayout btn_editProfile;
    ImageView btn_editProfile_2;


    //groupAdapter adapter;
    editSourceFragment editSource_Fragment;
    com.example.user.zeeals.fragment.menuFragment menuFragment;


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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        name = getActivity().getSharedPreferences("PROFILE",MODE_PRIVATE).getString("PROFILE_NAME","Set Your Name");
        desc = getActivity().getSharedPreferences("PROFILE",MODE_PRIVATE).getString("PROFILE_DESC","Set your description here");


        //Connection
        connection = new RetroConnection();

        //TOKEN
        token = getActivity().getSharedPreferences("TOKEN",MODE_PRIVATE).getString("TOKEN",null);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);

        recyclerViewTes = view.findViewById(R.id.recycler_view);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuShowed){
                    transaction = getChildFragmentManager().beginTransaction();
//                    transaction.setCustomAnimations(R.anim.enter_from_bottom,R.anim.exit_to_right);
                    transaction.hide(menuFragment);
                    transaction.commit();
                    rotateFab();
                }else{
                    openMenuFragment();
                    rotateFab();
                }

            }
        });

        userClient = connection.getConnection();

        /// FOR PROFILE UI ABOVE SEPARATOR//
        profileName =  view.findViewById(R.id.profileName);
        profileDesc =  view.findViewById(R.id.profileDesc);
        profileDesc.setMovementMethod(new ScrollingMovementMethod());
        imgProfpic =  view.findViewById(R.id.profilePicture);
        imgBannerProfPic =  view.findViewById(R.id.profileBanner);
        View topBar = view.findViewById(R.id.menu_appbar);

        btn_editProfile = topBar.findViewById(R.id.btnEditPofile);
        btn_editProfile_2=topBar.findViewById(R.id.btnEditPofile_2);

        profileName.setText(name);
        profileDesc.setText(desc);

        picChangePopUp = new Dialog(view.getContext());
        editGroupNamePopup = new Dialog(view.getContext());
        View tb =  view.findViewById(R.id.menu_appbar);
        tb.bringToFront();
        RelativeLayout btnBack = tb.findViewById(R.id.btnBack);
        RelativeLayout btnEdit =  tb.findViewById(R.id.btnEditPofile);
        btnBack.setVisibility(View.GONE);

        btn_editProfile.setOnClickListener(openEditProfile);
        btn_editProfile_2.setOnClickListener(openEditProfile);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        String groupJSON = getActivity().getSharedPreferences("TOKEN",MODE_PRIVATE).getString("GROUPLIST",null);
        Type listType = new TypeToken<List<zGroup>>(){}.getType();
        zLink = new ArrayList<>();
        zLink = new Gson().fromJson(groupJSON,listType);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapterTest = new RecyclerAdapterTest(recyclerViewTes,zLink,token,getContext());
        recyclerViewTes.setAdapter(adapterTest);
        recyclerViewTes.setLayoutManager(layoutManager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapterTest,getContext()));
        itemTouchHelper.attachToRecyclerView(recyclerViewTes);
        adapterTest.notifyDataSetChanged();

    }

    public void openMenuFragment() {

        menuFragment = menuFragment.newInstance();
        FragmentManager fragmentManager = getChildFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom,R.anim.enter_from_bottom);
        transaction.replace(R.id.fragment_menu_container, menuFragment).show(menuFragment).commit();

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

    public void openEditScreen(){
        Intent intent = new Intent(getContext(), editProfileScreen.class);
        intent.putExtra("nama",profileName.getText().toString().trim());
        intent.putExtra("desc",profileDesc.getText().toString().trim());
        startActivityForResult(intent,EDIT_PROFILE);
    }

    private View.OnClickListener openEditProfile= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openEditScreen();
        }
    };


}
