package comcast.stb.movielist;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.R;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.MovieCategory;
import comcast.stb.entity.MovieLink;
import comcast.stb.entity.MoviesItem;
import comcast.stb.livetv.ChannelRecyclerAdapter;
import comcast.stb.logout.LogoutApiInterface;
import comcast.stb.logout.LogoutPresImpl;
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

import static comcast.stb.StringData.MOVIE_ID;
import static comcast.stb.StringData.PURCHASE_TYPE_BUY;
import static comcast.stb.StringData.VIDEO_URL;

public class MovieNewActivity extends AppCompatActivity implements MovieListApiInterface.MovieWithCategoryView,
        MovieCategoryRecyclerAdapter.OnMovieCategoryInteraction, LogoutApiInterface.LogoutView,MovieListRecyclerAdapter.OnMovieListInteraction{

    @BindView(R.id.img_movie_logout)
    ImageView logout;
    @BindView(R.id.recycler_movie_list)
    RecyclerView movieListRecycler;
    @BindView(R.id.recycler_movie_category_list)
    RecyclerView movieCategoryRecycler;
    @BindView(R.id.current_movie_category)
    TextView selectedCategory;
    @BindView(R.id.txt_movie_description)
    TextView movieDescription;

    @BindView(R.id.txt_movie_price)
    TextView moviePrice;

    @BindView(R.id.progressbar_movieCategory)
    com.wang.avi.AVLoadingIndicatorView progressbar;

    @BindView(R.id.txt_movie_username)
    TextView userName;
    @BindView(R.id.buy_movie_option_container)
    LinearLayout buylayout;

    @BindView(R.id.movie_list_layout)
    LinearLayout movieListContainer;
    @BindView(R.id.img_movie_desc)
    ImageView imgDescription;

    ArrayList<MovieCategory> movieCategoryArrayList;
    MovieListRecyclerAdapter movieListRecyclerAdapter;
    MoviePresImpl moviePresenter;
    private Realm realm;
    LoginData loginData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_new);
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
    public void successfulLogout() {

    }

    @Override
    public void setMoviesWithCategory(List<MovieCategory> movieCategoryList) {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        MovieCategoryRecyclerAdapter categoryRecyclerAdapter = new MovieCategoryRecyclerAdapter(this, (ArrayList<MovieCategory>) movieCategoryList);
        movieCategoryRecycler.setLayoutManager(manager);
        movieCategoryRecycler.setAdapter(categoryRecyclerAdapter);
        stopAnim();
        movieCategoryRecycler.requestFocus();
        categoryRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorOccured(String message) {

    }

    @Override
    public void showProgress() {

    }
    private void stopAnim() {
        progressbar.smoothToHide();
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onCategoryClicked(MovieCategory movieCategory) {
        MovieCategory currentCategory=movieCategory;
        selectedCategory.setText(currentCategory.getCategoryTitle());
        movieListRecyclerAdapter = new MovieListRecyclerAdapter(this, (ArrayList<MoviesItem>) movieCategory.getMovies());
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        movieListRecycler.setLayoutManager(manager);
        movieListRecycler.setAdapter(movieListRecyclerAdapter);
    }

    @Override
    public void onMovieClicked(MoviesItem movie) {
        getMovieLink(movie);
    }

    private void getMovieLink(final MoviesItem movie) {
        startAnim();
        Retrofit retrofit = ApiManager.getAdapter();
        final int movieId = movie.getMovieId();
        final MovieListApiInterface movieListApiInterface = retrofit.create(MovieListApiInterface.class);
        Observable<Response<MovieLink>> observable = movieListApiInterface.getChannelLink(movieId, loginData.getToken());
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<MovieLink>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<MovieLink> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            Intent intent = new Intent(MovieNewActivity.this, MoviePlayActivity.class);
                            intent.putExtra(MOVIE_ID, movieId);
                            intent.putExtra(VIDEO_URL, value.body().getLink());
                            startActivity(intent);
                            stopAnim();
//                            startControllersTimer();
                        } else if (responseCode == 403) {
                            onErrorOccured("403");
                        } else {
                            onErrorOccured(value.message()); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            onErrorOccured("No Internet Connection");
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            onErrorOccured("Couldn't connect to server");
                        } else {
                            onErrorOccured("Error Occured");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onMovieSelected(MoviesItem movie) {
        movieListContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.white_selection));
        updateMovieDescriptionUI(movie);
    }

    private void updateMovieDescriptionUI(MoviesItem movie) {
        Picasso.with(this)
                .load(movie.getMoviePicture())
                .into(imgDescription);
        switch (movie.getSubscriptionStatus()) {
            case PURCHASE_TYPE_BUY:
                buylayout.setVisibility(View.VISIBLE);
                break;
            default:
                if (movie.getExpiryFlag()) {
                    buylayout.setVisibility(View.VISIBLE);
                } else {
                    buylayout.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onMovieDeselected() {
        movieListContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.white_no_selection));
    }
}
