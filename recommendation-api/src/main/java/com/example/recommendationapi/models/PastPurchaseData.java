package com.example.recommendationapi.models;

import java.util.ArrayList;
import java.util.List;

public class PastPurchaseData {

    private String title;
    private List<String> artistsName;
    private List<String> genres;
    private Integer releaseYear;


    public Integer getReleaseYear() {
        return releaseYear;
    }

    public List<String> getArtistsName() {

        return artistsName;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setArtistsName(List<String> artistsName) {
        this.artistsName = artistsName;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "PastPurchaseData{" +
                "title='" + title + '\'' +
                ", artistName='" + artistsName + '\'' +
                ", genre=" + genres +
                ", releaseYear=" + releaseYear +
                '}';
    }
}
