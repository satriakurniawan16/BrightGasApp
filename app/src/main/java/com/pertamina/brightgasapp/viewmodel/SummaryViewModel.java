package com.pertamina.brightgasapp.viewmodel;

import android.util.Log;

import com.google.gson.JsonObject;
import com.pertamina.brightgasapp.model.Order.SubmitOrder;
import com.pertamina.brightgasapp.model.register.RegisterModel;
import com.pertamina.brightgasapp.view.network.ApiClient;
import com.pertamina.brightgasapp.view.network.ApiInterface;
import com.pertamina.brightgasapp.view.network.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SummaryViewModel {

    public void orderNow(String token, SubmitOrder submitOrder, RetrofitInterface retrofitInterface){
        ApiInterface service = ApiClient.getDataBrightGas().create(ApiInterface.class);
        try {
            Call<JsonObject> call = service
                    .submitOrder(token,"application/json","application/json", submitOrder);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("lolll", "onResponse: "+ response.code());
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
}
