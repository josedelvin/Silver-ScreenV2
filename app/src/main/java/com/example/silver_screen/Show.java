package com.example.silver_screen;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.List;
import java.util.Map;

public class Show implements Parcelable {
    private String titleLowerCase;
    private String title;

    private String imageUrl;

    private String trailerUrl;
    private String description;
    private int episodes;
    private Map<String, Boolean> genre;
    private String language;
    private String releasedate;
    private int seasons;

    // Default constructor required for Firebase
    public Show() {
    }

    // Constructor with parameters
    public Show(String title, String imageUrl, String description, int episodes, Map<String, Boolean> genre,
                String language, String releasedate, int seasons, String trailerUrl) {
        this.title = title;
        this.titleLowerCase = title.toLowerCase();
        this.imageUrl = imageUrl;
        this.description = description;
        this.episodes = episodes;
        this.genre = genre;
        this.language = language;
        this.releasedate = releasedate;
        this.seasons = seasons;
        this.trailerUrl = trailerUrl;
    }

    // Getter methods
    public String getTitle() {
        return title;
    }

    public String getTitleLowerCase() {
        return titleLowerCase;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public String getDescription() {
        return description;
    }

    public int getEpisodes() {
        return episodes;
    }

    public Map<String, Boolean> getGenre() {
        return genre;
    }

    public String getLanguage() {
        return language;
    }

    public String getReleaseDate() {
        return releasedate;
    }

    public int getSeasons() {
        return seasons;
    }

    // Parcelable implementation
    protected Show(Parcel in) {
        title = in.readString();
        imageUrl = in.readString();
        description = in.readString();
        episodes = in.readInt();
        genre = in.readHashMap(Boolean.class.getClassLoader());
        language = in.readString();
        releasedate = in.readString();
        seasons = in.readInt();
        trailerUrl = in.readString();
    }

    public static final Creator<Show> CREATOR = new Creator<Show>() {
        @Override
        public Show createFromParcel(Parcel in) {
            return new Show(in);
        }

        @Override
        public Show[] newArray(int size) {
            return new Show[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeString(description);
        dest.writeInt(episodes);
        dest.writeMap(genre);
        dest.writeString(language);
        dest.writeString(releasedate);
        dest.writeInt(seasons);
        dest.writeString(trailerUrl);
    }
}
