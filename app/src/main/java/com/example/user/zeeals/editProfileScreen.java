package com.example.user.zeeals;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.user.zeeals.adapter.MainActivity;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.Objects;

public class editProfileScreen extends AppCompatActivity {
    private static final int PICK_IMAGE_PROF_PIC = 100;
    private static final int PICK_IMAGE_PROF_BANNER =101;

    private Button btnChangePicture;
    private Button btnSave;
    private RelativeLayout btnBack,  btnEdit;
    private ImageView imgProfPic, imgBanner;

    private EditText name, Description;

    private String imgType="";

    private View tb;

    Dialog pictureChangePopUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_screen);

        name = findViewById(R.id.etName);
        Description = findViewById(R.id.etDescription);

        name.setHint(getSharedPreferences("PROFILE",MODE_PRIVATE).getString("PROFILE_NAME","Set your name"));
        Description.setHint(getSharedPreferences("PROFILE",MODE_PRIVATE).getString("PROFILE_DESC","Set your descripton"));



        tb = findViewById(R.id.menu_appbar);
        tb.bringToFront();
        btnBack = tb.findViewById(R.id.btnBack);
        btnEdit = tb.findViewById(R.id.btnEditPofile);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnEdit.setVisibility(View.GONE);




        imgProfPic = findViewById(R.id.ivChangePhoto);
        imgBanner = findViewById(R.id.ivChangeBanner);
        Button btnChangeBanner = findViewById(R.id.btnChangeProfileBanner);
        btnChangePicture = findViewById(R.id.btnChangeProfilePicture);
        btnSave = findViewById(R.id.btnSaveEditProfile);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(editProfileScreen.this, "All your data has been recorded to server", Toast.LENGTH_SHORT).show();
//                Bitmap b; // your bitmap
//                b = convertImageViewToBitmap(imgProfPic);
//                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
//                b.compress(Bitmap.CompressFormat.PNG, 100, bStream);
//                byte[] byteArray = bStream.toByteArray();
                Intent intent = new Intent(editProfileScreen.this, MainActivity.class);
                String nama=name.getHint().toString();
                String desc=Description.getHint().toString();

                if(!name.getText().toString().equals("")){
                    nama=name.getText().toString();
                }
                if(!Description.getText().toString().equals("")){
                    desc=Description.getText().toString();
                }


                intent.putExtra("name", nama);
                intent.putExtra("desc", desc);

                SharedPreferences.Editor pref = getSharedPreferences("PROFILE",MODE_PRIVATE).edit().putString("PROFILE_NAME",nama);
                pref.putString("PROFILE_DESC",desc);
                pref.apply();
                //intent.putExtra("image", byteArray);
                startActivity(intent);
            }
        });


        pictureChangePopUp = new Dialog(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == PICK_IMAGE_PROF_PIC){
                Uri Source_uri = data.getData();
                Uri dest_uri = Uri.fromFile(new File(getCacheDir(),"Cropped_Profile_Picture"));
                imgType = "profile_picture";
                Crop.of(Source_uri, dest_uri).asSquare().start(this);
                imgProfPic.setImageURI(Crop.getOutput(data));
                pictureChangePopUp.dismiss();
            }
            else if(requestCode == PICK_IMAGE_PROF_BANNER){
                Uri Source_uri = data.getData();
                Uri dest_uri = Uri.fromFile(new File(getCacheDir(),"Cropped_Profile_Banner"));
                imgType = "banner";
                Crop.of(Source_uri, dest_uri).withAspect(3,1).start(this);
                imgBanner.setImageURI(Crop.getOutput(data));
                pictureChangePopUp.dismiss();

            }
            else if (requestCode == Crop.REQUEST_CROP){
                handle_crop_image(resultCode,data,imgType);
            }
        }
    }

    private void handle_crop_image(int code, Intent result, String imgType){
        if (code==RESULT_OK){
            if (imgType.equals("profile_picture")){
                imgProfPic.setImageURI(Crop.getOutput(result));
            }
            else if (imgType.equals("banner")){
                imgBanner.setImageURI(Crop.getOutput(result));
            }
        }
    }

    private Bitmap convertImageViewToBitmap(ImageView v){

        return ((BitmapDrawable)v.getDrawable()).getBitmap();
    }

    public void showPopUpChangeProfilePicture(View v){
        Button btnChangePhoto, btnClose;
        EditText imageFromUrl;
        ImageView imageViewShow;

        pictureChangePopUp.setContentView(R.layout.popupchangepicture);
        btnChangePhoto = (Button) pictureChangePopUp.findViewById(R.id.btnChangePhotoPopUpProfPic);
        btnClose = (Button) pictureChangePopUp.findViewById(R.id.btnClosePopUpProfPic);
        imageFromUrl = (EditText) pictureChangePopUp.findViewById(R.id.insertUrlPopUpProfPic);
        imageViewShow = (ImageView) pictureChangePopUp.findViewById(R.id.imageViewPopUpProfPic);
        Objects.requireNonNull(pictureChangePopUp.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pictureChangePopUp.show();

        if (imageViewShow!=null){
            imageViewShow.setImageBitmap(convertImageViewToBitmap(imgProfPic));
        }

        btnChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crop.pickImage(editProfileScreen.this,PICK_IMAGE_PROF_PIC);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureChangePopUp.dismiss();
            }
        });

    }

    public void showPopUpChangeProfileBanner(View v){
        Button btnChangePhoto, btnClose;
        EditText imageFromUrl;
        ImageView imageViewShow;

        pictureChangePopUp.setContentView(R.layout.popupchangepicture);
        btnChangePhoto = (Button) pictureChangePopUp.findViewById(R.id.btnChangePhotoPopUpProfPic);
        btnClose = (Button) pictureChangePopUp.findViewById(R.id.btnClosePopUpProfPic);
        imageFromUrl = (EditText) pictureChangePopUp.findViewById(R.id.insertUrlPopUpProfPic);
        imageViewShow = (ImageView) pictureChangePopUp.findViewById(R.id.imageViewPopUpProfPic);
        if (imageViewShow!=null){
            imageViewShow.setImageBitmap(convertImageViewToBitmap(imgBanner));
        }

        btnChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crop.pickImage(editProfileScreen.this,PICK_IMAGE_PROF_BANNER);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureChangePopUp.dismiss();
            }
        });
        Objects.requireNonNull(pictureChangePopUp.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT) {
        });
        pictureChangePopUp.show();

    }
}
