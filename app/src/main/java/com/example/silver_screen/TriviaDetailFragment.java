package com.example.silver_screen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.util.List;


public class TriviaDetailFragment extends Fragment {

    private TextView title, difficulty, totalQuestions;
    private Button startTriviaBtn;
    private int position;
    private NavController navController;

    private TriviaListViewModel viewModel;
    private ImageView movieImage;
    private String triviaId;
    private long totalQnsCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trivia_detail, container, false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(TriviaListViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.triviaDetailTitle);
        difficulty = view.findViewById(R.id.triviaDetailDifficulty);
        totalQuestions = view.findViewById(R.id.triviaDetailTotalQuestions);
        startTriviaBtn = view.findViewById(R.id.startTriviaBtn);
        movieImage = view.findViewById(R.id.triviaDetailImage);

        navController = Navigation.findNavController(view);

        position = TriviaDetailFragmentArgs.fromBundle(getArguments()).getPosition();

        viewModel.getTriviaListLiveData().observe(getViewLifecycleOwner(), new Observer<List<TriviaListModel>>() {
            @Override
            public void onChanged(List<TriviaListModel> triviaListModels) {
                TriviaListModel trivia = triviaListModels.get(position);
                difficulty.setText(trivia.getDifficulty());
                title.setText(trivia.getTitle());
                totalQuestions.setText(String.valueOf(trivia.getQuestions()));
                Glide.with(view).load(trivia.getImage()).into(movieImage);

                totalQnsCount = trivia.getQuestions();
                triviaId = trivia.getTriviaId();


            }
        });

        startTriviaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TriviaDetailFragmentDirections.ActionTriviaDetailFragmentToTriviaFragment intent =
                        TriviaDetailFragmentDirections.actionTriviaDetailFragmentToTriviaFragment();

                intent.setTriviaId(triviaId);
                intent.setTotalQnsCount(totalQnsCount);
                navController.navigate(intent);


            }
        });
    }


}