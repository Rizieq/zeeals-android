package com.example.user.zeeals.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
//import android.widget.Switch;
import android.widget.Switch;
import android.widget.Toast;

import com.example.user.zeeals.R;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.model.zSource;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class editLinkFragment extends Fragment {
//    private static final String TAG = "addLinkFragment";
    private Button btnAdd;
    private Spinner spinnerGroup;
    private EditText url, urlImage, title,message;
    private ConstraintLayout message_layout;
    private List<Zlink> zLink;
    private ProgressBar progressBar;
    private Switch status;

    private static int groupId;
    private static int linkId;
    private int groupPosition,linkPosition;

    RetroConnection conn;
    private String token,confirmed_message;;
    private UserClient userClient;


    public editLinkFragment() {
        // Required empty public constructor
    }

    public static editLinkFragment newInstance(int[] position) {
        editLinkFragment fragment = new editLinkFragment();
        groupId = position[0];
        linkId = position[1];
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = Objects.requireNonNull(getActivity()).getSharedPreferences("TOKEN",Context.MODE_PRIVATE).getString("TOKEN",null);
        conn = new RetroConnection();
        userClient = conn.getConnection();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_source, container, false);
        progressBar = view.findViewById(R.id.progress_bar_edit_link);
        progressBar.setVisibility(View.GONE);

        ImageView btnBack = view.findViewById(R.id.btnBackEditLink);
        btnAdd = view.findViewById(R.id.btnSaveEditLink);
        ImageView btnDelete = view.findViewById(R.id.btnDeleteEditLink);

        spinnerGroup = view.findViewById(R.id.spinnerEditLink);
        url = view.findViewById(R.id.etURLEditLink);
        urlImage = view.findViewById(R.id.etImageAddressEditLink);
        title = view.findViewById(R.id.etTitleEditLink);
        message = view.findViewById(R.id.editLink_message);
        message_layout = view.findViewById(R.id.editLink_message_layout);
        status = view.findViewById(R.id.editLink_show);

        /* initializing Hint*/
        initHint();
        List<String> groupNames = new ArrayList<>();
        for (int i=0;i<zLink.size();i++){
            groupNames.add(((zGroup)zLink.get(i)).getTitle());
        }

        ArrayAdapter<String> dataSpinner = new ArrayAdapter<>(Objects.requireNonNull(getContext()),android.R.layout.simple_spinner_item,groupNames);
        dataSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setPrompt("Select Group");
        spinnerGroup.setAdapter(new NothingSelectedSpinnerAdapter(dataSpinner, R.layout.contact_spinner_row_nothing_selected,getContext()));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLink();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        return view;

    }


    public void initHint(){

        String groupJSON = Objects.requireNonNull(getActivity()).getSharedPreferences("TOKEN",Context.MODE_PRIVATE).getString("GROUPLIST",null);
        Type listType = new TypeToken<List<zGroup>>(){}.getType();
        zLink = new ArrayList<>();
        zLink = new Gson().fromJson(groupJSON,listType);

        assert zLink != null;
        for(int i = 0; i<zLink.size(); i++){
            if(((zGroup)zLink.get(i)).getGroupLinkId()==groupId){
                groupPosition=i;
                break;
            }
        }
        zGroup zGroup = ((zGroup)zLink.get(groupPosition));
        for(int i=0;i<zGroup.getChildLink().size();i++){
            if(zGroup.getChildLink().get(i).getLinkId()==linkId){
                linkPosition=i;
                break;
            }
        }

        zSource zSource = zGroup.getChildLink().get(linkPosition);
        title.setHint(zSource.getTitle());
        url.setHint(zSource.getLinkKey());
        urlImage.setHint(zSource.getAvatar());
        if(zSource.getStatus()==1)status.setChecked(true);
        else status.setChecked(false);

        if(zSource.getMessage()!=null ){
            message_layout.setVisibility(View.VISIBLE);
            message.setHint(zSource.getMessage());
            confirmed_message = zSource.getMessage();
        }else message_layout.setVisibility(View.GONE);
        spinnerGroup.post(new Runnable() {
            @Override
            public void run() {
                spinnerGroup.setSelection(groupPosition+1);
            }
        });
        setWatcher(url);
    }

    public void saveLink(){
        zGroup oldGroup = ((zGroup)zLink.get(groupPosition));
        zGroup newGroup = ((zGroup)zLink.get(spinnerGroup.getSelectedItemPosition()-1));

        zSource zSources = oldGroup.getChildLink().get(linkPosition);
        String mtitle = title.getHint().toString();
        String link_key = url.getHint().toString();
        String mUrlImage=urlImage.getHint().toString();
        String mUrl="";


        if(!title.getText().toString().equals("")) mtitle = title.getText().toString();
        if(!url.getText().toString().equals("")) link_key = url.getText().toString();
        if(!urlImage.getText().toString().equals("")) mUrlImage = urlImage.getText().toString();

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

        zSources.setTitle(mtitle);
        zSources.setUrl(mUrl);
        zSources.setLinkKey(link_key);
        zSources.setGroupLinkId(newGroup.getGroupLinkId());
        zSources.setMessage(confirmed_message);
        if(status.isChecked()) zSources.setStatus(1);
        else zSources.setStatus(0);


        newGroup.getChildLink().add(zSources);
        oldGroup.getChildLink().remove(linkPosition);

        btnAdd.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Call<zSource> call = userClient.updateLink(token,zSources);
        call.enqueue(new Callback<zSource>() {
            @Override
            public void onResponse(@NonNull Call<zSource> call, @NonNull Response<zSource> response) {
                if (response.isSuccessful()){
                    Gson gson = new Gson();
                    String json = gson.toJson(zLink);
                    Objects.requireNonNull(getActivity()).getSharedPreferences("TOKEN",MODE_PRIVATE).edit().putString("GROUPLIST",json).apply();
                    getActivity().finish();
                }else{
                    Toast.makeText(getContext(),"Connection error !",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<zSource> call, @NonNull Throwable t) {
                Toast.makeText(getContext(),"Connection error !",Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void delete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle("Hapus Group")
                .setMessage("Apakah anda yakin menghapus group ini ? ")
                .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        zSource deletePosition = ((zGroup)zLink.get(groupPosition)).getChildLink().get(linkPosition);
                        Call<ResponseBody> call = userClient.deleteLink2(token,deletePosition);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                if(response.isSuccessful()){
                                    zGroup zGroup = ((zGroup)zLink.get(groupPosition));
                                    zGroup.getChildLink().remove(linkPosition);

                                    Gson gson = new Gson();
                                    String json = gson.toJson(zLink);
                                    Objects.requireNonNull(getActivity()).getSharedPreferences("TOKEN",MODE_PRIVATE).edit().putString("GROUPLIST",json).apply();

                                    getActivity().finish();
                                }else{
                                    Toast.makeText(getContext(),"Failed to delete group",Toast.LENGTH_SHORT).show();
                                    Objects.requireNonNull(getActivity()).finish();
                                }

                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                Toast.makeText(getContext(),"Failed to delete group",Toast.LENGTH_SHORT).show();
                                Objects.requireNonNull(getActivity()).finish();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel",null).setCancelable(false);
        AlertDialog alertOut = builder.create();
        alertOut.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
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
