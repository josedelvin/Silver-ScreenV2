package com.example.silver_screen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment {

    private List<Movie> wishlistMovies;
    private WishlistAdapter wishlistAdapter; // Declare WishlistAdapter variable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wishlist, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewWishlist);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wishlistMovies = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewWishlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext())); // Set LayoutManager for RecyclerView
        wishlistAdapter = new WishlistAdapter(wishlistMovies);
        recyclerView.setAdapter(wishlistAdapter);

        // Load user's wishlist from Firebase Realtime Database
        loadWishlistFromDatabase();
    }

    private void loadWishlistFromDatabase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            List<String> userWishlist = user.getWishlist();
                            if (userWishlist != null) {
                                for (String movieTitle : userWishlist) {
                                    loadMovieDetailsFromDatabase(movieTitle);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }
    }

    private void loadMovieDetailsFromDatabase(String movieTitle) {
        DatabaseReference moviesRef = FirebaseDatabase.getInstance().getReference("movies");

        moviesRef.orderByChild("title").equalTo(movieTitle).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int startPosition = wishlistMovies.size();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Movie movie = snapshot.getValue(Movie.class);
                        if (movie != null && !wishlistMovies.contains(movie)) {
                            wishlistMovies.add(movie);
                            Log.d("WishlistFragment", "Added movie: " + movie.getTitle());
                        }
                    }
                    // Notify the adapter of item insertion at specific positions
                    int itemCount = wishlistMovies.size() - startPosition;
                    if (itemCount > 0) {
                        wishlistAdapter.notifyItemRangeInserted(startPosition, itemCount);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("WishlistFragment", "Error loading movie details: " + databaseError.getMessage());
            }
        });
    }


    // Inner class for RecyclerView Adapter
    private static class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

        private final List<Movie> wishlistMovies;

        public WishlistAdapter(List<Movie> wishlistMovies) {
            this.wishlistMovies = wishlistMovies;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wishlist, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Movie movie = wishlistMovies.get(position);
            Picasso.get()
                    .load(movie.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            int itemCount = wishlistMovies.size();
            Log.d("WishlistAdapter", "getItemCount: " + itemCount);
            return itemCount;

        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
            }

        }

    }

}
