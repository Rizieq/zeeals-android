package com.example.user.zeeals.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.user.zeeals.R;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.util.NothingSelectedSpinnerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class addLinkFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "addLinkFragment";
    private Button btnBack, btnDelete, btnAdd;
    private Spinner spinnerGroup;
    private EditText url, urlImage, title;
    private Switch showLink;
    private List<Zlink> zLink;

    public addLinkFragment(){

    }
    public static addLinkFragment newInstance(){
        addLinkFragment fragment = new addLinkFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View addLinkView = inflater.inflate(R.layout.fragment_add_link, container, false);

        String groupJSON = getActivity().getSharedPreferences("TOKEN",Context.MODE_PRIVATE).getString("GROUPLIST",null);
        Log.d(TAG, "addLinkFragment: "+groupJSON);
        Type listType = new TypeToken<List<zGroup>>(){}.getType();
        zLink = new ArrayList<>();
        zLink = new Gson().fromJson(groupJSON,listType);




        btnBack = addLinkView.findViewById(R.id.btnBackEditLink);
        btnAdd = addLinkView.findViewById(R.id.btnAddEditLink);
        btnDelete = addLinkView.findViewById(R.id.btnDeleteEditLink);
        spinnerGroup = addLinkView.findViewById(R.id.spinnerEditGroup);
        List<String> groupNames = new ArrayList<String>();
        for (int i=0;i<zLink.size();i++){
            groupNames.add(((zGroup)zLink.get(i)).getTitle());
        }

        ArrayAdapter<String> dataSpinner = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,groupNames);
        dataSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setPrompt("Select your Grid");
        spinnerGroup.setAdapter(new NothingSelectedSpinnerAdapter(dataSpinner, R.layout.contact_spinner_row_nothing_selected,getContext()));


        url = addLinkView.findViewById(R.id.etURLEditLink);
        urlImage = addLinkView.findViewById(R.id.etImageAddressEditLink);
        title = addLinkView.findViewById(R.id.etTitleEditLink);
        showLink = addLinkView.findViewById(R.id.switchshowEditLink);

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
        return addLinkView;
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
        builder.setTitle("Hapus Link")
                .setMessage("apakah anda ingin menghapus link ini ?")
                .setPositiveButton("Ya",null)
                .setNegativeButton("Tidak",null);
        AlertDialog alertShow = builder.create();
        alertShow.show();
    }
}
