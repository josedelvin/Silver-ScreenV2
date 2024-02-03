package com.example.silver_screen;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class TriviaQuestionViewModel extends ViewModel implements TriviaQuestionRepository.OnQuestionLoad {

    private MutableLiveData<List<TriviaQuestionModel>> triviaMutableLiveData;
    private TriviaQuestionRepository triviaQuestionRepository;

    public MutableLiveData<List<TriviaQuestionModel>> getTriviaMutableLiveData() {
        return triviaMutableLiveData;
    }

    public TriviaQuestionViewModel() {
        triviaMutableLiveData = new MutableLiveData<>();
        triviaQuestionRepository = new TriviaQuestionRepository(this);
    }

    public void setTriviaId(String triviaId){
        triviaQuestionRepository.setTriviaId(triviaId);
        triviaQuestionRepository.getTriviaQuestions();;
    }

    public void getQuestions(){
        triviaQuestionRepository.getTriviaQuestions();
    }

    @Override
    public void onLoad(List<TriviaQuestionModel> questionModel) {
        triviaMutableLiveData.setValue(questionModel);
    }

//    @Override
//    public void onLoad(TriviaQuestionModel questionModel) {
//        triviaMutableLiveData.setValue(questionModel);
//    }

    @Override
    public void onError(Exception e) {
        Log.d("QuizError", "onError: " + e.getMessage());
    }
}
