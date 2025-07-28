package org.news.itword.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.news.itword.dto.ReplyRequest;
import org.news.itword.entity.Reply;
import org.news.itword.repository.ReplyRepository;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    @Override
    public void save(ReplyRequest request) {
        Reply reply = replyRequestToEntity(request);

        replyRepository.save(reply);
    }
}
