package com.example.silver_screen;

import android.os.Parcel;
import android.os.Parcelable;

public class TriviaListModel implements Parcelable {
    private String triviaId;
    private String title;
    private String image;
    private String difficulty;
    private long questions;

    // Default constructor required for Firebase
    public TriviaListModel() {
    }

    // Constructor with parameters
    public TriviaListModel(String triviaId, String title, String image, String difficulty, long questions) {
        this.triviaId = triviaId;
        this.title = title;
        this.image = image;
        this.difficulty = difficulty;
        this.questions = questions;
    }

    // Getter methods
    public String getTriviaId() {
        return triviaId;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public long getQuestions() {
        return questions;
    }

    // Parcelable implementation
    protected TriviaListModel(Parcel in) {
        triviaId = in.readString();
        title = in.readString();
        image = in.readString();
        difficulty = in.readString();
        questions = in.readLong();
    }

    public static final Creator<TriviaListModel> CREATOR = new Creator<TriviaListModel>() {
        @Override
        public TriviaListModel createFromParcel(Parcel in) {
            return new TriviaListModel(in);
        }

        @Override
        public TriviaListModel[] newArray(int size) {
            return new TriviaListModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(triviaId);
        dest.writeString(title);
        dest.writeString(image);
        dest.writeString(difficulty);
        dest.writeLong(questions);
    }
}
