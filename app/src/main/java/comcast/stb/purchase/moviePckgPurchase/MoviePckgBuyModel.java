package comcast.stb.purchase.moviePckgPurchase;




import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import comcast.stb.entity.BuyResponse;
import comcast.stb.utils.ApiManager;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by nitv on 2/1/18.
 */

public class MoviePckgBuyModel implements MoviePckgApiInterface.MoviePcgkBuyInteractor {
    MoviePckgApiInterface.MoviePcgkBuyListener moviePcgkBuyListener;

    public MoviePckgBuyModel(MoviePckgApiInterface.MoviePcgkBuyListener moviePcgkBuyListener) {
        this.moviePcgkBuyListener = moviePcgkBuyListener;
    }

    @Override
    public void buyMoviePcgk(final int packageId, int months, String token) {
        Retrofit retrofit = ApiManager.getAdapter();
        final MoviePckgApiInterface MoviePckgApiInterface = retrofit.create(MoviePckgApiInterface.class);


        Observable<Response<BuyResponse>> observable = MoviePckgApiInterface.buyMoviePackages(packageId, months, token);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<BuyResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<BuyResponse> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            moviePcgkBuyListener.setMoviePcgkBuyResponse(value.body());
                        } else if (responseCode == 403) {
                            moviePcgkBuyListener.onMoviePckgBuyError(packageId,"403");
                        } else if (responseCode == 401) {
//                            tokenPres.refreshTheToken(token);
                        } else {
                            moviePcgkBuyListener.onMoviePckgBuyError(packageId,value.message()); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            moviePcgkBuyListener.onMoviePckgBuyError(packageId,"No Internet Connection");
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            moviePcgkBuyListener.onMoviePckgBuyError(packageId,"Couldn't connect to server");
                        } else {
                            moviePcgkBuyListener.onMoviePckgBuyError(packageId,"Error Occured");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
