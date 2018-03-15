package comcast.stb.livetv;


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

import comcast.stb.entity.ChannelCategory;
import comcast.stb.entity.EpgResponse;
import comcast.stb.entity.EventItem;
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

import static comcast.stb.StringData.LIVE_CATEGORY_ERROR;

public class LiveTVModel implements LiveTVApiInterface.ChannelWithCategoryInteractor, TokenRefreshApiInterface.TokenRefreshView {
    LiveTVApiInterface.ChannelWithCategoryListener channelWithCategoryListener;
    TokenPresImpl tokenPres;
    LogoutPresImpl logoutPres;

    public LiveTVModel(LiveTVApiInterface.ChannelWithCategoryListener channelWithCategoryListener, LogoutPresImpl logoutPres) {
        this.channelWithCategoryListener = channelWithCategoryListener;
        tokenPres = new TokenPresImpl(this);
        this.logoutPres = logoutPres;
    }

    @Override
    public void getChannelsWithCategory(final String token) {
        Retrofit retrofit = ApiManager.getAdapter();
        final LiveTVApiInterface channelApiInterface = retrofit.create(LiveTVApiInterface.class);


        Observable<Response<List<ChannelCategory>>> observable = channelApiInterface.getChannelsWithCategory(token);
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
    public void getEpg(int channelId, final String token) {
        Retrofit retrofit = ApiManager.getAdapter();
        final LiveTVApiInterface channelApiInterface = retrofit.create(LiveTVApiInterface.class);


        Observable<Response<EpgResponse>> observable = channelApiInterface.getEpg(channelId, token);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<EpgResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<EpgResponse> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            channelWithCategoryListener.takeEpgList(getFilteredEpg(value.body()));
//                            channelWithCategoryListener.takeEpgList(value.body());
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

    private LinkedHashMap<String, ArrayList<EventItem>> getFilteredEpg(EpgResponse epgResponse) {
        List<EventItem> datewiseEpgList = null;
        LinkedHashMap<String, ArrayList<EventItem>> epgHash = new LinkedHashMap<>();
        List<EventItem> allEpgFrmServer = epgResponse.getEvents();
        Calendar currentCal = Calendar.getInstance();
        Calendar epgStartCal = Calendar.getInstance();
        Calendar epgEndCal = Calendar.getInstance();
        for (EventItem epgItem : allEpgFrmServer) {
            String startDate = epgItem.getBeginTime();
            String duration = epgItem.getDuration();
            SimpleDateFormat epgDurationFormat = new SimpleDateFormat("hhmmss");
            SimpleDateFormat epgParseDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                Date epgStartDate = epgParseDateFormat.parse(startDate);
                Date epgDuration = epgDurationFormat.parse(duration);
                epgStartCal.setTime(epgStartDate);
                epgEndCal.setTime(new Date(epgStartDate.getTime() + epgDuration.getTime()));
                if (epgEndCal.after(currentCal)) {
                    SimpleDateFormat dateDayFormat = new SimpleDateFormat("MMM DD,EEE");
                    String dayString = dateDayFormat.format(epgStartDate);
                    if (epgHash.containsKey(dayString)) {

                        //if contains key
                        assert datewiseEpgList != null;
                        datewiseEpgList.add(epgItem);
                        if (allEpgFrmServer.indexOf(epgItem) == allEpgFrmServer.size() - 1) {
                            epgHash.put(dayString, (ArrayList<EventItem>) datewiseEpgList);
                        }
                    } else {
                        epgHash.put(dayString, (ArrayList<EventItem>) datewiseEpgList);
                        datewiseEpgList = new ArrayList<>();
                        datewiseEpgList.add(epgItem);
                        if (allEpgFrmServer.indexOf(epgItem) == allEpgFrmServer.size() - 1)
                            epgHash.put(dayString, (ArrayList<EventItem>) datewiseEpgList);
                    }

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return epgHash;
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
        getChannelsWithCategory(newToken.getToken());
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