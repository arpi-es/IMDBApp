package com.example.android.imdbapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
            " poster TEXT ," +
            " ImdbID TEXT " +")" +
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
                    "(title , year, type, poster, ImdbID)" +
                    "VALUES( '" + movie.getTitle().replace("'", "") + "' ," +
                    "'" + movie.getYear() + "' ," +
                    "'" + movie.getType() + "' ," +
                    "'" + movie.getPoster() + "' ," +
                    "'" + movie.getImdbID() + "' )";

            Log.i("MYTAG", movie.getImdbID() );

            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(insertQuery);
            db.close();

        }catch(Exception e){

        }


    }

    public void updateToDB(Search movie) {


        try {
            String updateQuery = "UPDATE " + TABLE_NAME + "SET " +
                    "title ='" + movie.getTitle() + "'" +
                    "year ='" + movie.getYear() + "'" +
                    "type ='" + movie.getType() + "'" +
                    "poster ='" + movie.getPoster() + "'" +
                    " WHERE ImdbID='" + movie.getImdbID() + "'" ;

            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(updateQuery);
            db.close();

        }catch(Exception e){

        }


    }


    public Boolean isExist(String ImdbId){

        Boolean blResult = false;

        SQLiteDatabase db = this. getReadableDatabase();

        Cursor cursor = null;
        String sql ="SELECT ImdbID FROM "+TABLE_NAME+" WHERE ImdbID='"+ImdbId+"'";
        cursor= db.rawQuery(sql,null);
        Log.i("MYTAG", "Cursor Count : " + cursor.getCount());

        if(cursor.getCount()>0){
            //PID Found
            blResult = true;
        }else{
            //PID Not Found
            blResult = false;
        }
        cursor.close();
        return  blResult;


    }

    public List<Search> getMovies() {

        List<Search> items= new ArrayList<>();

        SQLiteDatabase db = this. getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT title, year, type, poster, ImdbID from " + TABLE_NAME, null);


        while (cursor.moveToNext()) {
            Search movie = new Search();
            movie.setTitle( cursor.getString(0) );
            movie.setYear( cursor.getString(1) );
            movie.setType( cursor.getString(2) );
            movie.setPoster( cursor.getString(3) );
            movie.setImdbID( cursor.getString(4) );
            items.add(movie);
        }

        db.close();
        return items;
    }
}
