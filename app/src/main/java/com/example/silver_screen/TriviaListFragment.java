package com.example.silver_screen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

public class TriviaListFragment extends Fragment implements TriviaListAdapter.OnItemClickedListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private NavController navController;
    private TriviaListViewModel triviaListViewModel;
    private TriviaListAdapter triviaListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trivia_list, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        triviaListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()))
                .get(TriviaListViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.triviaListRecyclerView);
        progressBar = view.findViewById(R.id.triviaListProgressBar);
        navController = Navigation.findNavController(view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        triviaListAdapter = new TriviaListAdapter(new TriviaListAdapter.OnItemClickedListener() {
//            @Override
//            public void onItemClick(int position) {
//            }
//        });

        triviaListAdapter = new TriviaListAdapter(this);

//        triviaListAdapter = new TriviaListAdapter(this);
        recyclerView.setAdapter(triviaListAdapter);

        triviaListViewModel.getTriviaListLiveData().observe(getViewLifecycleOwner(), new Observer<List<TriviaListModel>>() {
            @Override
            public void onChanged(List<TriviaListModel> quizListModels) {
                progressBar.setVisibility(View.GONE);
                triviaListAdapter.setTriviaListModels(quizListModels);
                triviaListAdapter.notifyDataSetChanged();
            }
        });
    }



    @Override
    public void onItemClick(int position) {
    TriviaListFragmentDirections.ActionTriviaListFragmentToTriviaDetailFragment intent =
            TriviaListFragmentDirections.actionTriviaListFragmentToTriviaDetailFragment();
        intent.setPosition(position);
        navController.navigate(intent);
    }



}
