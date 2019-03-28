package com.example.user.zeeals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class AddNewGroup extends AppCompatActivity {

    RecyclerView recyclerView;
    addNewGroupAdapter adapter;
    ArrayList<Source> listSource;
    Button btn_newSourceElement,btn_add_save_new_group;
    private static final String TAG = "AddNewGroup";
    EditText groupName;
    int groupSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_group);
        recyclerView = findViewById(R.id.recycler_source);
        listSource = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new addNewGroupAdapter(1);
        groupName = findViewById(R.id.editText_groupName_add_new_group);
        Intent intent = getIntent();
        groupSize = intent.getIntExtra("GROUPSIZE",0);
        Log.d(TAG, "onCreate: groupSize: "+groupSize);
        adapter.setOnTextSaved(new addNewGroupAdapter.onTextSavedListener() {
            @Override
            public void onTextSaved(Source source) {
                listSource.add(source);
            }
        });

        btn_newSourceElement = findViewById(R.id.btn_new_source_element);
        btn_newSourceElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addCount();
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnTextSaved(new addNewGroupAdapter.onTextSavedListener() {
            @Override
            public void onTextSaved(Source source) {
                listSource.add(source);
            }
        });

        btn_add_save_new_group = findViewById(R.id.btn_save_add_new_group);

        btn_add_save_new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listSource.get(0).setGroupName(groupName.getText().toString());
                for(int i =0;i<listSource.size();i++){
                    listSource.get(i).setGroupPosition(groupSize);
                }

                Group group = new Group(groupName.getText().toString(),listSource);
                Intent intent= new Intent();
                intent.putExtra("GROUP",group);
                setResult(RESULT_OK,intent);
                finish();
            }
        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AddNewGroup.this, MainActivity.class);
        setResult(RESULT_CANCELED,i);
    }
}
