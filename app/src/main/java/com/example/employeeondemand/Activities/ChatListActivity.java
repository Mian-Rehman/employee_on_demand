package com.example.employeeondemand.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employeeondemand.Adapters.CallListAdapter;
import com.example.employeeondemand.Adapters.ChatListAdapter;
import com.example.employeeondemand.Models.CallListData;
import com.example.employeeondemand.Models.ListData;
import com.example.employeeondemand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {
    float listPosition;
    Intent intent;
    String myId;
    ChatListAdapter chatListAdapter;
    ArrayList<ListData> chatListUsers;
    RecyclerView _preChatLstView, _preCallListView;
    TextView _chatRoomList, _callRoomList;
    LinearLayout _chatListLayout, _callListLayout;
    ArrayList<CallListData> callArrayList;
    CallListAdapter callListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        try {
            intent = getIntent();
            myId = intent.getStringExtra("myId");
            Log.i("MyId", myId);
            _callListLayout = findViewById(R.id._callListLayout);
            _chatListLayout = findViewById(R.id._chatListLayout);
            _preChatLstView = findViewById(R.id._preChatListView);
            _preCallListView = findViewById(R.id._preCallsListView);
            _chatRoomList = findViewById(R.id._chatRoomList);
            _callRoomList = findViewById(R.id._callRoomList);

            listPosition = 1500f;
            _callListLayout.setTranslationX(listPosition);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("ChatListError", e.getMessage().toString());
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        _preChatLstView.setLayoutManager(linearLayoutManager);

        chatListUsers = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(this, chatListUsers, myId);
        _preChatLstView.setAdapter(chatListAdapter);
        readChatList();

        LinearLayoutManager callLayoutmanager = new LinearLayoutManager(ChatListActivity.this);
        callLayoutmanager.setReverseLayout(true);
        callLayoutmanager.setStackFromEnd(true);
        _preCallListView.setLayoutManager(callLayoutmanager);

        callArrayList = new ArrayList<>();
        callListAdapter = new CallListAdapter(ChatListActivity.this, callArrayList);
        _preCallListView.setAdapter(callListAdapter);

        readCallList();

        _chatRoomList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listPosition != 1500f){
                    listPosition = 1500f;
                    _chatRoomList.setAlpha(1f);
                    _callRoomList.setAlpha(0.6f);
                    _chatListLayout.animate().translationXBy(listPosition).setDuration(300).start();
                    _callListLayout.animate().translationXBy(listPosition).setDuration(300).start();

                }
            }
        });

        _callRoomList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listPosition == 1500f){
                    listPosition = -1500f;
                    _callRoomList.setAlpha(1.0f);
                    _chatRoomList.setAlpha(0.6f);
                    _chatListLayout.animate().translationXBy(listPosition).setDuration(300).start();
                    _callListLayout.animate().translationXBy(listPosition).setDuration(300).start();

                }
            }
        });

    }

    private void readCallList() {
        FirebaseDatabase.getInstance().getReference().child("CallList").child(myId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            callArrayList.clear();
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                CallListData callListData = dataSnapshot.getValue(CallListData.class);
                                callArrayList.add(callListData);
                            }
                            callListAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void readChatList(){
        try {
            chatListUsers.clear();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference().child("ChatList").child(myId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ListData listData = dataSnapshot.getValue(ListData.class);
                        chatListUsers.add(listData);
                    }
                    chatListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("ChatLstError", e.getMessage().toString());
        }
    }
}