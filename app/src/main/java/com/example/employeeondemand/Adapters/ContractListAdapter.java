package com.example.employeeondemand.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeondemand.Activities.MyContractsActivity;
import com.example.employeeondemand.Models.ContractData;
import com.example.employeeondemand.Models.SendNotification;
import com.example.employeeondemand.Models.Userdata;
import com.example.employeeondemand.R;
import com.example.employeeondemand.Activities.ViewImage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ContractListAdapter extends RecyclerView.Adapter<ContractListAdapter.ContractListViewHolder>{
    Context context;
    ArrayList<ContractData> myContracts;
    String consumerId, serviceProviderId, contractStatus, myId, consumerToken, spToken, consumerUsername, spUsername;
    Bundle bundle;
    ArrayList<String> contractData;
    String notificationMessage;
    Integer finalBudget;

    public ContractListAdapter(Context context, ArrayList<ContractData> myContracts, String myId) {
        this.context = context;
        this.myContracts = myContracts;
        this.myId = myId;
        bundle = new Bundle();
    }

    @NonNull
    @Override
    public ContractListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contract_items, parent, false);
        return new ContractListAdapter.ContractListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContractListViewHolder holder, int position) {

        ContractData model = myContracts.get(position);
        consumerId = model.getConsumerId();
        serviceProviderId = model.getServiceProviderId();
        contractStatus = model.getContractStatus();
        Integer budget = Integer.parseInt(model.getServiceBudget());
        holder._contractBudgetItem.setText(model.getServiceBudget());
        holder._contractTimeItem.setText(model.getServiceTime());
        holder._contractWorkplaceItem.setText(model.getServiceWorkPlace());
        FirebaseDatabase.getInstance().getReference().child("Users").child(consumerId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Userdata userdata = new Userdata();
                        userdata = snapshot.getValue(Userdata.class);
                        Picasso.with(context).load(userdata.getProfilePic()).placeholder(R.drawable.placeholder).into(holder._consumerProfileItem);
                        Picasso.with(context).load(userdata.getCnicUri()).placeholder(R.drawable.placeholder).into(holder._consumerCnicPicItem);
                        consumerUsername = userdata.getUsername();
                        holder._consumerUsernameItem.setText(consumerUsername);
                        holder._consumerCnicNoItem.setText(userdata.getCnicNo());
                        holder._consumerAddressItem.setText(userdata.getAddress());
                        Integer earned = Integer.parseInt(userdata.getEarned());
                        finalBudget = 0;
                        finalBudget = budget+earned;
                        consumerToken = userdata.getToken();
                        Userdata finalUserdata = userdata;
                        holder._consumerCnicPicItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), ViewImage.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("tag", "cnic");
                                bundle.putString("cnic", finalUserdata.getCnicUri());
                                intent.putExtras(bundle);
                                v.getContext().startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("Users").child(serviceProviderId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Userdata userdata = new Userdata();
                        userdata = snapshot.getValue(Userdata.class);
                        Picasso.with(context).load(userdata.getProfilePic()).placeholder(R.drawable.placeholder).into(holder._spProfileItem);
                        Picasso.with(context).load(userdata.getCnicUri()).placeholder(R.drawable.placeholder).into(holder._spCnicPicItem);
                        spUsername = userdata.getUsername();
                        holder._spUsernameItem.setText(spUsername);
                        holder._spCnicNoItem.setText(userdata.getCnicNo());
                        holder._spAddressItem.setText(userdata.getAddress());
                        spToken = userdata.getToken();
                        Userdata finalUserdata = userdata;
                        holder._spCnicPicItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), ViewImage.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("tag", "cnic");
                                bundle.putString("cnic", finalUserdata.getCnicUri());
                                intent.putExtras(bundle);
                                v.getContext().startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        if (contractStatus.equals("Requesting...")){
            if (myId.equals(consumerId)){
                holder._contractStatus.setText("Request Sended");
            }
            if (myId.equals(serviceProviderId)){
                holder._buttonLayoutItem.setVisibility(LinearLayout.VISIBLE);
                holder._contractStatus.setText("Request Received");
            }
        }
        else if (contractStatus.equals("Rejected")){
            if (myId.equals(consumerId)){
                holder._contractStatus.setText("Rejected by service Provider");
            }
            if (myId.equals(serviceProviderId)){
                holder._contractStatus.setText("Rejected by You");
            }
        }
        else if (contractStatus.equals("Processing...")){
            holder._endContractButton.setVisibility(Button.VISIBLE);
            holder._contractStatus.setText(contractStatus);
        }
        else if (contractStatus.equals("Ended by Consumer")){
            if (myId.equals(consumerId)){
                holder._contractStatus.setText(contractStatus);
            }
            if (myId.equals(serviceProviderId)){
                holder._endContractButton.setVisibility(Button.VISIBLE);
                holder._contractStatus.setText(contractStatus);
            }
        }
        else if (contractStatus.equals("Ended by Service Provider")){
            if (myId.equals(consumerId)){
                holder._endContractButton.setVisibility(Button.VISIBLE);
                holder._contractStatus.setText(contractStatus);
            }
            if (myId.equals(serviceProviderId)){
                holder._contractStatus.setText(contractStatus);
            }
        }
        else{
            holder._contractStatus.setText(contractStatus);
        }

        holder._acceptContractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Contracts").child(consumerId)
                        .child(model.getContractId()).child("contractStatus").setValue("Processing...")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                FirebaseDatabase.getInstance().getReference().child("Contracts").child(serviceProviderId)
                                        .child(model.getContractId()).child("contractStatus").setValue("Processing...").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        notificationMessage = spUsername + " accepted your contract request";
                                        SendNotification sendNotification = new SendNotification("Contract Generated", notificationMessage, consumerToken, myId, context);
                                        sendNotification.sendNotification();
                                        Toast.makeText(v.getContext(), "Contract has been\nsuccessfully generated", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });

                holder._buttonLayoutItem.setVisibility(LinearLayout.GONE);

                holder._endContractButton.setVisibility(LinearLayout.VISIBLE);
            }
        });

        holder._rejectContractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Contracts").child(myId)
                        .child(model.getContractId()).child("contractStatus").setValue("Rejected");
                FirebaseDatabase.getInstance().getReference().child("Contracts").child(serviceProviderId)
                        .child(model.getContractId()).child("contractStatus").setValue("Rejected");

                String notificationMessage = spUsername + " rejected your contract request";
                SendNotification sendNotification = new SendNotification("Contract Rejected", notificationMessage, consumerToken, myId, context);
                sendNotification.sendNotification();

                holder._buttonLayoutItem.setVisibility(LinearLayout.GONE);
            }
        });
        Log.i("Budget", finalBudget+"");

        holder._endContractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contractData = new ArrayList<>();
                if (myId.equals(consumerId)){
                    contractData.add(0, consumerId);
                    contractData.add(1, serviceProviderId);
                    if ((contractStatus.equals("Processing..."))){
                        contractData.add(2, "Ended by Consumer");
                        contractData.add(3, spToken);
                        contractData.add(4,"Contract has been ended by " + consumerUsername);
                    }
                    else{
                        contractData.add(2, "Ended Successfully");
                        contractData.add(3,spToken);
                        contractData.add(4,"Your contract with " + consumerUsername + " has been successfully ended");
                    }

                }
                if (myId.equals(serviceProviderId)){
                    contractData.add(0, serviceProviderId);
                    contractData.add(1, consumerId);
                    if ((contractStatus.equals("Processing..."))){
                        contractData.add(2, "Ended by Service Provider");
                        contractData.add(3, consumerToken);
                        contractData.add(4,"Contract has been ended by " + spUsername);
                    }
                    else{
                        contractData.add(2, "Ended Successfully");
                        contractData.add(3, consumerToken);
                        contractData.add(4,"Your contract with " + spUsername + " has been successfully ended");
                    }
                }

                contractData.add(5, "Contract");
                contractData.add(6, model.getContractId());

                if (myId.equals(consumerId)){
                    contractData.add(7, finalBudget + "");
                }

                ((MyContractsActivity)context).findViewById(R.id._feedbackLayout).setVisibility(LinearLayout.VISIBLE);
                ((MyContractsActivity)context).findViewById(R.id._preContractsView).setVisibility(LinearLayout.GONE);
                Toast.makeText(v.getContext(), "Contract has been\nended Successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public ArrayList<String> getArrayList() {
        return contractData;
    }

    @Override
    public int getItemCount() {
        return myContracts.size();
    }

    public class ContractListViewHolder extends RecyclerView.ViewHolder{
        ImageView _consumerProfileItem, _spProfileItem, _consumerCnicPicItem, _spCnicPicItem;
        TextView _consumerUsernameItem, _consumerCnicNoItem, _consumerAddressItem,
                _spUsernameItem, _spCnicNoItem, _spAddressItem,
                _contractBudgetItem, _contractTimeItem, _contractWorkplaceItem, _contractStatus;
        LinearLayout _buttonLayoutItem;
        Button _acceptContractButton, _rejectContractButton, _endContractButton;

        public ContractListViewHolder(@NonNull View itemView) {
            super(itemView);
            _consumerProfileItem =(ImageView) itemView.findViewById(R.id._consumerProfileItem);
            _consumerCnicPicItem =(ImageView) itemView.findViewById(R.id._consumerCnicPicItem);

            _spProfileItem =(ImageView) itemView.findViewById(R.id._spProfileItem);
            _spCnicPicItem =(ImageView) itemView.findViewById(R.id._spCnicPicItem);

            _consumerUsernameItem = (TextView) itemView.findViewById(R.id._consumerUsernameItem);
            _consumerCnicNoItem = (TextView) itemView.findViewById(R.id._consumerCnicNoItem);
            _consumerAddressItem = (TextView) itemView.findViewById(R.id._consumerAddressItem);

            _spUsernameItem = (TextView) itemView.findViewById(R.id._spUsernameItem);
            _spCnicNoItem = (TextView) itemView.findViewById(R.id._spCnicNoItem);
            _spAddressItem = (TextView) itemView.findViewById(R.id._spAddressItem);

            _contractBudgetItem = (TextView) itemView.findViewById(R.id._contractBudgetItem);
            _contractTimeItem = (TextView) itemView.findViewById(R.id._contractTimeItem);
            _contractWorkplaceItem = (TextView) itemView.findViewById(R.id._contractWorkplaceItem);
            _buttonLayoutItem = (LinearLayout) itemView.findViewById(R.id._buttonLayoutItem);
            _rejectContractButton = (Button) itemView.findViewById(R.id._rejectContractButton);
            _acceptContractButton = (Button) itemView.findViewById(R.id._acceptContractButton);
            _endContractButton = (Button) itemView.findViewById(R.id._endContractButton);
            _contractStatus = (TextView) itemView.findViewById(R.id._notifyStatus);
        }
    }
}
