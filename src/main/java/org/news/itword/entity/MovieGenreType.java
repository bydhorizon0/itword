package org.news.itword.entity;

import java.util.HashMap;
import java.util.Map;

public enum MovieGenreType {
    EMPTY(0, "Empty"),
    ACTION(28, "Action"),
    ADVENTURE(12, "Adventure"),
    ANIMATION(16, "Animation"),
    COMEDY(35, "Comedy"),
    CRIME(80, "Crime"),
    DOCUMENTARY(99, "Documentary"),
    DRAMA(18, "Drama"),
    FAMILY(10751, "Family"),
    FANTASY(14, "Fantasy"),
    HISTORY(36, "History"),
    HORROR(27, "Horror"),
    MUSIC(10402, "Music"),
    MYSTERY(9648, "Mystery"),
    ROMANCE(10749, "Romance"),
    SCIENCE_FICTION(878, "Science Fiction"),
    TV_MOVIE(10770, "TV Movie"),
    THRILLER(53, "Thriller"),
    WAR(10752, "War"),
    WESTERN(37, "Western");

    private final int id;
    private final String name;

    MovieGenreType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    // 역으로 ID로 enum 찾기 위한 static map
    private static final Map<Integer, MovieGenreType> ID_MAP = new HashMap<>();

    static {
        for (MovieGenreType type : values()) {
            ID_MAP.put(type.getId(), type);
        }
    }

    public static MovieGenreType fromId(int id) {
        return ID_MAP.get(id);
    }
}
