package com.example.silver_screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Home extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private RecyclerView marvelRecyclerView;
    private RecyclerView bestmoviesRecyclerView;

    private RecyclerView actionmoviesRecyclerView;
    private RecyclerView comedymoviesRecyclerView;
    private ViewPager2 carouselViewPager;
    private MovieAdapter marvelAdapter;
    private MovieAdapter bestmoviesAdapter;

    private MovieAdapter comedyAdapter;

    private MovieAdapter actionAdapter;
    private CarouselAdapter carouselAdapter;
    private int currentPage = 0;
    private Timer timer;
    private final long DELAY_MS = 2000;
    private final long PERIOD_MS = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseReference = FirebaseDatabase.getInstance().getReference("movies");

        marvelRecyclerView = findViewById(R.id.marvelRecyclerView);
        bestmoviesRecyclerView = findViewById(R.id.bestmoviesRecyclerView);
        comedymoviesRecyclerView = findViewById(R.id.comedymoviesRecyclerView);
        actionmoviesRecyclerView = findViewById(R.id.actionmoviesRecyclerView);

        marvelRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        bestmoviesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        comedymoviesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        actionmoviesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false
        ));
        FirebaseRecyclerOptions<Movie> marvelOptions =
                new FirebaseRecyclerOptions.Builder<Movie>()
                        .setQuery(databaseReference.orderByChild("genre/Marvel").equalTo(true), Movie.class)
                        .build();

        FirebaseRecyclerOptions<Movie> bestmoviesOptions =
                new FirebaseRecyclerOptions.Builder<Movie>()
                        .setQuery(databaseReference.orderByChild("rating").startAt(8.7).endAt(10.0).limitToLast(5), Movie.class)
                        .build();

        FirebaseRecyclerOptions<Movie> comedyOptions =
                new FirebaseRecyclerOptions.Builder<Movie>()
                        .setQuery(databaseReference.orderByChild("genre/Comedy").equalTo(true).limitToFirst(20), Movie.class)
                        .build();

        FirebaseRecyclerOptions<Movie> actionoptions =
                new FirebaseRecyclerOptions.Builder<Movie>()
                        .setQuery(databaseReference.orderByChild("genre/Action").equalTo(true).limitToLast(20), Movie.class)
                        .build();

        marvelAdapter = new MovieAdapter(marvelOptions, new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                replaceWithMovieDetailsFragment(movie);
            }
        });

        bestmoviesAdapter = new MovieAdapter(bestmoviesOptions, new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                replaceWithMovieDetailsFragment(movie);
            }
        });
        comedyAdapter = new MovieAdapter(comedyOptions, new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                replaceWithMovieDetailsFragment(movie);
            }
        });

        actionAdapter = new MovieAdapter(actionoptions, new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                replaceWithMovieDetailsFragment(movie);
            }
        });

        marvelRecyclerView.setAdapter(marvelAdapter);
        bestmoviesRecyclerView.setAdapter(bestmoviesAdapter);
        comedymoviesRecyclerView.setAdapter(comedyAdapter);
        actionmoviesRecyclerView.setAdapter(actionAdapter);

        loadCarouselImagesFromDatabase();

        carouselViewPager = findViewById(R.id.carouselViewPager);
        carouselAdapter = new CarouselAdapter(new ArrayList<>(), this);
        carouselViewPager.setAdapter(carouselAdapter);


        startAutoScroll();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    // Already in Home page so no need to navigate
                    return true;
                } else if (itemId == R.id.search) {
                    // Navigate to DiscoverActivity
                    startActivity(new Intent(Home.this, DiscoverActivity.class));
                    return true;
                } else if (itemId == R.id.wishlist) {
                    // Navigate to MainTriviaPage
                    startActivity(new Intent(Home.this, MainTriviaPage.class));
                    return true;
                } else if (itemId == R.id.profile) {
                    // Navigate to ProfileActivity
                    startActivity(new Intent(Home.this, ProfileActivity.class));
                    return true;
                }

                return false; // Important for correct functioning
            }
        });
    }

    private void loadCarouselImagesFromDatabase() {
        DatabaseReference showsReference = FirebaseDatabase.getInstance().getReference("shows");

        showsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> imageUrls = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    if (imageUrl != null) {
                        imageUrls.add(imageUrl);
                    }
                }
                carouselAdapter.setImageUrls(imageUrls);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if needed
            }
        });
    }

    private void startAutoScroll() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (currentPage == carouselAdapter.getItemCount() - 1) {
                    currentPage = 0;
                } else {
                    currentPage++;
                }
                carouselViewPager.setCurrentItem(currentPage, true);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    private void replaceWithMovieDetailsFragment(Movie movie) {
        MovieDetailsFragment fragment = MovieDetailsFragment.newInstance(movie);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        marvelAdapter.startListening();
        bestmoviesAdapter.startListening();
        comedyAdapter.startListening();
        actionAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        marvelAdapter.stopListening();
        bestmoviesAdapter.stopListening();
        comedyAdapter.stopListening();
        actionAdapter.stopListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}
