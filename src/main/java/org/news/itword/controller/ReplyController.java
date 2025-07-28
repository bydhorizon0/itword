package org.news.itword.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.news.itword.dto.ReplyRequest;
import org.news.itword.service.ReplyServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/replies")
@Controller
public class ReplyController {

    private final ReplyServiceImpl replyService;

    @PostMapping("/save")
    public String save(@Valid ReplyRequest request) {
        log.info(request);

        replyService.save(request);

        return "redirect:/movies/detail?id=" + request.movieId();
    }
}
