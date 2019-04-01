package com.example.user.zeeals.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.zeeals.R;
import com.example.user.zeeals.fragment.editSourceFragment;
import com.example.user.zeeals.model.Group;
import com.example.user.zeeals.model.Source;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.model.zSource;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
//import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

//editGroupFragment.OnFragmentInteractionListener,
//
public class MainActivity extends AppCompatActivity implements editSourceFragment.OnFragmentInteractionListener{

    private EditText profileName, profileDesc;
    Dialog picChangePopUp;
    Uri imgUri;
    private ImageView imgProfpic,imgBannerProfPic;

    private static final int PICK_IMAGE_PROF_PIC = 100;
    private static final int PICK_IMAGE_PROF_BANNER =101;

    FloatingActionButton fab;
    groupAdapter adapter;
    RecyclerView recyclerViewTes;
    List <Group> groupList = new ArrayList<>();
    int posisi,childPosisi,groupPosisi;
    private RelativeLayout main_layout;
    RecyclerView recyclerView;
    FragmentTransaction transaction;
//    editGroupFragment fragment;
    editSourceFragment editSource_Fragment;

    RecyclerAdapterTest adapterTest;
    List<Zlink> zLink;



    private static final String TAG = "TESTING";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_layout = findViewById(R.id.main_activity_layou);

//        recyclerView = findViewById(R.id.recycler_view);
        recyclerViewTes = findViewById(R.id.recycler_view);



//        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
//        if (animator instanceof DefaultItemAnimator) {
//            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
//        }



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


//        Z-VERSION
        zSource zSource1 = new zSource(0,"Twitter","twitter.com/eldirohmanur",0);
        zSource zSource2 = new zSource(1,"Facebook","facebook.com/eldirohmanur",0);
        zSource zSource3 = new zSource(0,"Telkom","telyu.com/eldirohmanur",1);
        zSource zSource4 = new zSource(1,"KONG","kongs.com/eldirohmanur",1);
        zSource zSource5 = new zSource(2,"HohoHehe","HohoHehe.com/eldirohmanur",1);

        zSource[] zSourceList1 = {zSource1,zSource2};
        zSource[] zSourceList2 = {zSource3,zSource4,zSource5};

        zGroup zGroup1 = new zGroup(0,zSourceList1,"Social");
        zGroup zGroup2 = new zGroup(1,zSourceList2,"Work");

        zLink = new ArrayList<>();
        zLink.add(zGroup1);
        zLink.add(zGroup2);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapterTest = new RecyclerAdapterTest(recyclerViewTes,zLink);
        recyclerViewTes.setAdapter(adapterTest);
        recyclerViewTes.setLayoutManager(layoutManager);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,AddNewGroup.class);
                i.putExtra("GROUPSIZE",zLink.size());
                startActivityForResult(i,1);
            }
        });


//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(0,adapterTest));
//        itemTouchHelper.attachToRecyclerView(recyclerView);


//        Z-VERSION  END ---------------------------



