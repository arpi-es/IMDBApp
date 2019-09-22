package com.example.android.imdbapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.imdbapp.movie.Search;

import java.util.List;

public class OfflineMovies extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_movies);

        MySQLHelper mySQLHelper = new MySQLHelper(OfflineMovies.this, "dbMovies", null, 1);
        List<Search> items =  mySQLHelper.getMovies();

        RecyclerView recyclerViewMovies = findViewById(R.id.recyclerMoviesOffline);

        MovieRecyclerAdapter movieRecyclerAdapter = new MovieRecyclerAdapter(items, new MovieRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String item) {

            }
        });
        recyclerViewMovies.removeAllViews();
        recyclerViewMovies.setAdapter(movieRecyclerAdapter);
        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(OfflineMovies.this, RecyclerView.VERTICAL, false));

    }
}
