package com.example.user.zeeals.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.zeeals.R;
import com.example.user.zeeals.ResponsesAndRequest.Basic_Response;
import com.example.user.zeeals.ResponsesAndRequest.updateUser_Model;
import com.example.user.zeeals.activity.addGroupAndLinkFragmentHost;
import com.example.user.zeeals.adapter.MainActivity;
import com.example.user.zeeals.adapter.RecyclerAdapter_Main;
import com.example.user.zeeals.fragment.editLinkFragment;
import com.example.user.zeeals.model.User;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.service.RetroConnection;
import com.example.user.zeeals.service.UserClient;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserFragment extends Fragment {

    EditText etPhone,etAddress,etName;
    TextView etEmail;
//    ProgressBar progressBar;
//    RelativeLayout btn_save;
//    Button save;
    LinearLayout btn_change_password;

    RetroConnection conn;
    User user;
    String token;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userJSON = getActivity().getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE).getString("USER",null);
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

//        btn_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                save.setVisibility(View.GONE);
////                progressBar.setVisibility(View.VISIBLE);
//                String fullname = etName.getHint().toString();
//                String address = etAddress.getHint().toString();
//                String phone = etPhone.getHint().toString();
//
//                if(!etName.getText().toString().equals("")) fullname=etName.getText().toString();
//                if(!etPhone.getText().toString().equals("")) phone=etPhone.getText().toString();
//                if(!etAddress.getText().toString().equals("")) address=etAddress.getText().toString();
//
//
//                updateUser_Model updateUserModel = new updateUser_Model(fullname,address,phone);
//                Call<Basic_Response> call = conn.getConnection().userUpdate(token,updateUserModel);
//                final String finalAddress = address;
//                final String finalFullname = fullname;
//                final String finalPhone = phone;
//                call.enqueue(new Callback<Basic_Response>() {
//                    @Override
//                    public void onResponse(Call<Basic_Response> call, Response<Basic_Response> response) {
//                        if(response.isSuccessful()){
//                            user.setAddress(finalAddress);
//                            user.setFullName(finalFullname);
//                            user.setPhone(finalPhone);
//                            String userJSON = new Gson().toJson(user);
//                            getActivity().getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE).edit().putString("USER",userJSON).apply();
//                            Toast.makeText(getContext(),"Update success !",Toast.LENGTH_SHORT).show();
//                        }else Toast.makeText(getContext(),"Update failed !",Toast.LENGTH_SHORT).show();
////                        save.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onFailure(Call<Basic_Response> call, Throwable t) {
//                        Toast.makeText(getContext(),"Connection Errpr",Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
    }

    public void save(){
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
            public void onResponse(Call<Basic_Response> call, Response<Basic_Response> response) {
                if(response.isSuccessful()){
                    ((MainActivity)getActivity()).fabProgressCircleEnd();
                    ((MainActivity)getActivity()).enableFab();
                    user.setAddress(finalAddress);
                    user.setFullName(finalFullname);
                    user.setPhone(finalPhone);
                    String userJSON = new Gson().toJson(user);
                    getActivity().getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE).edit().putString("USER",userJSON).apply();
//                    Toast.makeText(getContext(),"Update success !",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"Update failed !",Toast.LENGTH_SHORT).show();
                    ((MainActivity)getActivity()).fabProgressCancel();
                }

            }

            @Override
            public void onFailure(Call<Basic_Response> call, Throwable t) {
                Toast.makeText(getContext(),"Connection Errpr",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
