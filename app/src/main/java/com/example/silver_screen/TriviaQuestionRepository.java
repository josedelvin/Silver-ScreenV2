package com.example.silver_screen;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TriviaQuestionRepository {

    private final OnQuestionLoad onQuestionLoad;
    private String triviaId;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public TriviaQuestionRepository(OnQuestionLoad onQuestionLoad) {
        this.onQuestionLoad = onQuestionLoad;
    }

    public void getTriviaQuestions() {
        String completeTriviaId = "T" + triviaId;
        DatabaseReference triviaQuestionsReference = databaseReference.child("TriviaList").child(completeTriviaId).child("qn");

        triviaQuestionsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseData", "DataSnapshot: " + dataSnapshot.toString());

                // Process the data directly within this method
                if (dataSnapshot.exists()) {
                    List<TriviaQuestionModel> questionList = new ArrayList<>();

                    for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                        TriviaQuestionModel triviaQuestion = questionSnapshot.getValue(TriviaQuestionModel.class);
                        if (triviaQuestion != null) {
                            questionList.add(triviaQuestion);
                        }
                    }

                    if (!questionList.isEmpty()) {
                        onQuestionLoad.onLoad(questionList);
                        Log.d("TriviaQuestionRepository", "Trivia Questions Loaded: " + questionList.toString());
                    } else {
                        onQuestionLoad.onError(new Exception("No questions found for the given triviaId"));
                        Log.e("TriviaQuestionRepository", "Error: No questions found for the given triviaId");
                    }
                } else {
                    onQuestionLoad.onError(new Exception("No questions found for the given triviaId"));
                    Log.e("TriviaQuestionRepository", "Error: No questions found for the given triviaId");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onQuestionLoad.onError(databaseError.toException());
                Log.e("TriviaQuestionRepository", "Error: " + databaseError.getMessage());
            }
        });
    }

    public void setTriviaId(String triviaId) {
        this.triviaId = triviaId;
    }

    public interface OnResultLoad{
        void onResultLoad(HashMap<String , Long> resultMap);
        void onError(Exception e);
    }


    public interface OnQuestionLoad {
        void onLoad(List<TriviaQuestionModel> questionList);
        void onError(Exception e);
    }
}
