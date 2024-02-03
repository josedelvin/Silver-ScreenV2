package com.example.silver_screen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    private EditText edtUpdatedName, edtUpdatedEmail;
    private Button btnSaveChanges;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Views
        edtUpdatedName = findViewById(R.id.edtUpdatedName);
        edtUpdatedEmail = findViewById(R.id.edtUpdatedEmail);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });
    }

    private void saveChanges() {
        String updatedName = edtUpdatedName.getText().toString().trim();
        String updatedEmail = edtUpdatedEmail.getText().toString().trim();

        // Check if the fields are not empty
        if (updatedName.isEmpty() || updatedEmail.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Update user information in Firebase
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

            userRef.child("username").setValue(updatedName);
            userRef.child("email").setValue(updatedEmail);


            // Show a success message
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

            setResult(RESULT_OK);
            finish();
        } else {
            // Handle the case where the user is not authenticated
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }
}
