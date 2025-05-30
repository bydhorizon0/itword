package org.news.itword.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.news.itword.dto.NewsDTO;
import org.news.itword.entity.QMember;
import org.news.itword.entity.QNews;

@Log4j2
@RequiredArgsConstructor
public class NewsRepositoryCustomImpl implements NewsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public NewsDTO getNewsInfo(Long id) {
        QNews news = QNews.news;
        QMember member = QMember.member;

        NewsDTO result = queryFactory.select(Projections.constructor(NewsDTO.class,
                        news.id,
                        news.title,
                        news.content,
                        news.createdAt,
                        news.updatedAt,
                        member.email
                ))
                .from(news)
                .join(member).on(news.member.id.eq(member.id))
                .where(news.id.eq(id))
                .fetchOne();

        System.out.println(result);

        return null;
    }
}
