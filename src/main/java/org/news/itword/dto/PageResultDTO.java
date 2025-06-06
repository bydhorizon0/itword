package org.news.itword.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@AllArgsConstructor
@Setter
@Getter
@ToString
public class PageResultDTO<DTO> {

    private int page;
    private int size;
    private int totalPage;

    private int start, end;
    private boolean prev, next;

    private List<Integer> pageList;
    private List<DTO> dtoList;

    public PageResultDTO(Page<DTO> result) {

    }
}
