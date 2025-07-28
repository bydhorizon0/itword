package org.news.itword.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.news.itword.dto.*;
import org.news.itword.service.MovieService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/movies")
@Controller
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public String movieList(PageRequestDTO requestDTO, Model model) {
        PageResultDTO<MovieDTO> movies = movieService.getMovies(requestDTO);
        model.addAttribute("movies", movies);
        model.addAttribute("requestDTO", requestDTO);

        return "/movie/index";
    }

    @GetMapping("/{id}")
    public String movieDetail(@RequestParam("id") Long id, @AuthenticationPrincipal MemberAuthDTO member, Model model) throws Exception {
        MovieDetailDTO movieDetailDTO = movieService.findMovieById(id);
        model.addAttribute("movieDetailDTO", movieDetailDTO);

        if (member != null) {
            model.addAttribute("memberId", member.getId());
            log.info("memberId: " + member.getId());
        }

        return "movie/detail";
    }

}
