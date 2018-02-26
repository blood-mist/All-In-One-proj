package comcast.stb.login;




import java.net.ConnectException;
import java.net.UnknownHostException;

import comcast.stb.entity.LoginData;
import comcast.stb.utils.ApiManager;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by nitv on 2/28/17.
 */

public class LoginModel implements LoginApiInterface.LoginInteractor {
    LoginApiInterface.LoginListener loginListener;
    Realm realm;
    public LoginModel(LoginApiInterface.LoginListener loginListener) {
        this.loginListener = loginListener;
        realm = Realm.getDefaultInstance();
    }




    @Override
    public void checkCredentials(final String userName, final String password) {
        Retrofit retrofit = ApiManager.getAdapter();
        final LoginApiInterface loginApiInterface = retrofit.create(LoginApiInterface.class);


        Observable<Response<LoginData>> observable = loginApiInterface.loginStatus(userName, password);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<LoginData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<LoginData> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            saveCredentials(value.body());
                            loginListener.successfullyLoggedIn(value.body());
                        } else {
                            loginListener.onErrorOccured(value.message()); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            loginListener.onErrorOccured("No Internet Connection");
                        } else if (e instanceof UnknownHostException) {
                            loginListener.onErrorOccured("Couldn't connect to server");
                        } else {
                            loginListener.onErrorOccured("Error Occured");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void saveCredentials(final LoginData loginData) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(loginData);
            }
        });
    }


    @Override
    public void getCredentials() {
        final RealmResults<LoginData> loginDatas = realm.where(LoginData.class).findAll();
        loginListener.logginFromReal(loginDatas.first());

    }



}

