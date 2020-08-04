package com.pertamina.brightgasapp.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.brightgasapp.R;
import com.pertamina.brightgasapp.model.login.LoginModel;
import com.pertamina.brightgasapp.view.base.BaseActivity;
import com.pertamina.brightgasapp.view.network.RetrofitInterface;
import com.pertamina.brightgasapp.view.network.RetrofitRequest;
import com.pertamina.brightgasapp.view.utilities.Preference;
import com.pertamina.brightgasapp.view.utilities.Util;
import com.pertamina.brightgasapp.viewmodel.LoginViewModel;

public class MainActivity extends BaseActivity {

    private TextView registerButton;
    private TextView termCondition;
    private TextView privacyPolicy;
    private TextView forgotPassword;
    private EditText username;
    private EditText password;
    private Button loginButton;

    private Util util;
    private Preference preference;
    private LoginViewModel loginViewModel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpView();
        setupListener();
        generateView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lol", "onStart: " + preference.getToken1() + " loll " + preference.getToken2() );
        if(preference.getToken1()!= null && preference.getToken2() != null){
            intentToHome();
        }
    }

    @Override
    public void setUpView() {
        registerButton = findViewById(R.id.register_button);
        privacyPolicy = findViewById(R.id.privacypolicy);
        termCondition = findViewById(R.id.termcondition);
        loginButton = findViewById(R.id.login_button);
        username = findViewById(R.id.email);
        password = findViewById(R.id.password);
        forgotPassword = findViewById(R.id.forgot_password);

        progressDialog =  new ProgressDialog(MainActivity.this);

        util = new Util();

        loginViewModel = new LoginViewModel();

        preference = new Preference(this);
    }

    @Override
    public void generateView() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        termCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,PrivacyPolicyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,TermConditionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

       }

    @Override
    public void setupListener() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAccount();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ForgotPassword.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void loginAccount(){
        String emailString = username.getText().toString();
        String passwordString = password.getText().toString();

        TextInputLayout emailTIL = findViewById(R.id.emailLayout);
        TextInputLayout passwordTIL = findViewById(R.id.passwordLayout);

        if(TextUtils.isEmpty(emailString)){
            emailTIL.setError("Username tidak boleh kosong");
            username.findFocus();
        }else {
            emailTIL.setError(null);
        }

        if(TextUtils.isEmpty(passwordString)){
            passwordTIL.setError("Password tidak boleh kosong");
            password.findFocus();
        }else {
            passwordTIL.setError(null);
        }

        if(passwordString.length()<6){
            passwordTIL.setError("Password tidak boleh kurang dari 6 huruf atau angka");
            password.findFocus();
        }else {
            util.displayLoader(progressDialog,"Memverifikasi akun");
            LoginModel loginModel = new LoginModel();
            loginModel.setUsername(emailString.toLowerCase());
            loginModel.setPassword(passwordString);
            submitLogin(loginModel);

        }

    }

    private void submitLogin(LoginModel loginModel){
        loginViewModel.loginAccount(loginModel, new RetrofitInterface() {
            @Override
            public void onRequestSuccess(JsonObject response) {
                if(!response.get("error").getAsBoolean()){
                    preference.setToken2(response.get("token").getAsString());
                    Gson gson = new Gson();
                    String json = gson.toJson(loginModel);
                    preference.setUser(json);
                    getToken();
                }else {
                    Toast.makeText(MainActivity.this, "Username atau Password anda salah", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onRequestSuccess2(JsonArray response) {
                progressDialog.dismiss();
            }

            @Override
            public void onRequestError(String error) {
                Log.d("lol", "onRequestError: "+ error);
                progressDialog.dismiss();
            }
        });
    }

    private void getToken(){
        RetrofitRequest retrofitRequest = new RetrofitRequest();
        retrofitRequest.getToken(new RetrofitInterface() {
            @Override
            public void onRequestSuccess(JsonObject response) {
                String token = response.get("tokenString").getAsString();
                preference.setToken1(token);
                Log.d("lolbro", "onRequestSuccess: " + token);
                Log.d("lolbro", "onRequestSuccess: " + preference.getToken1());
                intentToHome();
                progressDialog.dismiss();
            }

            @Override
            public void onRequestSuccess2(JsonArray response) {
                progressDialog.dismiss();
            }

            @Override
            public void onRequestError(String error) {
                progressDialog.dismiss();
            }
        });
    }

    private void intentToHome(){
        Intent intent = new Intent(MainActivity.this,MasterActivity.class);
        startActivity(intent);
        finish();
    }

}
