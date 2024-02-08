package com.example.silver_screen;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserPreferences {

    private static final String PREFERENCE_NAME = "user_preferences";
    private static final String KEY_WISHLIST = "wishlist";

    // Save the wishlist to SharedPreferences
    public static void setWishlist(Context context, String uid, List<Movie> wishlist) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Convert the list to a JSON string
        Gson gson = new Gson();
        String wishlistJson = gson.toJson(wishlist);

        // Save the JSON string to SharedPreferences
        editor.putString(uid + KEY_WISHLIST, wishlistJson);
        editor.apply();
    }

    // Retrieve the wishlist from SharedPreferences
    public static List<Movie> getWishlist(Context context, String uid) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        // Retrieve the JSON string from SharedPreferences
        String wishlistJson = preferences.getString(uid + KEY_WISHLIST, "");

        // Convert the JSON string back to a list of movies
        Gson gson = new Gson();
        Type type = new TypeToken<List<Movie>>() {}.getType();
        return gson.fromJson(wishlistJson, type);
    }
}