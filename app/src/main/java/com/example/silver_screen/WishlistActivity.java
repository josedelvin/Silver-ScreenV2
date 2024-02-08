package com.example.silver_screen;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class WishlistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container2, new WishlistFragment())
                .commit();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    // Navigate to HomeActivity
                    startActivity(new Intent(WishlistActivity.this, Home.class));
                    return true;
                } else if (itemId == R.id.search) {
                    // Navigate to DiscoverActivity
                    startActivity(new Intent(WishlistActivity.this, DiscoverActivity.class));
                    return true;
                } else if (itemId == R.id.wishlist1) {
                    // Already on wishlistactivity
                    return true;
                }
                else if (itemId == R.id.wishlist) {
                    startActivity(new Intent(WishlistActivity.this, MainTriviaPage.class));
                    return true;
                } else if (itemId == R.id.profile) {
                    // Navigate to ProfileActivity
                    startActivity(new Intent(WishlistActivity.this, ProfileActivity.class));
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
                ContextCompat.getColor(WishlistActivity.this, R.color.yellow),
                ContextCompat.getColor(WishlistActivity.this, R.color.yellow)
        };

        ColorStateList colorStateList = new ColorStateList(states, colors);
        bottomNavigationView.setItemIconTintList(colorStateList);

        bottomNavigationView.setSelectedItemId(R.id.wishlist1);

    }


}