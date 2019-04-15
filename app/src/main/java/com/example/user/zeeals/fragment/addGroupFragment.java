package com.example.user.zeeals.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.example.user.zeeals.adapter.MainActivity;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.service.RetroConnection;
import com.example.user.zeeals.util.NothingSelectedSpinnerAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class addGroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String GROUPNAME= "name";
    private static final String TAG = "addGroupFragment";

    // TODO: Rename and change types of parameters
    private Button btnBack, btnDelete, btnAdd;
    private TextView btnIcon_addGroup;  //icon
    private Switch switchStatus;           //status
    private EditText title;             //group title
    private Spinner spinner_grid;       //orientation :vertical horizontal

    static Context context;
    Dialog iconPicker_dialog;
    GridView gridView;
    addGroupFragmentInteraction sendback;
    String rawIcon,token;
    ProgressBar bar;


    RetroConnection conn;

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
        conn = new RetroConnection();
        token = getActivity().getSharedPreferences("TOKEN",Context.MODE_PRIVATE).getString("TOKEN",null);


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View addGroupView = inflater.inflate(R.layout.fragment_add_group, container, false);
        bar = addGroupView.findViewById(R.id.progress_bar_add_group);
        bar.setVisibility(View.GONE);

        title = addGroupView.findViewById(R.id.etTitleAddGroup);
        btnBack = addGroupView.findViewById(R.id.btnBackEditGroup);
        btnDelete = addGroupView.findViewById(R.id.btnDeleteEditGroup);
        btnAdd = addGroupView.findViewById(R.id.btnAddAddGroup);
        btnIcon_addGroup = addGroupView.findViewById(R.id.btnIcon);
        switchStatus = addGroupView.findViewById(R.id.switchshowAddGroup);
        Log.d(TAG, "onCreateView: "+rawIcon);
        spinner_grid = addGroupView.findViewById(R.id.spinnerAddGroup);
        ArrayAdapter<CharSequence> adapterGrid = ArrayAdapter.createFromResource(getContext(), R.array.grid, android.R.layout.simple_spinner_item);
        adapterGrid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_grid.setPrompt("Select your Grid");
        spinner_grid.setAdapter(new NothingSelectedSpinnerAdapter(adapterGrid, R.layout.contact_spinner_row_nothing_selected,getContext()));


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
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Yes berhasil di add", Toast.LENGTH_SHORT).show();
            }
        });

        btnIcon_addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: icon clicked");
                openIconPicker();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.GONE);
                final String titleData = title.getText().toString();
                final String orientationData;
                if(spinner_grid.getSelectedItem().toString().equals("Horizontal")){
                    orientationData="h";
                }else orientationData="v";
                final String iconData = rawIcon;
                final String status;
                if(switchStatus.isChecked()){
                    status="1";
                }else status="0";


                zGroup group = new zGroup(orientationData,titleData,iconData,Integer.parseInt(status));
                Call<zGroup> call = conn.getConnection().create(token,group);
                call.enqueue(new Callback<zGroup>() {
                    @Override
                    public void onResponse(Call<zGroup> call, Response<zGroup> response) {
                        if(response.isSuccessful()){
                            final String[] x = {orientationData,titleData,iconData,status,Integer.toString(response.body().getGroup_link_id())};
                            sendback.passData(x);
                            Log.d(TAG, "onResponse: Data added");
                        }
                    }

                    @Override
                    public void onFailure(Call<zGroup> call, Throwable t) {

                    }
                });

            }
        });

        return addGroupView;

    }

    public void openIconPicker(){
        final String[] iconList = new String []{"f042","f037","f039","f036","f038","f461","f0f9","f2a3","f13d","f103","f100","f101","f102","f107","f104","f105","f106","f187","f358","f359","f35a","f35b","f0ab","f0a8","f0a9","f0aa","f063","f060","f061","f062","f0b2","f337","f338","f2a2","f069","f1fa","f29e","f04a","f24e","f05e","f462","f02a","f0c9","f433","f434","f2cd","f244","f240","f242","f243"};

        iconPicker_dialog.setContentView(R.layout.popup_icon_picker);

        gridView = iconPicker_dialog.findViewById(R.id.iconGrid);
        IconAdapter iconAdapter = new IconAdapter(iconPicker_dialog.getContext(),iconList);
        gridView.setAdapter(iconAdapter);

        iconPicker_dialog.show();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String icon = new String (Character.toChars(Integer.parseInt(
                        iconList[position], 16)));

                rawIcon=iconList[position];
                btnIcon_addGroup.setText(icon);
                iconPicker_dialog.dismiss();
            }
        });





    }



    // TODO: Rename method, update argument and hook method into UI event
//    public void sendBack(String newName) {
//        if (mListener != null) {
//            mListener.onFragmentAddGroupInteraction(newName);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            sendback = (addGroupFragmentInteraction) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        sendback=null;
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

    public interface addGroupFragmentInteraction{
        void passData(String[] group);
    }


}
