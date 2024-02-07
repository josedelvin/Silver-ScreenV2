package com.example.silver_screen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class TriviaResultFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private NavController navController;

    private TextView triviaResultTextView;
    private ProgressBar scoreProgressBar;
    private TextView triviaScoreTextView;
    private TextView correctAnsResultTextView;
    private TextView wrongAnsResultTextView;
    private TextView emptyAnsResultTextView;
    private Button backToHomeBtn;

    public TriviaResultFragment() {
        // Required empty public constructor
    }
    public static TriviaResultFragment newInstance(String param1, String param2) {
        TriviaResultFragment fragment = new TriviaResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize NavController
        navController = Navigation.findNavController(view);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trivia_result, container, false);

        // Initialize your TextViews, ProgressBar, and Button
        triviaResultTextView = view.findViewById(R.id.triviaResult);
        scoreProgressBar = view.findViewById(R.id.scoreProgressBar);
        triviaScoreTextView = view.findViewById(R.id.triviaScore);
        correctAnsResultTextView = view.findViewById(R.id.correctAnsResult);
        wrongAnsResultTextView = view.findViewById(R.id.wrongAnsResult);
        emptyAnsResultTextView = view.findViewById(R.id.emptyAnsResult);
        backToHomeBtn = view.findViewById(R.id.backToHomeBtn);

        // Retrieve the results array from Safe Args
        int[] results = TriviaResultFragmentArgs.fromBundle(getArguments()).getResult();

        // Set the texts and progress
        setTriviaResultText(results[0]);
        setProgressBar(results[0]);
        setTriviaScoreText(results[0]);
        setCorrectAnsResultText(results[0]);
        setWrongAnsResultText(results[1]);
        setEmptyAnsResultText(results[2]);

        // Handle button click
        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if navController is not null before navigating
                if (navController != null) {
                    navController.navigate(R.id.action_triviaResultFragment_to_triviaListFragment);
                } else {
                    Log.e("TriviaResultFragment", "NavController is null");
                }
            }
        });

        return view;
    }

    private void setTriviaResultText(int correctAnswers) {
        if (correctAnswers < 5) {
            triviaResultTextView.setText("Are you sure you even watched these movies? 🎬");
        } else {
            triviaResultTextView.setText("We got a fan I see! 🎉");
        }
    }

    private void setProgressBar(int correctAnswers) {
        // Set progress based on correct answers
        int progress = correctAnswers * 10;
        scoreProgressBar.setProgress(progress);
    }

    private void setTriviaScoreText(int correctAnswers) {
        // Calculate the percentage or use any other scoring logic
        int totalQuestions = 10;
        int percentage = (correctAnswers * 100) / totalQuestions;

        // Set the trivia score text
        triviaScoreTextView.setText(percentage + "%");
    }

    private void setCorrectAnsResultText(int correctAnswers) {
        correctAnsResultTextView.setText(String.valueOf(correctAnswers));
    }

    private void setWrongAnsResultText(int wrongAnswers) {
        wrongAnsResultTextView.setText(String.valueOf(wrongAnswers));
    }

    private void setEmptyAnsResultText(int emptyAnswers) {
        emptyAnsResultTextView.setText(String.valueOf(emptyAnswers));
    }
}