//        adapter = new groupAdapter(groupList);
//
//        adapter.setOnItemClickListener(new groupAdapter.OnItemClickListener() {
//            @Override
//            public void OnItemClick(int position,CharSequence text) {
//                groupList.get(position).getItems().get(0).setGroupName(text.toString());
//                posisi = position;
//            }
//        });
//
//        adapter.setOnChildClickListener(new groupAdapter.OnChildClickListener() {
//            @Override
//            public void OnChildClick(int childPosition, CharSequence newSourceName, CharSequence newSourceLink,int groupPosition) {
//                fab.hide();
//
//                String sourceName = groupList.get(groupPosition).getItems().get(childPosition).getSourceName();
//                String sourceLink = groupList.get(groupPosition).getItems().get(childPosition).getSourceLink();
//
//                openSourceEditFragment(sourceName,sourceLink);
//                childPosisi = childPosition;
//                groupPosisi= groupPosition;
//
//            }
//        });
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);










        /// FOR PROFILE UI ABOVE SEPARATOR//
        Typeface poppinsBold = Typeface.createFromAsset(getAssets(),"fonts/Poppins-Bold.otf");
        Typeface poppinsRegular = Typeface.createFromAsset(getAssets(),"fonts/Poppins-Regular.otf");
        profileName = findViewById(R.id.profileName);
        profileDesc = findViewById(R.id.profileDesc);
        imgProfpic = findViewById(R.id.profilePicture);
        imgBannerProfPic = findViewById(R.id.profileBanner);
        profileName.setTypeface(poppinsBold);
        profileDesc.setTypeface(poppinsRegular);
        profileName.clearFocus();
        profileDesc.clearFocus();

        profileName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.d(TAG, "onEditorAction: FOCUS LOST");
                    profileName.clearFocus();
                    keyboardDown();
                    handled = true;
                }
                return handled;
            }
        });

        profileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>20){
                    profileName.setError("Maksimal 20 karakter");
                }else
                    profileName.setError(null);
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        profileDesc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.d(TAG, "onEditorAction: FOCUS LOST");
                    profileDesc.clearFocus();
                    keyboardDown();
                    handled = true;
                }
                return handled;
            }
        });

        picChangePopUp = new Dialog(this);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: Result Code: "+resultCode);
        Log.d(TAG, "onActivityResult: Request Code: "+requestCode);

        if(requestCode==1){
            if(resultCode==RESULT_OK){
                Parcelable[] sourceParcel = data.getParcelableArrayExtra("SOURCES");
                zGroup groupParcel = data.getParcelableExtra("GROUP");


//                List<zSource> zSources_new = new ArrayList<>();
//                for(int i = 0;i<sourceParcel.length;i++){
//                    zSources_new.add((zSource)sourceParcel[i]);
//                }

                zSource[] arraySource = {(zSource)sourceParcel[0]};
                groupParcel.setzSource(arraySource);

                zLink.add(groupParcel);
//                Group group = new Group(groupParcel.getTitle(),groupParcel.getItems());
//                for (int i = 0 ; i < group.getItems().size();i++){
//                    group.getItems().get(i).setGroupPosition(groupList.size());
//                }
//                zGroup newGroup = new zGroup(zLink.size(),sourceParcel,groupParcel.getName());
//                zLink.add(newGroup);
//                adapter.notifyItemInserted(zLink.size()-1);
                adapterTest.notifyItemInserted(zLink.size()-1);
            }
            if(requestCode==RESULT_CANCELED){
                Log.d(TAG, "onActivityResult: CANCELED");
            }
        }
        else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_PROF_PIC){
            imgUri = data.getData();
            imgProfpic.setImageURI(imgUri);
            picChangePopUp.dismiss();
        }else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_PROF_BANNER){
            imgUri = data.getData();
            imgBannerProfPic.setImageURI(imgUri);
            picChangePopUp.dismiss();
        }

    }

    public void openSourceEditFragment(String sourceName, String sourceLink) {

        editSource_Fragment = editSource_Fragment.newInstance(sourceName,sourceLink);
        FragmentManager fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_editGroup_container, editSource_Fragment, "BLANK_FRAGMENT").commit();

    }

    @Override
    public void onFragmentChildInteraction(String newName, String newLink) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        groupList.get(groupPosisi).getItems().get(childPosisi).setSourceName(newName);
        groupList.get(groupPosisi).getItems().get(childPosisi).setSourceLink(newLink);
        Log.d(TAG, "onFragmentChildInteraction: GroupPosisi: "+groupPosisi);
        adapter.addAll(groupList);
        adapter.notifyGroupDataChanged();
        adapter.notifyDataSetChanged();

        onBackPressed();
        fab.show();

    }


//    public void openFragment(String text) {
//
//        fragment = editGroupFragment.newInstance(text);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        transaction = fragmentManager.beginTransaction();
////        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
//        transaction.addToBackStack(null);
//        transaction.add(R.id.fragment_editGroup_container, fragment, "BLANK_FRAGMENT").commit();
//        Log.d(TAG, "openFragment: Pressed "+ text);
//    }

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

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        adapter.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        adapter.onRestoreInstanceState(savedInstanceState);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fab.show();
    }

    public void showPopUpProfPic(View v){
        Button chagePhoto;
        Button close;
        EditText imgUrl;
        ImageView profPic;
        picChangePopUp.setContentView(R.layout.popupchangepicture);
        chagePhoto = (Button) picChangePopUp.findViewById(R.id.btnChangePhotoPopUpProfPic);
        close = (Button) picChangePopUp.findViewById(R.id.btnClosePopUpProfPic);
        imgUrl = (EditText) picChangePopUp.findViewById(R.id.insertUrlPopUpProfPic);
        profPic = (ImageView) picChangePopUp.findViewById(R.id.imageViewPopUpProfPic);
        profPic.setImageBitmap(convertImageViewToBitmap(imgProfpic));

        chagePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFotoFromGallery(PICK_IMAGE_PROF_PIC);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picChangePopUp.dismiss();
            }
        });
        picChangePopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        picChangePopUp.show();
    }

    public void showPopUpBannerPic(View v){
        Button chagePhoto;
        Button close;
        EditText imgUrl;
        ImageView profPic;

        Typeface poppinsRegular = Typeface.createFromAsset(getAssets(),"fonts/Poppins-Regular.otf");

        picChangePopUp.setContentView(R.layout.popupchangepicture);

        chagePhoto = (Button) picChangePopUp.findViewById(R.id.btnChangePhotoPopUpProfPic);
        close = (Button) picChangePopUp.findViewById(R.id.btnClosePopUpProfPic);
        imgUrl = (EditText) picChangePopUp.findViewById(R.id.insertUrlPopUpProfPic);
        profPic = (ImageView) picChangePopUp.findViewById(R.id.imageViewPopUpProfPic);
        profPic.setImageBitmap(convertImageViewToBitmap(imgBannerProfPic));

        chagePhoto.setTypeface(poppinsRegular);
        close.setTypeface(poppinsRegular);

        chagePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFotoFromGallery(PICK_IMAGE_PROF_BANNER);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picChangePopUp.dismiss();
            }
        });
        picChangePopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        picChangePopUp.show();
    }

    /// THIS FUNCTION TO CONVERT IMAGEVIEW TO BITMAP ///
    private Bitmap convertImageViewToBitmap(ImageView v){
        Bitmap bm=((BitmapDrawable)v.getDrawable()).getBitmap();
        return bm;
    }

    public void pickFotoFromGallery(int image){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, image);
    }

    public void keyboardDown(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}
