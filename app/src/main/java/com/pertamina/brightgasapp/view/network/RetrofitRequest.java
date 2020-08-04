package com.pertamina.brightgasapp.view.network;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitRequest {

    public void getProvinces(RetrofitInterface retrofitInterface) {
        ApiInterface service = ApiClient.getDataMusicool().create(ApiInterface.class);
        Call<JsonArray> call = service.getProvinces();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();
                retrofitInterface.onRequestSuccess2(jsonArray);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
            }
        });
    }

    public void getCities(String id,RetrofitInterface retrofitInterface) {
        Log.d("lol", "getCities: " + id);
        ApiInterface service = ApiClient.getDataMusicool().create(ApiInterface.class);
        Call<JsonArray> call = service.getCities(id, "1", "0");

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();
                retrofitInterface.onRequestSuccess2(jsonArray);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
            }
        });
    }

    public void getDistricts(String id,RetrofitInterface retrofitInterface) {
        Log.d("lol", "getDistricts: " + id);
        ApiInterface service = ApiClient.getDataMusicool().create(ApiInterface.class);
        Call<JsonArray> call = service.getDistricts(id);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();
                retrofitInterface.onRequestSuccess2(jsonArray);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
            }
        });
    }

    public void getToken(RetrofitInterface retrofitInterface){
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);

        try {

            Call<JsonObject> call = service.getToken();
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("lol", "onResponse: " + response);
                    if(response.code() == 200 ){
                        Log.d("lol", "onResponse: " + response);
                        retrofitInterface.onRequestSuccess(response.body());
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("lolfinal", "error: " + t.toString());
                }
            });
        } catch (Exception e) {
            Log.d("lolfinal", "uploadError: " + e.toString());
        }
    }


}
