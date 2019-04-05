package com.example.user.zeeals.fragment;

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

import com.example.user.zeeals.R;

public class addGroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String GROUPNAME= "name";
    private static final String TAG = "addGroupFragment";

    // TODO: Rename and change types of parameters
    private String mGroupName;

//    private OnFragmentAddGroupInteractionListener mListener;

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
        View view = inflater.inflate(R.layout.activity_add_new_group, container, false);
        editTextGroupName_fragment = view.findViewById(R.id.etTitleAddGroup);
//        editTextGroupName_fragment.setHint(getArguments().getString(GROUPNAME));
//        spinner_grid = view.findViewById(R.id.spinner_grid_edit_group);
        btn_icon = view.findViewById(R.id.btnIconAddGroup);
        btn_save_add_group= view.findViewById(R.id.btnSaveAddGroup);

        btn_save_add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String sendBackNewName = editTextGroupName_fragment.getText().toString();
//                if(sendBackNewName.equals("")|| sendBackNewName.equals(null)|| sendBackNewName.equals(" ")){
//                    sendBack(editTextGroupName_fragment.getHint().toString());
//                }else{
//                    sendBack(sendBackNewName);
//                }
//                sendBack("MANTAP");


            }
        });

        return view;

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


}
