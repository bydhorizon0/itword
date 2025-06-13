package org.news.itword.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.news.itword.entity.MovieGenre;
import org.news.itword.entity.MovieGenreType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovieDetailDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<MovieImageDTO> movieImageDTOList = new ArrayList<>();

    private MovieGenreType mainGenre;

    @Builder.Default
    private List<MovieGenreType> subGenres = new ArrayList<>();

    private double averageRating;
    private long ratingCount; // 몇 명이 참여했는지

    @Builder.Default
    private List<ReplyDTO> replyDTOList = new ArrayList<>();
}
