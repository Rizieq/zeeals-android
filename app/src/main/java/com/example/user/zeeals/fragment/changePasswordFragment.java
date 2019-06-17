package com.example.user.zeeals.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.user.zeeals.R;
import com.example.user.zeeals.ResponsesAndRequest.Basic_Response;
import com.example.user.zeeals.ResponsesAndRequest.ChangePassword_Model;
import com.example.user.zeeals.service.RetroConnection;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class changePasswordFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    static Context context;

    EditText oldPassword,newPassword,confirmPassword;
    RelativeLayout btn_save;
    ProgressBar progressBar;
    Button save;
    ImageView back;

    boolean allRequirement=false;
    String token;
    RetroConnection conn;

    public changePasswordFragment() {
    }

    public static changePasswordFragment newInstance(Context c) {
        changePasswordFragment fragment = new changePasswordFragment();
        context = c;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conn = new RetroConnection();
        token = Objects.requireNonNull(getActivity()).getSharedPreferences("TOKEN", Context.MODE_PRIVATE).getString("TOKEN",null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        oldPassword = view.findViewById(R.id.changePassword_oldPassword);
        newPassword = view.findViewById(R.id.changePassword_newPassword);
        confirmPassword = view.findViewById(R.id.changePassword_confirmPassword);
        btn_save = view.findViewById(R.id.btn_save_change_password);
        progressBar = view.findViewById(R.id.progress_bar_change_password);
        save = view.findViewById(R.id.changePassword_btn_save);
        back = view.findViewById(R.id.changePassword_btn_back);
        progressBar.setVisibility(View.GONE);
        passwordWatcher();
        setButtonClick();

        return view;
    }

    void passwordWatcher(){
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(newPassword.getText().toString().length()<8){
                    newPassword.setError("Min. password length 8");
                    allRequirement=false;
                }else allRequirement=true;
            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!confirmPassword.getText().toString().equals(newPassword.getText().toString())){
                    confirmPassword.setError("Not match");
                    allRequirement=false;
                }else allRequirement=true;
            }
        });
    }

    void setButtonClick(){
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).finish();
            }
        });
    }

    /** Clicking method Area **/
    void save(){
        if(confirmPassword.getText().toString().equals("")){
            confirmPassword.setError("Please fill this field");
            allRequirement=false;
        } else allRequirement=true;
        if(newPassword.getText().toString().equals("")){
            newPassword.setError("Please fill this field");
            allRequirement=false;
        }else allRequirement=true;
        if(allRequirement){
            save.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            ChangePassword_Model passwordModel = new ChangePassword_Model(oldPassword.getText().toString(),newPassword.getText().toString());
            Call<Basic_Response> call = conn.getConnection().changepassword(token,passwordModel);
            call.enqueue(new Callback<Basic_Response>() {
                @SuppressLint("ShowToast")
                @Override
                public void onResponse(@NonNull Call<Basic_Response> call, @NonNull Response<Basic_Response> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),"Password updated!",Toast.LENGTH_SHORT);
                    }else Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),"Wrong password!",Toast.LENGTH_SHORT);
                    save.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    getActivity().finish();
                }

                @SuppressLint("ShowToast")
                @Override
                public void onFailure(@NonNull Call<Basic_Response> call, @NonNull Throwable t) {
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),"Connection error",Toast.LENGTH_SHORT);
                }
            });

        }
    }
}
