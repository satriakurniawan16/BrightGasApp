package com.pertamina.brightgasapp.view.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.brightgasapp.R;
import com.pertamina.brightgasapp.model.Order.Order;
import com.pertamina.brightgasapp.model.Order.OrderProductResult;
import com.pertamina.brightgasapp.model.login.LoginModel;
import com.pertamina.brightgasapp.model.productList.ProductResult;
import com.pertamina.brightgasapp.view.adapter.ProductAdapter;
import com.pertamina.brightgasapp.view.base.BaseActivity;
import com.pertamina.brightgasapp.view.network.RetrofitInterface;
import com.pertamina.brightgasapp.view.utilities.Preference;
import com.pertamina.brightgasapp.view.utilities.Util;
import com.pertamina.brightgasapp.viewmodel.ProductListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductListActivity extends BaseActivity {

    private ProductAdapter productAdapter;
    private List<ProductResult> productResultList;

    private LinearLayout emptyView;

    private Util util;

    private Preference preference;

    private ProgressBar progressBar;
    private ProgressDialog pDialog;

    private LinearLayout nextStep;
    private LinearLayout prevStep;
    private LinearLayout cekPromo;
    private EditText promoCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        setUpView();
        generateView();
        setupListener();
    }

    @Override
    public void setUpView() {

        progressBar = findViewById(R.id.progressBar);
        emptyView = findViewById(R.id.empty_view);

        util = new Util();
        pDialog = new ProgressDialog(this);

        productResultList = new ArrayList<>();
        productAdapter = new ProductAdapter(productResultList, emptyView);
        productAdapter.notifyDataSetChanged();

        nextStep = findViewById(R.id.button_next);
        promoCode = findViewById(R.id.promocode);
        cekPromo = findViewById(R.id.cek_promo);
        prevStep = findViewById(R.id.button_prev);

        RecyclerView recyclerView = findViewById(R.id.rv_product);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(productAdapter);

        preference = new Preference(this);
    }

    @Override
    public void generateView() {

    }

    @Override
    public void setupListener() {
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPromo();
            }
        });

        prevStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cekPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductListViewModel productListViewModel= new ProductListViewModel();
                productListViewModel.checkPromoCode(promoCode.getText().toString(), preference.getToken2(), new RetrofitInterface() {
                    @Override
                    public void onRequestSuccess(JsonObject response) {
                        boolean condition = response.get("promo").getAsBoolean();
                        if(condition == true){
                            Toast.makeText(ProductListActivity.this, "Kode promo anda tersedia", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ProductListActivity.this, "Kode promo anda tidak tersedia", Toast.LENGTH_SHORT).show();
                        }
                        preference.setPromo(response.get("potongan").getAsString());
                    }

                    @Override
                    public void onRequestSuccess2(JsonArray response) {

                    }

                    @Override
                    public void onRequestError(String error) {

                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProductList();
    }

    private void getProductList() {
        Gson gson = new Gson();
        String json = preference.getOrder();
        Order order= gson.fromJson(json, Order.class);
        ProductListViewModel productListViewModel= ViewModelProviders.of(this).get(ProductListViewModel.class);
        productListViewModel.getProduct(Objects.requireNonNull(this),order.getCitiesID()).observe(this, new Observer<List<ProductResult>>() {
            @Override
            public void onChanged(List<ProductResult> rewardResults) {
                Log.d("gantengtolol", rewardResults.toString());
                productResultList.clear();
                productResultList.addAll(rewardResults);
                productAdapter.notifyDataSetChanged();
                productAdapter.updateEmptyView();
                showLoading(false);
            }
        });
    }

    private void submitPromo(){
        ProductListViewModel productListViewModel= new ProductListViewModel();
        productListViewModel.checkPromoCode(promoCode.getText().toString(), preference.getToken2(), new RetrofitInterface() {
            @Override
            public void onRequestSuccess(JsonObject response) {
                boolean condition = response.get("promo").getAsBoolean();
                if(condition == true){
                    Toast.makeText(ProductListActivity.this, "Kode promo anda tersedia", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProductListActivity.this,SummaryActivity.class);
                    startActivity(intent);
                }else {
                    if(TextUtils.isEmpty(promoCode.getText().toString())){
                        Intent intent = new Intent(ProductListActivity.this,SummaryActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(ProductListActivity.this, "Kode promo anda tidak tersedia", Toast.LENGTH_SHORT).show();
                    }
                }
                preference.setPromo(response.get("potongan").getAsString());
            }

            @Override
            public void onRequestSuccess2(JsonArray response) {

            }

            @Override
            public void onRequestError(String error) {

            }
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

}
