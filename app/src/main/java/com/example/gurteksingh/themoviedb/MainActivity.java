package com.example.gurteksingh.themoviedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;


import com.example.gurteksingh.themoviedb.data.FavouriteDBHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView moviesList,list;
    private MoviesAdapter adapter;
    private FavMovieAdapter fadapter;
    private MoviesRepository moviesRepository;
    private List<Genre> movieGenres;
    private String sortBy = MoviesRepository.POPULAR;
    Toolbar toolbar;

    private boolean isFetchingMovies;
    private FavouriteDBHelper favouriteDBHelper;
    private int currentPage = 1;
    int flag = 0;
    private SearchView searchView;
    List<Movie> favmovies,fmovies;
    private  MenuItem searchItem,mitem;
    String q;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        favmovies = new ArrayList<>();
        setSupportActionBar(toolbar);
        moviesRepository = MoviesRepository.getInstance();
        moviesList = findViewById(R.id.movies_list);

        setupOnScrollListener();

        getGenres();
    }

    @Override
    protected void onStart() {
        favouriteDBHelper = new FavouriteDBHelper(MainActivity.this);
        fmovies = new ArrayList<>();
        fmovies.clear();
        fmovies.addAll(favouriteDBHelper.getAllFavourite());
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_movies, menu);

        searchItem = menu.findItem(R.id.search);
        mitem = menu.findItem(R.id.sort);

        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                flag =1;
                currentPage =1;
                q = query;
                getSearch(currentPage,query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                flag =1;
                q = newText;
                currentPage=1;
                getSearch(currentPage,newText);
                return false;

            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                showSortMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

        private void showSortMenu() {
            PopupMenu sortMenu = new PopupMenu(this, findViewById(R.id.sort));
            sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    /*
                     * Every time we sort, we need to go back to page 1
                     */
                    currentPage = 1;

                    switch (item.getItemId()) {
                        case R.id.popular:
                            sortBy = MoviesRepository.POPULAR;
                            getMovies(currentPage);
                            return true;
                        case R.id.top_rated:
                            sortBy = MoviesRepository.TOP_RATED;
                            getMovies(currentPage);
                            return true;
                        case R.id.upcoming:
                            sortBy = MoviesRepository.UPCOMING;
                            getMovies(currentPage);
                            return true;
                        case R.id.now_playing:
                            sortBy = MoviesRepository.NOW_PLAYING;
                            getMovies(currentPage);
                            return true;
                        case R.id.favourites:
                            flag =2;
                            getFavourites(currentPage);
                        default:
                            return false;
                    }
                }
            });
            sortMenu.inflate(R.menu.menu_movies_sort);
            sortMenu.show();
        }

    @Override
    public void onBackPressed() {
        if(flag ==2)
        {
            startActivity(new Intent(MainActivity.this,MainActivity.class));
        }
        else {
            super.onBackPressed();
        }
    }

    private void setupOnScrollListener() {
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        moviesList.setLayoutManager(manager);
        moviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (!isFetchingMovies && flag == 0) {
                    getMovies(currentPage + 1);
                }
                else
                if (!isFetchingMovies && flag == 1)
                {
                    getSearch(currentPage + 1,q);
                }
                else
                if(!isFetchingMovies && flag == 2)
                {
                    getFavourites(currentPage +1);
                }
            }
        });
    }
    private void getGenres() {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                movieGenres = genres;
                getMovies(currentPage);
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }
    private void getMovies(int page) {
        isFetchingMovies=true;
        moviesRepository.getMovies(page ,sortBy,new OnGetMoviesCallback() {
            @Override
            public void onSuccess(int page,List<Movie> movies) {
                Log.d("MoviesRepository", "Current Page = " + page);
                if (adapter == null) {
                    adapter = new MoviesAdapter(movies, movieGenres ,callback);
                    moviesList.setAdapter(adapter);
                } else {
                    if(page==1)
                    {
                        adapter.clearMovies();
                    }
                    adapter.appendMovies(movies);
                }
                currentPage = page;
                isFetchingMovies = false;

                setTitle();
            }

            @Override
            public void onError() {
                showError();
            }
        });
      }

    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(MainActivity.this, MovieActivity.class);
            intent.putExtra(MovieActivity.MOVIE_ID, movie.getId());
            startActivity(intent);
        }
    };
        private void showError(){
            Toast.makeText(MainActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }

    private void setTitle() {
        switch (sortBy) {
            case MoviesRepository.POPULAR:
                setTitle(getString(R.string.popular));
                break;
            case MoviesRepository.TOP_RATED:
                setTitle(getString(R.string.top_rated));
                break;
            case MoviesRepository.UPCOMING:
                setTitle(getString(R.string.upcoming));
                break;
            case MoviesRepository.NOW_PLAYING:
                setTitle(getString(R.string.now_playing));
                break;
        }
    }

    public void getFavourites(int page) {
        searchView.setVisibility(View.GONE);
        searchItem.setVisible(false);
        mitem.setVisible(false);
        isFetchingMovies=true;
        list = findViewById(R.id.movies_list);
        setTitle("Favourites");
        if(fadapter==null) {
            fadapter = new FavMovieAdapter(favmovies, callback);
            list.setAdapter(fadapter);
        }
        else {
            if(page==1)
            {
                fadapter.clearMovies();
            }
            fadapter.appendMovies(favmovies);
        }
        currentPage = page;
        isFetchingMovies = false;
        favouriteDBHelper = new FavouriteDBHelper(MainActivity.this);
        favmovies.clear();
        favmovies.addAll(favouriteDBHelper.getAllFavourite());

    }

    private void getSearch(int page, final String query) {
        isFetchingMovies=true;
        moviesRepository.getMovieSearch(page ,query,new OnGetMoviesCallback() {

            @Override
            public void onSuccess(int page,List<Movie> movies) {
                Log.d("MoviesRepository", "Current Page = " + page);
                if (adapter == null) {
                    adapter = new MoviesAdapter(movies, movieGenres ,callback);
                    moviesList.setAdapter(adapter);
                } else {
                    if(page==1)
                    {
                        adapter.clearMovies();
                    }
                    adapter.appendMovies(movies);
                }
                currentPage = page;
                isFetchingMovies = false;
                setTitle(query);
            }

            @Override
            public void onError() {
                // Do Nothing
            }
        });
    }
    }

