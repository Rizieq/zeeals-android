package com.example.user.zeeals.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.user.zeeals.R;
import com.example.user.zeeals.activity.addGroupAndLinkFragmentHost;

public class menuFragment extends Fragment {
    Button btn_add_group;
    Button btn_add_link;

    public menuFragment() {
    }

    public static menuFragment newInstance() {
        menuFragment fragment = new menuFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        btn_add_group = view.findViewById(R.id.btn_menu_add_group);
        btn_add_link = view.findViewById(R.id.btn_menu_add_link);

        btn_add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                menuFragment = menuFragment.newInstance();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                transaction = fragmentManager.beginTransaction();
//                transaction.addToBackStack(null);
//                transaction.add(R.id.fragment_editGroup_container, editSource_Fragment, "BLANK_FRAGMENT").commit();
                Intent i  = new Intent(getActivity(), addGroupAndLinkFragmentHost.class);
                startActivity(i);



            }
        });



        return view;
    }






}
