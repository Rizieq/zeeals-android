package com.example.user.zeeals.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.zeeals.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
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

    private OnFragmentInteractionListener mListener;

    private EditText editTextGroupName_fragment;
    private Button  btn_confirm_name_fragment;
    View editGroupNameView;

    public editGroupFragment() {
        // Required empty public constructor
    }

    public static editGroupFragment newInstance(String newGroupName) {
        editGroupFragment fragment = new editGroupFragment();
        Bundle args = new Bundle();
        args.putString(GROUPNAME, newGroupName);
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_edit_group, container, false);
//        editGroupNameView = view.findViewById(R.id.viewEditGroupName);
//        editGroupNameView.setEnabled(false);
        editTextGroupName_fragment = view.findViewById(R.id.edit_new_group_fragment);
        editTextGroupName_fragment.setHint(getArguments().getString(GROUPNAME));
        btn_confirm_name_fragment = view.findViewById(R.id.btn_confirm_edit_fragment);
        btn_confirm_name_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendBackNewName = editTextGroupName_fragment.getText().toString();
                if(sendBackNewName!="" || sendBackNewName!=null || sendBackNewName!=" "){
                    sendBack(sendBackNewName);
                    Log.d(TAG, "onClickFragment: Pressed "+sendBackNewName);
                }
//                Toast.makeText(getContext(),sendBackNewName,Toast.LENGTH_LONG).show();


            }
        });

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void sendBack(String newName) {
        if (mListener != null) {
            mListener.onFragmentInteraction(newName);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String uri);
    }


}
