package comcast.stb.livetv;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.R;
import comcast.stb.entity.Channel;
import comcast.stb.entity.ChannelCategory;
import comcast.stb.entity.EventItem;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.TvLink;
import comcast.stb.login.LoginActivity;
import comcast.stb.logout.LogoutApiInterface;
import comcast.stb.logout.LogoutPresImpl;
import comcast.stb.purchase.livetvpurchase.BuyChannelDialog;
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
import static comcast.stb.StringData.LIVE_EPG_ERROR;
import static comcast.stb.StringData.LIVE_PLAY_ERROR;
import static comcast.stb.StringData.MENU_FRAGMENT;
import static comcast.stb.StringData.PURCHASE_TYPE_BUY;


public class LiveTVActivity extends AppCompatActivity implements LiveTVApiInterface.ChannelWithCategoryView,
        MenuFragment.OnChannelClickedListener, LogoutApiInterface.LogoutView, SurfaceHolder.Callback, LiveDialogFragment.OnFragmentInteractionListener {
    private static final String TAG = "LiveTVActivity";
    private LiveTVPresenterImpl liveTVPresenter;
    private Handler hideMenuHandler;
    List<ChannelCategory> channelCategoryList;
    private Realm realm;
    LoginData loginData;
    @BindView(R.id.livetv_surface_view)
    SurfaceView surfaceView;
    @BindView(R.id.progressbar_container)
    FrameLayout progressContainer;
    public MediaPlayer player;
    Channel previousChannel;

    public Channel getCurrentChannel() {
        return currentChannel;
    }

    Channel currentChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_tv);
        ButterKnife.bind(this);
        progressContainer.setVisibility(View.VISIBLE);
        hideMenuHandler = new Handler();
        realm = Realm.getDefaultInstance();
        loginData = realm.where(LoginData.class).findFirst();
        String authToken = loginData.getToken();
        SurfaceHolder videoHolder = surfaceView.getHolder();
        videoHolder.addCallback(this);
        player = new MediaPlayer();
        LogoutPresImpl logoutPres = new LogoutPresImpl(this);
        liveTVPresenter = new LiveTVPresenterImpl(this, logoutPres);
        liveTVPresenter.getChannelsWithCategory(authToken);
        previousChannel = realm.where(Channel.class).findFirst();
        if (previousChannel != null) {
            onChannelClicked(previousChannel);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            showMenu();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showMenu() {
        Fragment menuFrag = getSupportFragmentManager().findFragmentById(R.id.livetv_menu_container);
        if (menuFrag == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.livetv_menu_container, MenuFragment.newInstance(channelCategoryList, loginData.getUser().getName()),MENU_FRAGMENT).commit();
        else {
            if (menuFrag.isHidden())
                getSupportFragmentManager().beginTransaction().show(menuFrag).commit();
            else
                getSupportFragmentManager().beginTransaction().hide(menuFrag).commit();


        }
    }

    @Override
    public void onBackPressed() {
        Fragment menuFrag = getSupportFragmentManager().findFragmentById(R.id.livetv_menu_container);
        if (menuFrag != null)
            getSupportFragmentManager().beginTransaction().hide(menuFrag).commit();
        else
            finish();
        super.onBackPressed();
    }

    private void updateMenuUI() {
        Fragment menuFrag = getSupportFragmentManager().findFragmentById(R.id.livetv_menu_container);
        if (menuFrag != null)
            getSupportFragmentManager().beginTransaction().hide(menuFrag).commit();

    }

    @Override
    public void setChannelsWithCategory(List<ChannelCategory> channelCategoryList) {
        this.channelCategoryList = channelCategoryList;
        progressContainer.setVisibility(View.GONE);
        showMenu();
    }

    @Override
    public void setEpg(LinkedHashMap<String, ArrayList<EventItem>> epgChannelList) {
        Log.d("hashmp", epgChannelList.size() + "");
        final ArrayList<Calendar> calendarList = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>(epgChannelList.keySet());
        for (int i = 0; i < keys.size(); i++) {
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime( new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(keys.get(i)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendarList.add(calendar);
        }
        updateEpginMenu(calendarList,epgChannelList);
    }

    private void updateEpginMenu(ArrayList<Calendar> calendarList,LinkedHashMap<String,ArrayList<EventItem>>epgChannelList) {
        Log.d("CalendarListSize", calendarList.size() + "");
        MenuFragment menuFragment= (MenuFragment) getSupportFragmentManager().findFragmentByTag(MENU_FRAGMENT);
        menuFragment.populateDayList(calendarList,epgChannelList);

    }

    @Override
    public void onErrorOccured(String message, Channel channel, String errorType) {
        switch (errorType) {
            case LIVE_EPG_ERROR:
                Toast.makeText(LiveTVActivity.this, "Couldn't load EEPG.", Toast.LENGTH_LONG).show();
                MenuFragment menuFragment= (MenuFragment) getSupportFragmentManager().findFragmentByTag(MENU_FRAGMENT);
                menuFragment.hideEpgMenu();
                break;
            default:
                progressContainer.setVisibility(View.GONE);
                LiveDialogFragment infoDialogFragment = LiveDialogFragment.newInstance("", message, channel, errorType, true);
                infoDialogFragment.show(getSupportFragmentManager(), "LiveTvErrorFragment");
                break;
        }


    }

    public void updateProgress(boolean showProgress) {
        if (showProgress)
            progressContainer.setVisibility(View.VISIBLE);
        else
            progressContainer.setVisibility(View.GONE);

    }

    @Override
    public void successfulLogout() {
        Intent intent = new Intent(LiveTVActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void stopHandler() {
        hideMenuHandler.removeCallbacks(closeFragmentRunnable);
    }

    public void startHandler() {
        hideMenuHandler.postDelayed(closeFragmentRunnable, TimeUnit.SECONDS.toMillis(10)); //for 10 secs
    }

    private Runnable closeFragmentRunnable = new Runnable() {
        @Override
        public void run() {
            try {

                updateMenuUI();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        stopHandler();
        startHandler();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            try {
                player.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finish();
    }


    @Override
    protected void onPause() {
        try {
            player.stop();
            player.release();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    private void playVideo(String link) {
        player.reset();
//        channelLink = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
        try {
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
//                            progressContainer.setVisibility(View.GONE);
                        }
                    });
                    player.setScreenOnWhilePlaying(true);
                    player.start();
                    progressContainer.setVisibility(View.GONE);
                    startHandler();

                }
            });

            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(LiveTVActivity.this, Uri.parse(link));
            try {

                player.prepareAsync();

            } catch (Exception e) {
                e.printStackTrace();
            }

            player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.d(TAG, "on error ");
                    progressContainer.setVisibility(View.GONE);
                    try {
                        player.stop();
                    } catch (Exception ignored) {
                    }
                    player.reset();
                    Toast.makeText(LiveTVActivity.this, "Error Playing Media", Toast.LENGTH_LONG).show();
                    Fragment menuFrag = getSupportFragmentManager().findFragmentById(R.id.livetv_menu_container);
                    if (menuFrag == null)
                        getSupportFragmentManager().beginTransaction().replace(R.id.livetv_menu_container, MenuFragment.newInstance(channelCategoryList, loginData.getUser().getName()),MENU_FRAGMENT).commit();
                    else {
                        if (menuFrag.isHidden())
                            getSupportFragmentManager().beginTransaction().show(menuFrag).commit();
                    }
//                    progressContainer.setVisibility(View.GONE);
//                    showAlertDialog(getActivity().getResources().getString(R.string.server_down));
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
//            progressContainer.setVisibility(View.GONE);
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        player.setDisplay(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onChannelClicked(final Channel channel) {
        switch (channel.getSubscriptionStatus()) {
            case PURCHASE_TYPE_BUY:
                showDialogFragment(channel);
                break;
            default:
                if (channel.isExpiryFlag()) {
                    showDialogFragment(channel);
                } else {
                    getChannelLink(channel);
                }
                break;

        }

    }

    @Override
    public void onChannelSelected(Channel channel) {
        liveTVPresenter.getEpg(channel.getChannelId(), loginData.getToken());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            player.stop();

            try {
                player.reset();
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }

            player.release();
            player = null;
        } catch (Exception e) {
            Log.d("Exception", e.getMessage() + "");
        }
        stopHandler();
        finish();
    }

    private void showDialogFragment(Channel channel) {
        BuyChannelDialog buyChannelDialog =
                BuyChannelDialog.newInstance(channel);
        buyChannelDialog.show(getSupportFragmentManager(), "buyFragmentDialog");
    }

    private void getChannelLink(final Channel channel) {
        updateProgress(true);
        Retrofit retrofit = ApiManager.getAdapter();
        final LiveTVApiInterface channelApiInterface = retrofit.create(LiveTVApiInterface.class);


        Observable<Response<TvLink>> observable = channelApiInterface.getChannelLink(channel.getChannelId(), loginData.getToken());
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<TvLink>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<TvLink> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            final Channel previousChannel = realm.where(Channel.class).findFirst();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    if (previousChannel != null && !previousChannel.equals(channel))
                                        previousChannel.deleteFromRealm();
                                    realm.insertOrUpdate(channel);
                                }
                            });
                            setCurrentChannel(channel);

                            playVideo(value.body().getLink());
//                            startControllersTimer();
                        } else if (responseCode == 403) {
                            onErrorOccured("403", channel, LIVE_PLAY_ERROR);
                        } else {
                            onErrorOccured(value.message(), channel, LIVE_PLAY_ERROR); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            onErrorOccured("No Internet Connection", channel, LIVE_PLAY_ERROR);
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            onErrorOccured("Couldn't connect to server", channel, LIVE_PLAY_ERROR);
                        } else {
                            onErrorOccured("Error Occured", channel, LIVE_PLAY_ERROR);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void setCurrentChannel(Channel currentChannel) {
        this.currentChannel = currentChannel;
    }

    @Override
    public void onRetryBtnInteraction(String errorType, Channel channel) {
        onDismissBtnInteraction();
        switch (errorType) {
            case LIVE_CATEGORY_ERROR:
                liveTVPresenter.getChannelsWithCategory(loginData.getToken());
                break;
            case LIVE_PLAY_ERROR:
                getChannelLink(channel);
                break;
        }

    }

    @Override
    public void onDismissBtnInteraction() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment infoDialogFragment = manager.findFragmentByTag("LiveTvErrorFragment");
        if (infoDialogFragment != null)
            manager.beginTransaction().remove(infoDialogFragment).commit();
    }
}
