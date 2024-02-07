package com.example.silver_screen;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private ImageView ivUserProfile;
    private TextView tvUserName, tvUserEmail, tvNotif;
    private HorizontalScrollView hsvWatchlistProfile;
    private Switch swDarkMode, swNotif;
    private Button btnLogout;
    private Button btnEditProfile;

    // SharedPreferences for saving user preferences
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Views
        ivUserProfile = findViewById(R.id.ivUserProfile);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvNotif = findViewById(R.id.tvNotif);
        swNotif = findViewById(R.id.swNotif);
        btnLogout = findViewById(R.id.btnLogout);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        // Initialize SharedPreferences
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        // Set user-specific data (replace with your data)
        ivUserProfile.setImageResource(R.mipmap.dummyprofile); // Replace with actual user profile image
        tvUserName.setText("User Name"); // Replace with actual user name

        // Set up watchlist items (replace with actual data and layouts)
        //setupWatchlist();

        fetchUserData();
        setupNotificationsSwitch();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();

            }
        });


        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivityForResult(intent, 1); // Use any positive integer as the requestCode
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            public boolean onNavigationItemSelected(@androidx.annotation.NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    // Navigate to HomeActivity
                    startActivity(new Intent(ProfileActivity.this, Home.class));
                    return true;
                } else if (itemId == R.id.search) {
                    // Navigate to DiscoverActivity
                    startActivity(new Intent(ProfileActivity.this, DiscoverActivity.class));
                    return true;
                }
                    else if (itemId == R.id.wishlist1) {
                        // Navigate to MainTriviaPage
                        startActivity(new Intent(ProfileActivity.this, WishlistActivity.class));
                        return true;
                } else if (itemId == R.id.wishlist) {
                    startActivity(new Intent(ProfileActivity.this, MainTriviaPage.class));
                    return true;
                } else if (itemId == R.id.profile) {
                    // Already in Profile page so no need to navigate
                    return true;
                }

                return false;
            }
        });

        // Customize colors for selected and unselected states
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_selected },
                new int[] { -android.R.attr.state_selected }
        };

        int[] colors = new int[] {
                ContextCompat.getColor(ProfileActivity.this, R.color.yellow),
                ContextCompat.getColor(ProfileActivity.this, R.color.yellow)
        };

        ColorStateList colorStateList = new ColorStateList(states, colors);
        bottomNavigationView.setItemIconTintList(colorStateList);

        bottomNavigationView.setSelectedItemId(R.id.profile);

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            fetchUserData();
        }
    }
    private void setupNotificationsSwitch() {
        // Set the initial state of the Notifications switch based on the saved preference
        boolean notificationsEnabled = sharedPreferences.getBoolean("notifications", true);
        swNotif.setChecked(notificationsEnabled);

        swNotif.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveNotificationsPreference(isChecked);

            if (isChecked) {
                showTestNotification();
            }
        });
    }

    private String getUserUid() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getString("userUid", "");
    }

    private void fetchUserData() {
        String uid = getUserUid();

        if (!uid.isEmpty()) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        updateUIWithUserData(user);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error if necessary
                }
            });
        } else {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null) {
                String currentUid = currentUser.getUid();

                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUid);

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // User data found, update UI
                            User user = dataSnapshot.getValue(User.class);
                            updateUIWithUserData(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        }
    }

    private void updateUIWithUserData(User user) {
        if (user != null) {
            tvUserName.setText(user.getUsername());

            if (user.getEmail() != null) {
                tvUserEmail.setText(user.getEmail());
            }
        }
    }

    private void showTestNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "channel_id";
        CharSequence channelName = "Channel Name";

        // Notification ID
        int notificationId = 1;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);

        // Create a Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Test Notification")
                .setContentText("This is a test notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(notificationId, builder.build());
    }



    private void saveNotificationsPreference(boolean notificationsEnabled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("notifications", notificationsEnabled);
        editor.apply();
    }
}

