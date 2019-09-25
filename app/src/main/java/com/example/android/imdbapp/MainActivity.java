package com.example.android.imdbapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.imdbapp.movie.MovieClass;
import com.example.android.imdbapp.movie.Search;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleBiFunction;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.protocol.HTTP;

public class MainActivity extends AppCompatActivity {

    //TODO CHECK IF A FILM EXICT , Dont add it any more
    //TODO IF No Movie has been searched.dont save .clear current list



     //String url = "https://www.omdbapi.com/?t=saw&apikey=d347e962" ;

    String BaseURL = "http://www.omdbapi.com/?apikey=d347e962";
    List<Search> currentList = new ArrayList<>();

    RecyclerView recyclerViewMovies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MySQLHelper mySQLHelper = new MySQLHelper(MainActivity.this, "dbMovies", null, 1);

        Button btSearch = findViewById(R.id.btnSearch);
        Button btSave = findViewById(R.id.btnSave);
        final EditText edtSearch = findViewById(R.id.edtSearch);

        recyclerViewMovies= findViewById(R.id.recyclerMovies);

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sSearch ;
                sSearch = edtSearch.getText().toString();

                if (!sSearch.isEmpty()){

                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    Search(sSearch);
                }else{
                     Toast.makeText(MainActivity.this, "Please Enter a Title", Toast.LENGTH_LONG).show();
                }
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveToDb();


            }
        });

        btSave.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent=new Intent(MainActivity.this, OfflineMovies.class);
               startActivity(intent);
               return true;

            }
        });


    }

    public void Search(String sSearch){
        final AsyncHttpClient client = new AsyncHttpClient() ;

        String finalURL = BaseURL + "&s=" + sSearch;
        client.get(finalURL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {

                    String res = response.getString("Response");
                    if (res.equalsIgnoreCase("True")) {
                        Gson gson = new Gson();
                        MovieClass movieClass = gson.fromJson(response.toString(), MovieClass.class);
                        List<Search> search = movieClass.getSearch();

                        FillRecyclerView(search);
                    }

                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(MainActivity.this,"Error Loading data." + throwable.getMessage(), Toast.LENGTH_LONG ).show();
            }

        }) ;

    }

    public void FillRecyclerView(List<Search> search){
        currentList = search;

        MovieRecyclerAdapter movieRecyclerAdapter = new MovieRecyclerAdapter(search, new MovieRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String item) {

            }
        });
        recyclerViewMovies.removeAllViews();
        recyclerViewMovies.setAdapter(movieRecyclerAdapter);
        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));

    }

    public void SaveToDb(){

        MySQLHelper mySQLHelper = new MySQLHelper(MainActivity.this, "dbMovies", null, 1);

        int added = 0;
        int updated = 0;
        String title = "";
        String msg = "";

        if ( currentList.size()>0 ){

            for (int i = 0; i < currentList.size(); i++) {
                Search movie = currentList.get(i);
                if (!mySQLHelper.isExist(movie.getImdbID())) {
                    Log.i("MYTAG", "added");
                    mySQLHelper.inserToDB(movie);
                    added += 1;
                }else{
                    mySQLHelper.updateToDB(movie);
                    updated+= 1;
                }

            }

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Save Result");
            alertDialog.setMessage("Database updated ! " + "\n" +
                                    "Rows Added= " +added + "\n" +
                                    "Rows Updated= " +updated );

            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }else{
            Toast.makeText(MainActivity.this, "No Movie to add to Database", Toast.LENGTH_LONG).show();
        }


    }


}
