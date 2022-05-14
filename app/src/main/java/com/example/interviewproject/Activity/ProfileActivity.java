package com.example.interviewproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.interviewproject.Netowrk.WebService;
import com.example.interviewproject.POJO.LoginResponsePOJO;
import com.example.interviewproject.POJO.LogoutPOJO;
import com.example.interviewproject.POJO.ProfilePOJO;
import com.example.interviewproject.R;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    TextView user_name, user_mobile, user_email, user_dob;
    String userid, token;
    Button logout, editprofile;
    LinearLayout back;
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        userid = sharedPreferences.getString("userid", "");

        Log.d("TAG", "onCreate: " + token + " \n" + userid);

        user_name = findViewById(R.id.user_name);
        profile_image = findViewById(R.id.profile_image);
        back = findViewById(R.id.back);
        logout = findViewById(R.id.logout);
        editprofile = findViewById(R.id.editprofile);
        user_mobile = findViewById(R.id.user_mobile);
        user_email = findViewById(R.id.user_email);
        user_dob = findViewById(R.id.user_dob);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutmethod();
            }
        });

        WebService.getClient().profilelist(userid, token).enqueue(new Callback<ProfilePOJO>() {
            @Override
            public void onResponse(Call<ProfilePOJO> call, Response<ProfilePOJO> response) {
                if (response.body().getResponse().getMessage().equals("profile details.")) {
                    Glide.with(ProfileActivity.this).load(response.body().getResponse().getProfile().get(0).getUserImage()).into(profile_image);
                    user_name.setText(response.body().getResponse().getProfile().get(0).getFullname());
                    user_mobile.setText(response.body().getResponse().getProfile().get(0).getMobile());
                    user_email.setText(response.body().getResponse().getProfile().get(0).getEmail());
                    user_dob.setText(response.body().getResponse().getProfile().get(0).getDob());
                } else {

                }
            }

            @Override
            public void onFailure(Call<ProfilePOJO> call, Throwable t) {

            }
        });
    }

    private void logoutmethod() {

        WebService.getClient().logout(userid, token).enqueue(new Callback<LogoutPOJO>() {
            @Override
            public void onResponse(Call<LogoutPOJO> call, Response<LogoutPOJO> response) {
                if (response.body().getMessage().equals("Logout   successfully..")) {
                    Toast.makeText(ProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LogoutPOJO> call, Throwable t) {

            }
        });
    }
}