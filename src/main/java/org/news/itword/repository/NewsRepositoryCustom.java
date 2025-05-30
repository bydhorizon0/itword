package org.news.itword.repository;

import org.news.itword.dto.NewsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsRepositoryCustom {
    Page<NewsDTO> getNewsList(Pageable pageable);
    NewsDTO getNewsInfo(Long id);
}
