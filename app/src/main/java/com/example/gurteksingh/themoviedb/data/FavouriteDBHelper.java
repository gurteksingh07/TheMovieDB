package com.example.gurteksingh.themoviedb.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gurteksingh.themoviedb.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavouriteDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="favorite.db";
    public static final int DATABASE_VERSION=1;
    public static final String LOGTAG = "FAVORITE";
    SQLiteOpenHelper dbhandler;
    SQLiteDatabase db;
    public FavouriteDBHelper(Context c)
    {
        super(c,DATABASE_NAME,null,DATABASE_VERSION);

    }
    public void open()
    {    Log.i(LOGTAG, "Database Opened");
        db=dbhandler.getWritableDatabase();
    }
    public  void  close(){
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sql)
    {
        final String table="CREATE TABLE "+ Favourite.FavoriteEntry.TABLE_NAME +" ("+
                Favourite.FavoriteEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Favourite.FavoriteEntry.MOVIE_ID+ " INTEGER , "+
                Favourite.FavoriteEntry.TITLE+ " TEXT NOT NULL, "+
                Favourite.FavoriteEntry.RATING+ " REAL NOT NULL, "+
                Favourite.FavoriteEntry.RELEASEDATE+ " TEXT NOT NULL, "+
                Favourite.FavoriteEntry.POSTER_PATH+ " TEXT NOT NULL, "+
                Favourite.FavoriteEntry.SYNOPSIS+ " TEXT NOT NULL, "+
                Favourite.FavoriteEntry.flag+ " TEXT NOT NULL "+" );";
        sql.execSQL(table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Favourite.FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addFavourite(Movie m)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Favourite.FavoriteEntry.MOVIE_ID,m.getId());
        contentValues.put(Favourite.FavoriteEntry.TITLE,m.getTitle());
        contentValues.put(Favourite.FavoriteEntry.RATING,m.getRating());
        contentValues.put(Favourite.FavoriteEntry.RELEASEDATE,m.getReleaseDate());
        contentValues.put(Favourite.FavoriteEntry.POSTER_PATH,m.getPosterPath());
        contentValues.put(Favourite.FavoriteEntry.SYNOPSIS,m.getOverview());
        contentValues.put(Favourite.FavoriteEntry.flag,m.isFavouriteflag());
        db.insert(Favourite.FavoriteEntry.TABLE_NAME,null,contentValues);
        db.close();
    }

    public void deleteFavourite(int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(Favourite.FavoriteEntry.TABLE_NAME,Favourite.FavoriteEntry.MOVIE_ID+"="+id,null);

    }
    public List<Movie> getAllFavourite()
    {
        String[] columns={
                Favourite.FavoriteEntry._ID,
                Favourite.FavoriteEntry.MOVIE_ID,
                Favourite.FavoriteEntry.TITLE,
                Favourite.FavoriteEntry.RATING,
                Favourite.FavoriteEntry.RELEASEDATE,
                Favourite.FavoriteEntry.POSTER_PATH,
                Favourite.FavoriteEntry.SYNOPSIS,
                Favourite.FavoriteEntry.flag
        };
        String order=Favourite.FavoriteEntry._ID+" ASC";
        List<Movie> favoriteList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(Favourite.FavoriteEntry.TABLE_NAME,
                columns,null,null,null,null,
                order);
        if(cursor.moveToFirst())
            do{
                Movie m=new Movie();
                m.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Favourite.FavoriteEntry.MOVIE_ID))));
                m.setTitle(cursor.getString(cursor.getColumnIndex(Favourite.FavoriteEntry.TITLE)));
                m.setRating(Float.parseFloat(cursor.getString(cursor.getColumnIndex(Favourite.FavoriteEntry.RATING))));
                m.setReleaseDate(cursor.getString(cursor.getColumnIndex(Favourite.FavoriteEntry.RELEASEDATE)));
                m.setPosterPath(cursor.getString(cursor.getColumnIndex(Favourite.FavoriteEntry.POSTER_PATH)));
                m.setOverview(cursor.getString(cursor.getColumnIndex(Favourite.FavoriteEntry.SYNOPSIS)));
                m.setFavouriteflag(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Favourite.FavoriteEntry.flag))));
                favoriteList.add(m);
            }while (cursor.moveToNext());
        cursor.close();
        db.close();
        return favoriteList;
  }
}
