package comcast.stb.movielist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.R;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.MovieCategory;
import comcast.stb.entity.MoviesItem;
import comcast.stb.login.LoginActivity;
import comcast.stb.logout.LogoutApiInterface;
import comcast.stb.logout.LogoutPresImpl;
import io.realm.Realm;

import static comcast.stb.StringData.MOVIE_CATEGORY;
import static comcast.stb.StringData.MOVIE_LIST;


public class MovieCategoryActivity extends AppCompatActivity implements MovieListApiInterface.MovieWithCategoryView,
        MovieCategoryRecyclerAdapter.OnMovieCategoryInteraction, LogoutApiInterface.LogoutView {
    @BindView(R.id.moviecategory_recycler_list)
    RecyclerView movieCategoryRecycler;

    @BindView(R.id.progressbar_movieCategory)
    com.wang.avi.AVLoadingIndicatorView progressbar;

    @BindView(R.id.txt_username)
    TextView userName;

    MoviePresImpl moviePresenter;
    private Realm realm;
    LoginData loginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_category);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        loginData = realm.where(LoginData.class).findFirst();
        String authToken = loginData.getToken();
        LogoutPresImpl logoutPres = new LogoutPresImpl(this);
        moviePresenter = new MoviePresImpl(this, logoutPres);
        startAnim();
        userName.setText(loginData.getUser().getName());
        moviePresenter.getMoviesWithCategory(authToken);
    }


    private void startAnim() {
        progressbar.smoothToShow();
    }
    @Override
    public void setMoviesWithCategory(List<MovieCategory> movieCategoryList) {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        MovieCategoryRecyclerAdapter categoryRecyclerAdapter = new MovieCategoryRecyclerAdapter(MovieCategoryActivity.this, (ArrayList<MovieCategory>) movieCategoryList);
        movieCategoryRecycler.setLayoutManager(manager);
        movieCategoryRecycler.setAdapter(categoryRecyclerAdapter);
        stopAnim();

    }



    @Override
    public void onErrorOccured(String message) {
        stopAnim();
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        moviePresenter.getMoviesWithCategory(loginData.getToken());
                    }
                }).setActionTextColor(getResources().getColor(R.color.white_color));
        snackbar.show();
    }

    private void stopAnim() {
        progressbar.smoothToHide();
    }

    @Override
    public void showProgress() {
        startAnim();

    }

    @Override
    public void hideProgress() {
        stopAnim();
    }

    @Override
    public void onCategoryClicked(final MovieCategory movieCategory) {
        showMovieList(movieCategory.getCategoryTitle(),movieCategory.getMovies());

    }

    private void showMovieList(String categoryTitle,List<MoviesItem> moviesList) {
        Intent movieIntent=new Intent(this,MovieListActivity.class);
        movieIntent.putParcelableArrayListExtra(MOVIE_LIST, (ArrayList<? extends Parcelable>) moviesList);
        movieIntent.putExtra(MOVIE_CATEGORY,categoryTitle);
        startActivity(movieIntent);
    }


    @Override
    public void successfulLogout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}

