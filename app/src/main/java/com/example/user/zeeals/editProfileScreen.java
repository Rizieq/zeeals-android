package com.example.user.zeeals;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.user.zeeals.adapter.MainActivity;
import com.example.user.zeeals.service.RetroConnection;
import com.example.user.zeeals.service.UserClient;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class editProfileScreen extends AppCompatActivity {
    private static final int PICK_IMAGE_PROF_PIC = 100;
    private static final int PICK_IMAGE_PROF_BANNER =101;
    private static final String TAG = "editProfileScreen";

    private Button btnChangePicture;
    private Button btnSave;
    private RelativeLayout btnBack,  btnEdit;
    private ImageView imgProfPic, imgBanner,btnBackIcon;
    private View tb;
    private EditText name, Description;
    private Dialog pictureChangePopUp;
    private String profUri,bannerUri,token;
    private boolean profileChanged=false;
    private boolean bannerChanged=false;




    private String imgType="";
    private RetroConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_screen);

        name = findViewById(R.id.etName);
        Description = findViewById(R.id.etDescription);

        name.setHint(getSharedPreferences("PROFILE",MODE_PRIVATE).getString("PROFILE_NAME","Set your name"));
        Description.setHint(getSharedPreferences("PROFILE",MODE_PRIVATE).getString("PROFILE_DESC","Set your descripton"));
        profUri=getSharedPreferences("PROFILE",MODE_PRIVATE).getString("PROFILE_URI",null);
        bannerUri=getSharedPreferences("PROFILE",MODE_PRIVATE).getString("BANNER_URI",null);
        token=getSharedPreferences("TOKEN",MODE_PRIVATE).getString("TOKEN",null);

        tb = findViewById(R.id.menu_appbar);
        tb.bringToFront();
        btnBack = tb.findViewById(R.id.btnBack);
        btnBackIcon = tb.findViewById(R.id.btnback_icon);
        btnEdit = tb.findViewById(R.id.btnEditPofile);
        btnBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnEdit.setVisibility(View.GONE);

        imgProfPic = findViewById(R.id.ivChangePhoto);
        imgBanner = findViewById(R.id.ivChangeBanner);
        if(profUri!=null) imgProfPic.setImageURI(Uri.parse(profUri));
        if(bannerUri!=null) imgBanner.setImageURI(Uri.parse(bannerUri));

        Button btnChangeBanner = findViewById(R.id.btnChangeProfileBanner);
        btnChangePicture = findViewById(R.id.btnChangeProfilePicture);
        btnSave = findViewById(R.id.editProfile_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nama=name.getHint().toString();
                String desc=Description.getHint().toString();

                if(!name.getText().toString().equals("")){
                    nama=name.getText().toString();
                }
                if(!Description.getText().toString().equals("")){
                    desc=Description.getText().toString();
                }

                SharedPreferences.Editor pref = getSharedPreferences("PROFILE",MODE_PRIVATE).edit().putString("PROFILE_NAME",nama);
                pref.putString("PROFILE_DESC",desc);
                pref.apply();

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
                Uri uri = Crop.getOutput(data);
                imgProfPic.setImageURI(Crop.getOutput(data));
                pictureChangePopUp.dismiss();
            }
            else if(requestCode == PICK_IMAGE_PROF_BANNER){
                Uri Source_uri = data.getData();
                Uri dest_uri = Uri.fromFile(new File(getCacheDir(),"Cropped_Profile_Banner"));
                imgType = "banner";
                Crop.of(Source_uri, dest_uri).withAspect(8,3).start(this);
                Uri uri = Crop.getOutput(data);
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
                Uri uri = Crop.getOutput(result);
                profUri=uri.toString();
                getSharedPreferences("PROFILE",MODE_PRIVATE).edit().putString("PROFILE_URI",uri.toString()).apply();
                profileChanged=true;
                uploadToServer(uri.getPath(),true);
            }
            else if (imgType.equals("banner")){
                imgBanner.setImageURI(Crop.getOutput(result));
                Uri uri = Crop.getOutput(result);
                bannerUri=uri.toString();
                getSharedPreferences("PROFILE",MODE_PRIVATE).edit().putString("BANNER_URI",uri.toString()).apply();
                bannerChanged=true;
                uploadToServer(uri.getPath(),false);
            }
        }
    }

    private void uploadToServer(String filePath,boolean profile) {
        conn  = new RetroConnection();
        UserClient userClient = conn.getConnection();

        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //
        Call<ResponseBody> call;
        if(profile){
            call= userClient.uploadProfile(token,part, description);
        }else call = userClient.uploadBanner(token,part, description);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(editProfileScreen.this,"Upload image Success",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(editProfileScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(editProfileScreen.this,"Upload image error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(editProfileScreen.this,"Connection error",Toast.LENGTH_SHORT).show();
            }
        });
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
