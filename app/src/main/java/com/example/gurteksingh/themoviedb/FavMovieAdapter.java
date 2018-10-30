package com.example.gurteksingh.themoviedb;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class FavMovieAdapter extends RecyclerView.Adapter<FavMovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private OnMoviesClickCallback callback;
    private static String POSTER_URL = "https://image.tmdb.org/t/p/w185";

    public FavMovieAdapter(List<Movie> movies,OnMoviesClickCallback callback) {
        this.callback = callback;
        this.movies = movies;
    }

    public void appendMovies(List<Movie> moviesToAppend) {
        movies.addAll(moviesToAppend);
        notifyDataSetChanged();
    }

    public void clearMovies() {
        movies.clear();
        notifyDataSetChanged();
    }

    @Override
    public FavMovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_movie, parent, false);
        return new FavMovieAdapter.MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavMovieAdapter.MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView releaseDate;
        TextView title;
        TextView rating;
        ImageView poster;
        Movie movie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            releaseDate = itemView.findViewById(R.id.release_date);
            title = itemView.findViewById(R.id.title);
            rating = itemView.findViewById(R.id.rating);
            poster = itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(movie);
                }
            });
        }

        public void bind(Movie movie) {
            this.movie = movie;
            releaseDate.setText(movie.getReleaseDate().split("-")[0]);
            title.setText(movie.getTitle());
            rating.setText(String.valueOf(movie.getRating()));
            String s1 = POSTER_URL + movie.getPosterPath();
            Glide.with(itemView)
                    .load(s1)
                    .apply(RequestOptions.placeholderOf(R.drawable.load))
                    .into(poster);
        }

    }
}
