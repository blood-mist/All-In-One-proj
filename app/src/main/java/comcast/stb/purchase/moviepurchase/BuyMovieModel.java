package comcast.stb.purchase.moviepurchase;



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
 * Created by blood-mist on 1/27/18.
 */

class BuyMovieModel implements BuyMovieApiInterface.BuyInteractor {
    BuyMovieApiInterface.BuyListener buyListener;
    public BuyMovieModel(BuyMovieApiInterface.BuyListener buyListener) {
        this.buyListener = buyListener;
    }

    @Override
    public void buyMovie(int movieId, int months, String token) {
        Retrofit retrofit = ApiManager.getAdapter();
        final BuyMovieApiInterface buyMovieApiInterface = retrofit.create(BuyMovieApiInterface.class);


        Observable<Response<BuyResponse>> observable = buyMovieApiInterface.buyMovie(movieId, months, token);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<BuyResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<BuyResponse> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            buyListener.setBuyResponse(value.body());
                        } else if (responseCode == 403) {
                            buyListener.onError("403");
                        } else if (responseCode == 401) {
//                            tokenPres.refreshTheToken(token);
                        } else {
                            buyListener.onError(value.message()); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            buyListener.onError("No Internet Connection");
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            buyListener.onError("Couldn't connect to server");
                        } else {
                            buyListener.onError("Error Occured");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
