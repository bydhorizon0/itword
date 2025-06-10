package org.news.itword.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Setter
@Getter
@ToString
public class PageRequestDTO {

    private int page;
    private int size;
    private String keyword;
    private String searchType;

    public PageRequestDTO() {
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort) {
        page = Math.max(page, 1);
        return PageRequest.of(page - 1, size, sort);
    }

}
