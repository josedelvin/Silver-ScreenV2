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

public class ShowAdapter extends FirebaseRecyclerAdapter<Show, ShowAdapter.ShowViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Show show);
    }

    private OnItemClickListener listener;

    public ShowAdapter(@NonNull FirebaseRecyclerOptions<Show> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    public void updateData(List<Show> shows) {
        // Clear existing data
        getSnapshots().clear();

        // Add new data
        getSnapshots().addAll(shows);

        // Notify adapter about the change
        notifyDataSetChanged();
    }

    public void clear() {
        int itemCount = getItemCount();
        notifyItemRangeRemoved(0, itemCount);
    }

    @Override
    protected void onBindViewHolder(@NonNull ShowViewHolder holder, int position, @NonNull Show model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_item, parent, false);
        return new ShowViewHolder(view, this); // Pass the adapter instance
    }

    static class ShowViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private ImageView imageView;
        private ShowAdapter adapter;

        public ShowViewHolder(@NonNull View itemView, ShowAdapter adapter) {
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

        public void bind(Show show) {
            titleTextView.setText(show.getTitle());
            Picasso.get().load(show.getImageUrl()).into(imageView);
        }
    }
}
