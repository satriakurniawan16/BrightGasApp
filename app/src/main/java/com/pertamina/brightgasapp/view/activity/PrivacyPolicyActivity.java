package com.pertamina.brightgasapp.view.activity;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.pertamina.brightgasapp.R;
import com.pertamina.brightgasapp.view.base.BaseActivity;

import static com.pertamina.brightgasapp.view.utilities.GlobalString.ReusableString.TNC;

public class PrivacyPolicyActivity extends BaseActivity {

    TextView text ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        setUpView();
        generateView();

    }
    @Override
    public void setUpView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kebijakan dan Privasi");
        toolbar.setNavigationIcon(R.drawable.ic_action_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        text = findViewById(R.id.privacy_text);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void generateView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text.setText(Html.fromHtml(TNC, Html.FROM_HTML_MODE_COMPACT));
        } else {
            text.setText(Html.fromHtml(TNC));
        }
    }

    @Override
    public void setupListener() {

    }
}
