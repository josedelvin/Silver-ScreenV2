package com.example.silver_screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MovieFragment extends Fragment implements Searchable {

    private RecyclerView movieRecyclerView;
    private MovieAdapter movieAdapter;
    private DatabaseReference movieDatabaseReference;

    public static MovieFragment newInstance(DatabaseReference databaseReference) {
        MovieFragment fragment = new MovieFragment();

        // Pass data or arguments to the fragment using a Bundle
        Bundle args = new Bundle();
        args.putString("databaseReference", databaseReference.toString());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        movieRecyclerView = view.findViewById(R.id.movieRecyclerView);

        // Initialize the Firebase Database reference
        movieDatabaseReference = FirebaseDatabase.getInstance().getReference("movies");

        // Initialize the adapter with the GridLayoutManager
        movieAdapter = new MovieAdapter(getMovieOptions(), new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                // Handle item click if needed
            }
        });

        movieRecyclerView.setAdapter(movieAdapter);

        GridLayoutManager movieLayoutManager = new GridLayoutManager(getContext(), 2);
        movieRecyclerView.setLayoutManager(movieLayoutManager);

        return view;
    }

    private FirebaseRecyclerOptions<Movie> getMovieOptions() {
        // Provide the appropriate query for ordering movies (adjust as needed)
        return new FirebaseRecyclerOptions.Builder<Movie>()
                .setQuery(movieDatabaseReference.orderByChild("rating").startAt(7.0).endAt(10.0).limitToLast(20), Movie.class)
                .build();
    }

    public void performSearch(String query) {
        // Implement search functionality specific to MovieFragment
        // Update the adapter with the new query
        updateAdapterWithQuery(query);
    }

    public void updateAdapterWithQuery(String query) {
        FirebaseRecyclerOptions<Movie> searchOptions = new FirebaseRecyclerOptions.Builder<Movie>()
                .setQuery(movieDatabaseReference.orderByChild("title").startAt(query).endAt(query + "\uf8ff"), Movie.class)
                .build();

        // Clear the adapter before updating with new data
        movieAdapter.clear();
        movieAdapter.updateOptions(searchOptions);
    }
    @Override
    public void onStart() {
        super.onStart();
        movieAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        movieAdapter.stopListening();
    }
}