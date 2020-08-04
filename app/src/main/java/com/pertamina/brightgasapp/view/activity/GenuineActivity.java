package com.pertamina.brightgasapp.view.activity;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pertamina.brightgasapp.R;
import com.pertamina.brightgasapp.view.base.BaseActivity;

import static com.pertamina.brightgasapp.view.utilities.GlobalString.IntentEstra.INTENT_EXTRA_NAME;
import static com.pertamina.brightgasapp.view.utilities.GlobalString.IntentEstra.INTENT_EXTRA_TYPE;

public class GenuineActivity extends BaseActivity {

    private  Toolbar toolbar;
    private TextView productName;
    private TextView sppbe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genuine);
        setUpView();
        generateView();
        setupListener();
    }

    @Override
    public void setUpView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);
        toolbar.setNavigationIcon(R.drawable.ic_action_back_black);

        productName = findViewById(R.id.product_name);
        sppbe= findViewById(R.id.sppbe);

    }

    @Override
    public void generateView() {

        if(getIntent().getStringExtra(INTENT_EXTRA_TYPE).equals("1")){
           productName.setText("BrightGas 12 kg");
        }else if(getIntent().getStringExtra(INTENT_EXTRA_TYPE).equals("2")){
            productName.setText("Elpiji 12 kg");
        }else {
            productName.setText("BrightGas 5,5 kg");
        }

        if(getIntent().getStringExtra(INTENT_EXTRA_NAME).equals("")){
            sppbe.setText("");
        }else{
            sppbe.setText("Diisi oleh SPPBE "+getIntent().getStringExtra(INTENT_EXTRA_NAME));
        }



    }

    @Override
    public void setupListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GenuineActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
