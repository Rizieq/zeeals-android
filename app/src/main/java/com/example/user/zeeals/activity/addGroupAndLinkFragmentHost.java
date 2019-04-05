package com.example.user.zeeals.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.zeeals.R;
import com.example.user.zeeals.fragment.addGroupFragment;

public class addGroupAndLinkFragmentHost extends AppCompatActivity {
    addGroupFragment addNewGroupFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_and_link_fragment_host);
        openAddGroupFragment();
    }



    public void openAddGroupFragment() {

        addNewGroupFragment = addNewGroupFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.add_group_and_link_host_container,addNewGroupFragment,"Add Group Fragment").commit();
//        transaction = fragmentManager.beginTransaction();
//        transaction.addToBackStack(null);
//        transaction.add(R.id.fragment_menu_container, menuFragment, "").commit();

    }
}
