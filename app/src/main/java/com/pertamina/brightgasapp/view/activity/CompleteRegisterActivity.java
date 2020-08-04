package com.pertamina.brightgasapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.pertamina.brightgasapp.R;

public class CompleteRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_register);

        LinearLayout linearLayout = findViewById(R.id.linearLayout4);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompleteRegisterActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
