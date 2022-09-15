package com.example.javachatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView txtSignUp;
    EditText loginEmail, loginPassword;
    Button btnSignIn;
    FirebaseAuth auth;
    //using email regex from StackOverflow
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        btnSignIn = findViewById(R.id.btn_sign_in);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if the user already exists after user inputs credentials
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                //checking if credentials are empty

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Fields cannot be blank", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    //if email entered does not match specified email pattern
                    //TODO what is this following statement?
                    loginEmail.setError("Please enter valid email");
                    Toast.makeText(LoginActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    loginPassword.setError("Password should be at least 6 characters");
                    Toast.makeText(LoginActivity.this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //checking if task successful, then send user to next activity i.e. chatroom
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                //TODO finish()???
                            } else {
                                Toast.makeText(LoginActivity.this, "Error logging in!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        txtSignUp = findViewById(R.id.txt_signup);
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                //TODO ? finish();
            }
        });
    }
}