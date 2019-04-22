package com.example.user.zeeals.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentEditGroupInteraction} interface
 * to handle interaction events.
 * Use the {@link editGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class editGroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String GROUPNAME= "name";
    private static final String TAG = "editGroupFragment";

    // TODO: Rename and change types of parameters
    private String mGroupName;

    private FragmentEditGroupInteraction mListener;

    private EditText group_title;
    private Button  btn_save_group;
    private TextView icon;
    private Spinner orientationSpinner;
    private Switch show;
    static int position;
    private zGroup zGroups;
    String rawIcon;
    ProgressBar bar;
    Button delete;
    Button back;
    String token;

    RetroConnection conn;


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

        String groupJSON = getActivity().getSharedPreferences("TOKEN",MODE_PRIVATE).getString("GROUPLIST",null);
        token = getActivity().getSharedPreferences("TOKEN",MODE_PRIVATE).getString("TOKEN",null);

        Type listType = new TypeToken<List<zGroup>>(){}.getType();
        zlinks = new ArrayList<>();
        zlinks = new Gson().fromJson(groupJSON,listType);
        zGroups=(zGroup) zlinks.get(position);

        conn = new RetroConnection();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_group, container, false);
        group_title=view.findViewById(R.id.editGroup_title);
        btn_save_group=view.findViewById(R.id.editGroup_save_btn);
        orientationSpinner=view.findViewById(R.id.editGroup_orientation);
        show=view.findViewById(R.id.editGroup_show);
        icon=view.findViewById(R.id.editGroup_icon);
        bar =view.findViewById(R.id.progress_bar_edit_group);
        bar.setVisibility(View.GONE);
        delete=view.findViewById(R.id.editGroup_delete_btn);
        back=view.findViewById(R.id.editGroup_btn_back);

        ArrayAdapter<CharSequence> adapterGrid = ArrayAdapter.createFromResource(getContext(), R.array.grid, android.R.layout.simple_spinner_item);
        adapterGrid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        orientationSpinner.setPrompt("Select your Grid");
        orientationSpinner.setAdapter(new NothingSelectedSpinnerAdapter(adapterGrid, R.layout.contact_spinner_row_nothing_selected,getContext()));


        group_title.setHint(zGroups.getTitle());
        if(zGroups.getStatus()==0){
            show.setChecked(false);
        }else show.setChecked(true);

        if(zGroups.getOrientation()=='h'){
            orientationSpinner.setSelection(1);
        }else orientationSpinner.setSelection(2);
        rawIcon=new String (Character.toChars(Integer.parseInt(zGroups.getIcon(), 16)));
        icon.setText(rawIcon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIconPicker();
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
                token = getActivity().getSharedPreferences("TOKEN",MODE_PRIVATE).getString("TOKEN",null);

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

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date currentTime = Calendar.getInstance().getTime();
                updated_at= sdf.format(currentTime);
                newGroup.setUpdated_at(updated_at);

                btn_save_group.setVisibility(View.GONE);
                bar.setVisibility(View.VISIBLE);

                // saving
                Call<ResponseBody> call = conn.getConnection().update(token,newGroup.getGroup_link_id(),newGroup);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        String[] group = {title,String.valueOf(orientation),rawIcon,Integer.toString(mShow),updated_at};
                        if(response.isSuccessful()){
                            Gson gson = new Gson();
                            String json = gson.toJson(zlinks);
                            getActivity().getSharedPreferences("TOKEN",MODE_PRIVATE).edit().putString("GROUPLIST",json).apply();

                            getActivity().finish();
                        }else{
                            try {
                                Toast.makeText(getContext(),response.body().string(),Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                    }
                });



            }


        });

        //delete
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void sendBack(String[] group) {
        if (mListener != null) {
            mListener.onFragmentEditGroupInteraction(group);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof FragmentEditGroupInteraction) {
//            mListener = (FragmentEditGroupInteraction) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface FragmentEditGroupInteraction {
        // TODO: Update argument type and name
        void onFragmentEditGroupInteraction(String[] group);
    }

    public void openIconPicker(){
        final String[] iconList = new String []{"f042","f037","f039","f036","f038","f461","f0f9","f2a3","f13d","f103","f100","f101","f102","f107","f104","f105","f106","f187","f358","f359","f35a","f35b","f0ab","f0a8","f0a9","f0aa","f063","f060","f061","f062","f0b2","f337","f338","f2a2","f069","f1fa","f29e","f04a","f24e","f05e","f462","f02a","f0c9","f433","f434","f2cd","f244","f240","f242","f243"};

        final Dialog iconPicker_dialog = new Dialog(getContext());
        iconPicker_dialog.setContentView(R.layout.popup_icon_picker);
        GridView gridView;
        gridView = iconPicker_dialog.findViewById(R.id.iconGrid);
        IconAdapter iconAdapter = new IconAdapter(iconPicker_dialog.getContext(),iconList);
        gridView.setAdapter(iconAdapter);

        iconPicker_dialog.show();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mIcon = new String (Character.toChars(Integer.parseInt(
                        iconList[position], 16)));

                rawIcon=iconList[position];
                icon.setText(mIcon);
                iconPicker_dialog.dismiss();
            }
        });

    }

    public void deleteItem(){
        Log.d(TAG, "deleteItem: clicked");
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Hapus Group")
                .setMessage("Apakah anda yakin menghapus group ini ? ")
                .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int deletePosition = ((zGroup)zlinks.get(position)).getGroup_link_id();
                        Call<ResponseBody> call = conn.getConnection().delete(token,deletePosition);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Log.d(TAG, "onResponse: DELETED_EDIT "+token);
                                zlinks.remove(position);

                                String json = new Gson().toJson(zlinks);
                                getActivity().getSharedPreferences("TOKEN",MODE_PRIVATE).edit().putString("GROUPLIST",json).apply();

                                getActivity().finish();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getContext(),"Connection error",Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null).setCancelable(true);
        AlertDialog alertOut = builder.create();
        alertOut.show();
    }



}
