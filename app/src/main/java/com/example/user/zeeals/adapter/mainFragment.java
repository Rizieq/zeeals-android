package com.example.user.zeeals.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.zeeals.R;
import com.example.user.zeeals.editProfileScreen;
import com.example.user.zeeals.model.User;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.service.RetroConnection;
import com.example.user.zeeals.service.UserClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import static android.content.Context.MODE_PRIVATE;

public class mainFragment extends Fragment{
    private TextView profileName, profileDesc;
    Dialog picChangePopUp, editGroupNamePopup;
    private ImageView imgProfpic,imgBannerProfPic;
    private static final int EDIT_PROFILE =102;

    boolean menuShowed;
    RecyclerView recyclerViewTes;
    FragmentTransaction transaction;
    RelativeLayout btn_editProfile;
    ImageView btn_editProfile_2;
    TextView account_url;

    com.example.user.zeeals.fragment.menuFragment menuFragment;

    //shared ke menu fragment
    public RecyclerAdapter_Main adapterTest;
    public List<Zlink> zLink;
    public UserClient userClient;

    public String token,name,desc,profImg,bannerImg;
    RetroConnection connection;
    ConstraintLayout dim;
    View thisView;
    ArrayList<Uri> listLinkUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        name = Objects.requireNonNull(getActivity()).getSharedPreferences("PROFILE",MODE_PRIVATE).getString("PROFILE_NAME","Set Your Name");
        desc = getActivity().getSharedPreferences("PROFILE",MODE_PRIVATE).getString("PROFILE_DESC","Set your description here");

        //Connection
        connection = new RetroConnection();

        //TOKEN
        token = getActivity().getSharedPreferences("TOKEN",MODE_PRIVATE).getString("TOKEN",null);
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        dim = view.findViewById(R.id.semi_white_bg);
        recyclerViewTes = view.findViewById(R.id.recycler_view);
        account_url = view.findViewById(R.id.account_url);
        userClient = connection.getConnection();
        listLinkUri = new ArrayList<>();

        initProfile(view);
        initListener();
        String userJSON = Objects.requireNonNull(getActivity()).getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE).getString("USER",null);
        User user= new Gson().fromJson(userJSON, User.class);
        account_url.setText(String.format("zeeals.link/%s", user.getAccount().get(0).getMainUrl()));
        thisView=view;

        return view;
    }

    private void initProfile(View view){
        profileName =  view.findViewById(R.id.profileName);
        profileDesc =  view.findViewById(R.id.profileDesc);
        profileDesc.setMovementMethod(new ScrollingMovementMethod());
        imgProfpic =  view.findViewById(R.id.profilePicture);
        imgBannerProfPic =  view.findViewById(R.id.profileBanner);
        if(profImg != null) imgProfpic.setImageURI(Uri.parse(profImg));
        if(bannerImg != null) imgBannerProfPic.setImageURI(Uri.parse(bannerImg));


        View topBar = view.findViewById(R.id.menu_appbar);
        topBar.bringToFront();
        btn_editProfile = topBar.findViewById(R.id.btnEditPofile);
        btn_editProfile_2=topBar.findViewById(R.id.btnEditPofile_2);

        profileName.setText(name);
        profileDesc.setText(desc);

        picChangePopUp = new Dialog(view.getContext());
        editGroupNamePopup = new Dialog(view.getContext());
        RelativeLayout btnBack = topBar.findViewById(R.id.btnBack);
        btnBack.setVisibility(View.GONE);

        btn_editProfile.setOnClickListener(openEditProfile);
        btn_editProfile_2.setOnClickListener(openEditProfile);
    }
    private void initListener(){
        dim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) Objects.requireNonNull(getActivity())).rotateFab();
                ((MainActivity)getActivity()).hideMenu();

                dim.setVisibility(View.GONE);
            }
        });
    }


    public void dimLayout(boolean on){
        dim = thisView.findViewById(R.id.semi_white_bg);
        if(on) dim.setVisibility(View.VISIBLE);
        else dim.setVisibility(View.GONE);
    }


    @Override
    public void onStart() {
        super.onStart();
        String groupJSON = Objects.requireNonNull(getActivity()).getSharedPreferences("TOKEN",MODE_PRIVATE).getString("GROUPLIST",null);
        profImg = getActivity().getSharedPreferences("PROFILE",MODE_PRIVATE).getString("PROFILE_URI",null);
        bannerImg = getActivity().getSharedPreferences("PROFILE",MODE_PRIVATE).getString("BANNER_URI",null);
        if(profImg != null) imgProfpic.setImageURI(Uri.parse(profImg));
        if(bannerImg != null) imgBannerProfPic.setImageURI(Uri.parse(bannerImg));
//        fab.setVisibility(View.VISIBLE);
        if(groupJSON!=null){
            Type listType = new TypeToken<List<zGroup>>(){}.getType();
            zLink = new ArrayList<>();
            zLink = new Gson().fromJson(groupJSON,listType);
            setupRecycler();
        }
        if(menuShowed){
            transaction = getChildFragmentManager().beginTransaction();
            transaction.hide(menuFragment);
            transaction.commit();
//            dim.setVisibility(View.GONE);
//            rotateFab();
        }


    }

    private void setupRecycler(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        adapterTest = new RecyclerAdapter_Main(recyclerViewTes,zLink,token,getContext());
        recyclerViewTes.setAdapter(adapterTest);
        recyclerViewTes.setLayoutManager(layoutManager);




            adapterTest.notifyDataSetChanged();
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
