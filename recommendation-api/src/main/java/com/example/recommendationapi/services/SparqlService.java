package com.example.recommendationapi.services;

import com.example.recommendationapi.models.SparqlQuery;
import com.example.recommendationapi.models.SparqlResponse;
import com.example.recommendationapi.models.UserPreferences;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SparqlService {

    private final SparqlQueryBuilder queryBuilder;
    private static final String VINYL = "vinyl";
    private static final String ARTIST = "artist";
    private static final String GENRE = "genre";
    private static final String RELEASEDATE = "releaseDate";

    @Inject
    public SparqlService(SparqlQueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    public SparqlResponse GetRecommendationByPreferences(UserPreferences userPreferences) {
        List<String> likedGenres = new ArrayList<>();
        if (userPreferences.likedArtists.size() != 0) {
            likedGenres.addAll(GetGenresByLikedArtists(userPreferences.likedArtists));
        }
        likedGenres.addAll(userPreferences.likedGenres);
        likedGenres = likedGenres.stream()
                .distinct()
                .collect(Collectors.toList());

        queryBuilder.ResetQuery();

        AddGeneralPrefixes();
        AddSelectAllFields();
        AddWhereBindingsAllFields();
        AddFilters(userPreferences.dislikedArtists, userPreferences.dislikedGenres,
                userPreferences.likedArtists, likedGenres,
                userPreferences.startYear, userPreferences.endYear);

        queryBuilder.CloseWhere();

        SparqlQuery query = userPreferences.limit == 0
                ? queryBuilder.Build()
                : queryBuilder.AddLimit(userPreferences.limit)
                .Build();

        return query.SendRequest();
    }

    public SparqlResponse GetTopByCount(String field, Integer limit) {
        queryBuilder.ResetQuery();
        queryBuilder
                .AddRdfPrefix()
                .AddSparqlResultPrefix()
                .AddXsdPrefix()
                .AddSelect()
                .AddSelectedField(field)
                .AddSelectedCountField(VINYL, "recordsCount")
                .AddWhere()
                .AddBindingVariableInWhere(VINYL)
                .AddBindingVariableInWhere(field)
                .CloseWhere()
                .AddGroupBy(field)
                .AddOrderBy("recordsCount", false);

        SparqlQuery query = limit == 0
                ? queryBuilder.Build()
                : queryBuilder.AddLimit(limit)
                .Build();

        return query.SendRequest();
    }

    private List<String> GetGenresByLikedArtists(List<String> likedArtists) {
        queryBuilder.ResetQuery();
        AddGeneralPrefixes();
        AddSelectAllFields();
        AddWhereBindingsAllFields();

        queryBuilder.AddFilterInWhere();
        AddContainsLikedList(likedArtists, ARTIST);

        SparqlResponse sparqlResponse = queryBuilder
                .CloseFilter()
                .CloseWhere()
                .Build()
                .SendRequest();

        return sparqlResponse.GetResult(1, 0).results
                .stream()
                .map(entity -> entity.get(GENRE))
                .collect(Collectors.toList());
    }

    private void AddGeneralPrefixes() {
        this.queryBuilder
                .AddRdfPrefix()
                .AddSparqlResultPrefix()
                .AddXsdPrefix();
    }

    private void AddSelectAllFields() {
        this.queryBuilder.AddSelectDistinct()
                .AddSelectedField(VINYL)
                .AddSelectedField(ARTIST)
                .AddSelectedField(GENRE)
                .AddSelectedField(RELEASEDATE);
    }

    private void AddWhereBindingsAllFields() {
        this.queryBuilder.AddWhere()
                .AddBindingVariableInWhere(VINYL)
                .AddBindingVariableInWhere(ARTIST)
                .AddBindingVariableInWhere(GENRE)
                .AddBindingVariableInWhere(RELEASEDATE);
    }

    private void AddContainsLikedList(List<String> likedList, String entity) {
        likedList
                .forEach(likeditem -> queryBuilder.AddContainsStringInFilter(entity, likeditem)
                        .AddORInFilter());
        this.queryBuilder.DeleteLastOperatorInFilter();

    }

    private void AddNotContainsLikedList(List<String> dislikedList, String entity) {
        dislikedList
                .forEach(dislikedItem -> queryBuilder.AddNotContainsStringInFilter(entity, dislikedItem)
                        .AddANDInFilter());
        this.queryBuilder.DeleteLastOperatorInFilter();
    }

    private void AddFilters(List<String> dislikedArtists, List<String> dislikedGenres,
                            List<String> likedArtists, List<String> likedGenres,
                            Integer startYear, Integer endYear) {
        if (likedArtists.size() != 0 || dislikedArtists.size() != 0 ||
                likedGenres.size() != 0 || dislikedGenres.size() != 0 ||
                startYear != 0 || endYear != 0) {
            queryBuilder.AddFilterInWhere();

            AddFilterForLikedItems(likedArtists, likedGenres);
            AddYearCondition(likedArtists, likedGenres, startYear, endYear);
            AddFilterForDislikedItems(dislikedArtists, dislikedGenres,
                    likedArtists, likedGenres);

            queryBuilder.CloseFilter();
        }
    }

    private void AddFilterForLikedItems(List<String> likedArtists, List<String> likedGenres) {
        if (likedArtists.size() != 0 || likedGenres.size() != 0) {
            queryBuilder.AddParanthesis();
        }

        if (likedArtists.size() != 0) {
            AddContainsLikedList(likedArtists, ARTIST);
        }

        if (likedArtists.size() != 0 && likedGenres.size() != 0) {
            queryBuilder.AddORInFilter();
        }

        if (likedGenres.size() != 0) {
            AddContainsLikedList(likedGenres, GENRE);
        }

        if (likedArtists.size() != 0 || likedGenres.size() != 0) {
            queryBuilder.CloseParanthesis();
        }
    }

    private void AddYearCondition(List<String> likedArtists, List<String> likedGenres, Integer startYear, Integer endYear) {
        if ((likedArtists.size() != 0 || likedGenres.size() != 0) && (startYear != 0 || endYear != 0)) {
            queryBuilder.AddANDInFilter();
        }

        if (startYear != 0) {
            queryBuilder.AddDateConditionInFilter(RELEASEDATE, ">=", startYear + "-01-01");
        }

        if (startYear != 0 && endYear != 0) {
            queryBuilder.AddANDInFilter();
        }

        if (endYear != 0) {
            queryBuilder.AddDateConditionInFilter(RELEASEDATE, "<=", endYear + "-01-01");
        }
    }

    private void AddFilterForDislikedItems(List<String> dislikedArtists, List<String> dislikedGenres,
                                           List<String> likedArtists, List<String> likedGenres) {
        if ((dislikedArtists.size() != 0 || dislikedGenres.size() != 0) &&
                (likedArtists.size() != 0 || likedGenres.size() != 0)) {
            queryBuilder.AddANDInFilter();
        }

        if (dislikedArtists.size() != 0) {
            AddNotContainsLikedList(dislikedArtists, ARTIST);
        }

        if (dislikedArtists.size() != 0 && dislikedGenres.size() != 0) {
            queryBuilder.AddANDInFilter();
        }

        if (dislikedGenres.size() != 0) {
            AddNotContainsLikedList(dislikedGenres, GENRE);
        }
    }
}
