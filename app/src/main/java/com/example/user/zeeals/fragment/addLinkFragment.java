package com.example.user.zeeals.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.user.zeeals.R;

import java.util.zip.Inflater;

public class addLinkFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "addLinkFragment";
    private Button btnBack, btnDelete, btnAdd;
    private Spinner spinnerGroup;
    private EditText url, urlImage, title;
    private Switch showLink;

    public addLinkFragment(){
        // Required empty public constructor
    }
    public static addLinkFragment newInstance(){
        addLinkFragment fragment = new addLinkFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View addLinkView = inflater.inflate(R.layout.fragment_add_link, container, false);
        btnBack = addLinkView.findViewById(R.id.btnBackEditLink);
        btnAdd = addLinkView.findViewById(R.id.btnAddEditLink);
        btnDelete = addLinkView.findViewById(R.id.btnDeleteEditLink);
        spinnerGroup = addLinkView.findViewById(R.id.spinnerEditGroup);
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
