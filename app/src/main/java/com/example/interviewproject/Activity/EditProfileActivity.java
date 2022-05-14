package com.example.interviewproject.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.interviewproject.Netowrk.WebService;
import com.example.interviewproject.POJO.EditeProfilePOJO;
import com.example.interviewproject.POJO.ProfilePOJO;
import com.example.interviewproject.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    EditText edite_name, edite_mobile, edite_DOB;
    CircleImageView profile_image_select, profile_image;
    Button save;
    String userid, token;
    SpinKitView spin_kit;
    Uri uri;
    String waypathPhoto_book1;
    MultipartBody.Part imagePartPhoto_book1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        userid = sharedPreferences.getString("userid", "");

        edite_name = findViewById(R.id.edite_name);
        profile_image_select = findViewById(R.id.profile_image_select);
        profile_image = findViewById(R.id.profile_image);
        edite_mobile = findViewById(R.id.edite_mobile);
        edite_DOB = findViewById(R.id.edite_DOB);
        save = findViewById(R.id.save);
        spin_kit = findViewById(R.id.spin_kit);

        profile_image_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(EditProfileActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            }
        });

        WebService.getClient().profilelist(userid, token).enqueue(new Callback<ProfilePOJO>() {
            @Override
            public void onResponse(Call<ProfilePOJO> call, Response<ProfilePOJO> response) {
                if (response.body().getResponse().getMessage().equals("profile details.")) {
                    edite_name.setText(response.body().getResponse().getProfile().get(0).getFullname());
                    edite_mobile.setText(response.body().getResponse().getProfile().get(0).getMobile());
                    edite_DOB.setText(response.body().getResponse().getProfile().get(0).getDob());
                } else {

                }
            }

            @Override
            public void onFailure(Call<ProfilePOJO> call, Throwable t) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (waypathPhoto_book1 == null) {
                    //Toast.makeText(EditeCoustomerDetailsActivity.this, "Null", Toast.LENGTH_SHORT).show();
                } else {
                    File file1 = new File(waypathPhoto_book1);
                    final RequestBody requestBody1 = RequestBody.create(MediaType.parse("image/*"), file1);
                    imagePartPhoto_book1 = MultipartBody.Part.createFormData("user_image", file1.getName(), requestBody1);
                    Log.d("TAG", "onClick: " + imagePartPhoto_book1);
                }
                spin_kit.setVisibility(View.VISIBLE);
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
                Sprite WanderingCubes = new Circle();
                progressBar.setIndeterminateDrawable(WanderingCubes);
                WebService.getClient().editprofile(
                        RequestBody.create(MediaType.parse("text/plain"), userid),
                        RequestBody.create(MediaType.parse("text/plain"), edite_name.getText().toString().trim()),
                        RequestBody.create(MediaType.parse("text/plain"), edite_mobile.getText().toString().trim()),
                        RequestBody.create(MediaType.parse("text/plain"), edite_DOB.getText().toString().trim()),
                        RequestBody.create(MediaType.parse("text/plain"), token),
                        imagePartPhoto_book1
                ).enqueue(new Callback<EditeProfilePOJO>() {
                    @Override
                    public void onResponse(Call<EditeProfilePOJO> call, Response<EditeProfilePOJO> response) {
                        spin_kit.setVisibility(View.GONE);
                        if (!response.body().getResponse().getStatus().equals("201")) {
                            Toast.makeText(EditProfileActivity.this, "Data Not Update", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Data Update", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<EditeProfilePOJO> call, Throwable t) {
                        spin_kit.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null) {
                uri = data.getData();
                waypathPhoto_book1 = getFilePath(EditProfileActivity.this, uri);
                Uri selectedImageUri = data.getData();
                profile_image.setImageURI(selectedImageUri);
                //profile_image.setImageURI(uri);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    public String getFilePath(Context context, Uri uri) {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/all_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}