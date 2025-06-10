package org.news.itword.repository;

import org.news.itword.dto.MovieDTO;
import org.news.itword.dto.MovieImageDTO;
import org.news.itword.entity.MovieImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieRepositoryCustom {
    Page<MovieDTO> findAllMovies(Pageable pageable);


    default MovieImageDTO movieImageToDTO(MovieImage movieImage) {
        return MovieImageDTO.builder()
                .id(movieImage.getId())
                .imgName(movieImage.getImgName())
                .path(movieImage.getPath())
                .build();
    }
}
