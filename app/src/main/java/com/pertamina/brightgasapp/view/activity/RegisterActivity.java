package com.pertamina.brightgasapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.brightgasapp.R;
import com.pertamina.brightgasapp.model.login.LoginModel;
import com.pertamina.brightgasapp.model.register.RegisterModel;
import com.pertamina.brightgasapp.view.base.BaseActivity;
import com.pertamina.brightgasapp.view.network.ApiClient;
import com.pertamina.brightgasapp.view.network.ApiInterface;
import com.pertamina.brightgasapp.view.network.RetrofitInterface;
import com.pertamina.brightgasapp.view.network.RetrofitRequest;
import com.pertamina.brightgasapp.view.utilities.Util;
import com.pertamina.brightgasapp.viewmodel.LoginViewModel;
import com.pertamina.brightgasapp.viewmodel.RegisterViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    private Toolbar toolbar;
    private LinearLayout registerButton;
    private Button choose;
    private TextInputEditText firstName;
    private TextInputEditText lastName;
    private TextInputEditText address;
    private TextInputEditText zipcode;
    private TextInputEditText username;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText retype_password;
    private TextInputEditText ktp;
    private TextInputEditText ktpPhoto;
    private TextInputEditText numberPhone;
    private TextInputEditText instagram;
    private BetterSpinner provinceBetterSpinner;
    private BetterSpinner cityBetterSpinner;
    private BetterSpinner districtBetterSpinner;

    private String firstNameString;
    private String lastNameString;
    private String addressString;
    private String zipcodeString;
    private String emailString;
    private String passwordString;
    private String retype_passwordString;
    private String provinceString;
    private String cityString;
    private String districtString;
    private String usernameString;
    private String numberPhoneString;
    private String ktpString;
    private String stringID;
    private String instagramString;

    private Util util;
    private RetrofitRequest retrofitRequest;

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
    private byte[] inputData = null;
    Bitmap bitmapImage;

    private File file;
    private RequestBody reqFile;


    private Uri filepath = null;
    private static int IMG_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setUpView();
        generateView();
        setupListener();
    }

    @Override
    public void setUpView() {

        util = new Util();
        retrofitRequest = new RetrofitRequest();

        progressDialog = new ProgressDialog(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Daftar akun");
        getSupportActionBar().setElevation(0);
        toolbar.setNavigationIcon(R.drawable.ic_action_back_white);

        username = findViewById(R.id.username);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.backName);
        address = findViewById(R.id.address);
        zipcode = findViewById(R.id.zipcode);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        retype_password = findViewById(R.id.retype_password);
        numberPhone = findViewById(R.id.numberPhone);
        ktp = findViewById(R.id.ktp);
        ktpPhoto = findViewById(R.id.ktpphoto);
        choose = findViewById(R.id.choosephoto);
        instagram = findViewById(R.id.ig);

        provinceBetterSpinner = findViewById(R.id.province);
        cityBetterSpinner = findViewById(R.id.city);
        districtBetterSpinner = findViewById(R.id.district);

        registerButton = findViewById(R.id.register_button);

        generateView();

    }

    @Override
    public void generateView() {
        getProvince();
    }

    @Override
    public void setupListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).
                        start(RegisterActivity.this);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccount();
            }
        });

        provinceBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                districtBetterSpinner.setText("");
                cityBetterSpinner.setText("");
                getCity(provinceID.get(i));
                provinceStringID = provinceID.get(i);
            }
        });

        cityBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                districtBetterSpinner.setText("");
                getDistricts(cityID.get(i));
                cityStringID = cityID.get(i);
            }
        });

        districtBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                districttStringID= districtID.get(i);
            }
        });

        cityBetterSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (city.size() == 0) {
                    Toast.makeText(RegisterActivity.this, "Pilih provinsi terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        districtBetterSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (district.size() == 0) {
                    Toast.makeText(RegisterActivity.this, "Pilih kota terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initString() {
        firstNameString = firstName.getText().toString().trim();
        lastNameString = lastName.getText().toString().trim();
        addressString = address.getText().toString().trim();
        zipcodeString = zipcode.getText().toString().trim();
        emailString = email.getText().toString().trim();
        passwordString = password.getText().toString().trim();
        retype_passwordString = retype_password.getText().toString().trim();
        provinceString = provinceBetterSpinner.getText().toString().trim();
        cityString = cityBetterSpinner.getText().toString().trim();
        districtString = districtBetterSpinner.getText().toString().trim();
        usernameString = username.getText().toString().trim();
        numberPhoneString = numberPhone.getText().toString().trim();
        ktpString = ktp.getText().toString().trim();
        instagramString = instagram.getText().toString().trim();
    }

    private void registerAccount() {
        initString();

        TextInputLayout firstNameTIL  = findViewById(R.id.firstNameLayout);
        TextInputLayout lastNameTIL  = findViewById(R.id.backNameLayout);
        TextInputLayout provinsiTIL  = findViewById(R.id.provinceLayout);
        TextInputLayout cityTIL  = findViewById(R.id.cityLayout);
        TextInputLayout districtTIL  = findViewById(R.id.districtLayout);
        TextInputLayout emailTIL  = findViewById(R.id.emailLayout);
        TextInputLayout addressTIL = findViewById(R.id.addressLayout);
        TextInputLayout zipCodeTIL  = findViewById(R.id.zipcodeLayout);
        TextInputLayout passwordTIL  = findViewById(R.id.passwordLayout);
        TextInputLayout retypeTIL= findViewById(R.id.retypeLayout);
        TextInputLayout usernameTIL= findViewById(R.id.usernameLayout);
        TextInputLayout numberPhoneTIL= findViewById(R.id.numberPhoneLayout);
        TextInputLayout ktpTIL= findViewById(R.id.noKTPLayout);

        if(TextUtils.isEmpty(usernameString)){
            usernameTIL.setError("Username tidak boleh kosong");
            usernameTIL.findFocus();
           return;
        }else {
            usernameTIL.setError(null);
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
        if(TextUtils.isEmpty(ktpString)){
            ktpTIL.setError("Nomor KTP tidak boleh kosong");
            ktpTIL.findFocus();
            return;
        }else {
            ktpTIL.setError(null);
        }
        if(ktpString.length()<16){
            ktpTIL.setError("Nomor KTP tidak boleh lebih dari 16 digit");
            ktpTIL.findFocus();
            return;
        } else {
            ktpTIL.setError(null);
        }
        if(TextUtils.isEmpty(numberPhoneString)){
            numberPhoneTIL.setError("Nomor Hp tidak boleh kosong");
            numberPhoneTIL.findFocus();
            return;
        }else {
            numberPhoneTIL.setError(null);
        }
        if(numberPhoneString.length() < 10 ){
            numberPhoneTIL.setError("Nomor Hp tidak boleh kurang dari 10 digit");
            numberPhoneTIL.findFocus();
            return;
        }else {
            numberPhoneTIL.setError(null);
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
        }else {
            zipcode.setError(null);
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

        if(TextUtils.isEmpty(passwordString)){
            passwordTIL.setError("Kata sandi tidak tidak boleh kosong");
            passwordTIL.findFocus();
            return;
        }else {
            passwordTIL.setError(null);
        }

        if(TextUtils.isEmpty(retype_passwordString)){
            retypeTIL.setError("Ulangi sandi tidak tidak boleh kosong");
            retypeTIL.findFocus();
           return;
        }else {
            retypeTIL.setError(null);
        }

        if(passwordString.length() < 6){
            passwordTIL.setError("Kata sandi tidak boleh kurang dari 6 kata");
            passwordTIL.findFocus();
            return;
        }else {
            passwordTIL.setError(null);
        }

        if(!passwordString.equals(retype_passwordString)){
            retypeTIL.setError("Kata sandi dan ulangi sandi tidak sama");
            retypeTIL.findFocus();
            return;
        }else {
            util.displayLoader(progressDialog,"Mendaftarkan akun");

            RegisterModel model = new RegisterModel();
            model.setEmail(emailString.toLowerCase());
            model.setAlamat(addressString);
            model.setJenis_kelamin("Laki-Laki");
            model.setFirst_name(firstNameString);
            model.setLast_name(lastNameString);
            model.setTgl_lahir("2020-01-01");
            model.setProvinsi(provinceStringID);
            model.setKota(cityStringID);
            model.setKecamatan(districttStringID);
            model.setPassword(passwordString);
            model.setPassword_confirmation(retype_passwordString);
            model.setUsername(usernameString.toLowerCase());
            model.setKode_pos(zipcodeString);
            model.setNo_hp(numberPhoneString);
            model.setNo_ktp(ktpString);
            model.setNo_ktp(instagramString);
            saveRegister(model);
        }
    }


    private void getProvince() {

        province = new ArrayList<>();
        provinceID = new ArrayList<>();
        city = new ArrayList<>();
        cityID = new ArrayList<>();
        district = new ArrayList<>();
        districtID = new ArrayList<>();

//        provinceBetterSpinner.setHint(R.string.wait_data);
        provinceBetterSpinner.setEnabled(false);

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
             provinceBetterSpinner.setEnabled(true);
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
        cityBetterSpinner.setEnabled(false);

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
                cityBetterSpinner.setEnabled(true);
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
        districtBetterSpinner.setEnabled(false);

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
                districtBetterSpinner.setEnabled(true);
                setSpinnerAdapter();
            }

            @Override
            public void onRequestError(String error) {

            }
        });
    }

    private void setSpinnerAdapter() {
        provinceAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, province);

        provinceBetterSpinner.setAdapter(provinceAdapter);

        cityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, city);

        cityBetterSpinner.setAdapter(cityAdapter);

        districtAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, district);

        districtBetterSpinner.setAdapter(districtAdapter);
    }

    private void saveRegister(RegisterModel registerModel){
        util.displayLoader(progressDialog,"Mendaftarkan akun");
        RegisterViewModel registerViewModel = new RegisterViewModel();
        registerViewModel.registerAccount(registerModel, new RetrofitInterface() {
            @Override
            public void onRequestSuccess(JsonObject response) {
                progressDialog.dismiss();
                Log.d("lolll", "saveRegister: " + response);
                if(!response.get("error").getAsBoolean()){
                    JsonObject jsonObject = response.get("data").getAsJsonObject();
                    stringID =  jsonObject.get("id").getAsString();
                    Log.d("idnya", "onRequestSuccess: " +stringID);
                    if(filepath == null){
                        intentToLogin();
                    }else {
                        getToken();
                    }

                }else {
                    JsonObject jsonObject = response.get("message").getAsJsonObject();
                    if(jsonObject.has("username")){
                        Toast.makeText(RegisterActivity.this, "Username telah terdaftar", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(RegisterActivity.this, "Email telah terdaftar", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onRequestSuccess2(JsonArray response) {
            }

            @Override
            public void onRequestError(String error) {
                Toast.makeText(RegisterActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    filepath = result.getUri();
                    ktpPhoto.setText("Foto telah terpilih");
                    //to getbyteOk
                    InputStream iStream = getContentResolver().openInputStream(filepath);
                    inputData = getBytes(iStream);
                    bitmapImage = BitmapFactory.decodeByteArray(inputData, 0, inputData.length);
                    //generate to Bitmap
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            } else if (requestCode == IMG_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            }
        } catch (Exception e) {
            Log.d("ClaimProduct", "onActivityResult: " + e.toString());
            Toast.makeText(RegisterActivity.this, "Silahkan coba lagi", Toast.LENGTH_LONG).show();
        }
    }

    private void getFile(Bitmap imageBitmap,String token) {
        Log.d("disiniktp", "getFile: " +token);
        file = createTempFile(imageBitmap);
        if(file!= null){
            uploadInformation(stringID,token);
        }else {
            getFile(imageBitmap,token);
        }
        Log.d("response final", "getFile: " + file + reqFile);
    }

    private void getToken(){

        LoginModel login = new LoginModel();
        login.setUsername(usernameString);
        login.setPassword(passwordString);
        LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.loginAccount(login, new RetrofitInterface() {
            @Override
            public void onRequestSuccess(JsonObject response) {
                if(!response.get("error").getAsBoolean()){
                    getFile(bitmapImage,response.get("token").getAsString());
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

    public void uploadInformation(String id,String token) {
        util.displayLoader(progressDialog,"Mengunggah foto KTP");

        Log.e("lolfinal", "disini");
        ApiInterface service = ApiClient.getDataBrightGas().create(ApiInterface.class);

        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), id);
        try {

            Call<JsonObject> call = service
                    .uploadKTP("application/json","application/json","Bearer "+token,body,MultipartBody.Part.createFormData("foto_ktp", file.getName(), reqFile));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.i("mamanglacing", "onResponse: " + response.code() + response.body());
                    System.out.println(response.code());
                    if(response.code() == 200){
                        intentToLogin();
                    }

                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("gagal", "onFailure: "+call );
                    System.out.println("outputnya" + call + t.toString());
                    progressDialog.dismiss();
                }
            });
        }
        catch (Exception e) {
            progressDialog.dismiss();
        }
    }

    private File createTempFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() + "_image.jpeg");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();


        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        byte[] bitmapdata = bos.toByteArray();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        return file;
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void intentToLogin(){
        Intent intent = new Intent(RegisterActivity.this,CompleteRegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
