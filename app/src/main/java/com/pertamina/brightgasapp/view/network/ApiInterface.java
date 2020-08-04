package com.pertamina.brightgasapp.view.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.brightgasapp.ChangePassword;
import com.pertamina.brightgasapp.model.Order.SubmitOrder;
import com.pertamina.brightgasapp.model.code.Code;
import com.pertamina.brightgasapp.model.code.Scan;
import com.pertamina.brightgasapp.model.login.ForgotPass;
import com.pertamina.brightgasapp.model.productList.ProductData;
import com.pertamina.brightgasapp.model.login.LoginModel;
import com.pertamina.brightgasapp.model.register.RegisterModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("api/auth")
    Call<JsonObject> getToken();

    @POST("api/product")
    Call<JsonObject> getResultCode(
            @Header("Authorization") String token,
            @Body Code codeQR
    );

    @GET("master/provinces")
    Call<JsonArray> getProvinces();

    @GET("master/provinces/{provinceId}/cities")
    Call<JsonArray> getCities
            (@Path("provinceId") String id,
             @Query("page") String page,
             @Query("page_size") String pageSize);

    @GET("master/cities/{cityId}/sub-districts")
    Call<JsonArray> getDistricts
            (@Path("cityId") String id);

    @POST("api/user/signup")
    Call<JsonObject> registerAccount(
            @Header("Accept") String accept,
            @Header("Content-Type") String content,
            @Body RegisterModel registerModel);

    @POST("api/user/signin")
    Call<JsonObject> signIn(
            @Header("Accept") String accept,
            @Header("Content-Type") String content,
            @Body LoginModel loginModel);

    @GET("api/product-order/by-region")
    Call<ProductData> getProduct(
            @Header("Content-Yype") String content,
            @Header("Accept") String accept,
            @Query("kota_id") String kota
    );

    @GET("api/master-promo/cek")
    Call<JsonObject> checkPromo(
            @Header("Authorization") String token,
            @Header("Content-Yype") String content,
            @Header("Accept") String accept,
            @Query("kode_promo") String kode
    );

    @POST("api/order")
    Call<JsonObject> submitOrder(
            @Header("Authorization") String token,
            @Header("Content-Yype") String content,
            @Header("Accept") String accept,
            @Body SubmitOrder submitOrder
    );

    @POST("api/user/change-password")
    Call<JsonObject> changePassword(
            @Header("Authorization") String token,
            @Header("Content-Yype") String content,
            @Header("Accept") String accept,
            @Body ChangePassword changePassword
    );

    @GET("api/user/auth")
    Call<JsonObject> getData(
            @Header("Authorization") String token,
            @Header("Content-Type") String content,
            @Header("Accept") String accept
    );


    @Multipart
    @POST("api/user/upload-ktp")
    Call<JsonObject> uploadKTP
            (@Header("Accept") String type,
             @Header("Content-Type") String accept,
             @Header("Authorization") String token,
             @Part("id") RequestBody id,
             @Part MultipartBody.Part foto_ktp
            );


    @POST("api/user/forgot-password")
    Call<JsonObject> forgotPassword(
            @Header("Content-Yype") String content,
            @Header("Accept") String accept,
            @Body ForgotPass forgotPass
    );


//    @POST("api/product/GetQrCode")
//    Call<JsonObject> getQR(
//            @Header("Authorization") String token,
//            @Body Code code
//    );

    @POST("api/scan")
    Call<JsonObject> postHistory(
            @Header("Content-Yype") String content,
            @Header("Accept") String accept,
            @Body Scan scan
    );


}
