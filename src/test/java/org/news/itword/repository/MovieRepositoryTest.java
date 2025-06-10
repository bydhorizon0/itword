package org.news.itword.repository;

import org.junit.jupiter.api.Test;
import org.news.itword.dto.MovieDTO;
import org.news.itword.entity.Movie;
import org.news.itword.entity.MovieImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImageRepository movieImageRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insertMovies() throws IOException {
        ClassPathResource resource = new ClassPathResource("images");

        File imageFolder = resource.getFile();
        File[] imageFiles = imageFolder.listFiles((dir, name) -> name.endsWith(".jpg"));

        if (imageFiles != null && imageFiles.length == 0) {
            throw new RuntimeException("No image files found");
        }

        for (File file : imageFiles) {
            String fileName = file.getName();
            String title = fileName.substring(0, fileName.lastIndexOf("."));
            String uuid = UUID.randomUUID().toString();

            Movie movie = Movie.builder()
                    .title(title)
                    .content("내용 없음")
                    .movieImages(new ArrayList<>())
                    .build();
            movieRepository.save(movie);

            MovieImage movieImage = MovieImage.builder()
                    .uuid(uuid)
                    .imgName(fileName)
                    .movie(movie)
                    .path("images")
                    .build();

            movieImageRepository.save(movieImage);
        }
    }

    @Test
    public void findAllMovies() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        Page<MovieDTO> movies = movieRepository.findAllMovies(pageable);

        for (MovieDTO movie : movies) {
            System.out.println(movie);
        }
    }

}