package com.example.silver_screen;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;


public class TriviaListViewModel extends ViewModel implements TriviaListRepository.OnTriviaTaskComplete {
    private MutableLiveData<List<TriviaListModel>> triviaListLiveData = new MutableLiveData<>();

    private TriviaListRepository repository = new TriviaListRepository(this);

    public MutableLiveData<List<TriviaListModel>> getTriviaListLiveData() {
        return triviaListLiveData;
    }

    public TriviaListViewModel() {
        repository.getTriviaData();
    }

//    public void onLoad(List<TriviaListModel> triviaListModels) {
//        triviaListLiveData.setValue(triviaListModels);
//    }

public void setTriviaId(String TriviaId) {
        repository.setTriviaId(TriviaId);
}

    @Override
    public void triviaDataLoaded(List<TriviaListModel> triviaListModels) {
        triviaListLiveData.setValue(triviaListModels);

    }

    @Override
    public void onError(Exception e) {
        Log.d("TriviaERROR", "onError: " + e.getMessage());
    }
}
