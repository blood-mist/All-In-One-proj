package comcast.stb.userutility;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import comcast.stb.entity.LoginData;
import comcast.stb.entity.NewToken;
import comcast.stb.entity.OrderItem;
import comcast.stb.entity.PackagesInfo;
import comcast.stb.entity.SubsItem;
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

import static comcast.stb.StringData.ORDER_ERROR;
import static comcast.stb.StringData.PACKAGE_ERROR;
import static comcast.stb.StringData.SUBSCRIPTION_ERROR;
import static comcast.stb.StringData.TOKEN_ERROR;

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
                            userDataListener.onErrorOccured("403","",SUBSCRIPTION_ERROR);
                        } else if (responseCode == 401) {
                            tokenPres.refreshTheToken(token);
                        } else {
                            userDataListener.onErrorOccured(value.message(),"",SUBSCRIPTION_ERROR); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            userDataListener.onErrorOccured("No Internet Connection","",SUBSCRIPTION_ERROR);
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            userDataListener.onErrorOccured("Couldn't connect to server","",SUBSCRIPTION_ERROR);
                        } else {
                            userDataListener.onErrorOccured("Error Occured","",SUBSCRIPTION_ERROR);
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
                            userDataListener.onErrorOccured("403","",ORDER_ERROR);
                        } else if (responseCode == 401) {
                            tokenPres.refreshTheToken(token);
                        } else {
                            userDataListener.onErrorOccured(value.message(),"",ORDER_ERROR); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            userDataListener.onErrorOccured("No Internet Connection","",ORDER_ERROR);
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            userDataListener.onErrorOccured("Couldn't connect to server","",ORDER_ERROR);
                        } else {
                            userDataListener.onErrorOccured("Error Occured","",ORDER_ERROR);
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
                            userDataListener.onErrorOccured("403",packageType,PACKAGE_ERROR);
                        } else if (responseCode == 401) {
                            tokenPres.refreshTheToken(token);
                        } else {
                            userDataListener.onErrorOccured(value.message(),"",PACKAGE_ERROR); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            userDataListener.onErrorOccured("No Internet Connection",packageType,PACKAGE_ERROR);
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            userDataListener.onErrorOccured("Couldn't connect to server",packageType,PACKAGE_ERROR);
                        } else {
                            userDataListener.onErrorOccured("Error Occured",packageType,PACKAGE_ERROR);
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
        getSubsHistory(newToken.getToken());
    }

    @Override
    public void onError(String message) {
        userDataListener.onErrorOccured(message,packageType,TOKEN_ERROR);
    }

    @Override
    public void logout() {
        logoutPres.logout();
    }
}
