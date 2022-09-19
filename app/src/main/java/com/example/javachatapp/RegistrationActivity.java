package com.example.javachatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    TextView signIn;
    CircleImageView profileImage;
    EditText regName, regEmail, regPassword, regConfirmPassword;
    Button btnSignUp;
    FirebaseAuth auth;
    Uri imageUri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String imageStrURI;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //creating instance of authorization variable and same for database and for storage
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        signIn = findViewById(R.id.txt_sign_in);
        profileImage = findViewById(R.id.profile_image);
        regName = findViewById(R.id.reg_name);
        regEmail = findViewById(R.id.reg_email);
        regPassword = findViewById(R.id.reg_password);
        regConfirmPassword = findViewById(R.id.reg_confirm_password);
        btnSignUp = findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = regName.getText().toString();
                String email = regEmail.getText().toString();
                String password = regPassword.getText().toString();
                String confirmPassword = regConfirmPassword.getText().toString();

                //data validation conditions
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(RegistrationActivity.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                } else if(!email.matches(emailPattern)){
                    regEmail.setError("Please enter valid email");
                    Toast.makeText(RegistrationActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                } else if(!password.equals(confirmPassword)){
                    Toast.makeText(RegistrationActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else if(password.length() < 6){
                    regPassword.setError("Password should be longer than 6 characters");
                    Toast.makeText(RegistrationActivity.this, "Password cannot be less than 6 characters", Toast.LENGTH_SHORT).show();
                } else {

                    //create new user when clicked
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                               // Toast.makeText(RegistrationActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                                //storing user data in the database

                                //make a database reference utilizing the database instances created at the beginning
                                //creating data table in Firebase for user
                                //and authentication id
                                //TODO understanding
                                DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
                                //storage reference
                                StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

                                //if username stored, checking if imageURI is empty or not. if it is null, we add it to storage reference

                                if(imageUri != null){
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                            //if uploading the image to database was successful
                                            //add on success listener instead of listener on TASK

                                            if(task.isSuccessful()){
                                                //TODO OnSuccessListener
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        //Uri is now listed above
                                                        //store obtained data into Image String URI variable
                                                        imageStrURI = uri.toString();

                                                        //now we have name, email, password and confirm password as well as image URI resource
                                                        //Create an Object class Users.java
                                                        // Ctrl + P to find all variables required in the object class

                                                        Users users = new Users(auth.getUid(), name, email, imageStrURI);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    //if task is successful send user to homepage in chat, short version of intent statement used below
                                                                    startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                                                } else {
                                                                    Toast.makeText(RegistrationActivity.this, "Error creating New User", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else {
                                    //here if user did not select a specific image for their login credentials, assign Firebase default user image (access token)

                                    imageStrURI = "https://firebasestorage.googleapis.com/v0/b/javachatapp-84fb5.appspot.com/o/profile.png?alt=media&token=1cc76bf4-85b7-4a3b-890e-d2cfd2f6b51f";

                                    Users users = new Users(auth.getUid(), name, email, imageStrURI);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                //if task is successful send user to homepage in chat, short version of intent statement used below
                                                startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                            } else {
                                                Toast.makeText(RegistrationActivity.this, "Error creating New User", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            } else {
                                Toast.makeText(RegistrationActivity.this, "Something doesn't feel right!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });

        //creating click event for profile image
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick image from gallery Android stackoverflow, replace requestCode with 10 as a reference number
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // ActivityResultContract(Intent.createChooser(intent,"select an image"));
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);

            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                //TODO finish();
            }
        });

    }
    //Gallery opens and user selects their preferred image, onActivityResult

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //
        if(requestCode==10){
            if(data!=null){
                //setup new Unique Resource Interface variable
                //TODO is this interface?
                imageUri = data.getData();
                profileImage.setImageURI(imageUri);
            }
        }
    }
}