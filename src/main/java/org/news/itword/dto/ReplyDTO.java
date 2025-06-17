package org.news.itword.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReplyDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt, updatedAt;
    private Long parentReplyId;
    private String memberEmail;

    @Builder.Default
    private List<ReplyDTO> childReplies = new ArrayList<>();
}
