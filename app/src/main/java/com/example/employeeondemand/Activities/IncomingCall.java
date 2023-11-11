package com.example.employeeondemand.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employeeondemand.Models.Userdata;
import com.example.employeeondemand.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class IncomingCall extends AppCompatActivity {

    Intent intent;
    String username, profileUri, userId, myId, meetingRoom;
    ImageView _icProfilePic, _icRejectCallButton, _icAttendCallButton;
    TextView _icUsername, _icCallStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);

        _icProfilePic = findViewById(R.id._icCallProfilePic);
        _icUsername = findViewById(R.id._icCallUsername);
        _icCallStatus = findViewById(R.id._icCallStatus);
        _icAttendCallButton = findViewById(R.id._icAttendCallButton);
        _icRejectCallButton = findViewById(R.id._icRejectCallButton);

        intent = getIntent();
        userId = intent.getStringExtra("userId");
        myId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        meetingRoom = userId+myId;

        FirebaseDatabase.getInstance().getReference().child("Users").child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Userdata userdata = snapshot.getValue(Userdata.class);
                        profileUri = userdata.getProfilePic();
                        username = userdata.getUsername();
                        Picasso.with(IncomingCall.this).load(profileUri).placeholder(R.drawable.placeholder).into(_icProfilePic);
                        _icUsername.setText(username);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        _icAttendCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(IncomingCall.this).withPermission(Manifest.permission.RECORD_AUDIO).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        FirebaseDatabase.getInstance().getReference().child("Call").child(meetingRoom)
                                .child("callStatus").setValue("attended");
                        joinVideoCall(meetingRoom);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(IncomingCall.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
            }
        });

        _icRejectCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Call").child(meetingRoom)
                        .child("callStatus").setValue("rejected");
                finish();
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Call").child(meetingRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = snapshot.child("callStatus").getValue(String.class);
                if (status.equals("ended")){
                    Toast.makeText(IncomingCall.this, "Call ended by\n" + username, Toast.LENGTH_LONG).show();
                    finish();
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
            JitsiMeetActivity.launch(IncomingCall.this, options);
            finish();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}