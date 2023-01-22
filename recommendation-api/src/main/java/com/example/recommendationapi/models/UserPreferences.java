package com.example.recommendationapi.models;

import java.util.ArrayList;
import java.util.List;

public class UserPreferences {
    public List<String> likedArtists = new ArrayList<>();
    public List<String> dislikedArtists = new ArrayList<>();
    public List<String> likedGenres = new ArrayList<>();
    public List<String> dislikedGenres = new ArrayList<>();
    public List<DatePreference> datePerArtist = new ArrayList<>();
    public List<String> likedYears = new ArrayList<>();
    public List<String> dislikedYears = new ArrayList<>();
    public Integer recommendationLimit = 0;

    public class DatePreference {
        public String artist;
        public String startYear;
        public String endYear;
    }
}
