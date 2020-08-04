package com.pertamina.brightgasapp.view.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.brightgasapp.R;
import com.pertamina.brightgasapp.model.Order.Order;
import com.pertamina.brightgasapp.model.register.RegisterModel;
import com.pertamina.brightgasapp.view.base.BaseActivity;
import com.pertamina.brightgasapp.view.network.ApiClient;
import com.pertamina.brightgasapp.view.network.ApiInterface;
import com.pertamina.brightgasapp.view.network.RetrofitInterface;
import com.pertamina.brightgasapp.view.network.RetrofitRequest;
import com.pertamina.brightgasapp.view.utilities.GlobalString;
import com.pertamina.brightgasapp.view.utilities.Preference;
import com.pertamina.brightgasapp.view.utilities.Util;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderData extends BaseActivity {

    private LinearLayout nextStep;
    private LinearLayout prevStep;
    private TextInputEditText dateOrder;
    private TextInputEditText firstName;
    private TextInputEditText lastName;
    private TextInputEditText numberPhone;
    private TextInputEditText email;
    private TextInputEditText address;
    private TextInputEditText firstname2;
    private TextInputEditText lastname2;
    private TextInputEditText numberphone2;
    private TextInputEditText ktp;
    private CheckBox cborder;
    private BetterSpinner provinceSpinner;
    private BetterSpinner citySpinner;
    private BetterSpinner districtSpinner;
    private TextInputEditText zipCode;


    private String firstNameString;
    private String lastNameString;
    private String addressString;
    private String zipcodeString;
    private String emailString;
    private String firstName2String;
    private String lastName2String;
    private String phoneNumber2String;
    private String ktpString;
    private String provinceString;
    private String cityString;
    private String districtString;
    private String dateString;
    private String phoneString;
    private int userid;

    private Calendar calendar;

    ArrayList<String> province;
    ArrayList<String> provinceID;
    ArrayList<String> city;
    ArrayList<String> cityID;
    ArrayList<String> district;
    ArrayList<String> districtID;

    private String provinceStringID;
    private String districttStringID;
    private String cityStringID;

    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<String> cityAdapter;
    private ArrayAdapter<String> districtAdapter;

    private ProgressDialog progressDialog;
    private RetrofitRequest retrofitRequest;

    private boolean status = false;

    Util util;

    Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_data);

        setUpView();
        setupListener();
        generateView();

    }

    @Override
    public void setUpView() {

        nextStep = findViewById(R.id.button_next);
        dateOrder = findViewById(R.id.date_order);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        numberPhone = findViewById(R.id.numberPhone);
        provinceSpinner= findViewById(R.id.provinces);
        address= findViewById(R.id.address);
        citySpinner = findViewById(R.id.cities);
        districtSpinner = findViewById(R.id.districts);
        email = findViewById(R.id.email);
        zipCode = findViewById(R.id.zipcode);
        firstname2 = findViewById(R.id.firstName2);
        lastname2 = findViewById(R.id.lastName2);
        numberphone2 = findViewById(R.id.numberPhone2);
        prevStep = findViewById(R.id.button_prev);
        ktp = findViewById(R.id.ktp);

        cborder = (CheckBox) findViewById(R.id.cb_windows);


        retrofitRequest = new RetrofitRequest();
        calendar = Calendar.getInstance();

        preference = new Preference(OrderData.this);

        util = new Util();

    }

    @Override
    public void generateView() {
        getProvince();
        getData();
    }

    @Override
    public void setupListener() {
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOrder();
            }
        });
        prevStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDate(v);
            }
        });

        provinceSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                districtSpinner.setText("");
                citySpinner.setText("");
                getCity(provinceID.get(i));
                provinceStringID = provinceID.get(i);
            }
        });

        citySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                districtSpinner.setText("");
                getDistricts(cityID.get(i));
                cityStringID = cityID.get(i);
            }
        });

        districtSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                districttStringID = districtID.get(i);
            }
        });

        citySpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (city.size() == 0) {
                    Toast.makeText(OrderData.this, "Pilih provinsi terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        districtSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (district.size() == 0) {
                    Toast.makeText(OrderData.this, "Pilih kota terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cborder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    firstName.setText(firstname2.getText().toString());
                    lastName.setText(lastname2.getText().toString());
                    numberPhone.setText(numberphone2.getText().toString());
                }else {
                    firstName.setText(null);
                    lastName.setText(null);
                    numberPhone.setText(null);
                }
            }
        });
    }

    private void initString() {
        firstNameString = firstName.getText().toString().trim();
        lastNameString = lastName.getText().toString().trim();
        addressString = address.getText().toString().trim();
        zipcodeString = zipCode.getText().toString().trim();
        emailString = email.getText().toString().trim();
        provinceString = provinceSpinner.getText().toString().trim();
        cityString = citySpinner.getText().toString().trim();
        districtString = districtSpinner.getText().toString().trim();
        phoneString = numberPhone.getText().toString().trim();
        dateString= dateOrder.getText().toString().trim();
        firstName2String= firstname2.getText().toString().trim();
        lastName2String = lastname2.getText().toString().trim();
        phoneNumber2String= numberphone2.getText().toString().trim();
        ktpString= ktp.getText().toString().trim();
    }

    private void getProvince() {

        province = new ArrayList<>();
        provinceID = new ArrayList<>();
        city = new ArrayList<>();
        cityID = new ArrayList<>();
        district = new ArrayList<>();
        districtID = new ArrayList<>();

        provinceSpinner.setEnabled(false);

        retrofitRequest.getProvinces(new RetrofitInterface() {
            @Override
            public void onRequestSuccess(JsonObject response) {}
            @Override
            public void onRequestSuccess2(JsonArray response) {
                Log.d("LOL TAIANYING", "onRequestSuccess: " + response);
                for (int i = 0; i < response.size(); i++) {

                    JsonObject jsonObject = response.get(i).getAsJsonObject();
                    String result = jsonObject.get("name").getAsString();
                    String resultID = jsonObject.get("id").getAsString();
                    Log.d("lol", "onResponse: " + result + "id = " + resultID);
                    province.add(result);
                    provinceID.add(resultID);
                }
//             provinceBetterSpinner.setHint("Pilih Provinsi");
                provinceSpinner.setEnabled(true);
                setSpinnerAdapter();
            }
            @Override
            public void onRequestError(String error) {

            }
        });
    }

    private void getCity(String id){
        city = new ArrayList<>();
        cityID = new ArrayList<>();
        district = new ArrayList<>();
        districtID = new ArrayList<>();

//        cityBetterSpinner.setHint(R.string.wait_data);
        citySpinner.setEnabled(false);

        retrofitRequest.getCities(id, new RetrofitInterface() {
            @Override
            public void onRequestSuccess(JsonObject response) {

            }

            @Override
            public void onRequestSuccess2(JsonArray response) {
                for (int i = 0; i < response.size(); i++) {
                    JsonObject jsonObject = response.get(i).getAsJsonObject();
                    String result = jsonObject.get("city").getAsString();
                    String resultID = jsonObject.get("id").getAsString();
                    Log.d("lol", "onResponse: " + result);
                    city.add(result);
                    cityID.add(resultID);
                }
//                cityBetterSpinner.setHint(R.string.choose_city);
                citySpinner.setEnabled(true);
                setSpinnerAdapter();
            }

            @Override
            public void onRequestError(String error) {

            }
        });
    }

    private void getDistricts(String id){
        district = new ArrayList<>();
        districtID = new ArrayList<>();

//        districtBetterSpinner.setHint(R.string.wait_data);
        districtSpinner.setEnabled(false);

        retrofitRequest.getDistricts(id, new RetrofitInterface() {
            @Override
            public void onRequestSuccess(JsonObject response) {

            }

            @Override
            public void onRequestSuccess2(JsonArray response) {
                for (int i = 0; i < response.size(); i++) {
                    JsonObject jsonObject = response.get(i).getAsJsonObject();
                    String result = jsonObject.get("sub_district").getAsString();
                    String resultID = jsonObject.get("id").getAsString();
                    Log.d("lol", "onResponse: " + result);
                    district.add(result);
                    districtID.add(resultID);
                }
//                districtBetterSpinner.setHint(R.string.choose_district);
                districtSpinner.setEnabled(true);
                setSpinnerAdapter();
            }

            @Override
            public void onRequestError(String error) {

            }
        });
    }

    private void setOrder(){
        initString();

        TextInputLayout firstNameTIL  = findViewById(R.id.firstnameLayout);
        TextInputLayout lastNameTIL  = findViewById(R.id.lastNameLayout);
        TextInputLayout provinsiTIL  = findViewById(R.id.provincesLayout);
        TextInputLayout cityTIL  = findViewById(R.id.citiesLayout);
        TextInputLayout districtTIL  = findViewById(R.id.districtLayout);
        TextInputLayout emailTIL  = findViewById(R.id.emailLayout);
        TextInputLayout addressTIL = findViewById(R.id.addressLayout);
        TextInputLayout zipCodeTIL  = findViewById(R.id.zipcodeLayout);
        TextInputLayout dateTIL= findViewById(R.id.dateLayout);
        TextInputLayout phoneTIL= findViewById(R.id.numberPhoneLayout);
        TextInputLayout firsnameTIL2= findViewById(R.id.firstnameLayout2);
        TextInputLayout lastNameTIL2= findViewById(R.id.lastNameLayout2);
        TextInputLayout phoneTIL2= findViewById(R.id.numberPhoneLayout2);
        TextInputLayout ktpTIL= findViewById(R.id.ktpLayout);

        if(TextUtils.isEmpty(dateString)){
            dateTIL.setError("Tanggal tidak boleh kosong");
            dateTIL.findFocus();
            return;
        }else {
            dateTIL.setError(null);
        }

        if(TextUtils.isEmpty(firstName2String)){
            firsnameTIL2.setError("Nama depan pemesan tidak boleh kosong");
            firsnameTIL2.findFocus();
            return;
        }else {
            firsnameTIL2.setError(null);
        }

        if(TextUtils.isEmpty(lastName2String)){
            lastNameTIL2.setError("Nama belakang pemesan tidak boleh kosong");
            lastNameTIL2.findFocus();
            return;
        }else {
            lastNameTIL2.setError(null);
        }

        if(TextUtils.isEmpty(phoneNumber2String)){
            phoneTIL2.setError("No Hp pemesan tidak boleh kosong");
            phoneTIL2.findFocus();
            return;
        }else {
            phoneTIL2.setError(null);
        }

        if(TextUtils.isEmpty(ktpString)){
            ktpTIL.setError("No KTP tidak boleh kosong");
            ktpTIL.findFocus();
            return;
        }else {
            ktpTIL.setError(null);
        }

        if(TextUtils.isEmpty(firstNameString)){
            firstNameTIL.setError("Nama depan tidak boleh kosong");
            firstNameTIL.findFocus();
            return;
        }else {
            firstNameTIL.setError(null);
        }
        if(TextUtils.isEmpty(lastNameString)){
            lastNameTIL.setError("Nama belakang tidak boleh kosong");
            lastNameTIL.findFocus();
            return;
        }else {
            lastNameTIL.setError(null);
        }


        if(TextUtils.isEmpty(phoneString)){
            phoneTIL.setError("No telefon tidak boleh kosong");
            phoneTIL.findFocus();
            return;
        }else {
            phoneTIL.setError(null);
        }

        if(TextUtils.isEmpty(emailString)){
            emailTIL.setError("Email tidak boleh kosong");
            emailTIL.findFocus();
            return;
        }else {
            emailTIL.setError(null);
        }

        if(!util.isEmailValid(emailString)){
            emailTIL.setError("Email tidak valid");
            emailTIL.findFocus();
            return;
        }else {
            emailTIL.setError(null);
        }

        if(TextUtils.isEmpty(provinceString)){
            provinsiTIL.setError("Provinsi tidak boleh kosong");
            provinsiTIL.findFocus();
            return;
        }else {
            provinsiTIL.setError(null);
        }
        if(TextUtils.isEmpty(cityString)){
            cityTIL.setError("Kota/Kabupaten tidak boleh kosong");
            cityTIL.findFocus();
            return;
        }else {
            cityTIL.setError(null);
        }

        if(TextUtils.isEmpty(districtString)){
            districtTIL.setError("Kecamatan tidak boleh kosong");
            districtTIL.findFocus();
            return;
        }else {
            districtTIL.setError(null);
        }

        if(TextUtils.isEmpty(addressString)){
            addressTIL.setError("Alamat tidak boleh kosong");
            addressTIL.findFocus();
            return;
        }else {
            addressTIL.setError(null);
        }
        if(TextUtils.isEmpty(zipcodeString)){
            zipCodeTIL.setError("Kode Pos tidak boleh kosong");
            zipCodeTIL.findFocus();
            return;
        }else  {
            zipCodeTIL.setError(null);
            Order order = new Order();
            order.setDate(dateString);
            order.setFirstname(firstNameString);
            order.setLastname(lastNameString);
            order.setEmail(lastNameString);
            order.setPhone(phoneString);
            order.setAddress(addressString);
            order.setProvinces(provinceString);
            order.setProvincesID(provinceStringID);
            order.setCities(cityString);
            order.setCitiesID(cityStringID);
            order.setDistrict(districtString);
            order.setDistrictID(districttStringID);
            order.setZipcode(zipcodeString);
            order.setFirstName2(firstName2String);
            order.setLastName2(lastName2String);
            order.setPhoneNumber2(phoneNumber2String);
            order.setKtp(ktpString);
            order.setUser_id(userid);
            Gson gson = new Gson();
            String json = gson.toJson(order);
            preference.setOrder(json);
            Intent intent = new Intent(OrderData.this,ProductListActivity.class);
            startActivity(intent);
        }
    }

    private void setSpinnerAdapter() {
        provinceAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, province);

        provinceSpinner.setAdapter(provinceAdapter);

        cityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, city);

        citySpinner.setAdapter(cityAdapter);

        districtAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, district);

        districtSpinner.setAdapter(districtAdapter);
    }

    private void pickerDate(View v) {
        DatePickerDialog tgl = new DatePickerDialog(OrderData.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                dateOrder.setText(i + "-" + i1 + "-" + i2);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        tgl.show();
    }


    public void getData(){
        ApiInterface service = ApiClient.getDataBrightGas().create(ApiInterface.class);

        try {

            Call<JsonObject> call = service.getData("Bearer "+preference.getToken2(),"application/json","application/json" );
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("lolcode", "onResponse: " + response.code());
                    if(response.code() == 200 ){
                        Log.d("aingmaung", "onResponse: " +response.body());
                        JsonObject jsonObject = response.body().get("user").getAsJsonObject();
                       try {
                           firstname2.setText(jsonObject.get("first_name").getAsString());
                           lastname2.setText(jsonObject.get("last_name").getAsString());
                           email.setText(jsonObject.get("email").getAsString());
                           ktp.setText(jsonObject.get("no_ktp").getAsString());
                           provinceStringID = jsonObject.get("provinsi").getAsString();
                           cityStringID = jsonObject.get("kota").getAsString();
                           districttStringID = jsonObject.get("kecamatan").getAsString();
                           zipCode.setText(jsonObject.get("kode_pos").getAsString());
                           address.setText(jsonObject.get("alamat").getAsString());
                           provinceSpinner.setText(jsonObject.get("provinsi_name").getAsString());
                           citySpinner.setText(jsonObject.get("kota_name").getAsString());
                           districtSpinner.setText(jsonObject.get("kecamatan_name").getAsString());
                           numberphone2.setText(jsonObject.get("no_hp").getAsString());
                       }catch (Exception e){
                           Toast.makeText(OrderData.this, "Lengkapi profile anda di website resmi BrightGas", Toast.LENGTH_SHORT).show();
                       }
                        userid = jsonObject.get("id").getAsInt();
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
