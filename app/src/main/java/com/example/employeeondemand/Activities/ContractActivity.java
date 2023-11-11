package com.example.employeeondemand.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employeeondemand.Models.ContractData;
import com.example.employeeondemand.Models.NotificationData;
import com.example.employeeondemand.Models.SendNotification;
import com.example.employeeondemand.Models.Userdata;
import com.example.employeeondemand.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ContractActivity extends AppCompatActivity {

    Intent intent;
    Bundle bundle;
    String consumerId, serviceProviderId, serviceBudget, serviceTime, serviceWorkPlace, contractStatus, consumerUsername, spToken;
    TextView _consumerUsername, _consumerCnicNo, _consumerAddress, _serviceProviderUsername, _serviceProviderCnicNo, _serviceProviderAddress;
    ImageView _contractConsumerProfile, _contractsProviderProfile, _consumerCnicPic, _serviceProviderCnicPic;
    EditText _contractBudgetInput, _contractTimeInput, _contractWorkplaceInput;
    Button _createContractButton;
    FirebaseDatabase database;
    String consumerCnicUrl, serviceProviderCnicUrl;
    Userdata userdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);

        _contractsProviderProfile = findViewById(R.id._contractsProviderProfile);
        _contractConsumerProfile = findViewById(R.id._contractConsumerProfile);
        _consumerCnicPic = findViewById(R.id._consumerCnicPic);
        _serviceProviderCnicPic = findViewById(R.id._serviceProviderCnicPic);
        _consumerUsername = findViewById(R.id._consumerUsername);
        _consumerCnicNo = findViewById(R.id._consumerCnicNo);
        _consumerAddress = findViewById(R.id._consumerAddress);
        _createContractButton = findViewById(R.id._createContractButton);
        _serviceProviderUsername = findViewById(R.id._serviceProviderUsername);
        _serviceProviderCnicNo = findViewById(R.id._serviceProviderCnicNo);
        _serviceProviderAddress = findViewById(R.id._serviceProviderAddress);
        _contractBudgetInput = findViewById(R.id._contractBudgetInput);
        _contractTimeInput = findViewById(R.id._contractTimeInput);
        _contractWorkplaceInput = findViewById(R.id._contractWorkplaceInput);
        database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();

        intent = getIntent();
        bundle = intent.getExtras();
        consumerId = bundle.getString("consumerId");
        serviceProviderId = bundle.getString("serviceProviderId");



            databaseReference.child("Users").child(consumerId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        userdata = new Userdata();
                        userdata = snapshot.getValue(Userdata.class);
                        Picasso.with(ContractActivity.this).load(userdata.getProfilePic()).placeholder(R.drawable.placeholder).into(_contractConsumerProfile);
                        Picasso.with(ContractActivity.this).load(userdata.getCnicUri()).placeholder(R.drawable.placeholder).into(_consumerCnicPic);
                        consumerUsername = userdata.getUsername();
                        _consumerUsername.setText(consumerUsername);
                        _consumerCnicNo.setText(userdata.getCnicNo());
                        _consumerAddress.setText(userdata.getAddress());
                        consumerCnicUrl = userdata.getCnicUri();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("Contract Data Retriving", e.getMessage().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        databaseReference.child("Users").child(serviceProviderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userdata = new Userdata();
                userdata = snapshot.getValue(Userdata.class);
                Picasso.with(ContractActivity.this).load(userdata.getProfilePic()).placeholder(R.drawable.placeholder).into(_contractsProviderProfile);
                Picasso.with(ContractActivity.this).load(userdata.getCnicUri()).placeholder(R.drawable.placeholder).into(_serviceProviderCnicPic);
                spToken = userdata.getToken();
                _serviceProviderUsername.setText(userdata.getUsername());
                _serviceProviderCnicNo.setText(userdata.getCnicNo());
                _serviceProviderAddress.setText(userdata.getAddress());
                serviceProviderCnicUrl = userdata.getCnicUri();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        _consumerCnicPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ContractActivity.this, ViewImage.class);
                bundle = new Bundle();
                bundle.putString("tag", "cnic");
                bundle.putString("cnic", consumerCnicUrl);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        _serviceProviderCnicPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    intent = new Intent(ContractActivity.this, ViewImage.class);
                    bundle = new Bundle();
                    bundle.putString("tag", "cnic");
                    bundle.putString("cnic", serviceProviderCnicUrl);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("ViewImageError",e.getMessage().toString());
                }
            }
        });

        _createContractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceBudget = _contractBudgetInput.getText().toString().trim();
                serviceTime = _contractTimeInput.getText().toString().trim();
                serviceWorkPlace = _contractWorkplaceInput.getText().toString().trim();
                if (serviceBudget.length()>1 && serviceTime.length()>1 && serviceWorkPlace.length()>1) {
                    String randomKey = databaseReference.push().getKey();
                    contractStatus = "Requesting...";
                    ContractData contractData = new ContractData(randomKey, consumerId, serviceProviderId, serviceBudget, serviceTime, serviceWorkPlace, contractStatus);
                    databaseReference.child("Contracts")
                            .child(consumerId)
                            .child(randomKey)
                            .setValue(contractData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            databaseReference.child("Contracts")
                                    .child(serviceProviderId)
                                    .child(randomKey)
                                    .setValue(contractData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ContractActivity.this, "Contract has been\nsuccessfully generated!", Toast.LENGTH_LONG).show();

                                    String notificationMessage = consumerUsername + " wants to build a contract with you";

                                    String notificationId = FirebaseDatabase.getInstance().getReference().push().getKey();
                                    NotificationData notificationData = new NotificationData(notificationId, consumerId, "contract", "Contract Request", notificationMessage, false);
                                    FirebaseDatabase.getInstance().getReference().child("Notifications").child(serviceProviderId).child(notificationId).setValue(notificationData);

                                    SendNotification sendNotification = new SendNotification("Contract Request", notificationMessage, spToken, consumerId, ContractActivity.this);
                                    sendNotification.sendNotification();
                                    finish();
                                }
                            });
                        }
                    });
                }else {
                    Toast.makeText(ContractActivity.this, "Enter all values!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}