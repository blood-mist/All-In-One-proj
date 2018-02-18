package comcast.stb.userInfo;



import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import comcast.stb.entity.LoginData;
import comcast.stb.entity.NewToken;
import comcast.stb.entity.UserInfo;
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
 * Created by blood-mist on 1/27/18.
 */

public class UserInfoModel implements UserInfoApiInterface.SplashInteractor, TokenRefreshApiInterface.TokenRefreshView {
    UserInfoApiInterface.SplashListener splashListener;
    TokenPresImpl tokenPres;
    LogoutPresImpl logoutPres;

    public UserInfoModel(UserInfoApiInterface.SplashListener splashListener, LogoutPresImpl logoutPres) {
        this.splashListener = splashListener;
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
        getUserInfo(newToken.getToken());
    }

    @Override
    public void onError(String message) {
        splashListener.onErrorOccured(message);
    }

    @Override
    public void logout() {
        logoutPres.logout();
    }

    @Override
    public void getUserInfo(final String token) {
        Retrofit retrofit = ApiManager.getAdapter();
        final UserInfoApiInterface userInfoApiInterface = retrofit.create(UserInfoApiInterface.class);


        Observable<Response<UserInfo>> observable = userInfoApiInterface.getUserInfo(token);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<UserInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<UserInfo> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            splashListener.takeUserInfo(value.body());
                        } else if (responseCode == 403) {
                            splashListener.onErrorOccured("403");
                        } else if (responseCode == 401) {
                            tokenPres.refreshTheToken(token);
                        } else {
                            splashListener.onErrorOccured(value.message()); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            splashListener.onErrorOccured("No Internet Connection");
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            splashListener.onErrorOccured("Couldn't connect to server");
                        } else {
                            splashListener.onErrorOccured("Error Occured");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
