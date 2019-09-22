package com.example.android.imdbapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.imdbapp.movie.Search;

import java.util.ArrayList;
import java.util.List;

public class MySQLHelper extends SQLiteOpenHelper {

     String TABLE_NAME = "tblMovies";

    String db_create_query = "" +
            "CREATE TABLE " + TABLE_NAME +"(" +
            " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " title TEXT ," +
            " year TEXT ," +
            " type TEXT ," +
            " poster TEXT " +")" +
            "";


    public MySQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(db_create_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void inserToDB(Search movie) {

        try {
            String insertQuery = "INSERT INTO " + TABLE_NAME +
                    "(title , year, type, poster)" +
                    "VALUES( '" + movie.getTitle().replace("'", "") + "' ," +
                    "'" + movie.getYear() + "' ," +
                    "'" + movie.getType() + "' ," +
                    "'" + movie.getPoster() + "' )";

            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(insertQuery);
            db.close();

        }catch(Exception e){

        }


    }


    public List<Search> getMovies() {

        List<Search> items= new ArrayList<>();

        SQLiteDatabase db = this. getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT title, year, type, poster from " + TABLE_NAME, null);


        while (cursor.moveToNext()) {
            Search movie = new Search();
            movie.setTitle( cursor.getString(0) );
            movie.setYear( cursor.getString(1) );
            movie.setType( cursor.getString(2) );
            movie.setPoster( cursor.getString(3) );

            items.add(movie);
        }


        db.close();
        return items;
    }
}
