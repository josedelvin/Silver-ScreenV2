package com.example.silver_screen;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

public class OnboardingFragment extends Fragment {

    private static final String ARG_IMAGE_RESOURCE = "imageResource";
    private static final String ARG_IMAGE_URL = "imageUrl";
    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";

    private ImageView onboardingImage;
    private TextView onboardingTitle;
    private TextView onboardingDescription;
    private Button nextButton;

    private String imageUrl;
    private String title;
    private String description;

    public OnboardingFragment() {
        // Required empty public constructor
    }

    public static OnboardingFragment newInstance(int imageResource, String title, String description) {
        OnboardingFragment fragment = new OnboardingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE_RESOURCE, imageResource);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    public static OnboardingFragment newInstanceForLoading() {
        return new OnboardingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onboardingImage = view.findViewById(R.id.onboardingImage);
        onboardingTitle = view.findViewById(R.id.onboardingText);
        onboardingDescription = view.findViewById(R.id.onboardingDescription);
        nextButton = view.findViewById(R.id.nextButton);

        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(ARG_IMAGE_RESOURCE)) {
                int imageResource = args.getInt(ARG_IMAGE_RESOURCE);
                onboardingImage.setImageResource(imageResource);
            } else if (args.containsKey(ARG_IMAGE_URL)) {
                imageUrl = args.getString(ARG_IMAGE_URL);
                // Load image from URL using Picasso
                Picasso.get().load(imageUrl).into(onboardingImage);
            }

            title = args.getString(ARG_TITLE);
            description = args.getString(ARG_DESCRIPTION);

            onboardingTitle.setText(title);
            onboardingDescription.setText(description);
        }

        nextButton.setOnClickListener(view1 -> {
            // Move to the next onboarding page or navigate to the main activity
            if (getActivity() instanceof OnboardingActivity) {
                ((OnboardingActivity) getActivity()).switchToNextFragment();
            }
        });
    }


    // Method to update the fragment content dynamically
    public void updateData(String imageUrl, String title, String description) {
        Log.d("OnboardingFragment", "Updating data: " + imageUrl + ", " + title + ", " + description);
        if (onboardingImage == null) {
            Log.e("OnboardingFragment", "onboardingImage is null!");
        } else {
            Picasso.get().load(imageUrl).into(onboardingImage);
        }

        if (onboardingTitle != null) {
            onboardingTitle.setText(title);
        }

        if (onboardingDescription != null) {
            onboardingDescription.setText(description);
        }
    }
}
