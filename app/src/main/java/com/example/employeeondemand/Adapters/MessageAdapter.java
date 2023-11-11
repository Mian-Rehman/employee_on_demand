package com.example.employeeondemand.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeondemand.Activities.ViewImage;
import com.example.employeeondemand.Models.MessageData;
import com.example.employeeondemand.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<MessageData> messages;
    final int item_sent = 1;
    final int item_receive = 2;
    String myId;

    public MessageAdapter(Context context, ArrayList<MessageData> messages, String _myId) {
        this.context = context;
        this.messages = messages;
        myId = _myId;
    }

    public MessageAdapter() {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

         if(viewType == item_sent){
           View view = LayoutInflater.from(context).inflate(R.layout.sent_message_item, parent, false);
            return new SentViewHolder(view);
        }else {
           View view = LayoutInflater.from(context).inflate(R.layout.receive_message_items, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        MessageData messageData = messages.get(position);
        if (myId.equals(messageData.getSenderId())) {
            return item_sent;
        } else {
            return item_receive;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageData messageData = messages.get(position);
        String  message = messageData.getMessageText();

        if (holder.getClass() == SentViewHolder.class){

            SentViewHolder viewHolder = (SentViewHolder) holder;
            if (message.equals("imageMessage")){
                Picasso.with(context).load(messageData.getImageUrl()).placeholder(R.drawable.placeholder).into(viewHolder._sendImage);
                viewHolder._sendImage.setVisibility(ImageView.VISIBLE);
                viewHolder._sentMessage.setVisibility(TextView.GONE);
                viewHolder._sendImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ViewImage.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("tag", "chatMessage");
                        bundle.putString("chatImage", messageData.getImageUrl());
                        intent.putExtras(bundle);
                        v.getContext().startActivity(intent);
                    }
                });
            }
            else {
                viewHolder._sentMessage.setText(message);
            }

            if (position == messages.size()-1){
                if (messageData.isSeen()){
                    viewHolder._textSeen.setText("Seen");
                }
                else{
                    viewHolder._textSeen.setText("Delivered");
                }
                viewHolder._textSeen.setVisibility(View.VISIBLE);
            }else{
                viewHolder._textSeen.setVisibility(View.GONE);
            }

        }else {

            ReceiverViewHolder viewHolder = (ReceiverViewHolder)  holder;
            if (message.equals("imageMessage")){
                Picasso.with(context).load(messageData.getImageUrl()).placeholder(R.drawable.placeholder).into(viewHolder._receiveImage );
                viewHolder._receiveImage.setVisibility(ImageView.VISIBLE);
                viewHolder._receiveMessage.setVisibility(TextView.GONE);
                viewHolder._receiveImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ViewImage.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("tag", "chatMessage");
                        bundle.putString("chatImage", messageData.getImageUrl());
                        intent.putExtras(bundle);
                        v.getContext().startActivity(intent);
                    }
                });
            }
            else {
                viewHolder._receiveMessage.setText(message);
            }


            if (position == messages.size()-1){
                if (messageData.isSeen()){
                    viewHolder._isSeen.setText("Seen");
                }
                else{
                    viewHolder._isSeen.setText("Delivered");
                }
                viewHolder._isSeen.setVisibility(View.VISIBLE);
            }else{
                viewHolder._isSeen.setVisibility(View.GONE);
            }

        }
        Log.i("Chat Adapter Error", myId);

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {

        TextView _sentMessage, _textSeen;
        ImageView _sendImage;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            _sentMessage = (TextView) itemView.findViewById(R.id._sentMessage);
            _sendImage = (ImageView) itemView.findViewById(R.id._sendImage);
            _textSeen = (TextView) itemView.findViewById(R.id._textSeen);
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView _receiveMessage, _isSeen;
        ImageView _receiveImage;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            _receiveMessage = (TextView) itemView.findViewById(R.id._receiveMessage);
            _isSeen = (TextView) itemView.findViewById(R.id._isSeen);
            _receiveImage = (ImageView) itemView.findViewById(R.id._receiveImage);
        }
    }

}
