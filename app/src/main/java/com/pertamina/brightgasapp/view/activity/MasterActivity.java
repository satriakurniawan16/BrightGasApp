package com.pertamina.brightgasapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.location.Location;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mukesh.OtpView;
import com.pertamina.brightgasapp.ChangePassword;
import com.pertamina.brightgasapp.R;
import com.pertamina.brightgasapp.model.code.Code;
import com.pertamina.brightgasapp.model.code.Scan;
import com.pertamina.brightgasapp.model.login.LoginModel;
import com.pertamina.brightgasapp.model.register.RegisterModel;
import com.pertamina.brightgasapp.view.base.BaseActivity;
import com.pertamina.brightgasapp.view.network.ApiClient;
import com.pertamina.brightgasapp.view.network.ApiInterface;
import com.pertamina.brightgasapp.view.network.RetrofitInterface;
import com.pertamina.brightgasapp.view.network.RetrofitRequest;
import com.pertamina.brightgasapp.view.utilities.Preference;
import com.pertamina.brightgasapp.view.utilities.Util;
import com.pertamina.brightgasapp.viewmodel.LoginViewModel;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

import static com.pertamina.brightgasapp.view.utilities.GlobalString.IntentEstra.INTENT_EXTRA_CODE;
import static com.pertamina.brightgasapp.view.utilities.GlobalString.IntentEstra.INTENT_EXTRA_NAME;
import static com.pertamina.brightgasapp.view.utilities.GlobalString.IntentEstra.INTENT_EXTRA_SCRATCH;
import static com.pertamina.brightgasapp.view.utilities.GlobalString.IntentEstra.INTENT_EXTRA_TYPE;

public class MasterActivity extends BaseActivity {


    private CodeScanner mCodeScanner;
    private static final int RC_PERMISSION = 10;
    private CodeScannerView scannerView;
    private Preference preference;
    private boolean mPermissionGranted;
    final int REQUEST_CODE_CAMERA = 999;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    Context context = this;
    private LoginViewModel loginViewModel;
    private RetrofitRequest retrofitRequest;
    private LinearLayout fab;
    private ProgressDialog progressDialog;
    private Util util;
    private FusedLocationProviderClient mFusedLocationClient;
    private double longitude, latitude;

    private String qrURL;
    boolean doubleBackToExitPressedOnce = false;

