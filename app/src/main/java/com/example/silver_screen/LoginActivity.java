package com.example.silver_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEdt, passEdt;

    private TextView signUpTextView;
    private Button loginBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdt = findViewById(R.id.editTextUsername);
        passEdt = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.loginBtn);
        signUpTextView = findViewById(R.id.signUpTextViewBtn);


        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateEmail() && validatePassword()) {
                    checkUser();
                }
            }
        });
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Home.class);
                startActivity(intent);
            }
        });
    }

    public Boolean validateEmail() {
        String val = emailEdt.getText().toString();
        if (val.isEmpty()) {
            emailEdt.setError("Email cannot be empty");
            return false;
        } else {
            emailEdt.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = passEdt.getText().toString();
        if (val.isEmpty()) {
            passEdt.setError("Password cannot be empty");
            return false;
        } else {
            passEdt.setError(null);
            return true;
        }
    }



    public void checkUser() {
        String userEmail = emailEdt.getText().toString().trim();
        String userPassword = passEdt.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();

                            saveUserUid(uid);

                            String nameFromDB = user.getDisplayName();
                            String emailFromDB = user.getEmail();
                            String usernameFromDB = user.getEmail();  // Using email as username

                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            intent.putExtra("name", nameFromDB);
                            intent.putExtra("email", emailFromDB);
                            intent.putExtra("username", usernameFromDB);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "User information not available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserUid(String uid) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userUid", uid);
        editor.apply();
    }
}