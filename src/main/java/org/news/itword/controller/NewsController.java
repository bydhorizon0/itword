package org.news.itword.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.news.itword.dto.NewsDTO;
import org.news.itword.dto.PageRequestDTO;
import org.news.itword.repository.NewsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/news")
@Controller
public class NewsController {

    private final NewsRepository newsRepository;

    @GetMapping
    public String index(PageRequestDTO requestDTO, Model model) {
        Pageable pageable = requestDTO.getPageable(Sort.by("id").descending());

        Page<NewsDTO> newsDTOS = newsRepository.getNewsList(pageable);
        model.addAttribute("newsDTOS", newsDTOS);

        return "news/index";
    }
}
