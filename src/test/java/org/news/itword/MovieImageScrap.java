package org.news.itword;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@SpringBootTest
public class MovieImageScrap {

    @Test
    public void scrap() throws IOException, JSONException {
        var client = new OkHttpClient();
        var futures = new ArrayList<CompletableFuture<Void>>();

        for (int page = 1; page <= 20; page++) {
            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/popular?language=ko-KR&page=" + page)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwOTQxMmE1ZDNiNTczNzMzOWI1MzAxMWJmYjhlZjA2NyIsIm5iZiI6MTc0OTIyNzY3NC40ODg5OTk4LCJzdWIiOiI2ODQzMTg5YWE2YmQ0MTQ4YzI0NTY4ZDQiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.4j0_0RgGuQdgWsMbYSe3oHVZe-Id3i6i2EP5vLVpujM")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String json = response.body().string();
                JSONObject jsonObject = new JSONObject(json);
                JSONArray results = jsonObject.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject movie = results.getJSONObject(i);
                    String title = movie.getString("title").replaceAll("[\\\\/:*?\"<>|]", "_");
                    String posterPath = movie.getString("poster_path");

                    if (posterPath != null && !posterPath.isEmpty()) {
                        String imageUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
                        String fileName = "images/" + title + ".jpg";

                        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                            try {
                                downloadImage(imageUrl, fileName);
                            } catch (MalformedURLException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        futures.add(future);
                    }
                }
            }
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void downloadImage(String imageUrl, String fileName) throws MalformedURLException {
        URL url = new URL(imageUrl);
        try (InputStream in = url.openStream()) {
            Files.createDirectories(Paths.get("images"));
            Files.copy(in, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
