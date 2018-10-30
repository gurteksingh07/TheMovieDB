package com.example.gurteksingh.themoviedb.data;

import android.provider.BaseColumns;

public class Favourite {
    public static final class FavoriteEntry implements BaseColumns {
        public static final String  TABLE_NAME="favorite";
        public static final String  MOVIE_ID="movieid";
        public static final String  TITLE="title";
        public static final String  RATING="rating";
        public static final String  RELEASEDATE="releasingDate";
        public static final String  POSTER_PATH="poster";
        public static final String  SYNOPSIS="plot";
        public static final String flag = "true";
    }
}
