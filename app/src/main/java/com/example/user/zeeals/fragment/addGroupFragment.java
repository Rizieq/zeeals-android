package com.example.user.zeeals.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.user.zeeals.R;
import com.example.user.zeeals.adapter.IconAdapter;
import com.example.user.zeeals.responses.PostGroupResponse;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.service.RetroConnection;
import com.example.user.zeeals.util.NothingSelectedSpinnerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class addGroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String GROUPNAME= "name";
    private static final String TAG = "addGroupFragment";

    // TODO: Rename and change types of parameters
    private Button  btnAdd;    //delete ditiadakan
//    private String btnIcon_addGroup;  //icon
    private Switch switchStatus;           //status
    private EditText title;             //group title
    private Spinner spinner_grid;
    private char orientationData;//orientation :vertical horizontal
    private ImageView btnBack, btnDelete;
    static Context context;
    Dialog iconPicker_dialog;
    GridView gridView;
    LinearLayout gridItemLayout;
    TextView prevGridText;
    int prevGridPosition;

    String rawIcon,token;
    ProgressBar bar;


    RetroConnection conn;
    ArrayList<String> iconList;

//    final String[] iconList = new String []{"f042","f037","f039","f036","f038","f461","f0f9","f2a3","f13d","f103","f100","f101","f102","f107","f104","f105","f106","f187","f358","f359","f35a","f35b","f0ab","f0a8","f0a9","f0aa","f063","f060","f061","f062","f0b2","f337","f338","f2a2","f069","f1fa","f29e","f04a","f24e","f05e","f462","f02a","f0c9","f433","f434","f2cd","f244","f240","f242","f243"};

    public addGroupFragment() {
        // Required empty public constructor
    }

    public static addGroupFragment newInstance(Context c) {
        addGroupFragment fragment = new addGroupFragment();
        context = c;
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.iconPicker_dialog = new Dialog(getContext());
        String iconJSON= getActivity().getSharedPreferences("ICON",MODE_PRIVATE).getString("ICON",null);
        iconList = new Gson().fromJson(iconJSON,ArrayList.class);
        conn = new RetroConnection();
        token = getActivity().getSharedPreferences("TOKEN", MODE_PRIVATE).getString("TOKEN",null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View addGroupView = inflater.inflate(R.layout.fragment_add_group, container, false);
        bar = addGroupView.findViewById(R.id.progress_bar_add_group);
        bar.setVisibility(View.GONE);

        title = addGroupView.findViewById(R.id.etTitleAddGroup);
//        btnBack2 = addGroupView.findViewById(R.id.addGroup_btn_back);
        btnBack = addGroupView.findViewById(R.id.addGroup_btn_back);
        btnAdd = addGroupView.findViewById(R.id.btnAddAddGroup);
        btnDelete = addGroupView.findViewById(R.id.addGroup_btn_delete);


        spinner_grid = addGroupView.findViewById(R.id.spinnerAddGroup);
        ArrayAdapter<CharSequence> adapterGrid = ArrayAdapter.createFromResource(getContext(), R.array.grid, android.R.layout.simple_spinner_item);
        adapterGrid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner_grid.setPrompt("Select your Grid");
        spinner_grid.setAdapter(new NothingSelectedSpinnerAdapter(adapterGrid, R.layout.contact_spinner_row_nothing_selected,getContext()));
        rawIcon="f059";


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDelete();
            }
        });
//        btnIcon_addGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openIconPicker();
//            }
//        });


        gridView = addGroupView.findViewById(R.id.iconGrid);
        IconAdapter iconAdapter = new IconAdapter(getContext(),iconList);
        gridView.setAdapter(iconAdapter);

//        iconPicker_dialog.show();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: GRID "+ position);
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
                String icon = new String (Character.toChars(Integer.parseInt(
                        iconList.get(position), 16)));

                rawIcon=iconList.get(position);
            }
        });



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.GONE);
                final String titleData = title.getText().toString();
                final String iconData = rawIcon;
                final String status;

                if(spinner_grid.getSelectedItem()!=null){
                    if(spinner_grid.getSelectedItem().toString().equals("Horizontal")){
                        orientationData='h';
                    }else orientationData='v';
                }else{
                    orientationData='v';
                }
                if(switchStatus.isChecked()){
                    status="1";
                }else status="0";

                final zGroup group = new zGroup(orientationData,titleData,iconData,Integer.parseInt(status));
                Call<PostGroupResponse> call = conn.getConnection().create(token,group);
                call.enqueue(new Callback<PostGroupResponse>() {
                    @Override
                    public void onResponse(Call<PostGroupResponse> call, Response<PostGroupResponse> response) {
                        if(response.isSuccessful()){
//                            final String[] x = {String.valueOf(orientationData),titleData,iconData,status,Integer.toString(response.body().getGroupLinkId())};
                            String groupJSON = getActivity().getSharedPreferences("TOKEN",MODE_PRIVATE).getString("GROUPLIST",null);
                            List<Zlink> zLink;
                            group.setGroupLinkId(response.body().getGroupID());
                            if(groupJSON!=null){
                                Type listType = new TypeToken<List<zGroup>>(){}.getType();
                                zLink = new Gson().fromJson(groupJSON,listType);
                            }else{
                                zLink = new ArrayList<>();
                            }
                            zLink.add(group);
                            Gson gson = new Gson();
                            String json = gson.toJson(zLink);

                            getActivity().getSharedPreferences("TOKEN",MODE_PRIVATE).edit().putString("GROUPLIST",json).apply();
                            getActivity().finish();
                            Log.d(TAG, "onResponse: Data added");
                        }
                    }

                    @Override
                    public void onFailure(Call<PostGroupResponse> call, Throwable t) {

                    }
                });

            }
        });

        return addGroupView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void dialogDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Hapus Grup")
                .setMessage("apakah anda ingin menghapus grup ini ?")
                .setPositiveButton("Ya",null)
                .setNegativeButton("Tidak",null);
        AlertDialog alertShow = builder.create();
        alertShow.show();
    }



}