    private int userid ;
    private String provinceStringID;
    private String districttStringID;
    private String cityStringID;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        setUpView();
        generateView();
        setupListener();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            logOut();
        } else if (item.getItemId() == R.id.input_code) {
            showDialog();
        }else {
            showDialogPassword();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setUpView() {
//        renewaltoken();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getData();
        getLocation();
        fetchLocation();

        preference = new Preference(this);
        scannerView = (CodeScannerView) findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        preference = new Preference(this);

        fab = findViewById(R.id.floating_action_button);

        util = new Util();

        progressDialog = new ProgressDialog(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);


    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void generateView() {

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                MasterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            v.vibrate(300);
                        }
                        String resultcode = result.getText();;
                        expandURL(resultcode);
                        if(resultcode.contains("http://")){
                            String newcode =  resultcode.replace("http://","");
                          getResultCodeManual(newcode);
                        }else {
                            getResultCodeManual(resultcode);
                        }
//                        if (length <= 9) {
                    }
                });
            }
        });

        checkCameraPermission();

        mCodeScanner.setErrorCallback(error -> runOnUiThread(
                () -> Toast.makeText(this, getString(R.string.app_name, error), Toast.LENGTH_LONG).show()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = false;
                requestPermissions(new String[]{Manifest.permission.CAMERA}, RC_PERMISSION);
            } else {
                mPermissionGranted = true;
            }
        } else {
            mPermissionGranted = true;
        }

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    public void setupListener() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MasterActivity.this, OrderData.class);
                startActivity(intent);
            }
        });
    }

    private void checkCameraPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mCodeScanner.startPreview();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
                                                                   PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }


    private void expandURL(String url){

        WebView myWebView = findViewById(R.id.webView);

        myWebView.getSettings().setJavaScriptEnabled(true);

        final String[] finalURL = {""};
        String url1 = url;
        String newUrl = "";
        String http = "http";
        if (url.contains(http)) {
            newUrl = url.replace(http, "https");
        }else {
            newUrl = "https://"+url1;
        }
        
        myWebView.loadUrl(newUrl);
        myWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                System.out.println(myWebView.getUrl());
                finalURL[0] = myWebView.getUrl();
                Log.d("mama", "onPageFinished: " + myWebView.getUrl());// whatever way to debug
                String example = myWebView.getUrl();
                if(example.contains("brightgas.co.id")){
                    uploadHistory(example.substring(example.lastIndexOf("?") + 4),url);
                }
                Log.d("mama", "onPageFinished: " + example.substring(example.lastIndexOf("?") + 4));// whatever way to debug
            }

        });
    }

    private void getResultCode(String code){
        util.displayLoader(progressDialog,"Memeriksa url");

        Code code1 = new Code();
        code1.setQr(code);
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);

        try {

            Call<JsonObject> call = service.getResultCode("bearer "+preference.getToken1(),code1);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("lol", "onResponse: " + response.code() + response.body());
                    if(response.code() == 200 ){
                        JsonObject newjson= response.body();
                        JsonObject  jsonObject = newjson.get("message").getAsJsonObject();
                        String scratchOff = jsonObject.get("scratchOff").getAsString();
                        String sppbeName= "";
                        JsonElement el = jsonObject.get("sppbeName");
                        if (el != null && !el.isJsonNull()){
                            sppbeName = jsonObject.get("sppbeName").getAsString();
                        }
                        String tipeSealCap= jsonObject.get("tipeSealCap").getAsString();
                        Intent intent = new Intent(MasterActivity.this,GenuineActivity.class);
                        intent.putExtra(INTENT_EXTRA_CODE ,code);
                        intent.putExtra(INTENT_EXTRA_SCRATCH,scratchOff);
                        intent.putExtra(INTENT_EXTRA_NAME,sppbeName);
                        intent.putExtra(INTENT_EXTRA_TYPE,tipeSealCap);
                        startActivity(intent);
                        progressDialog.dismiss();
//                        Toast.makeText(MasterActivity.this, String.valueOf(jsonObject), Toast.LENGTH_SHORT).show();
                    }else {
                        progressDialog.dismiss();
                        Intent intent = new Intent(MasterActivity.this,FakeActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
//                    pDialog.dismiss();
                    Log.d("lolfinal", "error: " + t.toString());
                }
            });
        } catch (Exception e) {
//            pDialog.dismiss();
            Log.d("lolfinal", "uploadError: " + e.toString());
            Toast.makeText(this, "Registrasi gagal coba lagi !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getResultCodeManual(String code){
        util.displayLoader(progressDialog,"Memeriksa url");

        Code code1 = new Code();
        code1.setQr(code);
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);

        try {

            Call<JsonObject> call = service.getResultCode("bearer "+preference.getToken1(),code1);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("lol", "onResponse: " + response.code() + response.body());
                    if(response.code() == 200 ){
                        if(code.contains("s.id")){
                            progressDialog.dismiss();
                            Toast.makeText(MasterActivity.this, "Verifikasi Sukses", Toast.LENGTH_SHORT).show();
                            showDialogNew();
                        }else {
                            JsonObject newjson= response.body();
                            JsonObject  jsonObject = newjson.get("message").getAsJsonObject();
                            String scratchOff = jsonObject.get("scratchOff").getAsString();
//                        String sppbeName= jsonObject.get("sppbeName").getAsString();
                            String tipeSealCap= jsonObject.get("tipeSealCap").getAsString();
                            Intent intent = new Intent(MasterActivity.this,GenuineActivity.class);
                            intent.putExtra(INTENT_EXTRA_CODE ,code);
                            intent.putExtra(INTENT_EXTRA_SCRATCH,scratchOff);
//                        intent.putExtra(INTENT_EXTRA_NAME,sppbeName);
                            intent.putExtra(INTENT_EXTRA_TYPE,tipeSealCap);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }
//                        Toast.makeText(MasterActivity.this, String.valueOf(jsonObject), Toast.LENGTH_SHORT).show();
                    }else {
                        progressDialog.dismiss();
                        Intent intent = new Intent(MasterActivity.this,FakeActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
//                    pDialog.dismiss();
                    Log.d("lolfinal", "error: " + t.toString());
                }
            });
        } catch (Exception e) {
//            pDialog.dismiss();
            Log.d("lolfinal", "uploadError: " + e.toString());
            Toast.makeText(this, "Registrasi gagal coba lagi !", Toast.LENGTH_SHORT).show();
        }
    }


        private void getResultCod(String code){
        util.displayLoader(progressDialog,"Memeriksa kode");

        Code code1 = new Code();
        code1.setQr(code);
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);

        try {

            Call<JsonObject> call = service.getResultCode("bearer "+preference.getToken1(),code1);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("lolapayaa", "mamahmuda " + response.code()+response.body());
                    if(response.code() == 200 ){
                        JsonObject newjson= response.body();
                        Log.d("lolapayaa", "onResponse: " +newjson);
                        JsonObject  jsonObject = newjson.get("message").getAsJsonObject();
                        String scratchOff = jsonObject.get("scratchOff").getAsString();
                        String sppbeName= "";
                        JsonElement el = jsonObject.get("sppbeName");
                        if (el != null && !el.isJsonNull()){
                            sppbeName = jsonObject.get("sppbeName").getAsString();
                        }
                        String tipeSealCap= jsonObject.get("tipeSealCap").getAsString();
                        Intent intent = new Intent(MasterActivity.this,GenuineActivity.class);
                        intent.putExtra(INTENT_EXTRA_CODE ,code);
                        intent.putExtra(INTENT_EXTRA_SCRATCH,scratchOff);
                        intent.putExtra(INTENT_EXTRA_NAME,sppbeName);
                        intent.putExtra(INTENT_EXTRA_TYPE,tipeSealCap);
                        startActivity(intent);
                        progressDialog.dismiss();
//                        Toast.makeText(MasterActivity.this, String.valueOf(jsonObject), Toast.LENGTH_SHORT).show();
                    }else {
                        progressDialog.dismiss();
//                        Intent intent = new Intent(MasterActivity.this,FakeActivity.class);
//                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
//                    pDialog.dismiss();
                    Log.d("lolfinal", "error: " + t.toString());
                }
            });
        } catch (Exception e) {
//            pDialog.dismiss();
            Log.d("lolfinal", "uploadError: " + e.toString());
            Toast.makeText(this, "Registrasi gagal coba lagi !", Toast.LENGTH_SHORT).show();
        }
    }

