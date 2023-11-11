package com.example.employeeondemand.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeondemand.Activities.ServiceProviderProfile;
import com.example.employeeondemand.Models.Userdata;
import com.example.employeeondemand.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    String spProfessionString, myId;
    Intent intent;
    Bundle bundle;
    Context context;
    List<Userdata> usersLists;

    public UserAdapter(Context context, List<Userdata> usersLists, String _myId) {
        this.context = context;
        this.usersLists = usersLists;
        myId = _myId;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_items, parent, false);
        return  new UserAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Userdata model = usersLists.get(position);
        try {
            spProfessionString = model.getProfession();
            Float rating = Float.parseFloat(model.getRating());
            Log.i("Activity's Status", spProfessionString);
            Picasso.with(context).load(model.getProfilePic()).placeholder(R.drawable.placeholder).into(holder._spProfilePic);
            holder._spUsername.setText(model.getUsername());
            holder._spCity.setText(model.getCity());
            holder._spProfession.setText(model.getProfession());
            holder._userRatingItem.setRating(rating);
            holder._viewSpProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        intent = new Intent(v.getContext(), ServiceProviderProfile.class);
                        bundle = new Bundle();
                        bundle.putString("myId", myId);
                        bundle.putString("Sp Id", model.getuId());
                        bundle.putString("Username", model.getUsername());
                        bundle.putString("Profession", model.getProfession());
                        bundle.putString("Rating", model.getRating());
                        bundle.putString("Phone No", model.getPhoneNo());
                        bundle.putString("Profile Pic", model.getProfilePic());
                        bundle.putString("Rate Per Day", model.getRatePerDay());
                        bundle.putString("Total Earned", model.getEarned());
                        bundle.putString("Skills Details", model.getSkillDetails());
                        bundle.putString("token", model.getToken());
                        intent.putExtras(bundle);
                        v.getContext().startActivity(intent);
                        Log.i("View Profile Status", "pressed");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("AdapterUserError", e.getMessage().toString());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("errr in Adapter", e.getMessage().toString());
        }
    }

    @Override
    public int getItemCount() {
        return usersLists.size();
    }


    class UserViewHolder extends RecyclerView.ViewHolder{
        ImageView _spProfilePic;
        TextView _spUsername, _spCity, _spProfession, _viewSpProfile;
        RatingBar _userRatingItem;

        public UserViewHolder(@NonNull View item) {
            super(item);

            _spProfilePic = (ImageView) item.findViewById(R.id._spProfilePic);
            _spUsername = (TextView)  item.findViewById(R.id._spUsername);
            _spCity = (TextView) item.findViewById(R.id._spCity);
            _spProfession = (TextView) item.findViewById(R.id._spProfession);
            _viewSpProfile = (TextView) item.findViewById(R.id._viewSpProfile);
            _userRatingItem = (RatingBar) item.findViewById(R.id._userRatingItem);

        }
    }

}
