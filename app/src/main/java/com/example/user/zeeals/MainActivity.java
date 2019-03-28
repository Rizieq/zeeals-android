package com.example.user.zeeals;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements editGroupFragment.OnFragmentInteractionListener,editSourceFragment.OnFragmentInteractionListener{

    FloatingActionButton fab;
    groupAdapter adapter;
    List <Group> groupList = new ArrayList<>();
    int posisi,childPosisi,groupPosisi;
    private RelativeLayout main_layout;
    RecyclerView recyclerView;
    FragmentTransaction transaction;
    editGroupFragment fragment;
    editSourceFragment editSource_Fragment;



    private static final String TAG = "TESTING";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_layout = (RelativeLayout) findViewById(R.id.main_activity_layou);

        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,AddNewGroup.class);
                i.putExtra("GROUPSIZE",groupList.size());
                startActivityForResult(i,1);
            }
        });

        List <Source> sourceList1 = new ArrayList<>();
        Source s1 = new Source("Instagram","http://instagram.com/eldirohmanur","Social Media",0);
        Source s2 = new Source("Facebook","http://facebook.com/eldirohmanur","Social Media",0);
        sourceList1.add(s1);
        sourceList1.add(s2);
        Group g1 = new Group("Social Media",sourceList1);

        List <Source> sourceList2 = new ArrayList<>();
        Source ss1 = new Source("Telkom","http://telyu.com/eldirohmanur","Career",1);
        Source ss2 = new Source("KONGS","http://kong.com/eldirohmanur","Career",1);
        sourceList2.add(ss1);
        sourceList2.add(ss2);
        Group g2 = new Group("Career",sourceList2);
        groupList.add(g1);
        groupList.add(g2);


        adapter = new groupAdapter(groupList);
        adapter.setOnItemClickListener(new groupAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position,CharSequence text) {
                fab.hide();
//                openFragment(groupList.get(position).getItems().get(0).getGroupName());
                posisi = position;
            }
        });

        adapter.setOnChildClickListener(new groupAdapter.OnChildClickListener() {
            @Override
            public void OnChildClick(int childPosition, CharSequence newSourceName, CharSequence newSourceLink,int groupPosition) {
                Log.d(TAG, "OnChildClick: Pressed : "+childPosition);
                Log.d(TAG, "OnChildClick: Group: "+groupPosition);
                fab.hide();

                String sourceName = groupList.get(groupPosition).getItems().get(childPosition).getSourceName();
                String sourceLink = groupList.get(groupPosition).getItems().get(childPosition).getSourceLink();

//                openSourceEditFragment(sourceName,sourceLink);
                childPosisi = childPosition;
                groupPosisi= groupPosition;

            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: Result Code: "+resultCode);
        Log.d(TAG, "onActivityResult: Request Code: "+requestCode);

        if(requestCode==1){
            if(resultCode==RESULT_OK){
                ExpandableGroup groupParcel = data.getParcelableExtra("GROUP");
                Group group = new Group(groupParcel.getTitle(),groupParcel.getItems());
                groupList.add(group);

                adapter.addAll(groupList);
                adapter.notifyGroupDataChanged();
                adapter.notifyItemInserted(groupList.size()-1);
            }
            if(requestCode==RESULT_CANCELED){
                Log.d(TAG, "onActivityResult: CANCELED");
            }
        }

    }

//    public void openSourceEditFragment(String sourceName, String sourceLink) {
//
//        editSource_Fragment = editSourceFragment.newInstance(sourceName,sourceLink);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        transaction = fragmentManager.beginTransaction();
//        transaction.addToBackStack(null);
//        transaction.add(R.id.fragment_editGroup_container, editSource_Fragment, "BLANK_FRAGMENT").commit();
//
//    }
//
//    @Override
//    public void onFragmentChildInteraction(String newName, String newLink) {
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//
//        groupList.get(groupPosisi).getItems().get(childPosisi).setSourceName(newName);
//        groupList.get(groupPosisi).getItems().get(childPosisi).setSourceLink(newLink);
//        Log.d(TAG, "onFragmentChildInteraction: GroupPosisi: "+groupPosisi);
//        adapter.addAll(groupList);
//        adapter.notifyGroupDataChanged();
//        adapter.notifyDataSetChanged();
//
//        onBackPressed();
//        fab.show();
//
//    }
//
//
//
//    public void openFragment(String text) {
//
//        fragment = editGroupFragment.newInstance(text);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        transaction = fragmentManager.beginTransaction();
////        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
//        transaction.addToBackStack(null);
//        transaction.add(R.id.fragment_editGroup_container, fragment, "BLANK_FRAGMENT").commit();
//        Log.d(TAG, "openFragment: Pressed "+ text);
//
//
//    }
//
//    @Override
//    public void onFragmentInteraction(String sendBackText) {
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//
//        groupList.get(posisi).getItems().get(0).setGroupName(sendBackText);
//
//        adapter.addAll(groupList);
//        adapter.notifyGroupDataChanged();
//        adapter.notifyDataSetChanged();
//        onBackPressed();
//        fab.show();
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fab.show();
    }
}
