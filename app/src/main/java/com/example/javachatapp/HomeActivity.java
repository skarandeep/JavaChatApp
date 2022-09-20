package com.example.javachatapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    //adding Firebase authentication
    //don't miss instantiating the Firebase authentication
    FirebaseAuth auth;
    //defining and assigning RecyclerView for binding
    RecyclerView userRecyclerView;
    //adapter
    UserAdapter userAdapter;
    FirebaseDatabase database;
    //arrayList to store the data fetched from firebase database
    ArrayList<Users> usersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // getWindow().getDecorView().setBackgroundColor(0xffff0000);

        auth = FirebaseAuth.getInstance();
        //again creating reference object first and then assigning reference
        database = FirebaseDatabase.getInstance();

        //allocating memory for new array list of user details
        usersArrayList = new ArrayList<>();

        //as we nee to get data from user, we select child - User from firebase database
        DatabaseReference reference = database.getReference().child("user");

        //run add Value Event Listener on the defined reference
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            //for each loop to get
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    //get data by instantiating users object
                    Users users = dataSnapshot.getValue(Users.class);
                    //adding users details into the ArrayList
                    usersArrayList.add(users);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userRecyclerView = findViewById(R.id.recycler_user_main);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //making new adapter
        UserAdapter adapter = new UserAdapter(HomeActivity.this, usersArrayList);
        userRecyclerView.setAdapter(userAdapter);

        //check if user is logged in
        if(auth.getCurrentUser() == null){
            //send user to registration activity
            Intent intent = new Intent(HomeActivity.this, RegistrationActivity.class);
            startActivity(intent);
            //TODO check if you need to finish();
        }
    }
}