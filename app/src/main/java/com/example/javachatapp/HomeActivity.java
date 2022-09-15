package com.example.javachatapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    //adding Firebase authentication
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // getWindow().getDecorView().setBackgroundColor(0xffff0000);

        //check if user is logged in
        if(auth.getCurrentUser() == null){
            //send user to registration activity
            Intent intent = new Intent(HomeActivity.this, RegistrationActivity.class);
            startActivity(intent);
            //TODO check if you need to finish();
        }
    }
}