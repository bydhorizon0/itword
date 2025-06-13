package org.news.itword.entity;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@ToString(exclude = "movie")
@Table(name = "tbl_movie_genres")
public class MovieGenre implements Comparable<MovieGenre> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovieGenreType genre;

    @Column(nullable = false)
    private boolean main;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    // 정렬 기준: main 장르 우선, 그 다음 장르 이름순
    @Override
    public int compareTo(@NotNull MovieGenre o) {
        if (this.main != o.main) {
            return Boolean.compare(o.main, this.main);
        }
        return this.genre.name().compareTo(o.genre.name());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;

        return switch (obj) {
            case MovieGenre movieGenre -> genre == movieGenre.genre &&
                    movie != null &&
                    movie.getId() != null &&
                    movie.getId().equals(movieGenre.movie.getId());
            default -> false;
        };
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie != null ? movie.getId() : 0, genre);
    }

}
