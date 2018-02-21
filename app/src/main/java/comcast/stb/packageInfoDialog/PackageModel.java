package comcast.stb.packageInfoDialog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import comcast.stb.entity.ChannelPckgItem;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.MoviePckgItem;
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

import static comcast.stb.StringData.CHANNEL_PACKAGE;
import static comcast.stb.StringData.MOVIE_PACKAGE;

/**
 * Created by blood-mist on 2/21/18.
 */

class PackageModel implements PackageApiInterface.PackageInteractor, TokenRefreshApiInterface.TokenRefreshView {
    PackageApiInterface.PackageListener packageListener;
    TokenPresImpl tokenPres;
    LogoutPresImpl logoutPres;
    String packageType;
    int packageId;

    public PackageModel(PackageApiInterface.PackageListener packageListener, LogoutPresImpl logoutPres) {
        this.packageListener = packageListener;
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
        if (packageType.equals(CHANNEL_PACKAGE))
            getChannlesInaPckg(packageId, newToken.getToken());
        else
            getMoviesInaPckg(packageId, newToken.getToken());
    }

    @Override
    public void onError(String message) {
        if (packageType.equals(CHANNEL_PACKAGE))
        packageListener.onChannelInaPckgError(packageId,message);
        else
            packageListener.onMoviesInaPckgError(packageId,message);
    }

    @Override
    public void logout() {

    }

    @Override
    public void getChannlesInaPckg(final int packageId, String token) {
        this.packageType = CHANNEL_PACKAGE;
        this.packageId = packageId;
        Retrofit retrofit = ApiManager.getAdapter();
        final PackageApiInterface packageApiInterface = retrofit.create(PackageApiInterface.class);


        Observable<Response<List<ChannelPckgItem>>> observable = packageApiInterface.getChannelsInAPckg(packageId, token);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<List<ChannelPckgItem>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<ChannelPckgItem>> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            packageListener.setChannelsInaPckg(packageId, value.body());
                        } else if (responseCode == 403) {
                            packageListener.onChannelInaPckgError(packageId, "403");
                        } else if (responseCode == 401) {
//                            tokenPres.refreshTheToken(token);
                        } else {
                            packageListener.onChannelInaPckgError(packageId, value.message()); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            packageListener.onChannelInaPckgError(packageId, "No Internet Connection");
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            packageListener.onChannelInaPckgError(packageId, "Couldn't connect to server");
                        } else {
                            packageListener.onChannelInaPckgError(packageId, "Error Occured");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getMoviesInaPckg(final int packageId, final String token) {
        this.packageType = MOVIE_PACKAGE;
        this.packageId = packageId;
        Retrofit retrofit = ApiManager.getAdapter();
        final PackageApiInterface packageApiInterface = retrofit.create(PackageApiInterface.class);


        Observable<Response<List<MoviePckgItem>>> observable = packageApiInterface.getMoviesInAPckg(packageId, token);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<List<MoviePckgItem>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<MoviePckgItem>> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            packageListener.setMoviesInaPckg(packageId, value.body());
                        } else if (responseCode == 403) {
                            packageListener.onChannelInaPckgError(packageId, "403");
                        } else if (responseCode == 401) {
                            tokenPres.refreshTheToken(token);
                        } else {
                            packageListener.onChannelInaPckgError(packageId, value.message()); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            packageListener.onChannelInaPckgError(packageId, "No Internet Connection");
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            packageListener.onChannelInaPckgError(packageId, "Couldn't connect to server");
                        } else {
                            packageListener.onChannelInaPckgError(packageId, "Error Occured");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
