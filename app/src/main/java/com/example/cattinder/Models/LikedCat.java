package com.example.cattinder.Models;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class LikedCat {
    public String imageUrl;
    public String name;

    public LikedCat() {
        // Default constructor required for calls to snapshot.toObjects(LikedCat.class)
    }

    public LikedCat(String imageUrl, String name) {
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }
}
