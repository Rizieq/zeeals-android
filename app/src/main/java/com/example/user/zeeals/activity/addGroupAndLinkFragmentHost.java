package com.example.user.zeeals.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.zeeals.R;
import com.example.user.zeeals.fragment.addGroupFragment;
import com.example.user.zeeals.fragment.addLinkFragment;
import com.example.user.zeeals.fragment.editGroupFragment;
import com.example.user.zeeals.model.zGroup;

public class addGroupAndLinkFragmentHost extends AppCompatActivity{
    addGroupFragment addNewGroupFragment;
    addLinkFragment addNewLinkFragment;
    com.example.user.zeeals.fragment.editGroupFragment editGroupFragment;
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
        }
    }

    public void openAddGroupFragment() {

        addNewGroupFragment = addNewGroupFragment.newInstance(this);
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


}
