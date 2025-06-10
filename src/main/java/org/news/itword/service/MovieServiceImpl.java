package org.news.itword.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.news.itword.dto.MovieDTO;
import org.news.itword.dto.PageRequestDTO;
import org.news.itword.dto.PageResultDTO;
import org.news.itword.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public PageResultDTO<MovieDTO> getMovies(PageRequestDTO requestDTO) {
        Page<MovieDTO> movies = movieRepository.findAllMovies(requestDTO.getKeyword(), requestDTO.getSearchType(), requestDTO.getPageable(Sort.by("id").descending()));

        return new PageResultDTO<>(movies);
    }
}
