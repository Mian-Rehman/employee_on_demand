package com.example.employeeondemand.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employeeondemand.Models.NotificationData;
import com.example.employeeondemand.Models.SendNotification;
import com.example.employeeondemand.R;
import com.example.employeeondemand.Models.Userdata;
import com.example.employeeondemand.UserMapActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;



import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity  {

    ImageView imageView,_profileimage, _signupBackButton;
    TextView _fname, _lname, _cnicNo, _dateOfBirth,_loginText;
    EditText _eMail,_password,_phoneNo, _ratePerDay, _sikllsDetails, _address;
    Uri imagefilepath,proPicFilePath;
    Bitmap bitmap;
    String token, uId, imageText, username, lName,cnicNo,gender,dateOfBirthString,province,
            city,address,profession, eMail,password,phoneNo,skillDetails,emailpattern,
            errorMessage, rating,ratePerDay,earned;
    int badReview;
    Pattern pattern;
    Matcher matcher;
    Spinner _gender, _province,_city,_profession;
    Button _signUpButton;
    ArrayAdapter<CharSequence> adapter,professionAdapter;
    Userdata userdata;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Intent userProfileActivity;
    ArrayList<Uri> imageList;
    private FirebaseAuth mAuth;
    public static Uri cnicPicUri;
    public static int ratioX,ratioY;
    public static int accountFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        _signupBackButton = findViewById(R.id._signupBackButton);
        imageView = findViewById(R.id._homeTutor);
        _profileimage = findViewById(R.id._profileimage);
        _fname = findViewById(R.id._fName);
        _lname = findViewById(R.id._lName);
        _cnicNo = findViewById(R.id._cnicNo);
        _dateOfBirth = findViewById(R.id._dateOfBirth);
        _gender = findViewById(R.id._gender);
        _eMail = findViewById(R.id._eMail);
        _password = findViewById(R.id._password);
        _phoneNo = findViewById(R.id._phoneNo);
        _province = findViewById(R.id._province);
        _city = findViewById(R.id._city);
        _address = findViewById(R.id._address);
        _profession = findViewById(R.id._profession);
        _ratePerDay = findViewById(R.id._ratePerDay);
        _sikllsDetails = findViewById(R.id._skillsDetails);
        _signUpButton = findViewById(R.id._signUpButton);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        errorMessage = "";
        skillDetails = " ";

        emailpattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$";

        _signupBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        _fname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                captureImage(3,2);
            }
        });
        _profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage(1,1);
            }
        });


        adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, R.layout.spinner_style_file);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _gender.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.province_array, R.layout.spinner_style_file);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _province.setAdapter(adapter);
        _province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                province = _province.getSelectedItem().toString();
                if (i==0){
                    adapter = ArrayAdapter.createFromResource(SignUpActivity.this,
                            R.array.punjab_array, R.layout.spinner_style_file);
                }
                else if (i==1){
                    adapter = ArrayAdapter.createFromResource(SignUpActivity.this,
                            R.array.balochistan_array, R.layout.spinner_style_file);
                }
                else if (i==2){
                    adapter = ArrayAdapter.createFromResource(SignUpActivity.this,
                            R.array.kpk_array, R.layout.spinner_style_file);
                }
                else if (i==3){
                    adapter = ArrayAdapter.createFromResource(SignUpActivity.this,
                            R.array.sindh_array, R.layout.spinner_style_file);
                }
                else{
                    adapter = ArrayAdapter.createFromResource(SignUpActivity.this,
                            R.array.punjab_array, R.layout.spinner_style_file);
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _city.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        professionAdapter= ArrayAdapter.createFromResource(this,
                R.array.profession_array, R.layout.spinner_style_file);
        professionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _profession.setAdapter(professionAdapter);

        _signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = _fname.getText().toString().trim();
                eMail = _eMail.getText().toString().trim();
                password = _password.getText().toString().trim();

                if (username == "Enter First Name"
                        && username.length() < 1
                        && _lname.getText().length()<1
                        && _cnicNo.getText().length()<1
                        && _dateOfBirth.getText().length()<1
                        &&_eMail.getText().length() < 1
                        && _password.getText().length() < 1
                        && _phoneNo.getText().length() < 1
                        && _ratePerDay.getText().length() < 1
                        && _address.getText().length()<1) {

                    Toast.makeText(SignUpActivity.this, "Enter All Values", Toast.LENGTH_SHORT).show();

                } else if (username != "Enter First Name" && username.length()>1 && _lname.getText().length()>1 && _cnicNo.getText().length()>1 && _dateOfBirth.getText().length()>1
                        && eMail.matches(emailpattern) && password.length() >= 8 && _ratePerDay.getText().length() > 1 && _address.getText().length()>1) {

                    uploadToFirebase();

                } else {
                    if (password.length() < 8) {

                        errorMessage = errorMessage + "Password must be greater than 8 characters\n";

                    }

//                    if (eMail.matches(emailpattern)) {
//
//                    } else {
//
//                        errorMessage = errorMessage + "Invalid e-mail\n";
//
//                    }
                    Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void testData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this, "saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void captureImage(int x, int y) {
        Dexter.withActivity(SignUpActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                ratioX=x;
                ratioY=y;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(ratioX,ratioY)
                        .start(SignUpActivity.this);

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(SignUpActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(ratioX==3 && ratioY==2) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    imagefilepath = result.getUri();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imagefilepath);
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        imageView.setImageBitmap(bitmap);
                        FirebaseVisionImage visionImage = FirebaseVisionImage.fromFilePath(getApplicationContext(), imagefilepath);
                        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
                        textRecognizer.processImage(visionImage)
                                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                    @Override
                                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                        imageText = "";
                                        for (FirebaseVisionText.TextBlock textBlock : firebaseVisionText.getTextBlocks()) {
                                            for (FirebaseVisionText.Line textline : textBlock.getLines()) {
                                                imageText = imageText + textline.getText().toString();
                                            }
                                        }
                                        userDataArangement(imageText);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    proPicFilePath = result.getUri();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(proPicFilePath);
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        _profileimage.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }

    }

    private void userDataArangement(String imageText) {
        username = lName =cnicNo="";
        try {
            //for Name String...
            pattern = Pattern.compile("Name(.*?)Father Name");
            matcher = pattern.matcher(imageText);
            while (matcher.find()) {
                username = matcher.group(1);
            }
            _fname.setText("" + username);
        } catch (Exception exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try {
            //for Father Name String...
            pattern = pattern.compile("Father Name(.*?)Gender");
            matcher = pattern.matcher(imageText);
            while (matcher.find()) {
                lName = matcher.group(1);
            }
            _lname.setText("" + lName);
        } catch (Exception exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //for cnic no...
        try {
            pattern = Pattern.compile("-(.*?)-");
            matcher = pattern.matcher(imageText);
            while (matcher.find()) {
                cnicNo = matcher.group();
            }
            String stringpattern = "Date of Birth(.*?)"+cnicNo;
            pattern = Pattern.compile(stringpattern);
            matcher = pattern.matcher(imageText);
            while (matcher.find()){
                cnicNo = matcher.group();
            }
            cnicNo = cnicNo.substring(cnicNo.length()-14, cnicNo.length());
            stringpattern = cnicNo+"(.*?)Date of Expiry";
            pattern = Pattern.compile(stringpattern);
            matcher = pattern.matcher(imageText);
            while (matcher.find()){
                cnicNo = matcher.group();
            }
            cnicNo = cnicNo.substring(0, 15);

            //Set the value of cnicNo...
            _cnicNo.setText(""+cnicNo);
            //for Date of Birth...
            try {
                stringpattern = cnicNo+"(.*?)Date of Expiry";
                pattern = Pattern.compile(stringpattern);
                matcher = pattern.matcher(imageText);
                while (matcher.find()){
                    dateOfBirthString = matcher.group(1);
                }
                dateOfBirthString = dateOfBirthString.substring(0 , 11);
                //set the value of Date of Birth...
                _dateOfBirth.setText(""+dateOfBirthString);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private void uploadToFirebase() {
        username = _fname.getText().toString().trim();
        lName = _lname.getText().toString().trim();
        cnicNo = _cnicNo.getText().toString().trim();
        gender = _gender.getSelectedItem().toString().trim();
        dateOfBirthString = _dateOfBirth.getText().toString().trim();
        eMail = _eMail.getText().toString().trim();
        password = _password.getText().toString().trim();
        phoneNo = _phoneNo.getText().toString().trim();
        province = _province.getSelectedItem().toString().trim();
        address = _address.getText().toString().trim();
        city = _city.getSelectedItem().toString().trim();
        profession = _profession.getSelectedItem().toString().trim();
        ratePerDay = _ratePerDay.getText().toString().trim();
        skillDetails =skillDetails + _sikllsDetails.getText().toString().trim();
        rating = "0";
        earned = "0";
        badReview = 0;


            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Signing Up");
            dialog.show();
            if(proPicFilePath.toString().length()>0) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(eMail, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    imageList = new ArrayList<>();
                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference uploadfile = storage.getReference().child("Profile").child("ProfilePic" + new Random());
                                    uploadfile.putFile(proPicFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            uploadfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri profileUri) {
                                                    StorageReference uploadcnicfile = storage.getReference().child("Cnic").child("CNICPic" + new Random());
                                                    uploadcnicfile.putFile(imagefilepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                            uploadcnicfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri CnicUri) {
                                                                    uId = mAuth.getCurrentUser().getUid().toString();
                                                                    userdata = new Userdata(uId, cnicNo, username, lName, eMail, password, gender, dateOfBirthString, phoneNo, province, city, address, profession, skillDetails, profileUri.toString(), CnicUri.toString(), rating, ratePerDay, earned, badReview, token);
                                                                    databaseReference.child("Users").child(uId).setValue(userdata);

                                                                    FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                                                                        @Override
                                                                        public void onSuccess(String s) {
                                                                            token = s;
                                                                            uId = mAuth.getCurrentUser().getUid();
                                                                            HashMap<String, Object> hashMap = new HashMap<>();
                                                                            hashMap.put("token", token);
                                                                            databaseReference.child("Users").child(uId).updateChildren(hashMap);
                                                                            String notificationId = FirebaseDatabase.getInstance().getReference().push().getKey();
                                                                            String notificationMessage = "Your account has been successfully registered";
                                                                            NotificationData notificationData = new NotificationData(notificationId, uId, "signUp", "Registration Successfull", notificationMessage, true);
                                                                            FirebaseDatabase.getInstance().getReference().child("Notifications").child(uId).child(notificationId).setValue(notificationData);

                                                                            SendNotification sendNotification = new SendNotification("Registration Successfull", notificationMessage, token, uId, SignUpActivity.this);
                                                                            sendNotification.sendNotification();
                                                                        }
                                                                    });


                                                                    userProfileActivity = new Intent(SignUpActivity.this, UserMapActivity.class);
                                                                    dialog.dismiss();
                                                                    startActivity(userProfileActivity);

                                                                }
                                                            });
                                                        }
                                                    });

                                                    _fname.setText("Enter First Name");
                                                    _lname.setText("Enter Last Name");
                                                    _cnicNo.setText("Enter CNIC No");
                                                    _dateOfBirth.setText("Date of Birth");
                                                    _eMail.setText("");
                                                    _password.setText("");
                                                    _phoneNo.setText("");
                                                    _sikllsDetails.setText("");
                                                    _address.setText("");
                                                    _ratePerDay.setText("");
                                                    _sikllsDetails.setText("");
                                                    _profileimage.setImageResource(R.drawable.innerprofileico);

                                                    Toast.makeText(SignUpActivity.this, "Signup Successfull!", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                            float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                            dialog.setMessage("Upload :" + (int) percent + "%");
                                        }
                                    });
                                }
                            }
                        });

            }else {
                Toast.makeText(SignUpActivity.this, "Profile not selected!", Toast.LENGTH_SHORT).show();
            }


    }
}