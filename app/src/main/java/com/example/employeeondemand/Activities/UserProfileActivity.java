package com.example.employeeondemand.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employeeondemand.Models.Userdata;
import com.example.employeeondemand.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    TextView _editProfileText, _nameText, _professionText, _rateText, _ratingText, _earnedText, _servicesTextview, _postTextview, _blockedUsersText;
    ImageView _userProfilePic, _contractIcon, _notificationIcon, _messageIcon, _feedbackIcon;
    Intent intent;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;
    String imagestring;
    String uId, userId, userCity;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        _editProfileText = findViewById(R.id._editProfileText);
        _userProfilePic = findViewById(R.id._userProfilePic);
        _nameText = findViewById(R.id._nameText);
        _professionText = findViewById(R.id._professionText);
        _rateText = findViewById(R.id._rateDetail);
        _ratingText = findViewById(R.id._ratingDetail);
        _earnedText = findViewById(R.id._earnedDetail);
        _servicesTextview = findViewById(R.id._servicesTextview);
        _postTextview = findViewById(R.id._postTextview);
        _contractIcon = findViewById(R.id._contractIcon);
        _notificationIcon = findViewById(R.id._notificationIcon);
        _feedbackIcon = findViewById(R.id._blockListIcon);
        _messageIcon = findViewById(R.id._messageIcon);
        _blockedUsersText = findViewById(R.id._blockedUsersText);
        extras = new Bundle();

        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference();
        uId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ProgressDialog dialog = new ProgressDialog(UserProfileActivity.this);
        dialog.setIcon(R.drawable.empicon);
        dialog.setTitle("Employee On Demand");
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.show();

        userReference.child("Users").child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userdata userdata = snapshot.getValue(Userdata.class);
                imagestring = userdata.getProfilePic();
                Picasso.with(getApplicationContext()).load(imagestring).placeholder(R.drawable.placeholder).into(_userProfilePic);
                _nameText.setText(userdata.getUsername());
                _professionText.setText(userdata.getProfession());
                _ratingText.setText(userdata.getRating());
                _rateText.setText(userdata.getRatePerDay()+ "/Day");
                _earnedText.setText(userdata.getEarned());
                userId = userdata.getuId();
                userCity = userdata.getCity();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("UserProfileEror",error.getMessage().toString());
            }
        });

        _editProfileText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("myId",userId);
                startActivity(intent);
            }
        });

        _servicesTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(UserProfileActivity.this,SearchServicesActivity.class);
                extras.putString("userId", userId);
                extras.putString("userCity", userCity);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        _postTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(UserProfileActivity.this, PostActivity.class);
                extras.putString("userId", userId);
                extras.putString("userCity", userCity);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        _contractIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(UserProfileActivity.this,MyContractsActivity.class);
                //intent.putExtra("myId", userId);
                startActivity(intent);
            }
        });

        _notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(UserProfileActivity.this,NotificationActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        _blockedUsersText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(UserProfileActivity.this, BlockListActivity.class);
                startActivity(intent);
            }
        });

        _feedbackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(UserProfileActivity.this, FeedbackActivity.class);
                intent.putExtra("myId", userId);
                startActivity(intent);
            }
        });

        _messageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(UserProfileActivity.this,ChatListActivity.class);
                intent.putExtra("myId", userId);
                startActivity(intent);
            }
        });
        new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        }.start();


    }

}