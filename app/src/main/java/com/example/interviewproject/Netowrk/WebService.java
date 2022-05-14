package com.example.interviewproject.Netowrk;


import com.example.interviewproject.POJO.EditeProfilePOJO;
import com.example.interviewproject.POJO.LoginResponsePOJO;
import com.example.interviewproject.POJO.LogoutPOJO;
import com.example.interviewproject.POJO.ProfilePOJO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public class WebService {
    private static WebServiceInterface webApiInterface;

    public static WebServiceInterface getClient() {
        if (webApiInterface == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okclient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .readTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(10, TimeUnit.MINUTES)
                    .build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .setLenient()
                    .create();

            Retrofit client = new Retrofit.Builder()
                    .baseUrl("https://single2mingal.com/chatapp/public/api/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okclient)
                    .build();

            webApiInterface = client.create(WebServiceInterface.class);
        }
        return webApiInterface;
    }

    public interface WebServiceInterface {

        @POST("login")
        @FormUrlEncoded
        Call<LoginResponsePOJO> login(
                @Field("email") String email,
                @Field("password") String password
        );

        @POST("profilelist")
        @FormUrlEncoded
        Call<ProfilePOJO> profilelist(
                @Field("id") String id,
                @Field("token") String token
        );

        @POST("logout")
        @FormUrlEncoded
        Call<LogoutPOJO> logout(
                @Field("id") String id,
                @Field("token") String token
        );

        @POST("editprofile")
        @Multipart
        Call<EditeProfilePOJO> editprofile(
                @Part("id") RequestBody id,
                @Part("Fullname") RequestBody Fullname,
                @Part("mobile") RequestBody mobile,
                @Part("Dob") RequestBody Dob,
                @Part("token") RequestBody token,
                @Part MultipartBody.Part user_image
                );
    }
}