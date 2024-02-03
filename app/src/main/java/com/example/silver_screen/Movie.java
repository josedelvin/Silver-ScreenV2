package com.example.silver_screen;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.List;
import java.util.Map;

public class Movie implements Parcelable {
    private String titleLowerCase;
    private String title;

    private String imageUrl;

    private String trailerUrl;
    private String description;
    private List<String> director;
    private String duration;
    private Map<String, Boolean> genre;
    private String language;
    private double rating;
    private String releasedate;
    private List<String> actors;
    private List<String> awards;

    // Default constructor required for Firebase
    public Movie() {
    }

    // Constructor with parameters
    public Movie(String title, String imageUrl, String description, List<String> director, String duration,
                 Map<String, Boolean> genre, String language, double rating, String release_date,
                 List<String> actors, List<String> awards, String trailerUrl) {
        this.title = title;
        this.titleLowerCase = title.toLowerCase();
        this.imageUrl = imageUrl;
        this.description = description;
        this.director = director;
        this.duration = duration;
        this.genre = genre;
        this.language = language;
        this.rating = rating;
        this.releasedate  = release_date;
        Log.d("MovieConstructor", "Release Date: " + release_date);
        this.actors = actors;
        this.awards = awards;
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

    public List<String>  getDirector() {
        return director;
    }

    public String getDuration() {
        return duration;
    }

    public Map<String, Boolean> getGenre() {
        return genre;
    }

    public String getLanguage() {
        return language;
    }

    public double getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releasedate;
    }

    public List<String> getActors() {
        return actors;
    }

    public List<String>  getAwards() {
        return awards;
    }

    // Setter methods (if needed)


    // Parcelable implementation
    protected Movie(Parcel in) {
        title = in.readString();
        imageUrl = in.readString();
        description = in.readString();
        director = in.createStringArrayList();
        duration = in.readString();
        genre = in.readHashMap(Boolean.class.getClassLoader());
        language = in.readString();
        rating = in.readDouble();
        releasedate = in.readString();
        actors = in.createStringArrayList();
        awards = in.createStringArrayList();
        trailerUrl=in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
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
        dest.writeStringList(director);
        dest.writeString(duration);
        dest.writeMap(genre);
        dest.writeString(language);
        dest.writeDouble(rating);
        dest.writeString(releasedate);
        dest.writeStringList(actors);
        dest.writeStringList(awards);
        dest.writeString(trailerUrl);
    }
}