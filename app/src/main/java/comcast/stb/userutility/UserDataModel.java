package comcast.stb.userutility;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import comcast.stb.entity.ChannelPckgItem;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.MoviePckgItem;
import comcast.stb.entity.NewToken;

import comcast.stb.entity.OrderItem;
import comcast.stb.entity.PackagesInfo;
import comcast.stb.entity.SubsItem;
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
 * Created by ACER on 2/15/2018.
 */

public class UserDataModel  implements UserApiInterface.UserDataInteractor, TokenRefreshApiInterface.TokenRefreshView {
    UserApiInterface.UserDataListener userDataListener;
    TokenPresImpl tokenPres;
    LogoutPresImpl logoutPres;
    String packageType;
    public UserDataModel(UserApiInterface.UserDataListener userDataListener,LogoutPresImpl logoutPres) {
        this.userDataListener = userDataListener;
        tokenPres = new TokenPresImpl(this);
        this.logoutPres = logoutPres;
    }
    @Override
    public void getSubsHistory(final String token) {
        Retrofit retrofit = ApiManager.getAdapter();
        final UserApiInterface userApiInterface = retrofit.create(UserApiInterface.class);


        Observable<Response<List<SubsItem>>> observable = userApiInterface.getSubsHistory(token);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<List<SubsItem>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<SubsItem>> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            userDataListener.takeSubsHistory(value.body());
                        } else if (responseCode == 403) {
                            userDataListener.onErrorOccured("403");
                        } else if (responseCode == 401) {
                            tokenPres.refreshTheToken(token);
                        } else {
                            userDataListener.onErrorOccured(value.message()); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            userDataListener.onErrorOccured("No Internet Connection");
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            userDataListener.onErrorOccured("Couldn't connect to server");
                        } else {
                            userDataListener.onErrorOccured("Error Occured");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getOrderHistory(final String token) {
        Retrofit retrofit = ApiManager.getAdapter();
        final UserApiInterface userApiInterface = retrofit.create(UserApiInterface.class);


        Observable<Response<List<OrderItem>>> observable = userApiInterface.getOrderHistory(token);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<List<OrderItem>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<OrderItem>> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            userDataListener.takeOrderHistory(value.body());
                        } else if (responseCode == 403) {
                            userDataListener.onErrorOccured("403");
                        } else if (responseCode == 401) {
                            tokenPres.refreshTheToken(token);
                        } else {
                            userDataListener.onErrorOccured(value.message()); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            userDataListener.onErrorOccured("No Internet Connection");
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            userDataListener.onErrorOccured("Couldn't connect to server");
                        } else {
                            userDataListener.onErrorOccured("Error Occured");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getPackageInfo(final String packageType, final String token) {
        Retrofit retrofit = ApiManager.getAdapter();
        this.packageType=packageType;
        final UserApiInterface packageApiInterface = retrofit.create(UserApiInterface.class);


        Observable<Response<List<PackagesInfo>>> observable = packageApiInterface.getChannelPackage(packageType,token);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<List<PackagesInfo>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<PackagesInfo>> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            userDataListener.takePackageInfo(value.body(),packageType);
                        } else if (responseCode == 403) {
                            userDataListener.onErrorOccured(packageType,"403");
                        } else if (responseCode == 401) {
                            tokenPres.refreshTheToken(token);
                        } else {
                            userDataListener.onErrorOccured(packageType,value.message()); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            userDataListener.onErrorOccured(packageType,"No Internet Connection");
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            userDataListener.onErrorOccured(packageType,"Couldn't connect to server");
                        } else {
                            userDataListener.onErrorOccured(packageType,"Error Occured");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getChannlesInaPckg(final int packageId, String token) {
        Retrofit retrofit = ApiManager.getAdapter();
        final UserApiInterface userApiInterface = retrofit.create(UserApiInterface.class);


        Observable<Response<List<ChannelPckgItem>>> observable = userApiInterface.getChannelsInAPckg(packageId,token);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<List<ChannelPckgItem>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<ChannelPckgItem>> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            userDataListener.setChannelsInaPckg(packageId,value.body());
                        } else if (responseCode == 403) {
                            userDataListener.onChannelInaPckgError(packageId,"403");
                        } else if (responseCode == 401) {
//                            tokenPres.refreshTheToken(token);
                        } else {
                            userDataListener.onChannelInaPckgError(packageId,value.message()); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            userDataListener.onChannelInaPckgError(packageId,"No Internet Connection");
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            userDataListener.onChannelInaPckgError(packageId,"Couldn't connect to server");
                        } else {
                            userDataListener.onChannelInaPckgError(packageId,"Error Occured");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getMoviesInaPckg(final int packageId, final String token) {
        Retrofit retrofit = ApiManager.getAdapter();
        final UserApiInterface userApiInterface = retrofit.create(UserApiInterface.class);


        Observable<Response<List<MoviePckgItem>>> observable = userApiInterface.getMoviesInAPckg(packageId,token);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<List<MoviePckgItem>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<MoviePckgItem>> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            userDataListener.setMoviesInaPckg(packageId,value.body());
                        } else if (responseCode == 403) {
                            userDataListener.onChannelInaPckgError(packageId,"403");
                        } else if (responseCode == 401) {
                            tokenPres.refreshTheToken(token);
                        } else {
                            userDataListener.onChannelInaPckgError(packageId,value.message()); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            userDataListener.onChannelInaPckgError(packageId,"No Internet Connection");
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            userDataListener.onChannelInaPckgError(packageId,"Couldn't connect to server");
                        } else {
                            userDataListener.onChannelInaPckgError(packageId,"Error Occured");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void getUserInfo(final String token) {
        Retrofit retrofit = ApiManager.getAdapter();
        final UserApiInterface splashApiInterface = retrofit.create(UserApiInterface.class);


        Observable<Response<UserInfo>> observable = splashApiInterface.getUserInfo(token);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<UserInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<UserInfo> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            userDataListener.takeUserInfo(value.body());
                        } else if (responseCode == 403) {
                            userDataListener.onErrorOccured("403");
                        } else if (responseCode == 401) {
                            tokenPres.refreshTheToken(token);
                        } else {
                            userDataListener.onErrorOccured(value.message()); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            userDataListener.onErrorOccured("No Internet Connection");
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            userDataListener.onErrorOccured("Couldn't connect to server");
                        } else {
                            userDataListener.onErrorOccured("Error Occured");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
        getPackageInfo(packageType,newToken.getToken());
    }

    @Override
    public void onError(String message) {
        userDataListener.onErrorOccured(packageType,message);
    }

    @Override
    public void logout() {
        logoutPres.logout();
    }
}
