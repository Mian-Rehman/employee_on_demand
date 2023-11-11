package com.example.employeeondemand.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeondemand.Models.ListData;
import com.example.employeeondemand.Models.Userdata;
import com.example.employeeondemand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BlockListAdapter extends RecyclerView.Adapter<BlockListAdapter.BlockListViewHolder>{
    Context context;
    ArrayList<ListData> blockListUsers;

    public BlockListAdapter(Context context, ArrayList<ListData> blockListUsers) {
        this.context = context;
        this.blockListUsers = blockListUsers;
    }


    @NonNull
    @Override
    public BlockListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new BlockListAdapter.BlockListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockListViewHolder holder, int position) {
        ListData model = blockListUsers.get(position);
        String userId = model.getUserId();

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userdata userdata = snapshot.getValue(Userdata.class);
                Picasso.with(context).load(userdata.getProfilePic()).placeholder(R.drawable.placeholder).into(holder._blockListProfile);
                holder._blockListUsername.setText(userdata.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return blockListUsers.size();
    }

    public class BlockListViewHolder extends RecyclerView.ViewHolder{
        ImageView _blockListProfile;
        TextView _blockListUsername;
        public BlockListViewHolder(@NonNull View itemView) {
            super(itemView);
            _blockListProfile = (ImageView) itemView.findViewById(R.id._listProfileItem);
            _blockListUsername = (TextView) itemView.findViewById(R.id._listUsernameItem);
        }
    }
}
