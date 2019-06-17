package com.example.user.zeeals;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.user.zeeals.service.RetroConnection;
import com.example.user.zeeals.service.UserClient;
import com.eyalbira.loadingdots.LoadingDots;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class editProfileScreen extends AppCompatActivity {
    private static final int PICK_IMAGE_PROF_PIC = 100;
    private static final int PICK_IMAGE_PROF_BANNER =101;
    final int READ_EXTERNAL_STORAGE = 110;
    private static final String TAG = "editProfileScreen";

    private Button btnChangePicture;
    private ImageView imgProfPic;
    private ImageView imgBanner;
    private EditText name, Description;
    private Dialog pictureChangePopUp;
    private String token;
    private boolean imgPick_Type;

    private String imgType="";
    private Uri imgPath;
    private LoadingDots profile_progress, banner_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_screen);

        name = findViewById(R.id.etName);
        Description = findViewById(R.id.etDescription);
        profile_progress=findViewById(R.id.editProfile_profilePict_progress);
        banner_progress=findViewById(R.id.editProfile_banner_progress);

        name.setHint(getSharedPreferences("PROFILE",MODE_PRIVATE).getString("PROFILE_NAME","Set your name"));
        Description.setHint(getSharedPreferences("PROFILE",MODE_PRIVATE).getString("PROFILE_DESC","Set your descripton"));
        String profUri = getSharedPreferences("PROFILE", MODE_PRIVATE).getString("PROFILE_URI", null);
        String bannerUri = getSharedPreferences("PROFILE", MODE_PRIVATE).getString("BANNER_URI", null);
        token=getSharedPreferences("TOKEN",MODE_PRIVATE).getString("TOKEN",null);

        View tb = findViewById(R.id.menu_appbar);
        tb.bringToFront();
        RelativeLayout btnBack = tb.findViewById(R.id.btnBack);
        ImageView btnBackIcon = tb.findViewById(R.id.btnback_icon);
        RelativeLayout btnEdit = tb.findViewById(R.id.btnEditPofile);


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
        if(profUri !=null) imgProfPic.setImageURI(Uri.parse(profUri));
        if(bannerUri !=null) imgBanner.setImageURI(Uri.parse(bannerUri));

        Button btnChangeBanner = findViewById(R.id.btnChangeProfileBanner);
        btnChangePicture = findViewById(R.id.btnChangeProfilePicture);
        Button btnSave = findViewById(R.id.editProfile_btn_save);
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


        pictureChangePopUp = new Dialog(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == PICK_IMAGE_PROF_PIC){
                imgType="profile";
                final Uri imageUri = data.getData();
                imgPath = Uri.fromFile(new File(getExternalCacheDir(), "avatar.jpeg"));
                CropImage.activity(imageUri).setOutputUri(imgPath).setAspectRatio(1,1).start(this);
                imgProfPic.setVisibility(View.INVISIBLE);
                pictureChangePopUp.dismiss();
            }
            else if(requestCode == PICK_IMAGE_PROF_BANNER){
                imgType="banner";
                final Uri imageUri = data.getData();
                imgPath = Uri.fromFile(new File(getExternalCacheDir(), "banner.jpeg"));
                CropImage.activity(imageUri).setOutputUri(imgPath).setAspectRatio(8,3).start(this);
                imgBanner.setVisibility(View.INVISIBLE);
                pictureChangePopUp.dismiss();
            }
            else if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
                String path = resultUri.getPath();
                uploadToServer(path,null);
            }
        }
    }

    private void uploadToServer(@Nullable final String filePath, @Nullable File fileFromURL) {
        RetroConnection conn = new RetroConnection();
        UserClient userClient = conn.getConnection();
        Uri path = null;

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("account_id","1")
                .addFormDataPart("mobile","$2y$10$s6ZYEuThB8IkZ0sl1ucOOeJFYf/4DmGNbeIyB6j4l9lPpwdu41n5K");

        if(filePath!=null){
            File imageFile = new File(filePath);
            path=Uri.parse(imageFile.getAbsolutePath());
            if(imageFile.exists()) builder.addFormDataPart("data",imageFile.getName(),RequestBody.create(MultipartBody.FORM,imageFile));
        }else if(fileFromURL!=null){
            if(fileFromURL.exists()){
                path=Uri.parse(fileFromURL.getAbsolutePath());
                builder.addFormDataPart("data",fileFromURL.getName(),RequestBody.create(MultipartBody.FORM,fileFromURL));
            }
        }

        RequestBody requestBody = builder.build();
        Call<ResponseBody> call=userClient.uploadProfile(token,requestBody);
        if(imgType.equals("banner")){
            call =  userClient.uploadBanner(token,requestBody);
            imgBanner.setVisibility(View.INVISIBLE);
            banner_progress.setVisibility(View.VISIBLE);
        }else {
            profile_progress.setVisibility(View.VISIBLE);
            imgProfPic.setVisibility(View.INVISIBLE);
        }

        final Uri finalPath = path;
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    if(imgType.equals("profile")){
                        getSharedPreferences("PROFILE",MODE_PRIVATE).edit().putString("PROFILE_URI",imgPath.toString()).apply();
                        profile_progress.setVisibility(View.GONE);
                        imgProfPic.setVisibility(View.VISIBLE);
                        imgProfPic.setImageURI(finalPath);
                    }
                    else{
                        getSharedPreferences("PROFILE",MODE_PRIVATE).edit().putString("BANNER_URI",imgPath.toString()).apply();
                        banner_progress.setVisibility(View.GONE);
                        imgBanner.setVisibility(View.VISIBLE);
                        imgBanner.setImageURI(finalPath);
                    }
                }else{
                    if(imgType.equals("profile")){
                        imgProfPic.setVisibility(View.VISIBLE);
                        profile_progress.setVisibility(View.GONE);
                    }else{
                        banner_progress.setVisibility(View.GONE);
                        imgBanner.setVisibility(View.VISIBLE);
                    }
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(editProfileScreen.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(editProfileScreen.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(editProfileScreen.this,"Upload image error",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d(TAG, "onFailure: "+t.getMessage());
                if(imgType.equals("profile")){
                    imgProfPic.setVisibility(View.VISIBLE);
                    profile_progress.setVisibility(View.GONE);
                }else{
                    banner_progress.setVisibility(View.GONE);
                    imgBanner.setVisibility(View.VISIBLE);
                }
                Toast.makeText(editProfileScreen.this,"Connection error",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private Bitmap convertImageViewToBitmap(ImageView v){

        return ((BitmapDrawable)v.getDrawable()).getBitmap();
    }

    public void showPopUpChangeProfilePicture(View v){
        Button btnChangePhoto, btnClose,btn_OK;
        final EditText imageFromUrl;
        final ImageView imageViewShow;

        pictureChangePopUp.setContentView(R.layout.popupchangepicture);
        btnChangePhoto =  pictureChangePopUp.findViewById(R.id.btnChangePhotoPopUpProfPic);
        btnClose =  pictureChangePopUp.findViewById(R.id.btnClosePopUpProfPic);
        imageFromUrl =  pictureChangePopUp.findViewById(R.id.insertUrlPopUpProfPic);
        imageViewShow =  pictureChangePopUp.findViewById(R.id.imageViewPopUpProfPic);
        btn_OK = pictureChangePopUp.findViewById(R.id.btnOkPopUpProfPic);
        Objects.requireNonNull(pictureChangePopUp.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pictureChangePopUp.show();

        if (imageViewShow!=null){
            imageViewShow.setImageBitmap(convertImageViewToBitmap(imgProfPic));
        }

        btnChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPick_Type=true;
                checkPermission();

            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureChangePopUp.dismiss();
            }
        });
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url_text = imageFromUrl.getText().toString();
                if(!url_text.equals("")) {
                    imgType="profile";
                    new DownloadImageTask(imgProfPic).execute(url_text);
                    pictureChangePopUp.dismiss();

                }else{
                    pictureChangePopUp.dismiss();
                }

            }
        });

    }

    public void showPopUpChangeProfileBanner(View v){
        Button btnChangePhoto, btnClose,btn_OK;
        final EditText imageFromUrl;
        CardView image_container;
        final ImageView imageViewShow;

        pictureChangePopUp.setContentView(R.layout.popupchangepicture);
        btnChangePhoto = pictureChangePopUp.findViewById(R.id.btnChangePhotoPopUpProfPic);
        btnClose =  pictureChangePopUp.findViewById(R.id.btnClosePopUpProfPic);
        imageFromUrl =  pictureChangePopUp.findViewById(R.id.insertUrlPopUpProfPic);
        imageViewShow =  pictureChangePopUp.findViewById(R.id.imageViewPopUpProfPic);
        image_container = pictureChangePopUp.findViewById(R.id.img_layout);
        btn_OK = pictureChangePopUp.findViewById(R.id.btnOkPopUpProfPic);
        image_container.requestLayout();
        image_container.getLayoutParams().width = Math.round(250*getResources().getDisplayMetrics().density);
        if (imageViewShow!=null){
            imageViewShow.setImageBitmap(convertImageViewToBitmap(imgBanner));
        }

        btnChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPick_Type=false;
                checkPermission();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureChangePopUp.dismiss();
            }
        });
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url_text = imageFromUrl.getText().toString();
                if(!url_text.equals("")) {
                    imgType="banner";
                    new DownloadImageTask(imgProfPic).execute(url_text);
                    pictureChangePopUp.dismiss();

                }else{
                    pictureChangePopUp.dismiss();
                }

            }
        });

        Objects.requireNonNull(pictureChangePopUp.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT) {});
        pictureChangePopUp.show();

    }

    void checkPermission(){
        if (ContextCompat.checkSelfPermission(editProfileScreen.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(editProfileScreen.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(editProfileScreen.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            if(imgPick_Type) startActivityForResult(photoPickerIntent, PICK_IMAGE_PROF_PIC);
            else startActivityForResult(photoPickerIntent, PICK_IMAGE_PROF_BANNER);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==READ_EXTERNAL_STORAGE) {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /* permission was granted, yay! */

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    if(imgPick_Type) startActivityForResult(photoPickerIntent, PICK_IMAGE_PROF_PIC);
                    else startActivityForResult(photoPickerIntent, PICK_IMAGE_PROF_BANNER);
                } else {
                    // permission denied !
                    Toast.makeText(editProfileScreen.this,"Permission denied",Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    /* Execute getting image from URL asynchronously */
    @SuppressLint("StaticFieldLeak")
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        private DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @SuppressLint("WrongThread")
        protected void onPostExecute(Bitmap result) {
            String file_path = getApplicationContext().getCacheDir().getPath();
            File dir = new File(file_path);
            if(!dir.exists())
                dir.mkdirs();
            File file = new File(dir, "avatar.jpeg");
            FileOutputStream fOut;
            try {
                fOut = new FileOutputStream(file);
                result.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                fOut.flush();
                fOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            imgPath = Uri.parse(file.getAbsolutePath());
            uploadToServer(null, file);
        }
    }
}
