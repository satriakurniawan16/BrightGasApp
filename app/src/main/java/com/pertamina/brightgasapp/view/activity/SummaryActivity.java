package com.pertamina.brightgasapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.brightgasapp.R;
import com.pertamina.brightgasapp.model.Order.Order;
import com.pertamina.brightgasapp.model.Order.OrderProductData;
import com.pertamina.brightgasapp.model.Order.OrderProductResult;
import com.pertamina.brightgasapp.model.Order.Product;
import com.pertamina.brightgasapp.model.Order.SubmitOrder;
import com.pertamina.brightgasapp.model.login.LoginModel;
import com.pertamina.brightgasapp.model.productList.ProductResult;
import com.pertamina.brightgasapp.view.adapter.OrderAdapter;
import com.pertamina.brightgasapp.view.adapter.ProductAdapter;
import com.pertamina.brightgasapp.view.base.BaseActivity;
import com.pertamina.brightgasapp.view.network.RetrofitInterface;
import com.pertamina.brightgasapp.view.utilities.Preference;
import com.pertamina.brightgasapp.view.utilities.Util;
import com.pertamina.brightgasapp.viewmodel.LoginViewModel;
import com.pertamina.brightgasapp.viewmodel.OrderViewModel;
import com.pertamina.brightgasapp.viewmodel.ProductListViewModel;
import com.pertamina.brightgasapp.viewmodel.SummaryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SummaryActivity extends BaseActivity {

    Preference preference;
    private TextView firstName;
    private TextView lastName;
    private TextView numberPhone;
    private TextView email;
    private TextView nameSummary;
    private TextView numberSummary;
    private TextView emailSummary;
    private TextView provinceSummary;
    private TextView citySummary;
    private TextView districtSummary;
    private TextView addressSummary;
    private TextView zipCodeSummary;
    private TextView total_summary;
    private TextView calculate_summary;
    private TextView date_summary;
    private LinearLayout orderNow;
    private LinearLayout prevStrp;
    int countTotal;
    private int total = 0;

    ProgressDialog progressDialog;

    Util util;

    int kali = 0 ;


    private List<OrderProductResult> productResultList;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        setUpView();
        setupListener();
        generateView();

    }

    @Override
    public void setUpView() {

        preference = new Preference(this);

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        numberPhone = findViewById(R.id.number_phone);
        email = findViewById(R.id.email);
        nameSummary = findViewById(R.id.name_summary);
        numberSummary = findViewById(R.id.number_summary);
        emailSummary = findViewById(R.id.email_summary);
        provinceSummary = findViewById(R.id.province_sumamry);
        citySummary = findViewById(R.id.city_summary);
        districtSummary = findViewById(R.id.district_summary);
        addressSummary = findViewById(R.id.address_summmary);
        zipCodeSummary = findViewById(R.id.zipcode_summary);
        orderNow = findViewById(R.id.order_now);
        total_summary = findViewById(R.id.total_summary);
        calculate_summary = findViewById(R.id.calculate_summary);
        date_summary = findViewById(R.id.date_summary);
        prevStrp = findViewById(R.id.button_prev);

        progressDialog = new ProgressDialog(this);
        util =new Util();

        productResultList = new ArrayList<>();
        orderAdapter = new OrderAdapter(productResultList);
        orderAdapter.notifyDataSetChanged();

        RecyclerView recyclerView = findViewById(R.id.rv_product);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(orderAdapter);
    }

    @Override
    public void generateView() {

        Gson gson = new Gson();
        String json = preference.getOrder();
        Order order = gson.fromJson(json, Order.class);

        firstName.setText(order.getFirstName2());
        lastName.setText(order.getLastName2());
        numberPhone.setText(order.getPhoneNumber2());
        email.setText(order.getEmail());
        nameSummary.setText(order.getFirstname() + " " + order.getLastname());
        numberSummary.setText(order.getPhone());
        emailSummary.setText(order.getEmail());
        provinceSummary.setText(order.getProvinces());
        citySummary.setText(order.getCities());
        districtSummary.setText(order.getDistrict());
        addressSummary.setText(order.getAddress());
        zipCodeSummary.setText(order.getZipcode());
        date_summary.setText(order.getDate());

        String total = String.valueOf(countPrice());
        String promocost = preference.getPromo();

        countTotal = Integer.parseInt(total) - Integer.parseInt(promocost);

        calculate_summary.setText(total);
        calculate_summary.setPaintFlags(calculate_summary.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        total_summary.setText(String.valueOf(countTotal));

    }

    @Override
    protected void onResume() {
        super.onResume();
        getProductList();
    }

    private void getProductList() {
        OrderViewModel orderViewModel= ViewModelProviders.of(this).get(OrderViewModel.class);
        orderViewModel.getProduct(Objects.requireNonNull(this)).observe(this, new Observer<List<OrderProductResult>>() {
            @Override
            public void onChanged(List<OrderProductResult> rewardResults) {
                Log.d("gantengtolol", rewardResults.toString());
                productResultList.clear();
                productResultList.addAll(rewardResults);
                orderAdapter.notifyDataSetChanged();
            }
        });
    }

    private int countPrice(){
        Preference preference = new Preference(this);
        Gson gson = new Gson();
        String jsonnew = preference.getProduct();
        OrderProductData order = gson.fromJson(jsonnew, OrderProductData.class);
        for (int i = 0; i < order.getProductResults().size() ; i++) {
            int a = Integer.parseInt(order.getProductResults().get(i).getPrice());
            int b = Integer.parseInt(order.getProductResults().get(i).getQty());
            kali =  a * b;
            total = total + kali;
            Log.d("totalnyo", "countPrice: "+ total);
        }
        return total;
    }

    @Override
    public void setupListener() {
        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        prevStrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Apakah anda akan melanjutkan pesanan ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ya",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        submitOrder();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Tidak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void submitOrder(){

        util.displayLoader(progressDialog,"Menyiapkan Pemesanan");

        Gson gson = new Gson();
        String json = preference.getOrder();
        Order order = gson.fromJson(json, Order.class);
        SubmitOrder submitOrder = new SubmitOrder();
        submitOrder.setUser_id_pemesan(order.getUser_id());
        submitOrder.setKode_promo(preference.getPromo());
        submitOrder.setAlamat_pengiriman(order.getAddress());
        submitOrder.setNama_penerima(order.getFirstname()+" "+order.getLastname());
        submitOrder.setNo_hp_penerima(order.getPhoneNumber2());
        submitOrder.setProvinsi_pengiriman(order.getProvincesID());
        submitOrder.setKota_pengiriman(order.getCitiesID());
        submitOrder.setKecamatan_pengiriman(order.getDistrictID());
        submitOrder.setUser_same(0);
        submitOrder.setKode_pos_pengiriman(order.getZipcode());
        submitOrder.setTotal_pesanan(countTotal);
        submitOrder.setTgl_pengiriman(order.getDate());
        submitOrder.setWaktu_pengiriman(3);
        Gson gsonNew = new Gson();
        String jsonnew = preference.getProduct();
        OrderProductData orderNow = gsonNew.fromJson(jsonnew, OrderProductData.class);
        ArrayList<Product> list = new ArrayList<>();
        for (int i = 0 ; i < orderNow.getProductResults().size(); i++){
                Product product  = new Product();
                product.setPrice(Integer.parseInt(orderNow.getProductResults().get(i).getPrice()));
                product.setProduct_id(Integer.parseInt(orderNow.getProductResults().get(i).getProduct_id()));
                product.setQty(Integer.parseInt(orderNow.getProductResults().get(i).getQty()));
                list.add(product);
        }
        submitOrder.setProducts(list);
        String jsonString = gson.toJson(submitOrder);
        Log.d("ordernyaak", "submitOrder: "+jsonString);

        SummaryViewModel summaryViewModel = new SummaryViewModel();

        summaryViewModel.orderNow("Bearer "+preference.getToken2(), submitOrder, new RetrofitInterface() {
            @Override
            public void onRequestSuccess(JsonObject response) {
                if(!response.get("message").equals("Token is Invalid")){
                    Log.d("ordermamang", "onRequestSuccess: " + response);
                    Intent intent = new Intent(SummaryActivity.this,OrderCompleteActivity.class);
                    startActivity(intent);
                    progressDialog.dismiss();
                }else {
                    Toast.makeText(SummaryActivity.this, "Sesi anda telah habis silahkan login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SummaryActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onRequestSuccess2(JsonArray response) {

            }

            @Override
            public void onRequestError(String error) {

            }
        });

    }

}
