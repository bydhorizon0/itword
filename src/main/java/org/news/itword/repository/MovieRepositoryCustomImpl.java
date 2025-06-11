package org.news.itword.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.news.itword.dto.MovieDTO;
import org.news.itword.dto.MovieDetailDTO;
import org.news.itword.dto.ReplyDTO;
import org.news.itword.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Log4j2
public class MovieRepositoryCustomImpl implements MovieRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MovieDTO> getAllMovies(String keyword, String searchType, Pageable pageable) {
        QMovie movie = QMovie.movie;
        QMovieImage movieImage = QMovieImage.movieImage;
        QReply reply = QReply.reply;

        // 검색 조건 빌더
        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.trim().isEmpty()) {
            if ("title".equalsIgnoreCase(searchType)) {
                builder.and(movie.title.containsIgnoreCase(keyword));
            } else if ("content".equalsIgnoreCase(searchType)) {
                builder.and(movie.content.containsIgnoreCase(keyword));
            }
        }

        List<Tuple> fetch = queryFactory.select(movie, movieImage, reply.count())
                .from(movie)
                .leftJoin(movie.movieImages, movieImage)
                .fetchJoin()
                .leftJoin(movie.replies, reply)
                .where(builder)
                .groupBy(movie, movieImage)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<MovieDTO> movieDTOList = new ArrayList<>();
        for (Tuple tuple : fetch) {
            Movie movieRes = tuple.get(0, Movie.class);
            MovieImage movieImageRes = tuple.get(1, MovieImage.class);
            Long replyCount = tuple.get(2, Long.class);

            MovieDTO movieDTO = MovieDTO.builder()
                    .id(movieRes.getId())
                    .title(movieRes.getTitle())
                    .content(movieRes.getContent())
                    .movieImageDTOList(new ArrayList<>())
                    .replyCount(replyCount)
                    .build();

            if (Objects.nonNull(movieImageRes)) {
                movieDTO.getMovieImageDTOList().add(movieImageToDTO(movieImageRes));
            }

            movieDTOList.add(movieDTO);
        }

        Long total = queryFactory.select(movie.count())
                .from(movie)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(movieDTOList, pageable, total);
    }

    @Override
    public MovieDetailDTO getMovie(long id) {
        QMovie movie = QMovie.movie;
        QMovieImage movieImage = QMovieImage.movieImage;

        Tuple tuple = queryFactory.select(movie, movieImage)
                .from(movie)
                .leftJoin(movie.movieImages, movieImage)
                .fetchJoin()
                .where(movie.id.eq(id))
                .fetchOne();

        Movie m = tuple.get(0, Movie.class);
        MovieImage mi = tuple.get(1, MovieImage.class);

        QReply reply = QReply.reply;
        QMember member = QMember.member;

        List<Reply> replies = queryFactory.selectFrom(reply)
                .join(reply.member, member).fetchJoin()
                .where(reply.movie.id.eq(id))
                .fetch();

        List<ReplyDTO> replyDTOList = replies.stream().map(this::replyToDTO)
                .toList();

        return MovieDetailDTO.builder()
                .id(m.getId())
                .title(m.getTitle())
                .content(m.getContent())
                .createdAt(m.getCreatedAt())
                .updatedAt(m.getUpdatedAt())
                .movieImageDTOList(List.of(movieImageToDTO(mi)))
                .replyDTOList(replyDTOList)
                .build();
    }
}
