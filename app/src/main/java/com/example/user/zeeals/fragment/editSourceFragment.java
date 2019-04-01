package com.example.user.zeeals.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.zeeals.R;

public class editSourceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SOURCENAME= "name";
    private static final String SOURCELINK= "link";
    private static final String TAG = "editSourceFragment";

    // TODO: Rename and change types of parameters
    private String mSourceLink;
    private String mSourceName;

    private editSourceFragment.OnFragmentInteractionListener mListener;

    private EditText editText_source_name_fragment;
    private EditText editText_source_link_fragment;
    private Button btn_save_source_fragment;

    public editSourceFragment() {
        // Required empty public constructor
    }

    public static editSourceFragment newInstance(String newSourceName,String newSourceLink) {
        editSourceFragment fragment = new editSourceFragment();
        Bundle args = new Bundle();
        args.putString(SOURCENAME, newSourceName);
        args.putString(SOURCELINK, newSourceLink);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSourceName = getArguments().getString(SOURCENAME);
            mSourceLink = getArguments().getString(SOURCELINK);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_source, container, false);
        editText_source_name_fragment= view.findViewById(R.id.edit_new_source_name_fragment);
        editText_source_link_fragment = view.findViewById(R.id.edit_new_source_link_fragment);
        editText_source_name_fragment.setHint(getArguments().getString(SOURCENAME));
        editText_source_link_fragment.setHint(getArguments().getString(SOURCELINK));

        btn_save_source_fragment = view.findViewById(R.id.btn_save_source_fragment);
        btn_save_source_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendBackNewName=editText_source_name_fragment.getText().toString();
                String sendBackNewLink=editText_source_link_fragment.getText().toString();;
                if(sendBackNewName.equals("") || sendBackNewName.equals(null) || sendBackNewName.equals(" ")){
                    sendBackNewName=editText_source_name_fragment.getHint().toString();
                }
                if(sendBackNewLink.equals("") || sendBackNewLink.equals(null) || sendBackNewLink.equals(" ")){
                    sendBackNewLink=editText_source_link_fragment.getHint().toString();
                }
                sendBack(sendBackNewName,sendBackNewLink);

//                Toast.makeText(getContext(),sendBackNewName,Toast.LENGTH_LONG).show();


            }
        });

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void sendBack(String newName,String newLink) {
        if (mListener != null) {
            mListener.onFragmentChildInteraction(newName,newLink);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof editSourceFragment.OnFragmentInteractionListener) {
            mListener = (editSourceFragment.OnFragmentInteractionListener) context;
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
        void onFragmentChildInteraction(String newName,String newLink);
    }
}
