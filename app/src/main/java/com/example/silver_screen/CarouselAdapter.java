package com.example.silver_screen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {

    private List<String> imageUrls;
    private Context context;

    public CarouselAdapter(List<String> imageUrls, Context context) {
        this.imageUrls = imageUrls;
        this.context = context;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_item, parent, false);
        return new CarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);

        // Load image into ImageView using Picasso or your preferred image loading library
        Picasso.get().load(imageUrl).into(holder.imageView);

        // Set onClickListener for "Watchlist" button
        holder.watchlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle "Watchlist" button click
                // You can implement the desired behavior, e.g., adding the show to the user's watchlist
            }
        });

        // Set onClickListener for "Details" button
        holder.detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle "Details" button click
                // You can implement the desired behavior, e.g., opening a details screen for the show
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class CarouselViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView watchlistButton;
        TextView detailsButton;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.carouselImageView);
            watchlistButton = itemView.findViewById(R.id.watchlistButton);
            detailsButton = itemView.findViewById(R.id.detailsButton);
        }
    }
}
