package com.pertamina.brightgasapp.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.pertamina.brightgasapp.model.Order.OrderProductData;
import com.pertamina.brightgasapp.model.Order.OrderProductResult;
import com.pertamina.brightgasapp.model.productList.ProductData;
import com.pertamina.brightgasapp.model.productList.ProductResult;
import com.pertamina.brightgasapp.view.network.ApiClient;
import com.pertamina.brightgasapp.view.network.ApiInterface;
import com.pertamina.brightgasapp.view.utilities.Preference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderViewModel extends ViewModel {

    private MutableLiveData<List<OrderProductResult>> productListData = new MutableLiveData<>();

    private ApiInterface service = ApiClient.getDataBrightGas().create(ApiInterface.class);

    public LiveData<List<OrderProductResult>> getProduct(Context mContext) {
        Preference preference = new Preference(mContext);
        Gson gson = new Gson();
        String jsonnew = preference.getProduct();
        OrderProductData order = gson.fromJson(jsonnew, OrderProductData.class);
        productListData.setValue(order.getProductResults());
//        productListData

        return productListData;
    }

}

