package comcast.stb.livetv;


import android.util.Log;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import comcast.stb.entity.ChannelCategory;
import comcast.stb.entity.EpgResponse;
import comcast.stb.entity.EventItem;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.NewToken;
import comcast.stb.logout.LogoutPresImpl;
import comcast.stb.tokenrefresh.TokenPresImpl;
import comcast.stb.tokenrefresh.TokenRefreshApiInterface;
import comcast.stb.utils.ApiManager;
import comcast.stb.utils.DurationUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

import static comcast.stb.StringData.LIVE_CATEGORY_ERROR;
import static comcast.stb.StringData.LIVE_EPG_ERROR;

public class LiveTVModel implements LiveTVApiInterface.ChannelWithCategoryInteractor, TokenRefreshApiInterface.TokenRefreshView {
    LiveTVApiInterface.ChannelWithCategoryListener channelWithCategoryListener;
    TokenPresImpl tokenPres;
    LogoutPresImpl logoutPres;
    private String language;

    public LiveTVModel(LiveTVApiInterface.ChannelWithCategoryListener channelWithCategoryListener, LogoutPresImpl logoutPres) {
        this.channelWithCategoryListener = channelWithCategoryListener;
        tokenPres = new TokenPresImpl(this);
        this.logoutPres = logoutPres;
    }

    @Override
    public void getChannelsWithCategory(final String token,String language) {
        Retrofit retrofit = ApiManager.getAdapter();
        final LiveTVApiInterface channelApiInterface = retrofit.create(LiveTVApiInterface.class);
        this.language=language;


        Observable<Response<List<ChannelCategory>>> observable = channelApiInterface.getChannelsWithCategory(token,language);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<List<ChannelCategory>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<ChannelCategory>> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            channelWithCategoryListener.takeChannelsWithCategory(value.body());
                        } else if (responseCode == 403) {
                            channelWithCategoryListener.onErrorOccured("403", null, LIVE_CATEGORY_ERROR);
                        } else if (responseCode == 401) {
                            tokenPres.refreshTheToken(token);
                        } else {
                            channelWithCategoryListener.onErrorOccured(value.message(), null, LIVE_CATEGORY_ERROR); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            channelWithCategoryListener.onErrorOccured("No Internet Connection", null, LIVE_CATEGORY_ERROR);
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            channelWithCategoryListener.onErrorOccured("Couldn't connect to server", null, LIVE_CATEGORY_ERROR);
                        } else {
                            channelWithCategoryListener.onErrorOccured("Error Occured", null, LIVE_CATEGORY_ERROR);
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
        getChannelsWithCategory(newToken.getToken(),language);
    }

    @Override
    public void onError(String message) {
        channelWithCategoryListener.onErrorOccured(message, null, LIVE_CATEGORY_ERROR);
    }

    @Override
    public void logout() {
        logoutPres.logout();
    }
}