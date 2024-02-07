package com.example.silver_screen;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieDetailsFragment extends DialogFragment {

    // Use newInstance method to pass data to the fragment
    public static MovieDetailsFragment newInstance(Movie movie) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Access data using getArguments()
        Movie movie = getArguments().getParcelable("movie");

        // Set data to UI components
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
        TextView durationTextView = view.findViewById(R.id.durationTextView);
        TextView ratingTextView = view.findViewById(R.id.ratingTextView);
        TextView releaseDateTextView = view.findViewById(R.id.releaseDateTextView);
        TextView actorsTextView = view.findViewById(R.id.actorsTextView);

        titleTextView.setText(movie.getTitle());
        descriptionTextView.setText(movie.getDescription());
        releaseDateTextView.setText(movie.getReleaseDate());
        durationTextView.setText("⏱︎ " + movie.getDuration());
        actorsTextView.setText(movie.getActors().toString());
        ratingTextView.setText("★ " + movie.getRating());

        ChipGroup chipGroupGenres = view.findViewById(R.id.chipGroupGenres);
        for (Map.Entry<String, Boolean> entry : movie.getGenre().entrySet()) {
            if (entry.getValue()) {
                // Create a Chip for each genre
                Chip chip = new Chip(requireContext());
                chip.setText(entry.getKey());
                chip.setChipBackgroundColorResource(R.color.chipBackground);
                chip.setTextColor(getResources().getColor(R.color.chipText));
                chip.setChipCornerRadius(getResources().getDimension(R.dimen.chipCornerRadius));
                chip.setCloseIconVisible(false); // Set to true if you want to enable close icon
                chipGroupGenres.addView(chip);
            }
        }



        // Initialize WebView
        WebView webView = view.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient());

        // Load YouTube video
        webView.loadUrl("https://www.youtube.com/embed/" + getVideoIdFromUrl(movie.getTrailerUrl()));

        // "Add to Wishlist" button
        Button addToWishlistButton = view.findViewById(R.id.addToWishlistButton);
        addToWishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the user is logged in
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() != null) {
                    // User is logged in
                    // Retrieve user's wishlist from SharedPreferences
                    List<Movie> wishlist = UserPreferences.getWishlist(requireContext(), mAuth.getUid());

                    if (wishlist == null) {
                        // If wishlist is null, initialize a new ArrayList
                        wishlist = new ArrayList<>();
                    }

                    // Check if the movie is already in the wishlist
                    if (!wishlist.contains(movie)) {
                        // Add the movie to the wishlist
                        wishlist.add(movie);

                        // Update user's wishlist in SharedPreferences
                        UserPreferences.setWishlist(requireContext(), mAuth.getUid(), wishlist);

                        // Update user's wishlist in Firebase Realtime Database
                        updateWishlistInDatabase(mAuth.getUid(), wishlist);

                        Toast.makeText(requireContext(), "Added to Wishlist", Toast.LENGTH_SHORT).show();

                        // After adding to wishlist, replace the current fragment with WishlistFragment

                    } else {
                        Toast.makeText(requireContext(), "Movie already in Wishlist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // User is not logged in
                    Toast.makeText(requireContext(), "Please log in to add to Wishlist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void updateWishlistInDatabase(String uid, List<Movie> wishlist) {
        if (uid != null && wishlist != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid);

            // Check if the "wishlist" node exists
            databaseReference.child("wishlist").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        // "wishlist" node doesn't exist, create it
                        databaseReference.child("wishlist").setValue(new HashMap<String, Boolean>())
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        // Wishlist created, now add movies
                                        addToWishlist(uid, wishlist);
                                    } else {
                                        Log.e("MovieDetailsFragment", "Error creating wishlist", task.getException());
                                    }
                                });
                    } else {
                        // Wishlist already exists, add movies
                        addToWishlist(uid, wishlist);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("MovieDetailsFragment", "Database error: " + databaseError.getMessage());
                }
            });
        } else {
            Log.e("MovieDetailsFragment", "UID or Wishlist is null. Not updating in the database.");
        }
    }

    private void addToWishlist(String uid, List<Movie> wishlist) {
        DatabaseReference wishlistReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("wishlist");

        wishlistReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Object> wishlistTitles = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    wishlistTitles.add(snapshot.getValue(String.class));
                }

                // Check if the title already exists in the wishlist
                for (Movie movie : wishlist) {
                    if (!wishlistTitles.contains(movie.getTitle())) {
                        wishlistTitles.add(movie.getTitle());
                    }
                }

                // Update the wishlist in the database
                wishlistReference.setValue(wishlistTitles)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("MovieDetailsFragment", "Wishlist updated in the database");

                                // Show notification
                            } else {
                                Log.e("MovieDetailsFragment", "Error updating wishlist in the database", task.getException());
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MovieDetailsFragment", "Database error: " + databaseError.getMessage());
            }
        });
    }










    private String listToString(List<String> list) {
        StringBuilder result = new StringBuilder();
        for (String item : list) {
            result.append(item).append(", ");
        }
        return result.length() > 2 ? result.substring(0, result.length() - 2) : "";
    }

    private String mapToString(Map<String, Boolean> map) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Boolean> entry : map.entrySet()) {
            if (entry.getValue()) {
                result.append(entry.getKey()).append(", ");
            }
        }
        return result.length() > 2 ? result.substring(0, result.length() - 2) : "";
    }

    private String getVideoIdFromUrl(String url) {
        String videoId = null;
        if (url != null && url.trim().length() > 0) {
            String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\\u200C\\u200C2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(url);
            if (matcher.find()) {
                videoId = matcher.group();
            }
        }
        return videoId;
    }
}