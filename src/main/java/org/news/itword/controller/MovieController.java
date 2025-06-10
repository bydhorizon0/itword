package org.news.itword.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.news.itword.dto.MovieDTO;
import org.news.itword.dto.PageRequestDTO;
import org.news.itword.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/movie")
@Controller
public class MovieController {

    private final MovieRepository movieRepository;

    @GetMapping
    public String movieList(PageRequestDTO requestDTO, Model model) {
        Page<MovieDTO> movies = movieRepository.findAllMovies(requestDTO.getPageable(Sort.by("id").descending()));
        model.addAttribute("movies", movies);

        return "/movie/index";
    }

}
