package comcast.stb.movielist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;


import com.wang.avi.AVLoadingIndicatorView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.R;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.MovieLink;
import comcast.stb.entity.MoviesItem;
import comcast.stb.purchase.moviepurchase.BuyMovieDialog;
import comcast.stb.utils.ApiManager;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

import static comcast.stb.StringData.MOVIE_CATEGORY;
import static comcast.stb.StringData.MOVIE_ID;
import static comcast.stb.StringData.MOVIE_LIST;
import static comcast.stb.StringData.PURCHASE_TYPE_BUY;
import static comcast.stb.StringData.VIDEO_URL;


public class MovieListActivity extends AppCompatActivity implements MovieListRecyclerAdapter.OnMovieListInteraction {
    @BindView(R.id.movie_list_recycler)
    RecyclerView movieListRecycler;
    @BindView(R.id.progressbar_movielist)
    AVLoadingIndicatorView progressBar;
    @BindView(R.id.txt_movie)
    TextView movieCategoryName;
    @BindView(R.id.txt_nomovie)
    TextView noMovie;
    @BindView(R.id.txt_currentSelection)
    TextView currentSelection;
    private ArrayList<MoviesItem> moviesList;
    private Realm realm;
    LoginData loginData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        moviesList = getIntent().getParcelableArrayListExtra(MOVIE_LIST);
        movieCategoryName.setText(getIntent().getStringExtra(MOVIE_CATEGORY));
        realm = Realm.getDefaultInstance();
        loginData = realm.where(LoginData.class).findFirst();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startAnim();
        if (moviesList.size() > 0) {
            noMovie.setVisibility(View.GONE);
            populateMovieList();
            movieListRecycler.setVisibility(View.VISIBLE);
        }
        else {
            stopAnim();
            noMovie.setVisibility(View.VISIBLE);
            movieListRecycler.setVisibility(View.GONE);
        }

    }

    private void populateMovieList() {
        stopAnim();
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        movieListRecycler.setLayoutManager(manager);
        MovieListRecyclerAdapter movieRecyclerAdapter = new MovieListRecyclerAdapter(this, moviesList);
        movieListRecycler.setAdapter(movieRecyclerAdapter);


    }

    @Override
    public void onMovieClicked(MoviesItem movie) {
        switch (movie.getSubscriptionStatus()) {
            case PURCHASE_TYPE_BUY:
                showBuyDialog(movie);
                break;
            default:
                if (movie.getExpiryFlag())
                    showBuyDialog(movie);
                else
                    getMovieLink(movie);
                break;
        }
    }

    @Override
    public void onMovieSelected(MoviesItem movie) {
        currentSelection.setText(movie.getMovieName());
    }

    @Override
    public void onMovieDeselected() {

    }

    private void showBuyDialog(MoviesItem movie) {
        BuyMovieDialog buyMovieDialog =
                BuyMovieDialog.newInstance(movie);
        buyMovieDialog.show(getSupportFragmentManager(), "fragmentDialog");
    }

    private void getMovieLink(final MoviesItem movie) {
        startAnim();
        Retrofit retrofit = ApiManager.getAdapter();
        final int movieId = movie.getMovieId();
        final MovieListApiInterface channelApiInterface = retrofit.create(MovieListApiInterface.class);
        Observable<Response<MovieLink>> observable = channelApiInterface.getChannelLink(movieId, loginData.getToken());
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<MovieLink>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<MovieLink> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            Intent intent = new Intent(MovieListActivity.this, MoviePlayActivity.class);
                            intent.putExtra(MOVIE_ID, movieId);
                            intent.putExtra(VIDEO_URL, value.body().getLink());
                            startActivity(intent);
                            stopAnim();
//                            startControllersTimer();
                        } else if (responseCode == 403) {
                            onErrorOccured("403", movie);
                        } else {
                            onErrorOccured(value.message(), movie); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            onErrorOccured("No Internet Connection", movie);
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            onErrorOccured("Couldn't connect to server", movie);
                        } else {
                            onErrorOccured("Error Occured", movie);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void stopAnim() {
        progressBar.smoothToHide();
    }

    public void updateProgress(boolean showProgress) {
        if (showProgress)
            startAnim();
        else
            stopAnim();
    }

    private void startAnim() {
        progressBar.smoothToShow();
    }

    private void onErrorOccured(String message, final MoviesItem movie) {
        stopAnim();
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getMovieLink(movie);
                    }
                }).setActionTextColor(getResources().getColor(R.color.white_color));
        snackbar.show();
    }
}
