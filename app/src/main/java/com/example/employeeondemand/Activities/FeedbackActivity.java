package com.example.employeeondemand.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.employeeondemand.Adapters.FeedbackAdapter;
import com.example.employeeondemand.Models.FeedbackData;
import com.example.employeeondemand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FeedbackActivity extends AppCompatActivity {

    Intent intent;
    String myId;
    RecyclerView _preReviewView;
    TextView _noReviewText;
    ArrayList<FeedbackData> feedbackDataList;
    FeedbackAdapter feedbackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        //initializing views
        _preReviewView = findViewById(R.id._preReviewView);
        _noReviewText = findViewById(R.id.noReviewText);

        //get data from previousactivity
        intent = getIntent();
        myId = intent.getStringExtra("myId");

        Log.i("FeedbackData", myId);

        feedbackDataList = new ArrayList<>(); //initialize Array List
        feedbackAdapter = new FeedbackAdapter(this, feedbackDataList); //initializingAdapter

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        _preReviewView.setLayoutManager(linearLayoutManager); //setting Layout manager for recycle vier
        _preReviewView.setAdapter(feedbackAdapter);

        readReviewList();//calling function to load into list for recycler view adapter

    }

    //Loding data from database to adapter
    private void readReviewList() {
        try {
            feedbackDataList.clear();
            FirebaseDatabase.getInstance().getReference().child("Feedback").child(myId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                FeedbackData feedbackData = dataSnapshot.getValue(FeedbackData.class);
                                feedbackDataList.add(feedbackData);
                                Log.i("ReviewStatus", "Successfull");
                            }
                            feedbackAdapter.notifyDataSetChanged();
                            if (feedbackDataList.size()<1){
                                _preReviewView.setVisibility(RecyclerView.GONE);
                                _noReviewText.setVisibility(TextView.VISIBLE);
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