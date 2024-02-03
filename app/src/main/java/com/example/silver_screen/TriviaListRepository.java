package com.example.silver_screen;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TriviaListRepository {

    private OnTriviaTaskComplete onTriviaTaskComplete;

    private String triviaId;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("TriviaList");

    public TriviaListRepository(OnTriviaTaskComplete onTriviaTaskComplete) {
        this.onTriviaTaskComplete = onTriviaTaskComplete;
    }

    public void setTriviaId(String triviaId) {
        this.triviaId = triviaId;
    }

    public void getTriviaData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<TriviaListModel> triviaListModels = new ArrayList<>();
                for (DataSnapshot triviaSnapshot : dataSnapshot.getChildren()) {
                    TriviaListModel triviaModel = triviaSnapshot.getValue(TriviaListModel.class);
                    if (triviaModel != null) {
                        triviaListModels.add(triviaModel);
                    }
                }
                onTriviaTaskComplete.triviaDataLoaded(triviaListModels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onTriviaTaskComplete.onError(databaseError.toException());
            }
        });
    }

    public interface OnTriviaTaskComplete {
        void triviaDataLoaded(List<TriviaListModel> triviaListModels);
        void onError(Exception e);
    }
}
