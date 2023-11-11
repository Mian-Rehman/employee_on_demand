package com.example.employeeondemand.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeondemand.Models.FeedbackData;
import com.example.employeeondemand.Models.Userdata;
import com.example.employeeondemand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>{

    Context context;
    ArrayList<FeedbackData> feedbackList;


    public FeedbackAdapter(Context context, ArrayList<FeedbackData> feedbackList) {
        this.context = context;
        this.feedbackList = feedbackList;
    }

    public FeedbackAdapter() {
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feedback_items, parent, false);
        return new FeedbackAdapter.FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {

        FeedbackData model = feedbackList.get(position);
        String userId = model.getSenderId();

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userdata userdata = snapshot.getValue(Userdata.class);
                Picasso.with(context).load(userdata.getProfilePic()).placeholder(R.drawable.placeholder).into(holder._feedbackProfileItem);
                holder._feedbackUsernameItem.setText(userdata.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String preRating = model.getRating();

        Float rating = Float.parseFloat(preRating);
        holder._feedbackRatingBarItem.setRating(rating);

        if (model.getComment().length()>1){
            holder._feedbackCommentItem.setText(model.getComment());
            holder._feedbackCommentItem.setVisibility(TextView.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public class FeedbackViewHolder extends RecyclerView.ViewHolder{

        ImageView _feedbackProfileItem;
        TextView _feedbackUsernameItem, _feedbackCommentItem;
        RatingBar _feedbackRatingBarItem;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            _feedbackProfileItem = (ImageView) itemView.findViewById(R.id._feedbackProfileItem);
            _feedbackUsernameItem = (TextView) itemView.findViewById(R.id._feedbackUsernameItem);
            _feedbackRatingBarItem = (RatingBar) itemView.findViewById(R.id._feedbackRatingBarItem);
            _feedbackCommentItem = (TextView) itemView.findViewById(R.id._feedbackCommentItem);
        }
    }
}
