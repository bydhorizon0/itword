package org.news.itword.repository;

import org.news.itword.dto.MovieDTO;
import org.news.itword.dto.MovieDetailDTO;
import org.news.itword.dto.MovieImageDTO;
import org.news.itword.dto.ReplyDTO;
import org.news.itword.entity.MovieImage;
import org.news.itword.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieRepositoryCustom {
    Page<MovieDTO> getAllMovies(String keyword, String searchType, Pageable pageable);
    MovieDetailDTO getMovie(long id) throws Exception;

    default MovieImageDTO movieImageToDTO(MovieImage movieImage) {
        return MovieImageDTO.builder()
                .id(movieImage.getId())
                .imgName(movieImage.getImgName())
                .path(movieImage.getPath())
                .build();
    }

    default ReplyDTO replyToDTO(Reply reply) {
        return ReplyDTO.builder()
                .id(reply.getId())
                .content(reply.getContent())
                .createdAt(reply.getCreatedAt())
                .updatedAt(reply.getUpdatedAt())
                .memberEmail(reply.getMember().getEmail())
                .build();
    }
}
