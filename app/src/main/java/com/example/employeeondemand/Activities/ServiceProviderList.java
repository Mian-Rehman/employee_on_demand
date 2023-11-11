package com.example.employeeondemand.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.employeeondemand.R;
import com.example.employeeondemand.Adapters.UserAdapter;
import com.example.employeeondemand.Models.Userdata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceProviderList extends AppCompatActivity {

    Intent intent;
    Bundle bundle;
    String serviceName, myId;
    TextView _userListheading, _noUserText;
    RecyclerView _preUsersList;
    UserAdapter _userListAdapter;
    List<Userdata> userdataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_list);

        _userListheading = findViewById(R.id._userListHeeading);
        _preUsersList = findViewById(R.id._preUsersList);
        _noUserText = findViewById(R.id._noUserText);

        intent = getIntent();
        bundle = intent.getExtras();
        serviceName = bundle.getString("serviceName");
        myId = bundle.getString("myId");
        _userListheading.setText(serviceName);

        _preUsersList.setLayoutManager(new LinearLayoutManager(this));

        userdataList = new ArrayList<>();
        _userListAdapter = new UserAdapter(getApplicationContext(), userdataList,myId);
        _preUsersList.setAdapter(_userListAdapter);
        readUsersData();
    }

    private void readUsersData(){

        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Users");
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userdataList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Userdata userdata = dataSnapshot.getValue(Userdata.class);
                    if (userdata.getProfession().equals(serviceName) && !userdata.getuId().equals(myId)){
                        userdataList.add(userdata);
                    }
                }
                if (userdataList.size()==0){
                    _noUserText.setVisibility(TextView.VISIBLE);
                }

                _userListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}