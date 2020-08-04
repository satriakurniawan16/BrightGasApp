package com.pertamina.brightgasapp.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pertamina.brightgasapp.model.login.LoginModel;
import com.pertamina.brightgasapp.model.productList.ProductData;
import com.pertamina.brightgasapp.model.productList.ProductResult;
import com.pertamina.brightgasapp.view.network.ApiClient;
import com.pertamina.brightgasapp.view.network.ApiInterface;
import com.pertamina.brightgasapp.view.network.RetrofitInterface;
import com.pertamina.brightgasapp.view.utilities.Preference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListViewModel extends ViewModel {

    private MutableLiveData<List<ProductResult>> productListData = new MutableLiveData<>();

    private ApiInterface service = ApiClient.getDataBrightGas().create(ApiInterface.class);

    public LiveData<List<ProductResult>> getProduct(Context mContext,String id) {
        Call<ProductData> call = service.getProduct("application/json","application/json",id);
        call.enqueue(new Callback<ProductData>() {
            @Override
            public void onResponse(@NonNull Call<ProductData> call, @NonNull Response<ProductData> response) {
                Log.d("ganteng", "onResponse: " + response.code() +response.message());
                if(response.code() == 200){
                    if (response.body() != null) {
                        Log.d("ganteng", "onResponse: " + response.body().toString());
                        if(response.body().getProductTrade().getProductResults() != null) {
                            productListData.setValue(response.body().getProductTrade().getProductResults());
                        } else{
                            String jsonString = "{\n" +
                                    "  \"data\": {\n" +
                                    "    \"Trade In\": [\n" +
                                    "       ]}\n" +
                                    "}";
                            Gson g = new Gson();
                            ProductData result = g.fromJson(jsonString, ProductData.class);

                            productListData.setValue(result.getProductTrade().getProductResults());
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductData> call, @NonNull Throwable t) {
                Toast.makeText(mContext, "njengtolo", Toast.LENGTH_LONG).show();
            }
        });
        return productListData;
    }

    public void checkPromoCode(String kode,String token, RetrofitInterface retrofitInterface){
        ApiInterface service = ApiClient.getDataBrightGas().create(ApiInterface.class);
        try {
            Call<JsonObject> call = service
                    .checkPromo("Bearer "+token,"application/json","application/json", kode);
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

}
