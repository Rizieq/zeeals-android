package com.example.user.zeeals.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.zeeals.R;
import com.example.user.zeeals.adapter.IconAdapter;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.service.RetroConnection;
import com.example.user.zeeals.util.NothingSelectedSpinnerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class editGroupFragment extends Fragment {
    private static final String TAG = "editGroupFragment";

    private EditText group_title;
    private Button  btn_save_group;
    private Spinner orientationSpinner;
    private Switch show;
    static int position;
    private zGroup zGroup;
    String rawIcon;
    ProgressBar bar;
    Button delete;
    Button back;
    String token = Objects.requireNonNull(getActivity()).getSharedPreferences("TOKEN",MODE_PRIVATE).getString("TOKEN",null);

    RetroConnection conn;
    ArrayList<String> iconList;

    TextView prevGridText;
    int prevGridPosition;


    private List<Zlink> zlinks;

    public editGroupFragment() {

        // Required empty public constructor
    }

    public static editGroupFragment newInstance(int posisi) {
        editGroupFragment fragment = new editGroupFragment();
        position=posisi;
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String groupJSON = Objects.requireNonNull(getActivity()).getSharedPreferences("TOKEN",MODE_PRIVATE).getString("GROUPLIST",null);
        token = getActivity().getSharedPreferences("TOKEN",MODE_PRIVATE).getString("TOKEN",null);

        Type listType = new TypeToken<List<zGroup>>(){}.getType();
        zlinks = new ArrayList<>();
        zlinks = new Gson().fromJson(groupJSON,listType);
        assert zlinks != null;
        zGroup=(zGroup) zlinks.get(position);

        conn = new RetroConnection();
        String iconJSON= getActivity().getSharedPreferences("ICON",MODE_PRIVATE).getString("ICON",null);
        iconList = new Gson().fromJson(iconJSON,ArrayList.class);
        rawIcon=zGroup.getUnicode();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_group, container, false);
        group_title=view.findViewById(R.id.editGroup_title);
        btn_save_group=view.findViewById(R.id.editGroup_save_btn);
        orientationSpinner=view.findViewById(R.id.editGroup_orientation);
        show=view.findViewById(R.id.editGroup_show);
        GridView icon = view.findViewById(R.id.editGroup_icon);
        bar =view.findViewById(R.id.progress_bar_edit_group);
        bar.setVisibility(View.GONE);
        delete=view.findViewById(R.id.editGroup_delete_btn);
        back=view.findViewById(R.id.editGroup_btn_back);

        IconAdapter iconAdapter = new IconAdapter(getContext(),iconList);
        icon.setAdapter(iconAdapter);



        ArrayAdapter<CharSequence> adapterGrid = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()), R.array.grid, android.R.layout.simple_spinner_item);
        adapterGrid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        orientationSpinner.setPrompt("Select your Grid");
        orientationSpinner.setAdapter(new NothingSelectedSpinnerAdapter(adapterGrid, R.layout.contact_spinner_row_nothing_selected,getContext()));


        group_title.setHint(zGroup.getTitle());
        if(zGroup.getStatus()==0){
            show.setChecked(false);
        }else show.setChecked(true);

        if(zGroup.getOrientation()=='h'){
            orientationSpinner.setSelection(1);
        }else orientationSpinner.setSelection(2);

        rawIcon=zGroup.getUnicode();





        icon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView iconText = view.findViewById(R.id.tv_icon);
                if(position!=prevGridPosition){
                    if(prevGridText==null){
                        prevGridText=iconText;
                        iconText.setTextColor(Color.WHITE);

                    }else{
                        prevGridText.setTextColor(Color.parseColor("#717171"));
                        iconText.setTextColor(Color.WHITE);
                        prevGridText=iconText;
                    }


                }
                rawIcon=iconList.get(position);
            }
        });


        btn_save_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                zGroup newGroup = (zGroup) zlinks.get(position);
                final String title;
                final char orientation;
                final int mShow;
                final String updated_at;
                token = Objects.requireNonNull(getActivity()).getSharedPreferences("TOKEN",MODE_PRIVATE).getString("TOKEN",null);

                if(group_title.getText().toString().equals("")){
                    title=group_title.getHint().toString();
                }else{
                    title =group_title.getText().toString();} 
                newGroup.setTitle(title);

                if(orientationSpinner.getSelectedItem().toString().equals("Horizontal")){
                    orientation='h';
                }else orientation='v';
                newGroup.setOrientation(orientation);

                if(show.isChecked()){
                    mShow=1;
                }else mShow=0;
                newGroup.setStatus(mShow);

                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date currentTime = Calendar.getInstance().getTime();
                updated_at= sdf.format(currentTime);
                newGroup.setUpdatedAt(updated_at);
                newGroup.setUnicode(rawIcon);

                delete.setEnabled(false);
                delete.setClickable(false);
                back.setEnabled(false);
                back.setClickable(false);
                btn_save_group.setVisibility(View.GONE);
                bar.setVisibility(View.VISIBLE);

                /*Saving Progress*/
                Call<ResponseBody> call = conn.getConnection().update(token,newGroup);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                        String[] group = {title,String.valueOf(orientation),rawIcon,Integer.toString(mShow),updated_at};
                        if(response.isSuccessful()){
                            Gson gson = new Gson();
                            String json = gson.toJson(zlinks);
                            getActivity().getSharedPreferences("TOKEN",MODE_PRIVATE).edit().putString("GROUPLIST",json).apply();
                            getActivity().finish();
                        }else{
                            try {
                                assert response.body() != null;
                                Toast.makeText(getContext(),response.body().string(),Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });



            }


        });

        /*delete button*/
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });

        /*back button*/
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).finish();
            }
        });

        return view;

    }


    public void deleteItem(){
        Log.d(TAG, "deleteItem: clicked");
        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));

        builder.setTitle("Hapus Group")
                .setMessage("Apakah anda yakin menghapus group ini ? ")
                .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        btn_save_group.setVisibility(View.GONE);
                        btn_save_group.setEnabled(false);
                        btn_save_group.setClickable(false);
                        bar.setVisibility(View.VISIBLE);
                        zGroup deletePosition = ((zGroup)zlinks.get(position));
                        Call<ResponseBody> call = conn.getConnection().delete(token,deletePosition);
                        call.enqueue(new Callback<ResponseBody>() {
                            @SuppressLint("ShowToast")
                            @Override
                            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                if(response.isSuccessful()){
                                    Log.d(TAG, "onResponse: DELETED_EDIT "+token);
                                    zlinks.remove(position);

                                    String json = new Gson().toJson(zlinks);
                                    Objects.requireNonNull(getActivity()).getSharedPreferences("TOKEN",MODE_PRIVATE).edit().putString("GROUPLIST",json).apply();


                                }else{
                                    Toast.makeText(getContext(),"Connection error",Toast.LENGTH_SHORT);
                                }
                                Objects.requireNonNull(getActivity()).finish();

                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                Toast.makeText(getContext(),"Connection error",Toast.LENGTH_SHORT).show();
                                Objects.requireNonNull(getActivity()).finish();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null).setCancelable(true);
        AlertDialog alertOut = builder.create();
        alertOut.show();
    }



}
