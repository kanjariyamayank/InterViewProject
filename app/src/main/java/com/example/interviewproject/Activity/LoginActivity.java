package com.example.interviewproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.interviewproject.Netowrk.WebService;
import com.example.interviewproject.POJO.LoginResponsePOJO;
import com.example.interviewproject.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edt_pass, edt_email;
    SpinKitView spin_kit;
    ImageSlider image_slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        image_slider = findViewById(R.id.image_slider);
        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pass);
        spin_kit = findViewById(R.id.spin_kit);

        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.slide1, null));
        slideModels.add(new SlideModel(R.drawable.slide2, null));
        slideModels.add(new SlideModel(R.drawable.slide3, null));
        image_slider.setImageList(slideModels, ScaleTypes.FIT);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginmethod();
            }
        });
    }

    private void loginmethod() {
        if (edt_email.getText().toString().equals("")) {
            Toast.makeText(this, "Plz enter the Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edt_pass.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Plz enter the password", Toast.LENGTH_SHORT).show();
        } else {

            spin_kit.setVisibility(View.VISIBLE);
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
            Sprite WanderingCubes = new Circle();
            progressBar.setIndeterminateDrawable(WanderingCubes);

            WebService.getClient().login(
                    edt_email.getText().toString().trim(),
                    edt_pass.getText().toString().trim()).enqueue(new Callback<LoginResponsePOJO>() {
                @Override
                public void onResponse(Call<LoginResponsePOJO> call, Response<LoginResponsePOJO> response) {
                    spin_kit.setVisibility(View.GONE);
                    if (response.body().getResponse().getMessage().equals("login success.")) {
                        SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userid", String.valueOf(response.body().getResponse().getUser().getId()));
                        editor.putString("token", String.valueOf(response.body().getResponse().getToken()));
                        /*editor.putString("status", "login");*/
                        editor.apply();

                        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));

                        Toast.makeText(LoginActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponsePOJO> call, Throwable t) {
                    Log.d("TAG", "onFailusdare: "+t.getMessage());
                    spin_kit.setVisibility(View.GONE);
                }
            });

        }
    }
}