package edu.lysak.advisor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

public class AuthHttpServer {
    private static final String CLIENT_ID = "d2649c25a2b44d65916508209712ee1d";
    private static final String CLIENT_SECRET = "52b3d27fea7f4c3cb7c551383b0ce121";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String REDIRECT_URI = "http://localhost:8080";
    private static String code = "";
    private static String accessToken = "";

    private final String accessServerPoint;
    private final String serverPath;
    private final OutputHandler outputHandler;
    private final JsonParser jsonParser;
    private final HttpClient client;

    public AuthHttpServer(
            String accessServerPoint,
            String serverPath, OutputHandler outputHandler,
            JsonParser jsonParser,
            HttpClient client
    ) {
        this.accessServerPoint = accessServerPoint;
        this.serverPath = serverPath;
        this.outputHandler = outputHandler;
        this.jsonParser = jsonParser;
        this.client = client;
    }

    public void provideAuthentication() throws IOException, InterruptedException {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);
        server.createContext("/",
                httpHandler -> {
                    String query = httpHandler.getRequestURI().getQuery();
                    String responseText = "Authorization code not found. Try again.";

                    if (query != null) {
                        Map<String, String> queryParameters = getQueryParameters(query);
                        if (queryParameters.containsKey("code")) {
                            responseText = "Got the code. Return back to your program.";
                            code = queryParameters.get("code");
                            outputHandler.printCodeReceived();
                            try {
                                makePostRequest();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    httpHandler.sendResponseHeaders(200, responseText.length());
                    httpHandler.getResponseBody().write(responseText.getBytes());
                    httpHandler.getResponseBody().close();
                }
        );

        server.start();

        outputHandler.printLinkForGettingPermission(accessServerPoint, CLIENT_ID, REDIRECT_URI);

        while (code.equals("")) {
            Thread.sleep(10);
        }
        server.stop(10);
        outputHandler.printSuccess();
    }

    private Map<String, String> getQueryParameters(String query) {
        return Arrays.stream(query.split("&"))
                .map(it -> it.split("="))
                .collect(Collectors.toMap(array -> array[0], array -> array[1]));
    }

    private void makePostRequest() throws InterruptedException, IOException {
        outputHandler.printTextForToken();
        String postRequestBody = String.format("client_id=%s&client_secret=%s&grant_type=%s&code=%s&redirect_uri=%s",
                CLIENT_ID,
                CLIENT_SECRET,
                GRANT_TYPE,
                code,
                REDIRECT_URI);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(accessServerPoint + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(postRequestBody))
                .build();

        accessToken = getAccessToken(getResponseBody(client, postRequest));
    }

    private String getResponseBody(HttpClient client, HttpRequest request) throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    private String getAccessToken(String body) {
        JsonObject jo = jsonParser.parse(body).getAsJsonObject();
        return jo.get("access_token").getAsString();
    }

    public List<?> makeRequestForFeaturedPlaylists() throws IOException, InterruptedException {
        HttpRequest request = getRequest(serverPath, "/v1/browse/featured-playlists");
        return parsePlaylists(getResponseBody(client, request));
    }

    public List<?> makeRequestForNewReleases() throws IOException, InterruptedException {
        HttpRequest request = getRequest(serverPath, "/v1/browse/new-releases");
        return parseReleases(getResponseBody(client, request));
    }

    public List<?> makeRequestForCategories() throws IOException, InterruptedException {
        HttpRequest request = getRequest(serverPath, "/v1/browse/categories");
        Map<String, String> categories = parseCategories(getResponseBody(client, request));
        return Arrays.asList(categories.keySet().toArray());
    }

    public List<?> makeRequestForCategoryByName(String categoryName) throws IOException, InterruptedException {
        String categoryId = getCategoryId(categoryName);

        HttpRequest request = getRequest(
                serverPath,
                String.format("/v1/browse/categories/%s/playlists", categoryId)
        );

        String responseBody = getResponseBody(client, request);
        if (responseBody.contains("error")) {
            JsonObject json = jsonParser.parse(responseBody).getAsJsonObject();
            JsonObject error = json.getAsJsonObject("error");
            throw new IllegalArgumentException(error.get("message").getAsString());
        }

        return parsePlaylists(responseBody);
    }

    private String getCategoryId(String categoryName) throws IOException, InterruptedException {
        HttpRequest request = getRequest(serverPath, "/v1/browse/categories");
        Map<String, String> categories = parseCategories(getResponseBody(client, request));

        String categoryId = categories.getOrDefault(categoryName, null);
        if (categoryId == null) {
            throw new IllegalArgumentException("Unknown category name.");
        }
        return categoryId;
    }

    private HttpRequest getRequest(String serverPath, String resourcePath) {
        return HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(serverPath + resourcePath))
                .GET()
                .build();
    }

    private List<Playlist> parsePlaylists(String responseBody) {
        JsonObject object = jsonParser.parse(responseBody).getAsJsonObject();
        JsonObject jsonPlaylists = object.getAsJsonObject("playlists");
        List<Playlist> playlists = new ArrayList<>();
        for (JsonElement itemElement : jsonPlaylists.getAsJsonArray("items")) {
            JsonObject item = itemElement.getAsJsonObject();
            JsonObject externalUrls = item.getAsJsonObject("external_urls");

            String name = item.get("name").getAsString();
            String href = externalUrls.get("spotify").getAsString();
            playlists.add(new Playlist(name, href));
        }
        return playlists;
    }

    private List<Release> parseReleases(String responseBody) {
        JsonObject object = jsonParser.parse(responseBody).getAsJsonObject();
        JsonObject jsonReleases = object.getAsJsonObject("albums");
        List<Release> releases = new ArrayList<>();
        for (JsonElement itemElement : jsonReleases.getAsJsonArray("items")) {
            JsonObject item = itemElement.getAsJsonObject();

            List<String> artists = new ArrayList<>();
            for (JsonElement artistElem : item.getAsJsonArray("artists")) {
                JsonObject artist = artistElem.getAsJsonObject();
                String artistName = artist.get("name").getAsString();
                artists.add(artistName);
            }

            String albumName = item.get("name").getAsString();

            JsonObject externalUrls = item.getAsJsonObject("external_urls");
            String href = externalUrls.get("spotify").getAsString();

            releases.add(new Release(albumName, artists, href));
        }
        return releases;
    }

    private Map<String, String> parseCategories(String responseBody) {
        JsonObject object = jsonParser.parse(responseBody).getAsJsonObject();
        JsonObject jsonCategories = object.getAsJsonObject("categories");
        Map<String, String> categories = new LinkedHashMap<>();
        for (JsonElement itemElement : jsonCategories.getAsJsonArray("items")) {
            JsonObject item = itemElement.getAsJsonObject();
            String categoryId = item.get("id").getAsString();
            String categoryName = item.get("name").getAsString();
            categories.put(categoryName, categoryId);
        }
        return categories;
    }
}
