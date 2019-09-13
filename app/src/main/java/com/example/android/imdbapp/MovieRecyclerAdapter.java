package com.example.android.imdbapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.imdbapp.movie.Search;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieRecyclerAdapterHolder> {

    private final OnItemClickListener listener;
    List<Search>  items;


    public interface OnItemClickListener {
        void onItemClick(String item);
    }






    public MovieRecyclerAdapter(List<Search>  items, OnItemClickListener listener){
        this.items = items;
        this.listener = listener;
    }



    @NonNull
    @Override
    public MovieRecyclerAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item, viewGroup, false);
        MovieRecyclerAdapterHolder movieRecyclerAdapterHolder = new MovieRecyclerAdapterHolder(v);
        return movieRecyclerAdapterHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull MovieRecyclerAdapterHolder movieRecyclerAdapterHolder, int i) {

        movieRecyclerAdapterHolder.bind(items.get(i).getTitle(), listener);


        movieRecyclerAdapterHolder.txtTitle.setText(items.get(i).getTitle());
        movieRecyclerAdapterHolder.txtYear.setText("Year : " + items.get(i).getYear());
        movieRecyclerAdapterHolder.txtType.setText("Type : " + items.get(i).getType());

        Picasso.get().load(items.get(i).getPoster()).into(movieRecyclerAdapterHolder.imgPoster);

}

    @Override
    public int getItemCount() {
        return items.size();
    }


    class MovieRecyclerAdapterHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtYear;
        TextView txtType;
        TextView txtDirector;
        ImageView imgPoster;


        public MovieRecyclerAdapterHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtYear = itemView.findViewById(R.id.txtYear);
            txtType = itemView.findViewById(R.id.txtType);

            txtDirector = itemView.findViewById(R.id.txtDirector);
            imgPoster = itemView.findViewById(R.id.imgPoster);

        }

        public void bind(final String item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }


    }


}
