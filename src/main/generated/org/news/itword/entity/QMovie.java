package org.news.itword.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMovie is a Querydsl query type for Movie
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMovie extends EntityPathBase<Movie> {

    private static final long serialVersionUID = -1935739891L;

    public static final QMovie movie = new QMovie("movie");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final SetPath<MovieGenre, QMovieGenre> genres = this.<MovieGenre, QMovieGenre>createSet("genres", MovieGenre.class, QMovieGenre.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<MovieImage, QMovieImage> movieImages = this.<MovieImage, QMovieImage>createList("movieImages", MovieImage.class, QMovieImage.class, PathInits.DIRECT2);

    public final SetPath<MovieRating, QMovieRating> ratings = this.<MovieRating, QMovieRating>createSet("ratings", MovieRating.class, QMovieRating.class, PathInits.DIRECT2);

    public final ListPath<Reply, QReply> replies = this.<Reply, QReply>createList("replies", Reply.class, QReply.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMovie(String variable) {
        super(Movie.class, forVariable(variable));
    }

    public QMovie(Path<? extends Movie> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMovie(PathMetadata metadata) {
        super(Movie.class, metadata);
    }

}

