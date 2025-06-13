package org.news.itword.repository;

import org.junit.jupiter.api.Test;
import org.news.itword.dto.MovieDTO;
import org.news.itword.dto.MovieDetailDTO;
import org.news.itword.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(properties = "spring.jpa.properties.hibernate.default_batch_fetch_size=1000") // 옵션 적용
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

    @Transactional
    @Rollback(value = false)
    @Test
    public void modifyMovies() {
        List<Movie> movieList = movieRepository.findAll();
        MovieGenreType[] genreTypes = MovieGenreType.values();

        List<Member> memberList = memberRepository.findAll();

        for (Movie movie : movieList) {
            Member member = memberList.get(RandomGenerator.getDefault().nextInt(1, memberList.size()));

            // 장르 3개 중복 없이 랜덤 선택
            List<MovieGenreType> shuffled = new ArrayList<>(List.of(genreTypes));
            Collections.shuffle(shuffled);

            MovieRating rating = MovieRating.builder()
                    .score(RandomGenerator.getDefault().nextInt(1, 6))
                    .movie(movie)
                    .member(member)
                    .build();

            MovieGenre mainGenre = MovieGenre.builder()
                    .genre(shuffled.get(0))
                    .main(true)
                    .movie(movie)
                    .build();

            MovieGenre subGenre1 = MovieGenre.builder()
                    .genre(shuffled.get(1))
                    .main(false)
                    .movie(movie)
                    .build();

            MovieGenre subGenre2 = MovieGenre.builder()
                    .genre(shuffled.get(2))
                    .main(false)
                    .movie(movie)
                    .build();

            // 평점 추가
            movie.addRating(rating);
            member.addRating(rating);
            // 장르 추가
            movie.addGenre(mainGenre);
            movie.addGenre(subGenre1);
            movie.addGenre(subGenre2);
        }
    }

    @Test
    public void findAllMovies() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        Page<MovieDTO> movies = movieRepository.getAllMovies("", "", pageable);

        for (MovieDTO movie : movies) {
            System.out.println(movie);
        }
    }

    @Test
    public void getMovie() {
        MovieDetailDTO movie = movieRepository.getMovie(7L);

        System.out.println(movie);
    }

}