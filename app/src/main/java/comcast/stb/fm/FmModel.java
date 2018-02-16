package comcast.stb.fm;



import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import comcast.stb.entity.FmCategory;
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

public class FmModel implements FmApiInterface.FmWithCategoryInteractor,TokenRefreshApiInterface.TokenRefreshView  {
    FmApiInterface.FmWithCategoryListener fmWithCategoryListener;
    TokenPresImpl tokenPres;
    LogoutPresImpl logoutPres;


    public FmModel(FmApiInterface.FmWithCategoryListener fmWithCategoryListener,LogoutPresImpl logoutPres) {
        this.fmWithCategoryListener = fmWithCategoryListener;
        tokenPres = new TokenPresImpl(this);
        this.logoutPres=logoutPres;
    }

    @Override
    public void getFmsWithCategory(final String token) {
        Retrofit retrofit = ApiManager.getAdapter();
        final FmApiInterface fmApiInterface = retrofit.create(FmApiInterface.class);


        Observable<Response<List<FmCategory>>> observable = fmApiInterface.getFmsWithCategory(token);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<List<FmCategory>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<FmCategory>> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            fmWithCategoryListener.takeFmsWithCategory(value.body());
                        } else if (responseCode == 403) {
                            fmWithCategoryListener.onErrorOccured("403");
                        }else if (responseCode == 401) {
                            tokenPres.refreshTheToken(token);
                        }else {
                            fmWithCategoryListener.onErrorOccured(value.message()); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            fmWithCategoryListener.onErrorOccured("No Internet Connection");
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            fmWithCategoryListener.onErrorOccured("Couldn't connect to server");
                        } else {
                            fmWithCategoryListener.onErrorOccured("Error Occured");
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
        getFmsWithCategory(newToken.getToken());
    }

    @Override
    public void onError(String message) {
        fmWithCategoryListener.onErrorOccured(message);
    }

    @Override
    public void logout() {
        logoutPres.logout();
    }
}
