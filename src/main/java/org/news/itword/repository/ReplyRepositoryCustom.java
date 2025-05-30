package org.news.itword.repository;

import org.news.itword.dto.ReplyDTO;

import java.util.List;

public interface ReplyRepositoryCustom {
    List<ReplyDTO> getParentReplies(Long newsId);
}
