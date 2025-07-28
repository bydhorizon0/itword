package org.news.itword.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "movie")
@Table(name = "tbl_movie_images")
public class MovieImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName;

    private String path;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;
}
