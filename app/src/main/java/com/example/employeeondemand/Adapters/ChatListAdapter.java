package com.example.employeeondemand.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeondemand.Activities.ChatActivity;
import com.example.employeeondemand.Models.ListData;
import com.example.employeeondemand.Models.MessageData;
import com.example.employeeondemand.Models.Userdata;
import com.example.employeeondemand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>{

    Context context;
    Intent intent;
    Bundle bundle;
    ArrayList<ListData> chatListUser;
    String myId;

    public ChatListAdapter(Context context, ArrayList<ListData> chatListUser, String myId) {
        this.context = context;
        this.chatListUser = chatListUser;
        this.myId = myId;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new ChatListAdapter.ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        ListData model = chatListUser.get(position);
        String userId = model.getUserId();
        Log.i("UserId", userId);
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userdata userdata = snapshot.getValue(Userdata.class);
                Picasso.with(context).load(userdata.getProfilePic()).placeholder(R.drawable.placeholder).into(holder._chatListProfileItem);
                FirebaseDatabase.getInstance().getReference("Chat")
                        .child(model.getUserId()+myId).child("messages")
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            MessageData messageData = dataSnapshot.getValue(MessageData.class);
                            if(messageData.getSenderId().equals(model.getUserId())){
                                holder._viewed.setVisibility(View.VISIBLE);
                                if (messageData.isSeen()){
                                    holder._viewed.setVisibility(View.GONE);
                                }else{
                                    holder._viewed.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                holder._chatListUsernameItem.setText(userdata.getUsername());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(v.getContext(), ChatActivity.class);
                        bundle = new Bundle();
                        bundle.putString("myId", myId);
                        bundle.putString("userId", userdata.getuId());
                        bundle.putString("username", userdata.getUsername());
                        bundle.putString("profileUri", userdata.getProfilePic());
                        bundle.putString("token", userdata.getToken());
                        intent.putExtras(bundle);
                        v.getContext().startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return chatListUser.size();
    }


    public class ChatListViewHolder extends RecyclerView.ViewHolder{

        ImageView _chatListProfileItem,_viewed;
        TextView _chatListUsernameItem;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);

            _chatListProfileItem = (ImageView) itemView.findViewById(R.id._listProfileItem);
            _viewed = (ImageView) itemView.findViewById(R.id._viewed);
            _chatListUsernameItem = (TextView) itemView.findViewById(R.id._listUsernameItem);

        }
    }
}
