package com.example.recommendationapi.models;

import java.util.ArrayList;
import java.util.List;

public class UserPreferences {
    public List<String> likedArtists = new ArrayList<>();
    public List<String> dislikedArtists = new ArrayList<>();
    public List<String> likedGenres = new ArrayList<>();
    public List<String> dislikedGenres = new ArrayList<>();
}
