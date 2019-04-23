package com.example.user.zeeals.adapter;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.zeeals.R;
import com.example.user.zeeals.fragment.editSourceFragment;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.service.RetroConnection;
import com.example.user.zeeals.service.UserClient;

import java.util.List;

import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class accountFragment extends Fragment {
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

//        name = getActivity().getSharedPreferences("PROFILE",MODE_PRIVATE).getString("PROFILE_NAME","Set Your Name");
//        desc = getActivity().getSharedPreferences("PROFILE",MODE_PRIVATE).getString("PROFILE_DESC","Set your description here");
//
//
//        //Connection
//        connection = new RetroConnection();
//
//        //TOKEN
//        token = getActivity().getSharedPreferences("TOKEN",MODE_PRIVATE).getString("TOKEN",null);





    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        return view;

    }



}
