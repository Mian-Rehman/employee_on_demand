package com.example.employeeondemand.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employeeondemand.Models.Userdata;
import com.example.employeeondemand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    Intent intent,passwordIntent;
    String myId;
    TextView _epUsername, _resetPassword;
    ImageView _epProfilePic, _epBackButton;
    EditText _epPhoneNo, _epRatePerDay, _epSkillsDetails;
    Spinner _epProfession;
    Button _epSaveButton;
    ArrayAdapter<CharSequence> professionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        intent = getIntent();
        myId = intent.getStringExtra("myId");//get intent data...

        _resetPassword = findViewById(R.id._resetPassword);
        _epUsername = findViewById(R.id._epUsername);
        _epProfilePic = findViewById(R.id._epProfilePic);
        _epBackButton = findViewById(R.id._epBackButton);
        _epPhoneNo = findViewById(R.id._epPhoneNo);
        _epProfession = findViewById(R.id._epProfession);
        _epRatePerDay = findViewById(R.id._epRatePerDay);
        _epSkillsDetails= findViewById(R.id._epSkillsDetails);
        _epSaveButton = findViewById(R.id._epSaveButton);

        _resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordIntent = new Intent(EditProfileActivity.this, ResetPasswordActivity.class);
                startActivity(passwordIntent);
            }
        });
        //Loading Current Data of User...
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(myId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userdata userdata= snapshot.getValue(Userdata.class);

                Picasso.with(EditProfileActivity.this).load(userdata.getProfilePic()).placeholder(R.drawable.placeholder).into(_epProfilePic);
                _epUsername.setText(userdata.getUsername());
                _epRatePerDay.setText(userdata.getRatePerDay());
                _epPhoneNo.setText(userdata.getPhoneNo());
                _epRatePerDay.setText(userdata.getRatePerDay());
                _epSkillsDetails.setText(userdata.getSkillDetails());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Setting Adapter...
        professionAdapter= ArrayAdapter.createFromResource(this,
                R.array.profession_array, R.layout.spinner_style_file);
        professionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _epProfession.setAdapter(professionAdapter);

        //back to User Profile Activity...
        _epBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //for updating Users data...
        _epSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child(myId);

                database.child("phoneNo").setValue(_epPhoneNo.getText().toString().trim());
                database.child("profession").setValue(_epProfession.getSelectedItem().toString().trim());
                database.child("skillDetails").setValue(_epSkillsDetails.getText().toString().trim());
                database.child("ratePerDay").setValue(_epRatePerDay.getText().toString().trim());
                Toast.makeText(EditProfileActivity.this, "Changes has been\nsuccessfully saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}