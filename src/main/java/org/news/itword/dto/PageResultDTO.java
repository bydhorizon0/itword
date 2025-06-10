package org.news.itword.dto;

import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.IntStream;

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
        dtoList = result.getContent();
        totalPage = result.getTotalPages();

        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {
        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();

        int tempEnd = (int) (Math.ceil(page / 10.0)) * 10;

        start = tempEnd - 9;
        end = Math.min(tempEnd, totalPage);

        prev = start > 1;
        next = tempEnd < totalPage;

        pageList = IntStream.rangeClosed(start, end)
                .boxed()
                .toList();
    }
}
