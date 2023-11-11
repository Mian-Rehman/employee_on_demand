package com.example.employeeondemand.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.gridlayout.widget.GridLayout;

import android.widget.ImageView;

import com.example.employeeondemand.R;

public class SearchServicesActivity extends AppCompatActivity {

    ImageView _servicesBackButton;
    Intent intent;
    Bundle bundle;
    GridLayout _services;
    String serviceName, myId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_services);

        _servicesBackButton = findViewById(R.id._servicesBackButton);
        _services = (GridLayout) findViewById(R.id._servicesContainer);

        accessServices(_services);

        _servicesBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void accessServices(GridLayout services) {

            for (int j = 0; j < 16; j++) {
                ImageView imageView = (ImageView) services.getChildAt(j);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = getIntent();
                        bundle = intent.getExtras();
                        myId = bundle.getString("userId");
                        serviceName = imageView.getTag().toString();
                        intent = new Intent(SearchServicesActivity.this, ServiceProviderList.class);
                        bundle = new Bundle();
                        bundle.putString("serviceName", serviceName);
                        bundle.putString("myId", myId);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

    }


}