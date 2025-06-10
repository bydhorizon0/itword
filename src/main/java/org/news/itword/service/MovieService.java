package org.news.itword.service;

import org.news.itword.dto.MovieDTO;
import org.news.itword.dto.PageRequestDTO;
import org.news.itword.dto.PageResultDTO;

public interface MovieService {
    PageResultDTO<MovieDTO> getMovies(PageRequestDTO requestDTO);
}
