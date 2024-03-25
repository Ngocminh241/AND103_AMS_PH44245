package com.example.and103_ams_ph44245.service;
import com.example.and103_ams_ph44245.Model.Distributor;
import com.example.and103_ams_ph44245.Model.Fruit;
import com.example.and103_ams_ph44245.Model.Response;
import com.example.and103_ams_ph44245.Model.User;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    String ipv4 = "10.24.22.198";
    String DOMAIN = "http://" + ipv4 + ":3000/api/";

    @GET("get-list-distributor")
    Call<Response<ArrayList<Distributor>>> getDistributor();
    @POST("add-distributor")
    Call<Response<Distributor>> addDistributor(@Body Distributor distributor);
    @PUT("update-distributor-by-id/{id}")
    Call<Response<Distributor>> updateDistributor(@Path("id") String id,@Body Distributor distributor);
    @DELETE("destroy-distributor-by-id/{id}")
    Call<Response<Distributor>> deleteDistributor(@Path("id") String id);
    @GET("search-distributor")
    Call<Response<ArrayList<Distributor>>> searchDistributor(@Query("key") String key);

    @GET("get-fruit")
    Call<Response<ArrayList<Fruit>>> getFruits();

    @DELETE("destroy-fruit-by-id/{id}")
    Call<Response<Void>> deleteFruit(@Path("id") String id);

    //lab 6
    @Multipart
    @POST("add-fruit-with-file-image")
    Call<Response<Fruit>> addFruitWithFileImage(@PartMap Map<String, RequestBody> requestBodyMap,
                                                @Part ArrayList<MultipartBody.Part> ds_hinh);

    @Multipart
    @PUT("update-fruit-by-id/{id}")
    Call<Response<Void>> updateFruitWithFileImage(@Path("id") String id,
                                                  @PartMap Map<String, RequestBody> requestBodyMap,
                                                  @Part ArrayList<MultipartBody.Part> ds_hinh);

    @GET("get-fruit-authenticate-token")
    Call<Response<ArrayList<Fruit>>> getFruitToken(@Header("Authorization") String token);

    @GET("get-fruit-by-id")
    Call<Response<ArrayList<Fruit>>> searchFruitById(@Query("id") String id);
    @GET("search-fruit-by-name")
    Call<Response<ArrayList<Fruit>>> searchFruitByName(@Query("name") String name);
    @GET("get-fruit-in-price")
    Call<Response<ArrayList<Fruit>>> searchFruitByPrice(@Query("price") String price);

    @Multipart
    @POST("register-send-email")
    Call<Response<User>> register(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("email") RequestBody email,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part avartar);

    @POST("login")
    Call<Response<User>> login(@Body User user);

    @GET("get-list-fruit")
    Call<Response<ArrayList<Fruit>>> getListFruit(@Header("Authorization") String token);
}