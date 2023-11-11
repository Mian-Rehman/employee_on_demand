package com.example.employeeondemand.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.employeeondemand.Adapters.BlockListAdapter;
import com.example.employeeondemand.Adapters.NotificationAdapter;
import com.example.employeeondemand.Models.ListData;
import com.example.employeeondemand.Models.NotificationData;
import com.example.employeeondemand.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    String myId;
    Intent intent;
    ArrayList<NotificationData> notificationList;
    NotificationAdapter notificationAdapter;
    RecyclerView _preNotifyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        _preNotifyView = findViewById(R.id._preNotifyView);
        //intent = getIntent();
        //myId = intent.getStringExtra("userId");

        myId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        try {
            notificationList = new ArrayList<>();
            notificationAdapter = new NotificationAdapter(this, notificationList, myId);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            _preNotifyView.setLayoutManager(new LinearLayoutManager(this));
            _preNotifyView.setAdapter(notificationAdapter);

            readNotificationList();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Block List Error", e.getMessage().toString());
        }
    }

    private void readNotificationList() {
        FirebaseDatabase.getInstance().getReference().child("Notifications").child(myId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            NotificationData notificationData = dataSnapshot.getValue(NotificationData.class);
                            notificationList.add(notificationData);
                        }
                        notificationAdapter.notifyDataSetChanged();

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}