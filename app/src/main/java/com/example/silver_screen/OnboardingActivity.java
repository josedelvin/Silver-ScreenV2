package com.example.silver_screen;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private OnboardingPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        adapter = new OnboardingPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Load data for the second fragment
        adapter.loadDataForSecondFragment();
    }

    public void switchToNextFragment() {
        if (viewPager.getCurrentItem() < viewPager.getAdapter().getItemCount() - 1) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        } else {
            startActivity(new Intent(OnboardingActivity.this, Home.class));
            finish();
        }
    }

    private class OnboardingPagerAdapter extends FragmentStateAdapter {

        private OnboardingActivity activity;

        OnboardingPagerAdapter(OnboardingActivity fragmentActivity) {
            super(fragmentActivity);
            this.activity = fragmentActivity;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return OnboardingFragment.newInstance(
                            R.mipmap.createprofile,
                            "Create your Profile",
                            "Complete your profile to help you find movies that will be right for you."
                    );
                case 1:
                    return OnboardingFragment.newInstanceForLoading(); // Loading state
                case 2:
                    return OnboardingFragment.newInstance(
                            R.mipmap.createprofile,
                            "Complete Profile",
                            "Now you're all set! Start exploring movies based on your preferences."
                    );
                default:
                    return new Fragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        // Load data for the second fragment
        public void loadDataForSecondFragment() {
            OnboardingFragment secondFragment = (OnboardingFragment) activity.getSupportFragmentManager().findFragmentByTag("f1");

            if (secondFragment != null) {
                // Fetch data from the database or use a default movie
                DatabaseReference moviesReference = FirebaseDatabase.getInstance().getReference("movies");
                moviesReference.orderByChild("rating").limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DataSnapshot firstMovieSnapshot = dataSnapshot.getChildren().iterator().next();
                            Movie movieFromDatabase = firstMovieSnapshot.getValue(Movie.class);
                            // Update the second fragment with the loaded data
                            secondFragment.updateData(
                                    movieFromDatabase.getImageUrl(),
                                    "Find your Favourite Movies!",
                                    "Discover the world of cinema at your fingertips."
                            );
                        } else {
                            // Handle the case when no movie is found
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors if needed
                    }
                });
            }
        }
    }
}
