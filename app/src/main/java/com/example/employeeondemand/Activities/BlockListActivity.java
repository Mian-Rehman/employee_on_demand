package com.example.employeeondemand.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.employeeondemand.Adapters.BlockListAdapter;
import com.example.employeeondemand.Models.ListData;
import com.example.employeeondemand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BlockListActivity extends AppCompatActivity {

    RecyclerView _preBlockUsers;
    TextView _noBlockUsers;
    ArrayList<ListData> blockListUsers;
    BlockListAdapter blockListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_list);

        _preBlockUsers = findViewById(R.id._preBlockUsers);
        _noBlockUsers = findViewById(R.id._noblockUser);

        try {
            blockListUsers = new ArrayList<>();
            blockListAdapter = new BlockListAdapter(this, blockListUsers);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            _preBlockUsers.setLayoutManager(linearLayoutManager);
            _preBlockUsers.setAdapter(blockListAdapter);

            readBlockList();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Block List Error", e.getMessage().toString());
        }
    }

    private void readBlockList() {
        FirebaseDatabase.getInstance().getReference().child("BlockList")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            ListData listData = dataSnapshot.getValue(ListData.class);
                            blockListUsers.add(listData);
                        }
                        blockListAdapter.notifyDataSetChanged();
                        if (blockListUsers.size()<1){
                            _preBlockUsers.setVisibility(RecyclerView.GONE);
                            _noBlockUsers.setVisibility(TextView.VISIBLE);
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}