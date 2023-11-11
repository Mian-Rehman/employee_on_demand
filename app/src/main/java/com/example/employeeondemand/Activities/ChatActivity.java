package com.example.employeeondemand.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.employeeondemand.Adapters.MessageAdapter;
import com.example.employeeondemand.Models.CallData;
import com.example.employeeondemand.Models.MessageData;
import com.example.employeeondemand.Models.NotificationData;
import com.example.employeeondemand.Models.SendNotification;
import com.example.employeeondemand.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    Intent intent;
    Bundle bundle;
    String profilePicString, senderUsername, username, receiverUId, senderUId, userToken;
    TextView _chatUsername, _chatHireText, _typingStatus;
    EditText _messageInput;
    ImageView _chatbackbutton, _chatProfilePic, _chatVCallIcon, _sendMessage, _imageMessage;
    RecyclerView _preMessagesView;
    HashMap hashMap;
    MessageAdapter messageAdapter;
    ArrayList <MessageData> messages;
    String senderRoom, receiverRoom;

    FirebaseDatabase database;
    DatabaseReference reference,reference2;
    FirebaseStorage storage;
    ProgressDialog dialog;

    ValueEventListener msgSeenListener1, msgSeenListener2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        try {
            _chatHireText = findViewById(R.id._chatHireText);
            _chatUsername = findViewById(R.id._chatUsername);
            _messageInput = findViewById(R.id._messageInput);
            _chatbackbutton = findViewById(R.id._chatBackButton);
            _chatProfilePic = findViewById(R.id._chatProfilePic);
            _chatVCallIcon = findViewById(R.id._chatVCallIcon);
            _sendMessage = findViewById(R.id._sendMessage);
            _imageMessage = findViewById(R.id._imageMessage);
            _preMessagesView = findViewById(R.id._preMessagesView);

            //geting intent value from previous activity...
            intent = getIntent();
            bundle = intent.getExtras();
            receiverUId = bundle.getString("userId");
            senderUId = bundle.getString("myId");
            profilePicString = bundle.getString("profileUri");
            username = bundle.getString("username");
            userToken = bundle.getString("token");

            //initializing progress dialoge for sending image type nmessage...
            dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading Image...");
            dialog.setCancelable(false);

            //assining values to the views...
            Picasso.with(ChatActivity.this).load(profilePicString).placeholder(R.drawable.placeholder).into(_chatProfilePic);
            _chatUsername.setText(username);

            //initializing chat room for sender and receiver to store messages...
            senderRoom = senderUId + receiverUId;
            receiverRoom = receiverUId + senderUId;

            Log.i("SenderId",senderUId);
            FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(senderUId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    senderUsername = snapshot.child("username").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            messages = new ArrayList<>();
            messageAdapter = new MessageAdapter(this, messages, senderUId);
            database = FirebaseDatabase.getInstance();
            storage = FirebaseStorage.getInstance();

            try {
                database.getReference().child("Chat")
                        .child(senderRoom)
                        .child("messages")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                try {
                                    messages.clear();
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        MessageData messageData = dataSnapshot.getValue(MessageData.class);
                                        messages.add(messageData);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.i("ChatError", e.getMessage().toString());
                                }
                                messageAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Error Chat", e.getMessage().toString());
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setStackFromEnd(true);
            //linearLayoutManager.setReverseLayout(true);
            _preMessagesView.setLayoutManager(linearLayoutManager);
            _preMessagesView.setAdapter(messageAdapter);

            _preMessagesView.scrollToPosition(messages.size()-1);

            _imageMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, 1);
                }
            });

            _sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Date date = new Date();
                    String messageText = _messageInput.getText().toString().trim();
                    MessageData messageData = new MessageData(messageText, senderUId, date.getTime(),receiverRoom,false);
                    if (messageText.length()<1){
                        Toast.makeText(ChatActivity.this, "Enter Message!", Toast.LENGTH_SHORT).show();
                    }else {
                        String randomKey = database.getReference().push().getKey();
                        database.getReference().child("Chat")
                                .child(senderRoom)
                                .child("messages").child(randomKey)
                                .setValue(messageData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("Chat")
                                        .child(receiverRoom)
                                        .child("messages").child(randomKey)
                                        .setValue(messageData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        String notificationId = FirebaseDatabase.getInstance().getReference().push().getKey();
                                        NotificationData notificationData = new NotificationData(notificationId, senderUId, "message", senderUsername, messageText, false);
                                        FirebaseDatabase.getInstance().getReference().child("Notifications").child(receiverUId).child(notificationId).setValue(notificationData);

                                        SendNotification sendNotification = new SendNotification(senderUsername, messageText, userToken, senderUId, ChatActivity.this);
                                        sendNotification.sendNotification();

                                        //sendMessageNotification(username, messageText, userToken);
                                        hashMap = new HashMap<>();
                                        hashMap.put("userId", receiverUId);
                                        database.getReference().child("ChatList").child(senderUId)
                                                .child(receiverUId).setValue(hashMap);
                                        hashMap = new HashMap<>();
                                        hashMap.put("userId", senderUId);
                                        database.getReference().child("ChatList").child(receiverUId)
                                                .child(senderUId).setValue(hashMap);
                                    }
                                });
                            }
                        });
                        _messageInput.setText("");
                    }
                }
            });

            _chatVCallIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dexter.withActivity(ChatActivity.this).withPermission(Manifest.permission.RECORD_AUDIO).withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            String messageBody = senderUId;
                            CallData callData = new CallData("calling", "VC", senderUId, receiverUId, "");
                            String randomKey = database.getReference().push().getKey();
                            database.getReference().child("Call")
                                    .child(senderRoom)
                                    .setValue(callData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    String callRandomkey = database.getReference().push().getKey();
                                    hashMap = new HashMap<>();
                                    hashMap.put("userId", receiverUId);
                                    hashMap.put("type","VC");
                                    database.getReference().child("CallList").child(senderUId)
                                            .child(callRandomkey).setValue(hashMap);
                                    hashMap = new HashMap<>();
                                    hashMap.put("userId", senderUId);
                                    hashMap.put("type", "Vc");
                                    database.getReference().child("CallList").child(receiverUId)
                                            .child(callRandomkey).setValue(hashMap);

                                    SendNotification sendNotification = new SendNotification("VC", messageBody, userToken, senderUId, ChatActivity.this);
                                    sendNotification.sendNotification();
                                }
                            });
                            nextActivity();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            Toast.makeText(ChatActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Chat Error", e.getMessage().toString());
        }

        _chatbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        _chatHireText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChatActivity.this);
                dialog.setTitle("Alert Message");
                dialog.setIcon(R.drawable.ic_baseline_warning_24);
                dialog.setMessage("Are you sure you want to generate contract if you want to generate then select your role,Are you");
                dialog.setPositiveButton("Service Provider", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ChatActivity.this, "Sorry Only Consumer\ncan generate Contract", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("Consumer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent = new Intent(ChatActivity.this, ContractActivity.class);
                        bundle = new Bundle();
                        bundle.putString("consumerId", senderUId);
                        bundle.putString("serviceProviderId", receiverUId);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });

        seenMessage(receiverUId);
    }

    void  sendMessageNotification(String name, String message, String token){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = "https://fcm.googleapis.com/fcm/send";

            JSONObject objectData = new JSONObject();
            if (name.length()<3) {
                final String[] messageBody = new String[1];
                FirebaseDatabase.getInstance().getReference().child("Users").child(message)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                messageBody[0] = snapshot.child("username").getValue(String.class);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                objectData.put("title", "Incoming Video Call");
                objectData.put("body", messageBody[0]);
            }
            else{
                objectData.put("title", name);
                objectData.put("body", message);
            }
            JSONObject notificationData = new JSONObject();
            notificationData.put("notification", objectData);
            notificationData.put("to", token);

            JsonObjectRequest request = new JsonObjectRequest(url, notificationData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("NtfyError", error.toString());
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String,String> map = new HashMap<>();
                    String serverKey = "Key=AAAA5sDPEp0:APA91bE1V8rXj9JeKYMaTooULUoqHXRLjps60OTjTm83kKGUXFPhkxEDEaZ8s2nYFoq_E2XFVfen3XeynCknGlTRYV25xjHxHthSsU7TPIw_0QURdTJjjbi9cthSFry20-fN-AiQx2le";
                    map.put("Authorization", serverKey);
                    map.put("Content-Type", "application/json");
                    return map;
                }
            };
            requestQueue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void seenMessage(String userId){
        String myId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Chat").child(myId+userId).child("messages");
        reference2 = FirebaseDatabase.getInstance().getReference("Chat").child(userId+myId).child("messages");
        msgSeenListener1 = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    MessageData messageData = dataSnapshot.getValue(MessageData.class);
                    if(messageData.getSenderId().equals(myId)){

                    }else{
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("seen",true);
                        dataSnapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        msgSeenListener2 = reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    MessageData messageData = dataSnapshot.getValue(MessageData.class);
                    if(messageData.getSenderId().equals(myId)){

                    }else{
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("seen",true);
                        dataSnapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void nextActivity(){
        intent = new Intent(ChatActivity.this, OutGoingCall.class);
        bundle = new Bundle();
        bundle.putString("userId", receiverUId);
        bundle.putString("myId", senderUId);
        bundle.putString("profileUri", profilePicString);
        bundle.putString("username", username);
        bundle.putString("token", userToken);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (data!=null){
                Uri selectImage = data.getData();
                Calendar calendar = Calendar.getInstance();
                StorageReference reference = storage.getReference().child("Chats").child(calendar.getTimeInMillis() + "");
                dialog.show();
                reference.putFile(selectImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String filePath = uri.toString();
                                    dialog.dismiss();
                                    Date date = new Date();
                                    String messageText = _messageInput.getText().toString().trim();
                                    MessageData messageData = new MessageData(messageText, senderUId, date.getTime(),receiverRoom,false);
                                    messageData.setMessageText("imageMessage");
                                    messageData.setImageUrl(filePath);
                                    String randomKey = database.getReference().push().getKey();
                                    database.getReference().child("Chat")
                                            .child(senderRoom)
                                            .child("messages").child(randomKey)
                                            .setValue(messageData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            database.getReference().child("Chat")
                                                    .child(receiverRoom)
                                                    .child("messages").child(randomKey)
                                                    .setValue(messageData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    hashMap = new HashMap<>();
                                                    hashMap.put("userId", receiverUId);
                                                    database.getReference().child("ChatList").child(senderUId)
                                                            .child(receiverUId).setValue(hashMap);
                                                    hashMap = new HashMap<>();
                                                    hashMap.put("userId", senderUId);
                                                    database.getReference().child("ChatList").child(receiverUId)
                                                            .child(senderUId).setValue(hashMap);
                                                }
                                            });
                                        }
                                    });
                                    _messageInput.setText("");
                                }
                            });
                        }
                    }
                });

            }
        }
    }

  @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(msgSeenListener1);
        reference2.removeEventListener(msgSeenListener2);
    }

}