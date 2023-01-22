package com.example.recommendationapi.models;

import java.util.ArrayList;
import java.util.List;

public class UserPreferences {
    public List<String> likedArtists = new ArrayList<>();
    public List<String> dislikedArtists = new ArrayList<>();
    public List<String> likedGenres = new ArrayList<>();
    public List<String> dislikedGenres = new ArrayList<>();
    public List<DatePreference> datePerArtist = new ArrayList<>();
    public Integer startYear = 0;
    public Integer endYear = 0;
    public Integer limit = 0;
    public Integer pageSize = 5;
    public Integer pageIndex = 1;

    public class DatePreference {
        public String artist;
        public Integer startYear;
        public Integer endYear;
    }
}
