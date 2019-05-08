package com.example.user.zeeals.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.zeeals.R;
import com.example.user.zeeals.fragment.addGroupFragment;
import com.example.user.zeeals.fragment.addLinkFragment;
import com.example.user.zeeals.fragment.editLinkFragment;
import com.example.user.zeeals.service.RetroConnection;

public class addGroupAndLinkFragmentHost extends AppCompatActivity{
    private static final String TAG = "addGroupAndLinkFragment";
    addGroupFragment addNewGroupFragment;
    addLinkFragment addNewLinkFragment;
    com.example.user.zeeals.fragment.editGroupFragment editGroupFragment;
    editLinkFragment editLinkFragment;
    RetroConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_and_link_fragment_host);
        String key = getIntent().getStringExtra("menuType");
        if (key.equals("addGroup")){
            openAddGroupFragment();
        }else if(key.equals("addLink")){
            openAddLinkFragment();
        }else if(key.equals("editGroup")){
            openEditGroupFragment(getIntent().getIntExtra("position",100));
        }else if(key.equals("editLink")){
            openEditLinkFragment(getIntent().getIntArrayExtra("position"));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void openAddGroupFragment() {
        addNewGroupFragment = addNewGroupFragment.newInstance(addGroupAndLinkFragmentHost.this);
        getSupportFragmentManager().beginTransaction().replace(R.id.add_group_and_link_host_container,addNewGroupFragment,"Add Group Fragment").commit();


    }

    public void openAddLinkFragment() {
        addNewLinkFragment = addLinkFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.add_group_and_link_host_container,addNewLinkFragment,"Add Link Fragment").commit();
    }

    public void openEditGroupFragment(int position){
        editGroupFragment = com.example.user.zeeals.fragment.editGroupFragment.newInstance(position);
        getSupportFragmentManager().beginTransaction().replace(R.id.add_group_and_link_host_container,editGroupFragment,"Edit Group Fragment").commit();
    }

    public void openEditLinkFragment(int[] position){
        editLinkFragment = editLinkFragment.newInstance(position);
        getSupportFragmentManager().beginTransaction().replace(R.id.add_group_and_link_host_container,editLinkFragment,"Edit Link Fragment").commit();
    }


}
