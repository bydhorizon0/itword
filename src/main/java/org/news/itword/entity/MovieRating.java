package org.news.itword.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member", "movie"})
@Table(
        name = "tbl_movie_ratings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "movie_id"}) // 한 사람이 같은 영화에 별점 주는 걸 막기위해 Unique Key 설정
)
public class MovieRating extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score; // 1~5, Validation 필요

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    // 영화 + 회원이 같으면 같은 별점으로 간주(즉, movie.id, member.id)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;

        return switch (obj) {
            case MovieRating other -> movie != null && movie.getId() != null &&
                    member != null && member.getId() != null &&
                    movie.getId().equals(other.movie.getId()) &&
                    member.getId().equals(other.member.getId());
            default -> false;
        };
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                movie != null && movie.getId() != null ? movie.getId() : 0,
                member != null && member.getId() != null ? member.getId() : 0
        );
    }

}
