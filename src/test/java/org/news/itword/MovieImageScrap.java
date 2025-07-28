package org.news.itword;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@SpringBootTest
public class MovieImageScrap {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void getJson() {
        record Genre(
                int id,
                String name
        ){}

        record GenreResponse(
                List<Genre> genres
        ){}

        Resource resource = resourceLoader.getResource("classpath:static/json/genres.json");

        try (InputStream is = resource.getInputStream()) {
            GenreResponse response = objectMapper.readValue(is, GenreResponse.class);

            response.genres.forEach(genre -> {
                System.out.println(genre);
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void scrap() throws IOException {
        var client = new OkHttpClient();
        var futures = new ArrayList<CompletableFuture<Void>>();

        for (int page = 1; page <= 10; page++) {
            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/popular?language=ko-KR&page=" + page)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwOTQxMmE1ZDNiNTczNzMzOWI1MzAxMWJmYjhlZjA2NyIsIm5iZiI6MTc0OTIyNzY3NC40ODg5OTk4LCJzdWIiOiI2ODQzMTg5YWE2YmQ0MTQ4YzI0NTY4ZDQiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.4j0_0RgGuQdgWsMbYSe3oHVZe-Id3i6i2EP5vLVpujM")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                JsonNode root = objectMapper.readTree(json);
                JsonNode results = root.get("results");

                if (results != null && results.isArray()) {
                    for (JsonNode movie : results) {
                        String overview = movie.path("overview").asText();
                        String title = movie.path("title")
                                .asText()
                                .replaceAll("[\\\\/:*?\"<>|]", "-");
                        String posterPath = movie.path("poster_path").asText();

                        String releaseDate = movie.path("release_date").asText();
                        List<Integer> genreIds = new ArrayList<>();

                        if (movie.path("genre_ids").isArray()) {
                            movie.path("genre_ids").forEach(idNode -> {
                                genreIds.add(idNode.asInt());
                            });
                        }

                        if (Stream.of(overview, title, posterPath, releaseDate)
                                .allMatch(e -> e != null && !e.isEmpty())) {

                            String imageUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
                            String fileName = "images/" + title + ".jpg";

                        /*
                        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                            try {
                                downloadImage(imageUrl, fileName);
                            } catch (MalformedURLException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        futures.add(future);
                         */
                        }
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
