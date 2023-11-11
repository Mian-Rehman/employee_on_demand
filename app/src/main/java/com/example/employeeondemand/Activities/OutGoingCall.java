package com.example.employeeondemand.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employeeondemand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class OutGoingCall extends AppCompatActivity {

    ImageView _ogCallProfilePic, _endCallButton;
    TextView _ogCallUsername, _ogCallStatus;
    Intent intent;
    Bundle bundle;
    String username, profileUri, userId, myId, userToken, meetingRoom, status;
    MediaPlayer player;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_going_call);

        _ogCallProfilePic = findViewById(R.id._ogCallProfilePic);
        _ogCallUsername = findViewById(R.id._ogCallUsername);
        _ogCallStatus = findViewById(R.id._ogCallStatus);
        _endCallButton = findViewById(R.id._endCallbUTTON);

        intent = getIntent();
        bundle = intent.getExtras();
        myId = bundle.getString("myId");
        userId = bundle.getString("userId");
        username = bundle.getString("username");
        profileUri = bundle.getString("profileUri");
        userToken = bundle.getString("token");
        meetingRoom = myId+userId;

        countDownTimer = new CountDownTimer(40000,2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                player = MediaPlayer.create(OutGoingCall.this,R.raw.call_beep_sound);
                player.start();
            }

            @Override
            public void onFinish() {
                FirebaseDatabase.getInstance().getReference().child("Call").child(meetingRoom)
                        .child("callStatus").setValue("ended");
                Toast.makeText(OutGoingCall.this, "User is not receiving the\ncall please try again later", Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        countDownTimer.start();
        Picasso.with(OutGoingCall.this).load(profileUri).placeholder(R.drawable.placeholder).into(_ogCallProfilePic);
        _ogCallUsername.setText(username);
        _ogCallStatus.setText("Calling");

        _endCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Call").child(meetingRoom)
                        .child("callStatus").setValue("ended");
                countDownTimer.cancel();
                finish();
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Call").child(meetingRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String[] status = {snapshot.child("callStatus").getValue(String.class)};



                if (status[0].equals("rejected")){
                    player.stop();
                    countDownTimer.cancel();
                    Toast.makeText(OutGoingCall.this, "User is busy\nPlease try later", Toast.LENGTH_LONG).show();
                    finish();
                }
                if (status[0].equals("attended")){
                    countDownTimer.cancel();
                    player.stop();
                    joinVideoCall(meetingRoom);
                }
                if(status[0].equals("ended")){

                    player.stop();
                    countDownTimer.cancel();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    private void joinVideoCall(String meetingRoom) {
        try {
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL("https://meet.jit.si"))
                    .setRoom(meetingRoom)
                    .build();
            JitsiMeetActivity.launch(OutGoingCall.this, options);
            finish();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}