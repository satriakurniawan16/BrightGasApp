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

public class TermConditionActivity extends BaseActivity {

    TextView tnc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_condition);
        setUpView();
        generateView();
    }

    @Override
    public void setUpView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Syarat dan ketentuan");
        toolbar.setNavigationIcon(R.drawable.ic_action_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        tnc = findViewById(R.id.tnc);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void generateView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tnc.setText(Html.fromHtml(TNC, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tnc.setText(Html.fromHtml(TNC));
        }
    }

    @Override
    public void setupListener() {

    }
}