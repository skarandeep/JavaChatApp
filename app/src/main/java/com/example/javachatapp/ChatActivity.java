package com.example.javachatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String receiverImage, receiverUID, receiverName;
    CircleImageView profileImage;
    TextView recipientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //following strings are the keys inserted in the Adapter
        receiverName = getIntent().getStringExtra("name");
        receiverImage = getIntent().getStringExtra("receiver image");
        receiverUID = getIntent().getStringExtra("uid");

        //now go to layout

        profileImage = findViewById(R.id.profile_image);
        recipientName = findViewById(R.id.recipient_name);

        Picasso.get().load(receiverImage).into(profileImage);
        recipientName.setText("" + receiverName);

    }
}