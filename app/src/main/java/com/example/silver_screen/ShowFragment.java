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

public class ShowFragment extends Fragment implements Searchable {


    private RecyclerView showRecyclerView;
    private ShowAdapter showAdapter;
    private DatabaseReference showDatabaseReference;

    public static ShowFragment newInstance(DatabaseReference databaseReference) {
        ShowFragment fragment = new ShowFragment();

        // Pass data or arguments to the fragment using a Bundle
        Bundle args = new Bundle();
        args.putString("databaseReference", databaseReference.toString());
        fragment.setArguments(args);

        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);

        showRecyclerView = view.findViewById(R.id.showRecyclerView);

        // Initialize the Firebase Database reference
        showDatabaseReference = FirebaseDatabase.getInstance().getReference("shows");

        // Initialize the adapter with the GridLayoutManager
        showAdapter = new ShowAdapter(getShowOptions(), new ShowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Show show) {
                // Handle item click if needed
            }
        });

        showRecyclerView.setAdapter(showAdapter);

        GridLayoutManager showLayoutManager = new GridLayoutManager(getContext(), 2);
        showRecyclerView.setLayoutManager(showLayoutManager);

        return view;
    }

    private FirebaseRecyclerOptions<Show> getShowOptions() {
        // Provide the appropriate query for ordering shows (adjust as needed)
        return new FirebaseRecyclerOptions.Builder<Show>()
                .setQuery(showDatabaseReference.orderByChild("releaseDate").limitToLast(20), Show.class)
                .build();
    }

    public void performSearch(String query) {
        // Implement search functionality specific to ShowFragment
        // Update the adapter with the new query
        updateAdapterWithQuery(query);
    }

    public void updateAdapterWithQuery(String query) {
        FirebaseRecyclerOptions<Show> searchOptions = new FirebaseRecyclerOptions.Builder<Show>()
                .setQuery(showDatabaseReference.orderByChild("title").startAt(query).endAt(query + "\uf8ff"), Show.class)
                .build();

        // Clear the adapter before updating with new data
        showAdapter.clear();
        showAdapter.updateOptions(searchOptions);
    }

    @Override
    public void onStart() {
        super.onStart();
        showAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        showAdapter.stopListening();
    }
}

