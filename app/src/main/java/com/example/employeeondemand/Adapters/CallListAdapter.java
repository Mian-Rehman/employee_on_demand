package com.example.employeeondemand.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeondemand.Activities.ChatActivity;
import com.example.employeeondemand.Models.CallListData;
import com.example.employeeondemand.Models.ListData;
import com.example.employeeondemand.Models.Userdata;
import com.example.employeeondemand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.CallListViewHolder>{

    Context context;
    ArrayList<CallListData> callArrayList;
    String myId;

    public CallListAdapter(Context context, ArrayList<CallListData> callArrayList) {
        this.context = context;
        this.callArrayList = callArrayList;
    }

    public CallListAdapter() {
    }

    @NonNull
    @Override
    public CallListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_list_items, parent, false);
        return new CallListAdapter.CallListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallListViewHolder holder, int position) {
        CallListData model = callArrayList.get(position);
        String userId = model.getUserId();
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userdata userdata = snapshot.getValue(Userdata.class);
                holder._callListUsernameItem.setText(userdata.getUsername());
                Picasso.with(context).load(userdata.getProfilePic()).placeholder(R.drawable.placeholder).into(holder._callListProfileItem);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (model.getType().equals("AC")){
            holder._callListTypeItem.setImageResource(R.drawable.ic_baseline_local_phone_24);
        }
        if (model.getType().equals("VC")){
            holder._callListTypeItem.setImageResource(R.drawable.call_list_videocam_24);
        }
    }

    @Override
    public int getItemCount() {
        return callArrayList.size();
    }

    public class CallListViewHolder extends RecyclerView.ViewHolder{
        ImageView _callListProfileItem, _callListTypeItem;
        TextView _callListUsernameItem,_callListTimeItem;
        public CallListViewHolder(@NonNull View itemView) {
            super(itemView);
            _callListProfileItem = (ImageView) itemView.findViewById(R.id._callListProfileItem);
            _callListTypeItem = (ImageView) itemView.findViewById(R.id._callListTypeItem);
            _callListUsernameItem = (TextView) itemView.findViewById(R.id._callListUsernameItem);
            _callListTimeItem = (TextView) itemView.findViewById(R.id._callListTimeItem);
        }
    }
}
