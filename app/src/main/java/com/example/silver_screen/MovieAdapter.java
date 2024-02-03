package com.example.silver_screen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends FirebaseRecyclerAdapter<Movie, MovieAdapter.MovieViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    private OnItemClickListener listener;

    public MovieAdapter(@NonNull FirebaseRecyclerOptions<Movie> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }
    public void updateData(List<Movie> movies) {
        // Clear existing data
        getSnapshots().clear();

        // Add new data
        getSnapshots().addAll(movies);

        // Notify adapter about the change
        notifyDataSetChanged();
    }
    public void clear() {
        int itemCount = getItemCount();
        notifyItemRangeRemoved(0, itemCount);
    }

    @Override
    protected void onBindViewHolder(@NonNull MovieViewHolder holder, int position, @NonNull Movie model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view, this); // Pass the adapter instance
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private ImageView imageView;
        private MovieAdapter adapter;

        public MovieViewHolder(@NonNull View itemView, MovieAdapter adapter) {
            super(itemView);
            this.adapter = adapter;

            titleTextView = itemView.findViewById(R.id.titleTextView);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && adapter.listener != null) {
                        adapter.listener.onItemClick(adapter.getItem(position));
                    }
                }
            });
        }

        public void bind(Movie movie) {
            titleTextView.setText(movie.getTitle());
            Picasso.get().load(movie.getImageUrl()).into(imageView);
        }
    }
}
