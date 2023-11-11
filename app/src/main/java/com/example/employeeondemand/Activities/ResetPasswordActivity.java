package com.example.employeeondemand.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employeeondemand.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText _resetPasswordMail;
    TextView _message;
    Button _submitButton;
    String resetMail;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        _resetPasswordMail = findViewById(R.id._resetPasswordMail);
        _message = findViewById(R.id._message);
        _submitButton = findViewById(R.id._submitButton);
        mAuth = FirebaseAuth.getInstance();

        _resetPasswordMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _message.setText("");
                _message.setVisibility(View.GONE);
            }
        });

        _submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetMail = _resetPasswordMail.getText().toString().trim();
                mAuth.sendPasswordResetEmail(resetMail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        _message.setText("Reset link send to your email please check your email!");
                        _message.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        _message.setText("Unable to send reset password link!");
                        _message.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

    }
}