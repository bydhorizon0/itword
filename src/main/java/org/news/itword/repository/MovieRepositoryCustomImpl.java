package org.news.itword.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLSubQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.news.itword.dto.MovieDTO;
import org.news.itword.dto.MovieDetailDTO;
import org.news.itword.dto.MovieImageDTO;
import org.news.itword.dto.ReplyDTO;
import org.news.itword.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
public class MovieRepositoryCustomImpl implements MovieRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MovieDTO> getAllMovies(String keyword, String searchType, Pageable pageable) {
        QMovie movie = QMovie.movie;
        QMovieImage movieImage = QMovieImage.movieImage;
        QMovieImage subMovieImage = new QMovieImage("subMovieImage");
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
                .leftJoin(movie.replies, reply)
                .leftJoin(movieImage).on(
                        movieImage.id.eq(
                                JPAExpressions.select(subMovieImage.id.min())
                                        .from(subMovieImage)
                                        .where(subMovieImage.movie.id.eq(movie.id))
                        )
                )
                .where(builder)
                .groupBy(movie, movieImage)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<MovieDTO> movieDTOList = new ArrayList<>();
        for (Tuple tuple : fetch) {
            Movie movieRes = tuple.get(0, Movie.class);
            MovieImage movieImageRes = Objects.requireNonNullElse(tuple.get(1, MovieImage.class), new MovieImage());
            Long replyCount = tuple.get(2, Long.class);

            MovieDTO movieDTO = MovieDTO.builder()
                    .id(movieRes.getId())
                    .title(movieRes.getTitle())
                    .content(movieRes.getContent())
                    .movieImageDTO(movieImageToDTO(movieImageRes))
                    .replyCount(replyCount)
                    .build();

            movieDTOList.add(movieDTO);
        }

        Long total = queryFactory.select(movie.count())
                .from(movie)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(movieDTOList, pageable, total);
    }

    @Override
    public MovieDetailDTO getMovie(long id) throws Exception {
        QMovie movie = QMovie.movie;
        QMovieImage movieImage = QMovieImage.movieImage;
        QMovieRating movieRating = QMovieRating.movieRating;

        // 평균 별점
        JPQLSubQuery<Double> avgSubquery = JPAExpressions.select(movieRating.score.avg())
                .from(movieRating)
                .where(movieRating.movie.id.eq(id));

        // 별점 개수
        JPQLSubQuery<Long> countSubquery = JPAExpressions.select(movieRating.id.count())
                .from(movieRating)
                .where(movieRating.movie.id.eq(id));

        Tuple tuple = queryFactory.select(movie, avgSubquery, countSubquery)
                .from(movie)
                .where(movie.id.eq(id))
                .fetchOne();

        if (tuple == null) {
            throw new Exception("영화를 찾을 수 없습니다. id=" + id);
        }

        Movie m = tuple.get(0, Movie.class);
        double avgRating = Objects.requireNonNullElse(tuple.get(1, Double.class), 0.0);
        long ratingCount = Objects.requireNonNullElse(tuple.get(2, Long.class), 0L);

        // 영화 이미지 조회
        List<MovieImage> movieImages = queryFactory.selectFrom(movieImage)
                .join(movieImage.movie, movie)
                .where(movieImage.movie.id.eq(id))
                .fetch();

        List<MovieImageDTO> movieImageDTOList = movieImages.stream().map(this::movieImageToDTO)
                .toList();

        // 영화 장르 조회
        QMovieGenre movieGenre = QMovieGenre.movieGenre;
        List<MovieGenreType> genreTypeList = queryFactory.select(movieGenre.genre)
                .from(movieGenre)
                .join(movieGenre.movie, movie)
                .where(movieGenre.movie.id.eq(id))
                .fetch();
        MovieGenreType mainGenre = genreTypeList.isEmpty() ? MovieGenreType.EMPTY : genreTypeList.getFirst();

        // 영화 댓글들 조회
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        // 루트 댓글 조회
        List<Reply> rootReplies = queryFactory.selectFrom(reply)
                .join(reply.member, member).fetchJoin()
                .where(reply.movie.id.eq(id)
                        .and(reply.parentReply.id.isNull()))
                .fetch();

        // 루트 댓글 아이디
        List<Long> rootReplyIds = rootReplies.stream()
                .map(Reply::getId)
                .toList();

        // 루트 댓글에 해당하는 자식 댓글 조회
        List<Reply> childReplies = queryFactory.selectFrom(reply)
                .join(reply.member, member).fetchJoin()
                .where(reply.parentReply.id.in(rootReplyIds))
                .fetch();

        // 루트 댓글 ID로 자식 댓글 그룹핑
        Map<Long, List<Reply>> childrenMap = childReplies.stream()
                .collect(Collectors.groupingBy(childReply -> childReply.getParentReply().getId()));

        // 루트 댓글에 자식 댓글들 매핑
        List<ReplyDTO> result = rootReplies.stream()
                .map(rootReply -> {
                    ReplyDTO rootDTO = replyToDTO(rootReply);

                    // 자식 댓글이 있다면 DTO 리스트로 변환해서 세팅
                    List<Reply> children = childrenMap.getOrDefault(rootDTO.getId(), Collections.emptyList());
                    List<ReplyDTO> childDTOs = children.stream()
                            .sorted(Comparator.comparing(Reply::getCreatedAt))
                            .map(this::replyToDTO)
                            .toList();

                    rootDTO.setChildReplies(childDTOs);
                    return rootDTO;
                })
                .toList();

        return MovieDetailDTO.builder()
                .id(m.getId())
                .title(m.getTitle())
                .content(m.getContent())
                .createdAt(m.getCreatedAt())
                .updatedAt(m.getUpdatedAt())
                .mainGenre(mainGenre)
                .subGenres(
                        genreTypeList.stream()
                                .skip(1)
                                .toList()
                )
                .movieImageDTOList(movieImageDTOList)
                .averageRating(avgRating)
                .ratingCount(ratingCount)
                .replyDTOList(result)
                .build();
    }
}
