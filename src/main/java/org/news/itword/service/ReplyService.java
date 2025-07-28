package org.news.itword.service;

import org.news.itword.dto.ReplyRequest;
import org.news.itword.entity.Member;
import org.news.itword.entity.Movie;
import org.news.itword.entity.Reply;

public interface ReplyService {
    void save(ReplyRequest request);


    default Reply replyRequestToEntity(ReplyRequest replyRequest) {
        Movie movie = Movie.builder()
                .id(replyRequest.movieId())
                .build();

        Member member = Member.builder()
                .id(replyRequest.memberId())
                .build();

        Reply.ReplyBuilder reply = Reply.builder()
                .content(replyRequest.content())
                .movie(movie)
                .member(member);

        if (replyRequest.parentReplyId() != null) {
            Reply parent = Reply.builder()
                    .id(replyRequest.parentReplyId())
                    .build();

            reply.parentReply(parent);
        }

        return reply.build();
    }
}
