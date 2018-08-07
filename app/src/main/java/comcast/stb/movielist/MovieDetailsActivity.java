package comcast.stb.movielist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comcast.stb.R;
import comcast.stb.entity.BuyResponse;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.MovieCategory;
import comcast.stb.entity.MovieLink;
import comcast.stb.entity.MoviesItem;
import comcast.stb.entity.NewToken;
import comcast.stb.entity.events.BuyEvent;
import comcast.stb.purchase.moviepurchase.BuyMovieDialog;
import comcast.stb.tokenrefresh.TokenPresImpl;
import comcast.stb.tokenrefresh.TokenRefreshApiInterface;
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

import static comcast.stb.StringData.BUY_ERROR;
import static comcast.stb.StringData.LANGUAGE_ENGLISH;
import static comcast.stb.StringData.MOVIE_CATEGORY;
import static comcast.stb.StringData.MOVIE_CATEGORY_ERROR;
import static comcast.stb.StringData.MOVIE_ITEM;
import static comcast.stb.StringData.MOVIE_PLAY_ERROR;
import static comcast.stb.StringData.PREF_LANG;
import static comcast.stb.StringData.PURCHASE_TYPE_BOUGHT;
import static comcast.stb.StringData.PURCHASE_TYPE_BUY;
import static comcast.stb.StringData.TOKEN_ERROR;
import static comcast.stb.StringData.VIDEO_URL;

public class MovieDetailsActivity extends AppCompatActivity implements MovieDialogFragment.OnFragmentInteractionListener,TokenRefreshApiInterface.TokenRefreshView {
    private static final String BUY_MOVIE_FRAGMENT ="buy_movie_fragment" ;
    @BindView(R.id.movie_picture)
    ImageView movieImage;
    @BindView(R.id.movie_details)
    LinearLayout movieDetailsLayout;
    @BindView(R.id.movie_name)
    TextView movieName;
    @BindView(R.id.userBalance)
    TextView userBalance;
    @BindView(R.id.movie_desc)
    TextView movieDescription;

    @BindView(R.id.txt_price)
    TextView moviePrice;

    @BindView(R.id.movie_details_progress)
    AVLoadingIndicatorView progressbar;

    @BindView(R.id.watch_button)
    LinearLayout watchMovie;

    @BindView(R.id.buy_button)
    LinearLayout buyMovie;

    @BindView(R.id.simiar_movies_recycler)
    RecyclerView similarRecycler;

    private MovieCategory currentMovieCategory;

    private TokenPresImpl tokenPres;
    private MoviesItem currentMovie;

    private ArrayList<MoviesItem> similarMoviesList;

