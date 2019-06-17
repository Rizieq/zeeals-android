package com.example.user.zeeals.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.zeeals.R;
import com.example.user.zeeals.ResponsesAndRequest.Basic_Response;
import com.example.user.zeeals.ResponsesAndRequest.updateUser_Model;
import com.example.user.zeeals.activity.addGroupAndLinkFragmentHost;
import com.example.user.zeeals.adapter.MainActivity;
import com.example.user.zeeals.model.User;
import com.example.user.zeeals.service.RetroConnection;
import com.google.gson.Gson;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {

    EditText etPhone,etAddress,etName;
    TextView etEmail;
    LinearLayout btn_change_password;
    ConstraintLayout dim;

    RetroConnection conn;
    User user;
    String token;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userJSON = Objects.requireNonNull(getActivity()).getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE).getString("USER",null);
        user= new Gson().fromJson(userJSON,User.class);
        token = getActivity().getSharedPreferences("TOKEN", Context.MODE_PRIVATE).getString("TOKEN",null);
        conn = new RetroConnection();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        etEmail = view.findViewById(R.id.editAccount_email);
        etPhone = view.findViewById(R.id.editAccount_phone);
        etAddress = view.findViewById(R.id.editAccount_address);
        etName = view.findViewById(R.id.editAccount_name);
        dim = view.findViewById(R.id.semi_white_bg);

//        progressBar = view.findViewById(R.id.progress_bar_edit_account);
//        btn_save = view.findViewById(R.id.btnSaveAccount);
//        save = view.findViewById(R.id.editAccount_btn_save);
        btn_change_password = view.findViewById(R.id.btn_goto_change_password);

        initHint();
        setButtonOnclick();

        return view;
    }

    void initHint(){
        etEmail.setHint(user.getEmail());
        etPhone.setHint(user.getPhone());
        etAddress.setHint(user.getAddress());
        etName.setHint(user.getFullName());
//        progressBar.setVisibility(View.GONE);
    }

    void setButtonOnclick(){
        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), addGroupAndLinkFragmentHost.class).putExtra("menuType","changePassword"));
            }
        });
    }

    public void save(){

        dim.setAnimation(grow());

        if(etName.getHint()== null) etName.setHint("");
        if(etAddress.getHint() == null) etAddress.setHint("");
        if(etPhone.getHint() == null) etPhone.setHint("");

        String fullname = etName.getHint().toString();
        String address = etAddress.getHint().toString();
        String phone = etPhone.getHint().toString();


        if(!etName.getText().toString().equals("")) fullname=etName.getText().toString();
        if(!etPhone.getText().toString().equals("")) phone=etPhone.getText().toString();
        if(!etAddress.getText().toString().equals("")) address=etAddress.getText().toString();


        updateUser_Model updateUserModel = new updateUser_Model(fullname,address,phone);
        Call<Basic_Response> call = conn.getConnection().userUpdate(token,updateUserModel);
        final String finalAddress = address;
        final String finalFullname = fullname;
        final String finalPhone = phone;
        call.enqueue(new Callback<Basic_Response>() {
            @Override
            public void onResponse(@NonNull Call<Basic_Response> call, @NonNull Response<Basic_Response> response) {

                if(response.isSuccessful()){

                    ((MainActivity) Objects.requireNonNull(getActivity())).enableFab();
                    user.setAddress(finalAddress);
                    user.setFullName(finalFullname);
                    user.setPhone(finalPhone);
                    String userJSON = new Gson().toJson(user);
                    getActivity().getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE).edit().putString("USER",userJSON).apply();
                    Toasty.success(Objects.requireNonNull(getContext()), "Success!", Toast.LENGTH_SHORT, true).show();
                    dim.setAnimation(shrink());
                }else {
                    Toasty.error(Objects.requireNonNull(getContext()), "Update failed.", Toast.LENGTH_SHORT, true).show();
                    dim.setAnimation(shrink());
                }
                dim.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(@NonNull Call<Basic_Response> call, @NonNull Throwable t) {
                Toast.makeText(getContext(),"Connection Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    AnimationSet grow(){
        dim.setVisibility(View.VISIBLE);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setDuration(1000);
        return animationSet;
    }

    AnimationSet shrink(){
        AnimationSet animationSet= new AnimationSet(false);
        Animation fadeOut = new AlphaAnimation(1, 0);

        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setDuration(200);
        animationSet.setStartOffset(200);
        animationSet.addAnimation(fadeOut);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dim.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animationSet;
    }

}
