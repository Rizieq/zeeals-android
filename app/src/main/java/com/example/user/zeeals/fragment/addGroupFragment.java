package com.example.user.zeeals.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.user.zeeals.R;

public class addGroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String GROUPNAME= "name";
    private static final String TAG = "addGroupFragment";

    // TODO: Rename and change types of parameters
    private String mGroupName;
    private Button btnBack, btnDelete, btnAdd, btnIcon;
    private Switch showGroup;
    private EditText title;


    private EditText editTextGroupName_fragment;
    private Button btn_icon;
    private Button  btn_save_add_group;
    private Spinner spinner_grid;

    public addGroupFragment() {
        // Required empty public constructor
    }

    public static addGroupFragment newInstance() {
        addGroupFragment fragment = new addGroupFragment();
//        Bundle args = new Bundle();
//        args.putString(GROUPNAME, newGroupName);
//        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGroupName = getArguments().getString(GROUPNAME);
        }

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View addGroupView = inflater.inflate(R.layout.fragment_add_group, container, false);
        btnBack = addGroupView.findViewById(R.id.btnBackEditGroup);
        btnDelete = addGroupView.findViewById(R.id.btnDeleteEditGroup);
        btnAdd = addGroupView.findViewById(R.id.btnAddEditGroup);
        btnIcon = addGroupView.findViewById(R.id.btnIcon);
        showGroup = addGroupView.findViewById(R.id.switchshowEditGroup);

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

        return addGroupView;

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
//        if (context instanceof OnFragmentAddGroupInteractionListener) {
//            mListener = (OnFragmentAddGroupInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }
//    public interface OnFragmentAddGroupInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentAddGroupInteraction(String uri);
//    }

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
