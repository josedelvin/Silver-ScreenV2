package com.example.silver_screen;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TriviaQuestionModel implements Parcelable {
    private String answer;
    private String option_a;
    private String option_b;
    private String option_c;
    private String question;
    private int timer;

    // Default constructor
    public TriviaQuestionModel() {
        // Default constructor required for Firebase
    }

    // Constructor with parameters
    public TriviaQuestionModel(String answer, String option_a, String option_b, String option_c, String question, int timer) {
        this.answer = answer;
        this.option_a = option_a;
        this.option_b = option_b;
        this.option_c = option_c;
        this.question = question;
        this.timer = timer;
    }

    // Parcelable implementation
    protected TriviaQuestionModel(Parcel in) {
        answer = in.readString();
        option_a = in.readString();
        option_b = in.readString();
        option_c = in.readString();
        question = in.readString();
        timer = in.readInt();
    }

    public static final Creator<TriviaQuestionModel> CREATOR = new Creator<TriviaQuestionModel>() {
        @Override
        public TriviaQuestionModel createFromParcel(Parcel in) {
            return new TriviaQuestionModel(in);
        }

        @Override
        public TriviaQuestionModel[] newArray(int size) {
            return new TriviaQuestionModel[size];
        }
    };

    // Getters and setters


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getOption_a() {
        return option_a;
    }

    public void setOption_a(String option_a) {
        this.option_a = option_a;
    }

    public String getOption_b() {
        return option_b;
    }

    public void setOption_b(String option_b) {
        this.option_b = option_b;
    }

    public String getOption_c() {
        return option_c;
    }

    public void setOption_c(String option_c) {
        this.option_c = option_c;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(answer);
        dest.writeString(option_a);
        dest.writeString(option_b);
        dest.writeString(option_c);
        dest.writeString(question);
        dest.writeInt(timer);
    }


}

