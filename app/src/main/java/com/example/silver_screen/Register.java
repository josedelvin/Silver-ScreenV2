package com.example.silver_screen;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button registerBtn;
    private FirebaseAuth mAuth;

     //If user is already logged in will automatically redirect to Home page
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(getApplication(), Home.class);
            startActivity(i);
            finish();
        }
    }
    // user password validation
    private boolean isPasswordValid(String password) {
        // It should contain at least one digit, one lowercase letter, one uppercase letter, and one special character and 6 characters long
//        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$";
//        return password.matches(passwordRegex);
        // Password must be 6 characters long
        return password.length() >= 6;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // firebase auth initialization
        mAuth = FirebaseAuth.getInstance();
        editTextUsername = findViewById(R.id.username);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPassword = findViewById(R.id.confirmPassword);
        registerBtn = findViewById(R.id.registerButton);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Register buton animations

                String username, email, password, confirmPassword;

                try {
                    username = String.valueOf(editTextUsername.getText());
                    email = String.valueOf(editTextEmail.getText());
                    password = String.valueOf(editTextPassword.getText());
                    confirmPassword = String.valueOf(editTextConfirmPassword.getText());


                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!isPasswordValid(password)) {
                        Toast.makeText(getApplicationContext(), "Your password should contain at least one digit, one lowercase letter, one uppercase letter, and one special character", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (password.equals(confirmPassword)) {
                        mAuth.createUserWithEmailAndPassword(email, password)
                             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Get the UID of the newly created user
                                            String uid = mAuth.getCurrentUser().getUid();

                                            // Save user data to the database with UID
                                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

                                            User newUser = new User(uid, username, email, password, null,null); // Originally set to NUll
                                            usersRef.setValue(newUser);

                                            // Sign in success, update UI with the signed-in user's information
                                            Toast.makeText(Register.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                            startActivity(intent);
//                                            finish();
                                            // Sign in the user After Successfull registration
                                            mAuth.signInWithEmailAndPassword(email, password)
                                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> signInTask) {
                                                            if (signInTask.isSuccessful()) {
                                                                // Successfully signed in after registration
                                                                Log.d(TAG, "signInWithEmail:success");
                                                                Intent intent = new Intent(getApplicationContext(), OnboardingActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                // If sign-in fails, display a message to the user.
                                                                Log.w(TAG, "signInWithEmail:failure", signInTask.getException());
                                                                Toast.makeText(Register.this, "Authentication after registration failed.",
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(Register.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });


    }
}