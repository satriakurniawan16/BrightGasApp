package com.pertamina.brightgasapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.brightgasapp.R;
import com.pertamina.brightgasapp.model.login.ForgotPass;
import com.pertamina.brightgasapp.view.base.BaseActivity;
import com.pertamina.brightgasapp.view.network.RetrofitInterface;
import com.pertamina.brightgasapp.view.utilities.Util;
import com.pertamina.brightgasapp.viewmodel.LoginViewModel;

public class ForgotPassword extends BaseActivity {

    Util util;
    TextInputEditText textEmail;
    TextInputLayout textEmailLayout;
    LinearLayout sendEmail;
    ProgressDialog progressDialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        setUpView();
        setupListener();
        generateView();

    }

    @Override
    public void setUpView() {
        util = new Util();
        textEmail = findViewById(R.id.email);
        textEmailLayout = findViewById(R.id.emailLayout);
        sendEmail = findViewById(R.id.sendForgotPassword);
        progressDialog = new ProgressDialog(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lupa kata sandi");
        getSupportActionBar().setElevation(0);
        toolbar.setNavigationIcon(R.drawable.ic_action_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void generateView() {

    }

    @Override
    public void setupListener() {
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!util.isEmailValid(textEmail.getText().toString())){
                    textEmailLayout.setError("Email tidak valid");
                }else {
                    util.displayLoader(progressDialog,"Email anda sedang diproses");
                    textEmailLayout.setError(null);
                    ForgotPass forgotPass = new ForgotPass();
                    forgotPass.setEmail(textEmail.getText().toString());
                    LoginViewModel loginViewModel = new LoginViewModel();
                    loginViewModel.forgotPassword(forgotPass, new RetrofitInterface() {
                        @Override
                        public void onRequestSuccess(JsonObject response) {
                            Log.d("hasil", "onRequestSuccess: " + response);
                            if(response.get("valid").getAsBoolean() == true){
                                progressDialog.dismiss();
                                Intent intent = new Intent(ForgotPassword.this,EmailSentActivity.class);
                                startActivity(intent);
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(ForgotPassword.this, "Email tidak ditemukan", Toast.LENGTH_SHORT).show();
                            }
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
            }
        });


    }
}
