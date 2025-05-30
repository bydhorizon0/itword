package org.news.itword.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewsDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt, updatedAt;

    private String memberEmail;
}
