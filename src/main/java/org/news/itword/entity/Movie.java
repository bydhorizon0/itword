package org.news.itword.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"genres", "ratings", "movieImages", "replies"})
@Table(name = "tbl_movies")
public class Movie extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    // 영화 장르 필드
    @Builder.Default
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private SortedSet<MovieGenre> genres = new TreeSet<>();

    // 별점 필드
    @Builder.Default
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MovieRating> ratings = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieImage> movieImages = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    public void addGenre(MovieGenre genre) {
        genres.add(genre);
        genre.setMovie(this);
    }

    public void addGenreAll(Collection<MovieGenre> genres) {
        genres.addAll(genres);
        genres.forEach(genre -> genre.setMovie(this));
    }

    public void addRating(MovieRating rating) {
        ratings.add(rating);
        rating.setMovie(this);
    }

    public void addMovieImage(MovieImage image) {
        movieImages.add(image);
        image.setMovie(this);
    }
}
