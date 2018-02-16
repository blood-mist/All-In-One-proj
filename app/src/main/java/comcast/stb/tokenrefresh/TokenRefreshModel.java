package comcast.stb.tokenrefresh;




import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import comcast.stb.entity.NewToken;
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
 * Created by anilpaudel on 1/9/18.
 */

public class TokenRefreshModel implements TokenRefreshApiInterface.TokenRefreshInteractor {
    TokenRefreshApiInterface.TokenRefreshListener tokenRefreshListener;

    public TokenRefreshModel(TokenRefreshApiInterface.TokenRefreshListener tokenRefreshListener) {
        this.tokenRefreshListener = tokenRefreshListener;
    }

    @Override
    public void generateNewToken(String oldToken) {
        Retrofit retrofit = ApiManager.getAdapter();
        final TokenRefreshApiInterface tokenRefreshApiInterface = retrofit.create(TokenRefreshApiInterface.class);


        Observable<Response<NewToken>> observable = tokenRefreshApiInterface.getNewToken(oldToken);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<NewToken>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<NewToken> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            tokenRefreshListener.tokenIsRefreshed(value.body());
                        } else if (responseCode == 403) {
                            tokenRefreshListener.onError("403");
                        }else if (responseCode == 498) {
                            tokenRefreshListener.logout();
                        } else {
                            tokenRefreshListener.onError(value.message()); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException ||  e instanceof ConnectException) {
                            tokenRefreshListener.onError("No Internet Connection");
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            tokenRefreshListener.onError("Couldn't connect to server");
                        } else {
                            tokenRefreshListener.onError("Error Occured");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
