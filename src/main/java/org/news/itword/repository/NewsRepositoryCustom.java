package org.news.itword.repository;

import org.news.itword.dto.NewsDTO;

public interface NewsRepositoryCustom {
    NewsDTO getNewsInfo(Long id);
}
