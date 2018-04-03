package comcast.stb.movielist;




import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import comcast.stb.entity.BuyResponse;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.MovieCategory;
import comcast.stb.entity.NewToken;
import comcast.stb.logout.LogoutPresImpl;
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
import static comcast.stb.StringData.MOVIE_CATEGORY_ERROR;

/**
 * Created by nitv on 12/26/17.
 */

public class MovieModel implements MovieListApiInterface.MovieWithCategoryInteractor,TokenRefreshApiInterface.TokenRefreshView {
    MovieListApiInterface.MovieWithCategoryListener movieWithCategoryListener;
    LogoutPresImpl logoutPres;
    TokenPresImpl tokenPres;
    public MovieModel(MovieListApiInterface.MovieWithCategoryListener movieWithCategoryListener,LogoutPresImpl logoutPres) {
        this.movieWithCategoryListener = movieWithCategoryListener;
        this.logoutPres = logoutPres;
        tokenPres = new TokenPresImpl(this);
    }

    @Override
    public void getMoviesWithCategory(final String token) {
        Retrofit retrofit = ApiManager.getAdapter();
        final MovieListApiInterface MovieListApiInterface = retrofit.create(MovieListApiInterface.class);


        Observable<Response<List<MovieCategory>>> observable = MovieListApiInterface.getMoviesWithCategory(token);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<List<MovieCategory>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<MovieCategory>> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            movieWithCategoryListener.takeMoviesWithCategory(value.body());
                        } else if (responseCode == 403) {
                            movieWithCategoryListener.onErrorOccured("403",null,MOVIE_CATEGORY_ERROR);
                        } else if (responseCode == 401) {
                            tokenPres.refreshTheToken(token);
                        }else {
                            movieWithCategoryListener.onErrorOccured(value.message(),null,MOVIE_CATEGORY_ERROR); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException ||  e instanceof ConnectException) {
                            movieWithCategoryListener.onErrorOccured("No Internet Connection",null,MOVIE_CATEGORY_ERROR);
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            movieWithCategoryListener.onErrorOccured("Couldn't connect to server",null,MOVIE_CATEGORY_ERROR);
                        }
                        else {
                            movieWithCategoryListener.onErrorOccured("Error Occured",null,MOVIE_CATEGORY_ERROR);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void buyMovie( int duration, int movieId,final String token) {
        Retrofit retrofit = ApiManager.getAdapter();
        final MovieListApiInterface MovieListApiInterface = retrofit.create(MovieListApiInterface.class);


        Observable<Response<BuyResponse>> observable = MovieListApiInterface.buyMovie(duration,movieId,token);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<BuyResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<BuyResponse> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            movieWithCategoryListener.onMovieBought(value.body());
                        } else if (responseCode == 403) {
                            movieWithCategoryListener.onErrorOccured("403",null,BUY_ERROR);
                        } else if (responseCode == 401) {
                            tokenPres.refreshTheToken(token);
                        }else {
                            movieWithCategoryListener.onErrorOccured(value.message(),null,BUY_ERROR); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException ||  e instanceof ConnectException) {
                            movieWithCategoryListener.onErrorOccured("No Internet Connection",null,MOVIE_CATEGORY_ERROR);
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            movieWithCategoryListener.onErrorOccured("Couldn't connect to server",null,MOVIE_CATEGORY_ERROR);
                        }
                        else {
                            movieWithCategoryListener.onErrorOccured("Error Occured",null,MOVIE_CATEGORY_ERROR);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

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
        getMoviesWithCategory(newToken.getToken());
    }

    @Override
    public void onError(String message) {
        movieWithCategoryListener.onErrorOccured(message,null,MOVIE_CATEGORY_ERROR);
    }

    @Override
    public void logout() {
        logoutPres.logout();
    }
}
