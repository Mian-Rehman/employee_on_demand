package com.example.employeeondemand.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.employeeondemand.Adapters.ContractListAdapter;
import com.example.employeeondemand.Models.ContractData;
import com.example.employeeondemand.Models.FeedbackData;
import com.example.employeeondemand.Models.ListData;
import com.example.employeeondemand.Models.NotificationData;
import com.example.employeeondemand.Models.SendNotification;
import com.example.employeeondemand.Models.Userdata;
import com.example.employeeondemand.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class MyContractsActivity extends AppCompatActivity {

    RecyclerView _preContractView;
    Intent intent;
    String myId;
    float rating;
    int badReview, totalEarned;
    TextView _noContractText;
    ContractData contractData;
    ContractListAdapter contractListAdapter;
    ArrayList<ContractData> myContractList;
    LinearLayout _feedbackLayout;
    RatingBar _ratingBar;
    EditText _feedbackInput;
    Button _submitFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contracts);

        try {
            intent = getIntent();
            myId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            _preContractView = findViewById(R.id._preContractsView);
            _noContractText = findViewById(R.id._noContractText);
            _feedbackLayout = findViewById(R.id._feedbackLayout);
            myContractList = new ArrayList<>();
            _ratingBar = findViewById(R.id._ratingBar);
            _feedbackInput = findViewById(R.id._feedbackInput);
            _submitFeedback = findViewById(R.id._submitFeedback);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(true);
            _preContractView.setLayoutManager(linearLayoutManager);
            contractListAdapter = new ContractListAdapter(MyContractsActivity.this, myContractList, myId);
            _preContractView.setAdapter(contractListAdapter);

            readContractsData();

            _submitFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    String my_Id, user_Id, contract_Status, contract_ID, comment, randomKey, token, notificationMessage, title;
                    //totalEarned = 0;
                    my_Id = contractListAdapter.getArrayList().get(0);
                    user_Id = contractListAdapter.getArrayList().get(1);
                    contract_Status = contractListAdapter.getArrayList().get(2);
                    token  = contractListAdapter.getArrayList().get(3);
                    notificationMessage = contractListAdapter.getArrayList().get(4);
                    title = contractListAdapter.getArrayList().get(5);
                    contract_ID = contractListAdapter.getArrayList().get(6);
                    rating = ((float) _ratingBar.getRating());
                    comment = "" + _feedbackInput.getText().toString().trim();
                    randomKey = database.getReference().push().getKey().toString();
                    FeedbackData feedbackData = new FeedbackData(my_Id, "" + rating, comment);
                    database.getReference().child("Feedback").child(user_Id)
                            .child(randomKey).setValue(feedbackData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    database.getReference().child("Contracts").child(my_Id)
                                            .child(contract_ID).child("contractStatus")
                                            .setValue(contract_Status).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            database.getReference().child("Contracts").child(user_Id)
                                                    .child(contract_ID).child("contractStatus")
                                                    .setValue(contract_Status).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    database.getReference().child("Users").child(user_Id).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            Userdata userdata = snapshot.getValue(Userdata.class);
                                                            float userrating = (float) Float.parseFloat(userdata.getRating());
                                                            badReview = userdata.getBadReview();

                                                            if (userrating > 0.0) {
                                                                userrating = (rating + userrating) / 2;
                                                            }

                                                            if (badReview >= 5) {
                                                                ListData blockListData = new ListData(user_Id);
                                                                database.getReference().child("BlockList").child(user_Id).setValue(blockListData);
                                                            }

                                                            String finalRating = new DecimalFormat("#.#").format(rating);
                                                            Log.i("rating", finalRating);
                                                            database.getReference().child("Users").child(user_Id).child("rating").setValue(finalRating);

                                                            _preContractView.setVisibility(RecyclerView.VISIBLE);
                                                            _feedbackLayout.setVisibility(LinearLayout.GONE);
                                                            _ratingBar.setRating(0f);
                                                            _feedbackInput.setText("");
                                                            Log.i("Bad Review", ""+badReview);
                                                            String notificationId = FirebaseDatabase.getInstance().getReference().push().getKey();
                                                            NotificationData notificationData = new NotificationData(notificationId, my_Id,"contract", title, notificationMessage, false);
                                                            FirebaseDatabase.getInstance().getReference().child("Notifications").child(user_Id).child(notificationId).setValue(notificationData);

                                                            if (rating < 2.0) {
                                                                ++badReview;
                                                            }
                                                            database.getReference().child("Users").child(user_Id).child("badReview").setValue(badReview);

                                                            SendNotification sendNotification = new SendNotification(title, notificationMessage, token, my_Id, MyContractsActivity.this);
                                                            sendNotification.sendNotification();
                                                            finish();

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                                }
                                            });
                                        }
                                    });
                                }
                            });

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("My Contract Error", e.getMessage().toString());
        }

    }

    private void readContractsData() {
        FirebaseDatabase.getInstance().getReference().child("Contracts").child(myId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        myContractList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            contractData = dataSnapshot.getValue(ContractData.class);
                            myContractList.add(contractData);
                        }
                        if (myContractList.size()<1){
                            _noContractText.setVisibility(TextView.VISIBLE);
                        }
                        contractListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}