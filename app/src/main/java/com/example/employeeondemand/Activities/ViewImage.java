package com.example.employeeondemand.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.employeeondemand.R;
import com.squareup.picasso.Picasso;

import java.security.acl.Group;

public class ViewImage extends AppCompatActivity {

    ImageView _viewImage, _viewChatImage;
    Intent intent;
    Bundle bundle;
    String imageUrl;
    View _view;
    TextView _imageText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        _viewImage = findViewById(R.id._viewImage);
        _viewChatImage = findViewById(R.id._viewImage);
        _view = findViewById(R.id._view);
        _imageText = findViewById(R.id._imageText);

        intent = getIntent();
        bundle = intent.getExtras();

        if (bundle.getString("tag").equals("cnic")) {
            imageUrl = bundle.getString("cnic");
            Picasso.with(ViewImage.this).load(imageUrl).placeholder(R.drawable.placeholder).into(_viewImage);
            _viewImage.setVisibility(ImageView.VISIBLE);
            _view.setVisibility(View.VISIBLE);
            _imageText.setVisibility(TextView.VISIBLE);
        }else{
            imageUrl = bundle.getString("chatImage");
            Picasso.with(ViewImage.this).load(imageUrl).into(_viewChatImage);
            _viewChatImage.setVisibility(ImageView.VISIBLE);
        }

    }
}

