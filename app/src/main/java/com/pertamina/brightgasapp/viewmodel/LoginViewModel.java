package com.pertamina.brightgasapp.viewmodel;


import android.util.Log;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.pertamina.brightgasapp.ChangePassword;
import com.pertamina.brightgasapp.model.login.ForgotPass;
import com.pertamina.brightgasapp.model.login.LoginModel;
import com.pertamina.brightgasapp.view.network.ApiClient;
import com.pertamina.brightgasapp.view.network.ApiInterface;
import com.pertamina.brightgasapp.view.network.RetrofitInterface;
import com.pertamina.brightgasapp.view.utilities.Preference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    public void loginAccount(LoginModel loginModel, RetrofitInterface retrofitInterface){
        ApiInterface service = ApiClient.getDataBrightGas().create(ApiInterface.class);
        try {
            Call<JsonObject> call = service
                    .signIn("application/json","application/json", loginModel);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("lolll", "onResponse: "+ response.code() + response.body());
                    if(response.code() == 200){
                        retrofitInterface.onRequestSuccess(response.body());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("lolfinal", "error: " + t.toString());
                    retrofitInterface.onRequestError(t.toString());
                }
            });
        } catch (Exception e) {
            retrofitInterface.onRequestError(e.toString());
        }
    }


    public void changePassword(String token,ChangePassword changePassword, RetrofitInterface retrofitInterface){
        ApiInterface service = ApiClient.getDataBrightGas().create(ApiInterface.class);
        try {
            Call<JsonObject> call = service
                    .changePassword(token,"application/json","application/json",
                            changePassword);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("lolll", "onResponse: "+ response.code() + response.body());
                    if(response.code() == 200){
                        retrofitInterface.onRequestSuccess(response.body());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("lolfinal", "error: " + t.toString());
                    retrofitInterface.onRequestError(t.toString());
                }
            });
        } catch (Exception e) {
            retrofitInterface.onRequestError(e.toString());
        }
    }
    public void forgotPassword(ForgotPass forgotPass, RetrofitInterface retrofitInterface){
        Log.d("lolll", "onResponseTolol: ");
        ApiInterface service = ApiClient.getDataBrightGas().create(ApiInterface.class);
        try {
            Call<JsonObject> call = service
                    .forgotPassword("application/json","application/json",forgotPass);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("lolll", "onResponseTolol: "+ response.code() + response.body());
                    if(response.code() == 200){
                        retrofitInterface.onRequestSuccess(response.body());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("lolfinal", "error: " + t.toString());
                    retrofitInterface.onRequestError(t.toString());
                }
            });
        } catch (Exception e) {
            Log.d("lolfinal", "error: " + e.toString());
            retrofitInterface.onRequestError(e.toString());
        }
    }

}
