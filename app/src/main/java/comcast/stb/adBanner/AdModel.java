package comcast.stb.adBanner;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import comcast.stb.entity.AdItem;
import comcast.stb.entity.LoginData;
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

/**
 * Created by blood-mist on 2/21/18.
 */

public class AdModel implements AdApiInterface.AdInteractor,TokenRefreshApiInterface.TokenRefreshView{
    AdApiInterface.AdListener adListener;
    TokenPresImpl tokenPres;
    LogoutPresImpl logoutPres;

    public AdModel(AdApiInterface.AdListener adListener, LogoutPresImpl logoutPres) {
        this.adListener=adListener;
        tokenPres = new TokenPresImpl(this);
        this.logoutPres = logoutPres;
    }

    @Override
    public void newToken(final NewToken newToken) {
        Realm mRealm = Realm.getInstance(Realm.getDefaultConfiguration());
        final LoginData loginDatas = mRealm.where(LoginData.class).findFirst();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                loginDatas.setToken(newToken.getToken());
                realm.insertOrUpdate(loginDatas);
            }
        });
        getAdBanners(newToken.getToken());
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void logout() {
        logoutPres.logout();
    }

    @Override
    public void getAdBanners(final String token) {
        Retrofit retrofit = ApiManager.getAdapter();
        final AdApiInterface adApiInterface = retrofit.create(AdApiInterface.class);


        Observable<Response<List<AdItem>>> observable = adApiInterface.getAdBanners(token);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<List<AdItem>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<AdItem>> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            adListener.takeAdBanners(value.body());
                        } else if (responseCode == 401) {
                            tokenPres.refreshTheToken(token);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            adListener.onErrorOccured("No Internet Connection");
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            adListener.onErrorOccured("Couldn't connect to server");
                        } else {
                            adListener.onErrorOccured("Error Occured");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
