package com.example.user.zeeals.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.zeeals.R;
import com.example.user.zeeals.fragment.addGroupFragment;
import com.example.user.zeeals.fragment.addLinkFragment;
import com.example.user.zeeals.model.zGroup;

public class addGroupAndLinkFragmentHost extends AppCompatActivity implements addGroupFragment.addGroupFragmentInteraction{
    addGroupFragment addNewGroupFragment;
    addLinkFragment addNewLinkFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_and_link_fragment_host);
        String key = getIntent().getStringExtra("menuType");
        if (key.equals("addGroup")){
            openAddGroupFragment();
        }else
            openAddLinkFragment();
    }

    public void openAddGroupFragment() {

        addNewGroupFragment = addNewGroupFragment.newInstance(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.add_group_and_link_host_container,addNewGroupFragment,"Add Group Fragment").commit();

    }

    public void openAddLinkFragment() {
        addNewLinkFragment = addLinkFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.add_group_and_link_host_container,addNewLinkFragment,"Add Link Fragment").commit();
    }

    @Override
    public void passData(String[] group) {
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putStringArray("newGroup",group);
        i.putExtras(b);
        setResult(RESULT_OK,i);
        finish();
    }
}
