package com.example.silver_screen;

import java.util.List;

public class User {

    private String uid;
    private String username;
    private String email;
    private String password;
    private String url;

    private List<String> wishlist;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid, String username, String email, String password, String url,  List<String> wishlist) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.url = url;
        this.wishlist = wishlist;
    }

    // Getter methods
    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    // Setter methods
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getWishlist() {
        return wishlist;
    }
    public void setWishlist(List<String> wishlist) {
        this.wishlist = wishlist;
    }


}
