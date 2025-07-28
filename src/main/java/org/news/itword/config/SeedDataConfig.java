package org.news.itword.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.news.itword.entity.Movie;
import org.news.itword.entity.MovieGenre;
import org.news.itword.entity.MovieGenreType;
import org.news.itword.entity.MovieImage;
import org.news.itword.repository.MovieImageRepository;
import org.news.itword.repository.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class SeedDataConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public CommandLineRunner commandLineRunner(MovieRepository movieRepository, MovieImageRepository movieImageRepository) {
        return args -> {
            if (movieRepository.count() == 0) {
                scrap(movieRepository, movieImageRepository);
            }
        };
    }

    public void scrap(MovieRepository movieRepository, MovieImageRepository movieImageRepository) throws IOException {
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
                    for (JsonNode movieJson : results) {
                        String overview = movieJson.path("overview").asText();
                        String title = movieJson.path("title")
                                .asText()
                                .replaceAll("[\\\\/:*?\"<>|]", "-");
                        String posterPath = movieJson.path("poster_path").asText();
                        String releaseDate = movieJson.path("release_date").asText();
                        List<Integer> genreIds = new ArrayList<>();

                        if (movieJson.path("genre_ids").isArray()) {
                            movieJson.path("genre_ids").forEach(idNode -> {
                                genreIds.add(idNode.asInt());
                            });
                        }

                        if (Stream.of(overview, title, posterPath, releaseDate)
                                .allMatch(e -> e != null && !e.isEmpty())) {

                            String imageUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
                            String fileName = "images/" + title + ".jpg";

                            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                                try {
                                    downloadImage(imageUrl, fileName);

                                    //
                                    Movie movie = Movie.builder()
                                            .title(title)
                                            .content(overview)
                                            .build();

                                    Set<MovieGenre> movieGenreSet = genreIds.stream()
                                            .map(id ->
                                                    MovieGenre.builder()
                                                            .genre(MovieGenreType.fromId(id))
                                                            .build()
                                            ).collect(Collectors.toSet());
                                    movie.addGenreAll(movieGenreSet);

                                    MovieImage movieImage = MovieImage.builder()
                                            .movie(movie)
                                            .imgName(title)
                                            .path(fileName)
                                            .build();
                                    movie.addMovieImage(movieImage);

                                    movieRepository.save(movie);
                                } catch (MalformedURLException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            futures.add(future);
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
