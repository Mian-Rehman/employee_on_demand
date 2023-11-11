package com.example.employeeondemand.Activities;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.employeeondemand.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import android.os.Bundle;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    EditText _loginEmail, _loginPassword;
    TextView _signUpText, _invalidEmailText, _invalidPasswordText,_forgotPassword;
    Button _loginButton;
    String email, password;
    ImageView _loginIntro, _loginGear;
    CountDownTimer forwardAnimation, waitTime, reverseWaitTime, backwardAnimation;

    private FirebaseAuth login;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _loginIntro = findViewById(R.id._loginIntro);
        _loginGear = findViewById(R.id._loginGear);
        _loginEmail = findViewById(R.id._loginEmail);
        _loginPassword = findViewById(R.id._loginPassword);
        _loginButton = findViewById(R.id._loginButton);
        _signUpText = findViewById(R.id._signUpText);
        _invalidEmailText = findViewById(R.id._invalidemailText);
        _invalidPasswordText = findViewById(R.id._invalidPasswordText);
        _forgotPassword = findViewById(R.id._forgotPassword);

        _loginIntro.setScaleX(0f);
        _loginIntro.setScaleY(0f);
        _loginIntro.animate().scaleX(1f).scaleY(1f).setDuration(1000).start();

        forwardAnimation = new CountDownTimer(3000,3000) {
            @Override
            public void onTick(long millisUntilFinished) {
                _loginGear.animate().rotation(180f).setDuration(3000);
            }

            @Override
            public void onFinish() {
                waitTime.start();
            }
        };

        waitTime = new CountDownTimer(1000,500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                backwardAnimation.start();
            }
        };

        backwardAnimation = new CountDownTimer(3000,3000) {
            @Override
            public void onTick(long millisUntilFinished) {
                _loginGear.animate().rotation(-180f).setDuration(3000);
            }

            @Override
            public void onFinish() {
                reverseWaitTime.start();
            }
        };

        reverseWaitTime = new CountDownTimer(1000,500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                forwardAnimation.start();
            }
        };

        forwardAnimation.start();

        _signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forwardAnimation.cancel();
                waitTime.cancel();
                backwardAnimation.cancel();
                reverseWaitTime.cancel();
                intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        _forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_loginEmail.getText().length()<1 && _loginPassword.getText().length()<1 ){
                    if (_loginEmail.getText().length()<1){
                        _invalidEmailText.setVisibility(TextView.VISIBLE);
                    }

                    if (_loginPassword.getText().length()<8){
                        _invalidPasswordText.setVisibility(TextView.VISIBLE);
                    }
                }
                else{
                    Dexter.withActivity(LoginActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            Dexter.withActivity(LoginActivity.this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {
                                    loginFromFirebase();
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                }
                            }).check();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        }
                    }).check();
                }
            }
        });
    }

    private void loginFromFirebase() {
        email = _loginEmail.getText().toString().trim();
        password = _loginPassword.getText().toString().trim();
        login = FirebaseAuth.getInstance();
        ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setTitle("Employee On Demand");
        dialog.setMessage("Logging In...");
        dialog.setIcon(R.drawable.empicon);
        dialog.setCancelable(false);
        dialog.show();

        try {
            login.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {


                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if ((task.isSuccessful())) {
                                forwardAnimation.cancel();
                                waitTime.cancel();
                                backwardAnimation.cancel();
                                reverseWaitTime.cancel();
                                FirebaseUser user = login.getCurrentUser();
                                FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(user.getUid()).child("badReview").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.getValue(Integer.class) < 5) {

                                            intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                                            dialog.dismiss();
                                            startActivity(intent);
                                            _loginEmail.setText("");
                                            _loginPassword.setText("");

                                            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                                                @Override
                                                public void onSuccess(String s) {
                                                    String token = s;
                                                    String uId = login.getCurrentUser().getUid();
                                                    HashMap<String, Object> hashMap = new HashMap<>();
                                                    hashMap.put("token", token);
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("Users").child(uId).updateChildren(hashMap);
                                                    String notificationId = FirebaseDatabase.getInstance().getReference().push().getKey();
                                                    String notificationMessage = "You have successfully login to your account";
                                                    //NotificationData notificationData = new NotificationData(notificationId, uId, "login", "Login Successfull", notificationMessage, false);
                                                    //FirebaseDatabase.getInstance().getReference().child("Notifications").child(uId).child(notificationId).setValue(notificationData);

                                                }
                                            });

                                        } else {
                                            Intent intent = new Intent(LoginActivity.this, BlockedProfileActivity.class);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            } else {
                                _invalidEmailText.setVisibility(TextView.VISIBLE);
                                _invalidPasswordText.setVisibility(TextView.VISIBLE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}