    private LoginData loginData;
    private Realm realm;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        language= PreferenceManager.getDefaultSharedPreferences(this).getString(PREF_LANG,LANGUAGE_ENGLISH);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        if (getIntent().getExtras() == null) {
            Toast.makeText(this, "Something went Wrong, Please try again", Toast.LENGTH_LONG).show();
            finish();

        } else {
            currentMovie = getIntent().getParcelableExtra(MOVIE_ITEM);
            currentMovieCategory = getIntent().getParcelableExtra(MOVIE_CATEGORY);
        }
        realm = Realm.getDefaultInstance();
        loginData = realm.where(LoginData.class).findFirst();
        userBalance.setText("$" + loginData.getUser().getBalance());
        setMovieDetails();
    }

    private void setMovieDetails() {
        Picasso.with(MovieDetailsActivity.this)
                .load(currentMovie.getMoviePicture())
                .resize(250, 350)
                .placeholder(R.drawable.placeholder)
                .into(movieImage);


        movieName.setText(currentMovie.getMovieName());

        movieDescription.setText(currentMovie.getMovieDesc());
        moviePrice.setText("$"+ currentMovie.getMoviePrice());

        setButtons();
        setSimilarMovies();


    }

    @OnClick(R.id.watch_button)
    public void onWatchClick() {
        getMovieLink(currentMovie);
    }

    @OnClick(R.id.buy_button)
    public void onBuyClick(){
        showBuyDialog();
    }

    private void showBuyDialog() {
        BuyMovieDialog buyMovieDialog = BuyMovieDialog.newInstance(currentMovie);
        buyMovieDialog.show(getSupportFragmentManager(), BUY_MOVIE_FRAGMENT);
    }

    private void purchaseMovie(MoviesItem currentMovie) {
        startAnim();
        Retrofit retrofit = ApiManager.getAdapter();
        final int movieId = currentMovie.getMovieId();
        final MovieListApiInterface movieListApiInterface = retrofit.create(MovieListApiInterface.class);
        Observable<Response<BuyResponse>> observable = movieListApiInterface.buyMovie(1,movieId,loginData.getToken(),language);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<BuyResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<BuyResponse> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            onMovieBought(value.body());
                        } else if (responseCode == 403) {
                           onErrorOccured("403",null,BUY_ERROR);
                        } else if (responseCode == 401) {
                            tokenPres.refreshTheToken(loginData.getToken());
                        }else {
                           onErrorOccured(value.message(),null,BUY_ERROR); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException ||  e instanceof ConnectException) {
                           onErrorOccured("No Internet Connection",null,BUY_ERROR);
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            onErrorOccured("Couldn't connect to server",null,BUY_ERROR);
                        }
                        else {
                            onErrorOccured("Error Occured",null,BUY_ERROR);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void onMovieBought(BuyResponse body) {
        Toast.makeText(MovieDetailsActivity.this, "Movie successfully purchased", Toast.LENGTH_SHORT).show();
        currentMovie.setSubscriptionStatus(PURCHASE_TYPE_BOUGHT);
        currentMovie.setExpiryFlag(false);
        changeButtonVisibility(true);
        userBalance.setText(getUpdatedUserbalance());
    }

    private String getUpdatedUserbalance() {
        Double movieRate=currentMovie.getMoviePrice();
        Double currentBalance= Double.valueOf(loginData.getUser().getBalance());
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(currentBalance-movieRate);
    }

    private void getMovieLink(final MoviesItem currentMovie) {
        startAnim();
        Retrofit retrofit = ApiManager.getAdapter();
        final int movieId = currentMovie.getMovieId();
        final MovieListApiInterface movieListApiInterface = retrofit.create(MovieListApiInterface.class);
        Observable<Response<MovieLink>> observable = movieListApiInterface.getChannelLink(movieId, loginData.getToken(),language);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<MovieLink>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<MovieLink> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            Intent intent = new Intent(MovieDetailsActivity.this, MovieExoPlay.class);
//                            intent.putExtra(MOVIE_ID, movie);
                            intent.putExtra(VIDEO_URL, value.body().getLink());
                            startActivity(intent);
                            stopAnim();
//                            startControllersTimer();
                        } else if (responseCode == 403) {
                            onErrorOccured("403", currentMovie, MOVIE_PLAY_ERROR);
                        } else {
                            onErrorOccured(value.message(), currentMovie, MOVIE_PLAY_ERROR); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            onErrorOccured("No Internet Connection", currentMovie, MOVIE_PLAY_ERROR);
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            onErrorOccured("Couldn't connect to server", currentMovie, MOVIE_PLAY_ERROR);
                        } else {
                            onErrorOccured("Error Occured", currentMovie, MOVIE_PLAY_ERROR);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void onErrorOccured(String message, MoviesItem currentMovie, String errorType) {
        stopAnim();
        if (errorType.equals(BUY_ERROR)) {
            Toast.makeText(MovieDetailsActivity.this, "Movie Couldn't be purchased", Toast.LENGTH_SHORT).show();
        } else {
            MovieDialogFragment infoDialogFragment = MovieDialogFragment.newInstance("", message, currentMovie, errorType, true);
            infoDialogFragment.show(getSupportFragmentManager(), "MovieErrorFragment");
        }
    }

    private void startAnim() {
        progressbar.smoothToShow();
    }

    private void stopAnim() {
        progressbar.smoothToHide();
    }

    private void setSimilarMovies() {
        similarMoviesList = (ArrayList<MoviesItem>) currentMovieCategory.getMovies();
        similarMoviesList.remove(currentMovie);
        SingleItemListAdapter itemListAdapter = new SingleItemListAdapter(MovieDetailsActivity.this, similarMoviesList);
        LinearLayoutManager manager = new CenterLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        similarRecycler.setLayoutManager(manager);
        similarRecycler.setHasFixedSize(true);
        similarRecycler.setAdapter(itemListAdapter);
        itemListAdapter.SetOnItemFocusedListener(new SingleItemListAdapter.OnItemSelectListener() {
            @Override
            public void onItemSelected(View view, int position) {
                similarRecycler.smoothScrollToPosition(position);
            }
        });

        itemListAdapter.SetOnItemClickListener(new SingleItemListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int itemPosition, MoviesItem model) {
                Intent intent = new Intent(MovieDetailsActivity.this, MovieDetailsActivity.class);
                intent.putExtra(MOVIE_ITEM, model);
                intent.putExtra(MOVIE_CATEGORY, currentMovieCategory);
                startActivity(intent);
                MovieDetailsActivity.this.finish();
            }
        });

    }

    private void setButtons() {
        switch (currentMovie.getSubscriptionStatus()) {
            case PURCHASE_TYPE_BUY:
                //moviePresenter.buyMovie(1, currentMovie.getMovieId(), loginData.getToken());
                changeButtonVisibility(false);
                break;
            default:
                if (currentMovie.getExpiryFlag()) {
//                    moviePresenter.buyMovie(1, currentMovie.getMovieId(), loginData.getToken());
                    changeButtonVisibility(false);
                } else {
                    changeButtonVisibility(true);

                }
                break;

        }
    }

    private void changeButtonVisibility(boolean canWatch) {
        if (canWatch) {
            watchMovie.setFocusable(true);
            watchMovie.setEnabled(true);
            buyMovie.setFocusable(false);
            buyMovie.setEnabled(false);
        } else {
            buyMovie.setFocusable(true);
            buyMovie.setEnabled(true);
            watchMovie.setFocusable(false);
            watchMovie.setEnabled(false);

        }
    }

    @Override
    public void onRetryBtnInteraction(String errorType, MoviesItem movie) {
        onDismissBtnInteraction();
        switch (errorType) {
            case MOVIE_PLAY_ERROR:
                getMovieLink(movie);
                break;
        }
    }

    @Override
    public void onDismissBtnInteraction() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment infoDialogFragment = manager.findFragmentByTag("MovieErrorFragment");
        if (infoDialogFragment != null)
            manager.beginTransaction().remove(infoDialogFragment).commit();

    }

    @Override
    public void newToken(final NewToken newToken) {
        Realm mRealm = Realm.getDefaultInstance();
        final LoginData loginDatas = mRealm.where(LoginData.class).findFirst();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                loginDatas.setToken(newToken.getToken());
                realm.insertOrUpdate(loginDatas);
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BuyEvent event) {
        stopAnim();
        Toast.makeText(MovieDetailsActivity.this, "Movie successfully purchased", Toast.LENGTH_SHORT).show();
        currentMovie.setSubscriptionStatus(PURCHASE_TYPE_BOUGHT);
        currentMovie.setExpiryFlag(false);
        changeButtonVisibility(true);
        userBalance.setText(getUpdatedUserbalance());}

    @Override
    public void onError(String message) {
        onErrorOccured(message,currentMovie,TOKEN_ERROR);

    }

    @Override
    public void logout() {

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void updateProgress(boolean b) {
        if(b){
            startAnim();
        }else{
            stopAnim();
        }
    }
}
