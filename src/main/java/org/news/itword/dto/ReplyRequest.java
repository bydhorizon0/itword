package org.news.itword.dto;

import jakarta.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;


public record ReplyRequest(
        @NotNull Long movieId,
        @NotNull Long memberId,
        Long parentReplyId,
        @NotBlank String content
){}
