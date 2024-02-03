package com.example.silver_screen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

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
        ImageView closeImageView = view.findViewById(R.id.closeImageView);

        titleTextView.setText(movie.getTitle());
        descriptionTextView.setText(movie.getDescription());
        releaseDateTextView.setText(movie.getReleaseDate());
        durationTextView.setText("⏱︎ " + movie.getDuration());
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

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Initialize WebView
        WebView webView = view.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient());

        // Load YouTube video
        webView.loadUrl("https://www.youtube.com/embed/" + getVideoIdFromUrl(movie.getTrailerUrl()));
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
            String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200C2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(url);
            if (matcher.find()) {
                videoId = matcher.group();
            }
        }
        return videoId;
    }
}
