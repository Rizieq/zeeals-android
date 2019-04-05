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
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.zeeals.R;
import com.example.user.zeeals.fragment.editSourceFragment;
import com.example.user.zeeals.fragment.menuFragment;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.model.zSource;

import java.util.ArrayList;
import java.util.List;

//addGroupFragment.OnFragmentInteractionListener,
// implements editSourceFragment.OnFragmentInteractionListener
public class MainActivity extends AppCompatActivity {

    private EditText profileName, profileDesc;
    Dialog picChangePopUp;
    Uri imgUri;
    private ImageView imgProfpic,imgBannerProfPic;
    boolean menuShowed;

    private static final int PICK_IMAGE_PROF_PIC = 100;
    private static final int PICK_IMAGE_PROF_BANNER =101;

    FloatingActionButton fab;
    RecyclerView recyclerViewTes;
    FragmentTransaction transaction;
    editSourceFragment editSource_Fragment;
    menuFragment menuFragment;
    RecyclerAdapterTest adapterTest;
    List<Zlink> zLink;
    private static final String TAG = "TESTING";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuShowed=false;
        recyclerViewTes = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuShowed){
                    transaction = getSupportFragmentManager().beginTransaction();
//                    transaction.commit();
                    transaction.setCustomAnimations(R.anim.animation_enter,R.anim.animation_exit_fragment);
                    transaction.hide(menuFragment);
                    transaction.commit();
                    rotateFab();
                }else{
                    openMenuFragment();
                    rotateFab();
                }


            }
        });






//        Z-VERSION
        zSource zSource1 = new zSource(0,"Twitter","twitter.com/eldirohmanur",0);
        zSource zSource2 = new zSource(1,"Facebook","facebook.com/eldirohmanur",0);
        zSource zSource3 = new zSource(0,"Telkom","telyu.com/eldirohmanur",1);
        zSource zSource4 = new zSource(1,"KONG","kongs.com/eldirohmanur",1);
        zSource zSource5 = new zSource(2,"HohoHehe","HohoHehe.com/eldirohmanur",1);

        ArrayList<zSource> zSourceList1 = new ArrayList<>();
        zSourceList1.add(zSource1); zSourceList1.add(zSource2);
        ArrayList<zSource> zSourceList2 = new ArrayList<>();
        zSourceList2.add(zSource3); zSourceList2.add(zSource4); zSourceList2.add(zSource5);

        zGroup zGroup1 = new zGroup(0,zSourceList1,"Social");
        zGroup zGroup2 = new zGroup(1,zSourceList2,"Work");

        zLink = new ArrayList<>();
        zLink.add(zGroup1);
        zLink.add(zGroup2);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapterTest = new RecyclerAdapterTest(recyclerViewTes,zLink,this.findViewById(R.id.snackbar_container));
        recyclerViewTes.setAdapter(adapterTest);
        recyclerViewTes.setLayoutManager(layoutManager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapterTest));
        itemTouchHelper.attachToRecyclerView(recyclerViewTes);


//        Z-VERSION  END ---------------------------




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

    public void rotateFab(){
        if (menuShowed){
            final OvershootInterpolator interpolator = new OvershootInterpolator();
            ViewCompat.animate(fab).
                    rotation(0.0f).
                    withLayer().
                    setDuration(1000).
                    setInterpolator(interpolator).
                    start();

            menuShowed=false;
        }else{
            menuShowed=true;
            final OvershootInterpolator interpolator = new OvershootInterpolator();
            ViewCompat.animate(fab).
                    rotation(405f).
                    withLayer().
                    setDuration(1000).
                    setInterpolator(interpolator).
                    start();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if(resultCode==RESULT_OK){
                ArrayList<Parcelable> sourceParcel = data.getParcelableArrayListExtra("SOURCES");
                zGroup groupParcel = data.getParcelableExtra("GROUP");


                ArrayList<zSource> arraySource = new ArrayList<>();
                for(int i =0;i<sourceParcel.size();i++){
                    arraySource.add((zSource) sourceParcel.get(i));
                }
                groupParcel.setzSource(arraySource);

                zLink.add(groupParcel);
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

    public void openMenuFragment() {

        menuFragment = menuFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
//        transaction.addToBackStack(null);
        transaction.setCustomAnimations(R.anim.animation_enter,R.anim.animation_exit_fragment,R.anim.animation_pop_enter_fragment,R.anim.animation_pop_exit_animation);
        transaction.add(R.id.fragment_menu_container, menuFragment, "").commit();

    }

//    CURRENTLY WAITING ACTIVITY LAYOUT FROM IKHDAR
//    1/4/2019

//    @Override
//    public void onFragmentChildInteraction(String newName, String newLink) {
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//
//        groupList.get(groupPosisi).getItems().get(childPosisi).setSourceName(newName);
//        groupList.get(groupPosisi).getItems().get(childPosisi).setSourceLink(newLink);
//        Log.d(TAG, "onFragmentChildInteraction: GroupPosisi: "+groupPosisi);
////        adapter.addAll(groupList);
////        adapter.notifyGroupDataChanged();
////        adapter.notifyDataSetChanged();
//
//        onBackPressed();
//        fab.show();
//
//    }


//    public void openFragment(String text) {
//
//        fragment = addGroupFragment.newInstance(text);
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
