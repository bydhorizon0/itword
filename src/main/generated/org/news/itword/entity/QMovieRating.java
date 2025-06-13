package org.news.itword.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMovieRating is a Querydsl query type for MovieRating
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMovieRating extends EntityPathBase<MovieRating> {

    private static final long serialVersionUID = -809266166L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMovieRating movieRating = new QMovieRating("movieRating");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final QMovie movie;

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMovieRating(String variable) {
        this(MovieRating.class, forVariable(variable), INITS);
    }

    public QMovieRating(Path<? extends MovieRating> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMovieRating(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMovieRating(PathMetadata metadata, PathInits inits) {
        this(MovieRating.class, metadata, inits);
    }

    public QMovieRating(Class<? extends MovieRating> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
        this.movie = inits.isInitialized("movie") ? new QMovie(forProperty("movie")) : null;
    }

}

