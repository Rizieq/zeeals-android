package com.example.user.zeeals.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.user.zeeals.R;
import com.example.user.zeeals.activity.addGroupAndLinkFragmentHost;
import com.example.user.zeeals.adapter.MainActivity;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.service.RetroConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class menuFragment extends Fragment {
    Button btn_add_group;
    Button btn_add_link;
    ConstraintLayout btn_add_group2;
    ConstraintLayout btn_add_link2;
    addMenuFragmentInteraction sendback;


//    REQUES CODE
    int ADD_GROUP_REQUEST_CODE = 100;

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

        btn_add_group2 = view.findViewById(R.id.left_layout);
        btn_add_link2 = view.findViewById(R.id.rightLayout);
        btn_add_group.setEnabled(true);

        btn_add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(getActivity(), addGroupAndLinkFragmentHost.class);
                i.putExtra("menuType","addGroup");
                startActivityForResult(i,ADD_GROUP_REQUEST_CODE);
            }
        });

        btn_add_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(getActivity(), addGroupAndLinkFragmentHost.class);
                i.putExtra("menuType","addLink");
                startActivity(i);}
        });

        btn_add_group2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(getActivity(), addGroupAndLinkFragmentHost.class);
                i.putExtra("menuType","addGroup");
                startActivityForResult(i,ADD_GROUP_REQUEST_CODE);;
            }
        });

        btn_add_link2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(getActivity(), addGroupAndLinkFragmentHost.class);
                i.putExtra("menuType","addLink");
                startActivity(i);}
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== -1){
            if(requestCode==ADD_GROUP_REQUEST_CODE){
                MainActivity act = (MainActivity)getActivity();
                Bundle b = data.getExtras();
                String[] dataParcel = b.getStringArray("newGroup");
                zGroup newGroup = new zGroup(dataParcel[0].charAt(0),dataParcel[1],dataParcel[2], Integer.parseInt(dataParcel[3]),Integer.parseInt(dataParcel[4]));
                newGroup.setPosition(act.zLink.size());
                act.zLink.add(newGroup);
                act.adapterTest.notifyItemInserted(act.zLink.size()-1);

            }
        }
    }

    public interface addMenuFragmentInteraction{
        void passDataFromMenutoAct(zGroup group);
    }

}

