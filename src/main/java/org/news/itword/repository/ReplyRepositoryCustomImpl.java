package org.news.itword.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.news.itword.dto.ReplyDTO;
import org.news.itword.entity.QMember;
import org.news.itword.entity.QReply;
import org.news.itword.entity.Reply;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class ReplyRepositoryCustomImpl implements ReplyRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<ReplyDTO> getParentReplies(Long newsId) {
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        List<ReplyDTO> result = queryFactory.select(Projections.constructor(ReplyDTO.class,
                    reply.id,
                reply.content,
                reply.createdAt,
                reply.updatedAt,
                reply.parentReply.id,
                reply.news.id,
                member.email
                ))
                .from(reply)
                .join(member).on(reply.member.id.eq(member.id))
                .where(reply.news.id.eq(newsId))
                .fetch();

        return result;
    }
}
