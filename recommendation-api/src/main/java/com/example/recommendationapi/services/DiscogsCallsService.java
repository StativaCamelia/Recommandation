package com.example.recommendationapi.services;

import com.example.recommendationapi.models.UserPreferences;
import com.example.recommendationapi.services.exceptions.UserHasNoData;
import com.example.recommendationapi.services.utils.DiscogsCallsUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DiscogsCallsService {

    private static final String ORDERS_URL = "https://api.discogs.com/marketplace/orders";
    private static final String ALL_COLLECTIONS_API = "https://api.discogs.com/users/%s/collection/folders";
    private static final String IDENTITY_API = "https://api.discogs.com/oauth/identity";
    private static final String COLLECTION_ITEMS_API = "https://api.discogs.com/users/%s/collection/folders/%d/releases";

    private static final String SECRET = "nSMWxGCXJOgvXuHCOjXtKvDsACsZLQTx";
    private static final String KEY = "ExSRYFyYoKQkcFRzaUfX";
    private String userToken;
    private String userSecret;

    public String getUserIdentity() throws IOException {

        JSONObject userIdentity = makeGETCallToDiscogs(IDENTITY_API);

        return (String) userIdentity.get("username");
    }

    public UserPreferences getPastPurchases(String userToken, String userSecret) throws IOException, UserHasNoData {

        this.userToken = userToken;
        this.userSecret = userSecret;

        String username = getUserIdentity();
        Integer collectionId = getCollectionId(username);

        return getPastPurchaseData(username, collectionId);
    }

    private UserPreferences getPastPurchaseData(String username, Integer id) throws IOException, UserHasNoData {

        final JSONObject collectionItems = makeGETCallToDiscogs(String.format(COLLECTION_ITEMS_API, username, id));
        final JSONArray itemsArray = collectionItems.getJSONArray("releases");

        UserPreferences userPreferences = new UserPreferences();

        List<String> artistNames = new ArrayList<>();
        List<String> genresNames = new ArrayList<>();

        for (int i = 0; i < itemsArray.length(); i++) {

            JSONObject basicData = itemsArray.getJSONObject(i).getJSONObject("basic_information");

            final JSONArray artists = basicData.getJSONArray("artists");
            final JSONArray genres = basicData.getJSONArray("genres");

            getArtistNames(artistNames, artists);
            getGenresNames(genresNames, genres);
        }

        userPreferences.likedArtists = artistNames.stream().distinct().collect(Collectors.toList());
        userPreferences.likedGenres = genresNames.stream().distinct().collect(Collectors.toList());

        if (itemsArray.length() == 0) {

            throw new UserHasNoData("No item in the collection");
        }

        return userPreferences;
    }

    private List<String> getGenresNames(List<String> genresNames, JSONArray genres) {
        for (int k = 0; k < genres.length(); k++) {

            genresNames.add(genres.getString(k).toLowerCase(Locale.ROOT));
        }
        return genresNames;
    }

    private List<String> getArtistNames(List<String> artistsNames, JSONArray artists) {

        for (int j = 0; j < artists.length(); j++) {


            artistsNames.add(artists.getJSONObject(j).getString("name"));
        }
        return artistsNames;
    }

    private Integer getCollectionId(String username) throws IOException, UserHasNoData {

        final JSONObject collections = makeGETCallToDiscogs(String.format(ALL_COLLECTIONS_API, username));

        final JSONArray collectionsArray = collections.getJSONArray("folders");

        if (collectionsArray.length() == 0) {

            throw new UserHasNoData("No past purchase found");

        }
        return collectionsArray.getJSONObject(0).getInt("id");
    }

    private JSONObject makeGETCallToDiscogs(String url) throws IOException, JSONException {

        final HttpGet getOrders = new HttpGet(url);
        getOrders.setHeader(HttpHeaders.AUTHORIZATION, getApiOauthHeader(this.userToken, this.userSecret));

        final HttpResponse response = HttpClients.createDefault().execute(getOrders);
        final HttpEntity entity = response.getEntity();

        final String body = DiscogsCallsUtils.readInputStream(entity.getContent());

        return new JSONObject(body);
    }

    private String getApiOauthHeader(String userToken, String userSecret) {

        return "OAuth " +
                "oauth_consumer_key=\"" + KEY + "\", " +
                "oauth_nonce=\"" + UUID.randomUUID() + "\", " +
                "oauth_token=\"" + userToken + "\", " +
                "oauth_signature=\"" + SECRET + "&" + userSecret + "\", " +
                "oauth_signature_method=\"" + "PLAINTEXT" + "\", " +
                "oauth_timestamp=\"" + System.currentTimeMillis() / 1000 + "\"";
    }
}