//    public void getQRCode(){
//
//        Code code = new Code();
//        code.setCode("9012345678Aa");
//
//        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
//        try {
//            Call<JsonObject> call = service
//                    .getQR("bearer "+preference.getToken1(), code);
//            call.enqueue(new Callback<JsonObject>() {
//                @Override
//                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                    Log.d("loldong", "onResponse: "+ response.code() +response.body());
//
//                }
//
//                @Override
//                public void onFailure(Call<JsonObject> call, Throwable t) {
//                    Log.d("loldong", "error: " + t.toString());
//                }
//            });
//        } catch (Exception e) {
//            Log.d("loldong", "error: " + e.toString());
//        }
//    }


    private void showDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MasterActivity.this);

        View mView = getLayoutInflater().inflate(R.layout.code_popup,
                null);

        Button button = mView.findViewById(R.id.submit);
        EditText otpView;
        otpView = mView.findViewById(R.id.otp_view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = Objects.requireNonNull(otpView.getText()).toString();
                expandURL(code);
                    if(code.contains("http://")){
                        String newcode =  code.replace("http://","");
                        getResultCodeManual(newcode);
                    }else {
                        getResultCodeManual(code);
                    }
            }
        });
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    private void showDialogPassword() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MasterActivity.this);

        View mView = getLayoutInflater().inflate(R.layout.pop_up_changepassword,
                null);

        TextInputEditText oldPassword = mView.findViewById(R.id.password);
        TextInputEditText retypeoldPassword = mView.findViewById(R.id.retype_password);
        TextInputLayout oldPasswordTIL= mView.findViewById(R.id.passwordLayout);
        TextInputLayout retypeoldPasswordTIL = mView.findViewById(R.id.retypeLayout);

        TextInputEditText newPassword = mView.findViewById(R.id.newpassword);
        TextInputEditText retypenewPassword = mView.findViewById(R.id.newretype_password);
        TextInputLayout newPasswordTIL= mView.findViewById(R.id.newpasswordLayout);
        TextInputLayout retypenewPasswordTIL = mView.findViewById(R.id.newretypeLayout);

        LinearLayout linearLayout= mView.findViewById(R.id.change_password);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(oldPassword.getText().toString())){
                    oldPasswordTIL.setError("Password lama tidak boleh kosong");
                    oldPassword.findFocus();
                    return;
                }else {
                    oldPasswordTIL.setError(null);
                }

                if(oldPassword.getText().toString().length() < 6){
                    oldPasswordTIL.setError("Password tidak boleh kurang dari 6 digit");
                    oldPassword.findFocus();
                    return;
                }else {
                    oldPasswordTIL.setError(null);
                }

                if(TextUtils.isEmpty(retypeoldPassword.getText().toString())){
                    retypeoldPasswordTIL.setError("Konfirmasi password lama tidak boleh kosong");
                    retypeoldPassword.findFocus();
                    return;
                }else {
                    retypeoldPasswordTIL.setError(null);
                }

                if(!oldPassword.getText().toString().equals(retypeoldPassword.getText().toString())){
                    retypeoldPasswordTIL.setError("Password lama dan Konfirmasi password lama tidak sama");
                    retypeoldPassword.findFocus();
                    return;
                }else {
                    retypeoldPasswordTIL.setError(null);
                }

                if(TextUtils.isEmpty(newPassword.getText().toString())){
                    newPasswordTIL.setError("Password baru tidak boleh kosong");
                    newPassword.findFocus();
                    return;
                }else {
                    newPasswordTIL.setError(null);
                }

                if(newPassword.getText().toString().length() < 6){
                    newPasswordTIL.setError("Password tidak boleh kurang dari 6 digit");
                    newPassword.findFocus();
                    return;
                }else {
                    newPasswordTIL.setError(null);
                }

                if(TextUtils.isEmpty(retypenewPassword.getText().toString())){
                    retypenewPasswordTIL.setError("Konfirmasi password baru tidak boleh kosong");
                    retypenewPassword.findFocus();
                    return;
                }else {
                    retypenewPasswordTIL.setError(null);
                }

                if(!newPassword.getText().toString().equals(retypenewPassword.getText().toString())){
                    retypenewPasswordTIL.setError("Password baru dan Konfirmasi password baru tidak sama");
                    retypenewPassword.findFocus();
                    return;
                }else {
                    retypenewPasswordTIL.setError(null);
                    LoginModel loginModel = new LoginModel();
                    Gson gson = new Gson();
                    String json = preference.getUser();
                    LoginModel login = gson.fromJson(json, LoginModel.class);
                    loginModel.setUsername(login.getUsername());
                    loginModel.setPassword(oldPassword.getText().toString());
                    ChangePassword changePassword = new ChangePassword();
                    changePassword.setNew_password(newPassword.getText().toString());
                    changePassword.setNew_password_confirm(retypenewPassword.getText().toString());
                    changePassword.setOld_password(oldPassword.getText().toString());

                    checkPassword(loginModel,changePassword);
                }

            }
        });

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    private void checkPassword(LoginModel loginModel, ChangePassword changePassworde){
        util.displayLoader(progressDialog,"Mengganti Password");
        LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.loginAccount(loginModel, new RetrofitInterface() {
            @Override
            public void onRequestSuccess(JsonObject response) {
                if(!response.get("error").getAsBoolean()){
                    progressDialog.dismiss();
                    changePassword(changePassworde);
                }else {
                    Toast.makeText(MasterActivity.this, "Email atau Password anda salah", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
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

    private void changePassword(ChangePassword changePassword){
        LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.changePassword("Bearer " + preference.getToken2(), changePassword, new RetrofitInterface() {
            @Override
            public void onRequestSuccess(JsonObject response) {
                if(!response.get("message").getAsString().equals("Password wrong!")){
                Toast.makeText(MasterActivity.this, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MasterActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                }else {
                    Toast.makeText(MasterActivity.this, "Password anda salah", Toast.LENGTH_SHORT).show();
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

    public void logOut(){
        preference.removeToken1();
        preference.removeToken2();
        preference.removeUser();
        Intent intent = new Intent(MasterActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDialogNew() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MasterActivity.this);

        View mView = getLayoutInflater().inflate(R.layout.code_new_popup,
                null);

        Button button = mView.findViewById(R.id.submit);
        OtpView otpView;
        otpView = mView.findViewById(R.id.otp_view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = Objects.requireNonNull(otpView.getText()).toString();
                if (code.length() < 8) {
                    Toast.makeText(MasterActivity.this, "Kode tidak boleh kurang dari 8", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    getResultCod(otpView.getText().toString());
                }
            }
        });
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public void renewaltoken(){
        retrofitRequest = new RetrofitRequest();
        loginViewModel = new LoginViewModel();
        retrofitRequest.getToken(new RetrofitInterface() {
            @Override
            public void onRequestSuccess(JsonObject response) {
                String token = response.get("tokenString").getAsString();
                preference.setToken1(token);
                Log.d("lolmaster", "onRequestSuccess: " + token);
            }

            @Override
            public void onRequestSuccess2(JsonArray response) {

            }

            @Override
            public void onRequestError(String error) {

            }
        });

        Gson gson = new Gson();
         String json = preference.getUser();
        LoginModel login = gson.fromJson(json, LoginModel.class);

        loginViewModel.loginAccount(login, new RetrofitInterface() {
            @Override
            public void onRequestSuccess(JsonObject response) {
                if(!response.get("error").getAsBoolean()){
                    Log.d("lolmaster2", "onRequestSuccess: "+response.get("token").getAsString());
                    preference.setToken2(response.get("token").getAsString());
                }else {
                    Toast.makeText(MasterActivity.this, "Sesi anda habis silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MasterActivity.this,MainActivity.class);
                    preference.removeUser();
                    preference.removeToken1();
                    preference.removeToken2();
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


    protected void getLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You dont have location permission", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.d("lol", "latitude" + latitude + "longitude" + longitude);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = true;
                mCodeScanner.startPreview();
            } else {
                mPermissionGranted = false;
            }
        }

        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = true;
                mCodeScanner.startPreview();
            } else {

                mPermissionGranted = false;
            }
        }
    }

    private void fetchLocation() {


        if (ContextCompat.checkSelfPermission(MasterActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MasterActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission to acess this feature")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MasterActivity.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MasterActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object\
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Log.d("lol", "latitude" + latitude + "longitude" + longitude);
                            }
                        }
                    });
        }
    }


    private void uploadHistory(String shorturl,String url){

        Scan scan = new Scan();
        scan.setAlamat(address);
        scan.setUser_id(String.valueOf(userid));
        scan.setProvinsi(provinceStringID);
        scan.setKecamatan(districttStringID);
        scan.setKota(cityStringID);
        scan.setLatitude(String.valueOf(latitude));
        scan.setLongitude(String.valueOf(longitude));
        scan.setShort_url(shorturl);
        scan.setSource("android");

        ApiInterface service = ApiClient.getDataBrightGas().create(ApiInterface.class);
        try {
            Call<JsonObject> call = service
                    .postHistory("application/json","application/json",
                            scan);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("lolll", "onResponse: "+ response.code() + response.body());
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                }
            });
        } catch (Exception e) {
        }
    }

    public void getData(){
        ApiInterface service = ApiClient.getDataBrightGas().create(ApiInterface.class);

        try {
            Log.d("lolcencol", "getData: " + preference.getToken2());
            Call<JsonObject> call = service.getData("Bearer "+preference.getToken2(),"application/json","application/json" );
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("lolcode", "onResponse: " + response.code());
                    if(response.code() == 200 ){
                        Log.d("aingmaung", "onResponse: " +response.body());
                        JsonObject jsonObject = response.body().get("user").getAsJsonObject();
                        try {
                            provinceStringID = jsonObject.get("provinsi").getAsString();
                            cityStringID = jsonObject.get("kota").getAsString();
                            districttStringID = jsonObject.get("kecamatan").getAsString();
                            address= jsonObject.get("alamat").getAsString();
                        }catch (Exception e){
                            Toast.makeText(MasterActivity.this, "Lengkapi profile anda di website resmi BrightGas", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
        renewaltoken();
        getData();
    }
}
