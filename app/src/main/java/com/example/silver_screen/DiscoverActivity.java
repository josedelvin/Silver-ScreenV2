    package com.example.silver_screen;

    import android.content.Intent;
    import android.content.res.ColorStateList;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.util.Log;
    import android.view.MenuItem;
    import android.view.View;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.SearchView;
    import androidx.core.content.ContextCompat;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.GridLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.firebase.ui.database.FirebaseRecyclerOptions;
    import com.google.android.material.bottomnavigation.BottomNavigationView;
    import com.google.android.material.navigation.NavigationBarView;
    import com.google.android.material.tabs.TabLayout;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;

    public class DiscoverActivity extends AppCompatActivity{

        private TabLayout tabLayout;
        private Fragment currentFragment;
        private DatabaseReference databaseReference;
        private DatabaseReference showDatabaseReference;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_discover);

            tabLayout = findViewById(R.id.tabLayout);

            // Initialize the Firebase Database reference
            databaseReference = FirebaseDatabase.getInstance().getReference("movies");
            showDatabaseReference = FirebaseDatabase.getInstance().getReference("shows");

            // Setup SearchView
            SearchView searchView = findViewById(R.id.search_bar);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // Handle the search query when the user submits
                    performSearch(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // Handle the search query as the user types
                    performSearch(newText);
                    return true;
                }
            });

            // Setup tabs for "Movies" and "Shows"
            tabLayout.addTab(tabLayout.newTab().setText("Movies"));
            tabLayout.addTab(tabLayout.newTab().setText("Shows"));

            // Set a TabLayout.OnTabSelectedListener
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    updateAdapterWithTab(tab.getText().toString());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    // Handle tab unselection if needed
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    updateAdapterWithTab(tab.getText().toString());
                }
            });
            updateAdapterWithTab("Movies");

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();

                    if (itemId == R.id.home) {
                        // Navigate to HomeActivity
                        startActivity(new Intent(DiscoverActivity.this, Home.class));
                        return true;
                    } else if (itemId == R.id.search) {
                        // Already on DiscoverActivity, no need to navigate
                        return true;
                    } else if (itemId == R.id.wishlist1) {
                        // Navigate to MainTriviaPage
                        startActivity(new Intent(DiscoverActivity.this, WishlistActivity.class));
                        return true;
                    } else if (itemId == R.id.wishlist) {
                        // Navigate to MainTriviaPage
                        startActivity(new Intent(DiscoverActivity.this, MainTriviaPage.class));
                        return true;
                    } else if (itemId == R.id.profile) {
                        // Navigate to ProfileActivity
                        startActivity(new Intent(DiscoverActivity.this, ProfileActivity.class));
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
                    ContextCompat.getColor(DiscoverActivity.this, R.color.yellow),
                    ContextCompat.getColor(DiscoverActivity.this, R.color.yellow)
            };

            ColorStateList colorStateList = new ColorStateList(states, colors);
            bottomNavigationView.setItemIconTintList(colorStateList);
            bottomNavigationView.setSelectedItemId(R.id.search);

        }


        public void performSearch(String query) {
            // Check if the current fragment is not null and implements the Searchable interface
            if (currentFragment instanceof Searchable) {
                ((Searchable) currentFragment).performSearch(query);
            }
        }

        private void updateAdapterWithTab(String tabName) {
            // Adjust the database reference based on the selected tab
            DatabaseReference tabReference;

            if (tabName.equals("Movies")) {
                // Load MovieFragment into the fragmentContainer
                MovieFragment movieFragment = MovieFragment.newInstance(databaseReference);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, movieFragment)
                        .commit();
                currentFragment = movieFragment;
            } else if (tabName.equals("Shows")) {
                // Load ShowFragment into the fragmentContainer
                ShowFragment showFragment = ShowFragment.newInstance(showDatabaseReference);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, showFragment)
                        .commit();
                currentFragment = showFragment;
            }
        }


        private FirebaseRecyclerOptions<Movie> getBestMoviesOptions() {
            // Provide the appropriate query for ordering movies (adjust as needed)
            return new FirebaseRecyclerOptions.Builder<Movie>()
                    .setQuery(databaseReference.orderByChild("rating").startAt(7.0).endAt(10.0).limitToLast(20), Movie.class)
                    .build();
        }

        private FirebaseRecyclerOptions<Show> getAllShowsOptions() {
            // Provide the appropriate query for ordering shows (adjust as needed)
            return new FirebaseRecyclerOptions.Builder<Show>()
                    .setQuery(showDatabaseReference.orderByChild("releaseDate").limitToLast(20), Show.class)
                    .build();
        }



        @Override
        protected void onStart() {
            super.onStart();
        }

        @Override
        protected void onStop() {
            super.onStop();
        }

    }