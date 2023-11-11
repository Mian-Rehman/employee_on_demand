package com.example.employeeondemand.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.employeeondemand.Adapters.BlockListAdapter;
import com.example.employeeondemand.Adapters.FeedbackAdapter;
import com.example.employeeondemand.Models.FeedbackData;
import com.example.employeeondemand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ServiceProviderProfile extends AppCompatActivity {

    Intent intent;
    String myId, spProfilePic, spId, spPhoneNo, spUsername, spRating, spRatePerDay,
            spTotalEarned, spProfession, spSkillsDetails, token;
    DatabaseReference databaseReference;
    TextView _nameDetail, _professionDetail, _rateDetail, _ratingDetail, _earnedDetail, _startChat, _hireText, _phoneNoDetail, _skillsDetail, _noFeedbackText;
    ImageView _profileDetail;
    Bundle sendValues;
    RecyclerView _preFeedbackView;
    ArrayList<FeedbackData> feedbackDataList;
    FeedbackAdapter feedbackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_profile);

        try {
            _nameDetail = (TextView) findViewById(R.id._nameDetail);
            _professionDetail = (TextView) findViewById(R.id._professionDetail);
            _rateDetail = (TextView) findViewById(R.id._rateDetail);
            _ratingDetail = (TextView) findViewById(R.id._ratingDetail);
            _earnedDetail = (TextView) findViewById(R.id._earnedDetail);
            _startChat = (TextView) findViewById(R.id._startChatText);
            _hireText = (TextView) findViewById(R.id._hireText);
            _profileDetail = (ImageView) findViewById(R.id._profileDetail);
            _phoneNoDetail = (TextView) findViewById(R.id._phoneNoDetail);
            _skillsDetail = (TextView) findViewById(R.id._skillsDetail);
            _preFeedbackView = (RecyclerView) findViewById(R.id._preFeedbackView);
            _noFeedbackText = (TextView) findViewById(R.id._noFeedbackText);

            intent = getIntent();
            Bundle bundle = intent.getExtras();
            myId = bundle.getString("myId");
            spProfilePic = bundle.getString("Profile Pic");
            spUsername = bundle.getString("Username");
            spProfession = bundle.getString("Profession");
            spPhoneNo = bundle.getString("Phone No");
            spId = bundle.getString("Sp Id");
            spRatePerDay = bundle.getString("Rate Per Day");
            spRating = bundle.getString("Rating");
            spTotalEarned = bundle.getString("Total Earned");
            spSkillsDetails = bundle.getString("Skills Details");
            token = bundle.getString("token");

            Picasso.with(ServiceProviderProfile.this).load(spProfilePic).placeholder(R.drawable.placeholder).into(_profileDetail);
            _nameDetail.setText(spUsername);
            _professionDetail.setText(spProfession);
            _phoneNoDetail.setText(spPhoneNo);
            _ratingDetail.setText(spRating);
            _rateDetail.setText(spRatePerDay+"/Day");
            _earnedDetail.setText(spTotalEarned);
            _skillsDetail.setText(spSkillsDetails);
            Log.i("My Id", myId);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("SpProfileError", e.getMessage().toString());
        }

        _startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ServiceProviderProfile.this, ChatActivity.class);
                sendValues = new Bundle();
                sendValues.putString("userId",spId);
                sendValues.putString("username", spUsername);
                sendValues.putString("profileUri", spProfilePic);
                sendValues.putString("myId", myId);
                sendValues.putString("token", token);
                intent.putExtras(sendValues);
                startActivity(intent);
            }
        });

        _hireText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ServiceProviderProfile.this, ContractActivity.class);
                sendValues = new Bundle();
                sendValues.putString("serviceProviderId", spId);
                sendValues.putString("consumerId",myId);
                sendValues.putString("token", token);
                intent.putExtras(sendValues);
                startActivity(intent);
            }
        });

        feedbackDataList = new ArrayList<>();
        feedbackAdapter = new FeedbackAdapter(this, feedbackDataList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        _preFeedbackView.setLayoutManager(linearLayoutManager);
        _preFeedbackView.setAdapter(feedbackAdapter);

        readFeedbackList();

    }

    private void readFeedbackList() {
        try {
            FirebaseDatabase.getInstance().getReference().child("Feedback").child(spId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                FeedbackData feedbackData = dataSnapshot.getValue(FeedbackData.class);
                                feedbackDataList.add(feedbackData);
                            }
                            feedbackAdapter.notifyDataSetChanged();
                            if (feedbackDataList.size()<1){
                                _preFeedbackView.setVisibility(RecyclerView.GONE);
                                _noFeedbackText.setVisibility(TextView.VISIBLE);
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("feedback Eror",e.getMessage().toString());
        }

    }
}