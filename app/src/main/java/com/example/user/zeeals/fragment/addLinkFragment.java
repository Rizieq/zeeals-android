package com.example.user.zeeals.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.zeeals.R;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.model.zSource;
import com.example.user.zeeals.ResponsesAndRequest.PostLinkResponse;
import com.example.user.zeeals.service.RetroConnection;
import com.example.user.zeeals.service.UserClient;
import com.example.user.zeeals.util.NothingSelectedSpinnerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class addLinkFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "addLinkFragment";
    private Button btnAdd;
    private Spinner spinnerGroup;
    private EditText url, title;
    private List<Zlink> zLink;
    private ProgressBar progressBar;
    private ConstraintLayout message_layout;
    private EditText message, image_url;
    private String confirmed_message;


    RetroConnection conn;

    public addLinkFragment(){

    }
    public static addLinkFragment newInstance(){
        return new addLinkFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View addLinkView = inflater.inflate(R.layout.fragment_add_link, container, false);

        conn = new RetroConnection();
        String groupJSON = Objects.requireNonNull(getActivity()).getSharedPreferences("TOKEN",Context.MODE_PRIVATE).getString("GROUPLIST",null);
        Type listType = new TypeToken<List<zGroup>>(){}.getType();
        zLink = new ArrayList<>();
        zLink = new Gson().fromJson(groupJSON,listType);


        progressBar = addLinkView.findViewById(R.id.progress_bar_add_link);
        progressBar.setVisibility(View.GONE);

        ImageView btnBack = addLinkView.findViewById(R.id.btnBackAddLink);
        btnAdd = addLinkView.findViewById(R.id.btnAddAddLink);
        spinnerGroup = addLinkView.findViewById(R.id.spinnerAddLink);
        image_url = addLinkView.findViewById(R.id.etImageAddressAddLink);
        List<String> groupNames = new ArrayList<String>();
        for (int i=0;i<zLink.size();i++){
            groupNames.add(((zGroup)zLink.get(i)).getTitle());
        }

        ArrayAdapter<String> dataSpinner = new ArrayAdapter<>(Objects.requireNonNull(getContext()),android.R.layout.simple_spinner_item,groupNames);
        dataSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setPrompt("Select Group");
        spinnerGroup.setAdapter(new NothingSelectedSpinnerAdapter(dataSpinner, R.layout.contact_spinner_row_nothing_selected,getContext()));


        url = addLinkView.findViewById(R.id.etURLAddLink);
        title = addLinkView.findViewById(R.id.etTitleAddLink);
        message_layout = addLinkView.findViewById(R.id.addLink_message_layout);
        message=addLinkView.findViewById(R.id.addLink_message);
        message_layout.setVisibility(View.GONE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLink();
            }
        });

        setWatcher(url);




        return addLinkView;
    }

    @SuppressLint("SetTextI18n")
    private void saveLink(){
        int spinnerPosition = 0;
        boolean allRequired=true;

        //TODO set if spinner ==null, then link will goes to "Uncategorized" category
        if(spinnerGroup.getSelectedItem()==null){
            allRequired=false;
        }else{
            spinnerPosition = spinnerGroup.getSelectedItemPosition();
        }
        if(url.getText().toString().equals("")){
            url.setError("URL required !");
            allRequired=false;
        }
        if(title.getText().toString().equals("")){
            title.setError("Link title required !");
            allRequired=false;
        }

        if(allRequired){
            btnAdd.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            String link_key=url.getText().toString();
            String mUrl="";

            if(checkEmail(link_key)){
                confirmed_message = message.getText().toString();
                mUrl="mailto:"+link_key+"?subject=Hello"+title.getText().toString()+"?&body="+message.getText().toString();
            }else if(checkPhone(link_key)){
                if(link_key.length()>=10){
                    confirmed_message=message.getText().toString();
                    mUrl="https://api.whatsapp.com/send?phone="+link_key+"&text="+confirmed_message;
                }
            }else if(checkUrl(link_key)){
                if(!link_key.contains("http://")|| !link_key.contains("https://")){
                    mUrl="http://"+link_key;
                }
            }else{
                if((!link_key.contains("http://")|| !link_key.contains("https://"))&&(!link_key.contains(".com"))){
                    mUrl="http://"+link_key+".com";
                }else if(!link_key.contains(".com")){
                    mUrl=link_key+".com";
                }else if(!link_key.contains("http://")|| !link_key.contains("https://")){
                    mUrl="http://"+link_key;
                }
            }
            int groupID = ((zGroup)zLink.get(spinnerPosition-1)).getGroupLinkId();
            final zSource source = new zSource(1,groupID,link_key,url.getText().toString(),title.getText().toString(),0,image_url.getText().toString(),"v",1);
            source.setMessage(confirmed_message);
            source.setLinkKey(link_key);
            source.setUrl(mUrl);
            String token = Objects.requireNonNull(getActivity()).getSharedPreferences("TOKEN",Context.MODE_PRIVATE).getString("TOKEN",null);
            UserClient userClient = conn.getConnection();
            Call<PostLinkResponse> call = userClient.createLink(token,source);
            final int finalSpinnerPosition = spinnerPosition;
            call.enqueue(new Callback<PostLinkResponse>() {
                @Override
                public void onResponse(@NonNull Call<PostLinkResponse> call, @NonNull Response<PostLinkResponse> response) {
                    if(response.isSuccessful()){
                        zGroup group = ((zGroup)zLink.get(finalSpinnerPosition -1));
                        if(group.getChildLink()==null){
                            ArrayList<zSource> zSources = new ArrayList<>();
                            zSources.add(source);
                            group.setChildLink(zSources);
                        }else{
                            group.getChildLink().add(source);
                        }

                        assert response.body() != null;
                        source.setLinkId(response.body().getServe().getLinkId());

                        Gson gson = new Gson();
                        String json = gson.toJson(zLink);
                        Objects.requireNonNull(getActivity()).getSharedPreferences("TOKEN",MODE_PRIVATE).edit().putString("GROUPLIST",json).apply();
                        getActivity().finish();
                    }else{
                        Toast.makeText(getContext(),"Oops, Something went error !",Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(getActivity()).finish();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PostLinkResponse> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(),"Connection error !",Toast.LENGTH_SHORT).show();
                    Objects.requireNonNull(getActivity()).finish();
                }
            });
        }

    }

    public void setWatcher(final EditText text){
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String target = text.getText().toString();
                if(checkEmail(s)){
                    message_layout.setVisibility(View.VISIBLE);
                }else if(checkPhone(s)){
                    if(target.length()>=10){
                        message_layout.setVisibility(View.VISIBLE);
                    }
                }else{
                    message_layout.setVisibility(View.GONE);
                }
            }
        });
    }


    public static boolean checkEmail(CharSequence target){
        Log.d("TEXTWATCH", "checkEmail: "+target);
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean checkPhone (CharSequence target){
        Log.d("TEXTWATCH", "checkPhone: "+target);
        Pattern phone_pattern = Pattern.compile("^((\\+62 ((\\d{3}([ -]\\d{3,})([- ]\\d{4,})?)|(\\d+)))|(\\(\\d+\\) \\d+)|\\d{3}( \\d+)+|(\\d+[ -]\\d+)|\\d+)$");
        return phone_pattern.matcher(target).matches();
    }

    public static boolean checkUrl(CharSequence target){
        Pattern url_pattern= Pattern.compile("/^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$/i");
        Pattern url_pattern_2= Pattern.compile("/^[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$/i");
        if(url_pattern.matcher(target).matches()){
            return true;
        }else if(url_pattern_2.matcher(target).matches()) return true;
        else return false;
    }


}
