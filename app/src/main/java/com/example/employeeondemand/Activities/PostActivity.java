package com.example.employeeondemand.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.employeeondemand.Adapters.ChatListAdapter;
import com.example.employeeondemand.Adapters.PostAdapter;
import com.example.employeeondemand.Models.ListData;
import com.example.employeeondemand.Models.NotificationData;
import com.example.employeeondemand.Models.PostData;
import com.example.employeeondemand.Models.SendNotification;
import com.example.employeeondemand.Models.Userdata;
import com.example.employeeondemand.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    ImageView _postBackButton;
    Spinner _enterPostProfession;
    EditText _enterPostBudget, _enterPostTime, _enterPostWorkplace;
    Button _postSubmitButton;
    ArrayAdapter<CharSequence> postProfessionAdapter;
    Intent intent;
    Bundle extras;
    String myId, userCity, postId, serviceName, serviceTime, serviceBudget, serviceWorkplace;
    DatabaseReference databaseReference;
    ArrayList<PostData> postList;
    RecyclerView _prePostView;
    PostAdapter _postAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        try {
            _postBackButton = findViewById(R.id._postBackButton);
            _enterPostBudget = findViewById(R.id._enterPostBudget);
            _enterPostTime = findViewById(R.id._enterPostTime);
            _enterPostWorkplace = findViewById(R.id._enterPostWorkplace);
            _enterPostProfession = findViewById(R.id._enterPostProfession);
            _postSubmitButton = findViewById(R.id._postSubmitButton);
            _prePostView = findViewById(R.id._prePostView);

            intent = getIntent();
            extras = intent.getExtras();
            myId = extras.getString("userId");
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);

            _prePostView.setLayoutManager(linearLayoutManager);
            postList = new ArrayList<>();
            _postAdapter = new PostAdapter(this, postList, myId);
            _prePostView.setAdapter(_postAdapter);
            readPostList();

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Post Error", e.getMessage().toString());
        }

        postProfessionAdapter = ArrayAdapter.createFromResource(this,
                R.array.profession_array,R.layout.post_spinner_style);
        postProfessionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _enterPostProfession.setAdapter(postProfessionAdapter);

        _postBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        _postSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( _enterPostBudget.getText().length()<1 &&_enterPostTime.getText().length()<1 && _enterPostWorkplace.getText().length()<1){
                    Toast.makeText(PostActivity.this, "Enter all details!", Toast.LENGTH_LONG).show();
                }
                else if (_enterPostProfession.getSelectedItem().toString().matches("Consumer")){
                    Toast.makeText(PostActivity.this, "Select Service", Toast.LENGTH_LONG).show();
                }
                else {
                    uploadPost();
                }

            }
        });

    }

    private void readPostList() {
        try {
            postList.clear();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference().child("Posts").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        PostData postData = dataSnapshot.getValue(PostData.class);
                        postList.add(postData);
                    }
                    _postAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("PostListError", e.getMessage().toString());
        }
    }

    private void uploadPost() {
        try {
            intent = getIntent();
            extras = intent.getExtras();
            myId = extras.getString("userId");
            userCity = extras.getString("userCity");
            serviceName = _enterPostProfession.getSelectedItem().toString().trim();
            serviceBudget = _enterPostBudget.getText().toString().trim();
            serviceTime = _enterPostTime.getText().toString().trim();
            serviceWorkplace = _enterPostWorkplace.getText().toString().trim();

            databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
            postId = databaseReference.push().getKey();

            PostData postData = new PostData(postId, serviceBudget, serviceName, serviceTime, serviceWorkplace, userCity, myId);
            databaseReference.child(postId).setValue(postData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Userdata userdata = dataSnapshot.getValue(Userdata.class);
                                if (serviceName.equals(userdata.getProfession())){
                                    String userToken, notificationMessage;
                                    userToken = userdata.getToken();
                                    notificationMessage = userdata.getUsername() + " is looking for a " + serviceName;

                                    String notificationId = FirebaseDatabase.getInstance().getReference().push().getKey();

                                    NotificationData notificationData = new NotificationData(notificationId, myId, "post", "Job Alert", notificationMessage, false);
                                    FirebaseDatabase.getInstance().getReference().child("Notifications").child(userdata.getuId()).child(notificationId).setValue(notificationData);

                                    SendNotification sendNotification = new SendNotification("Job Alert", notificationMessage, userToken, myId, PostActivity.this);
                                    sendNotification.sendNotification();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, "Network Problem\nCheck your Internet\nconnection!", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Log.i("PostError", e.getMessage().toString());
        }

        _enterPostBudget.setText("");
        _enterPostTime.setText("");
        _enterPostWorkplace.setText("");
        _enterPostProfession.setAdapter(postProfessionAdapter);
    }
}