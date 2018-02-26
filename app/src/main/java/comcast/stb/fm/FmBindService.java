package comcast.stb.fm;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import comcast.stb.entity.TvLink;
import comcast.stb.entity.events.FmErrorEvent;
import comcast.stb.entity.events.FmPlayingEvent;
import comcast.stb.utils.ApiManager;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

import static comcast.stb.StringData.AUTH_TOKEN;
import static comcast.stb.StringData.FM_ID;


/**
 * Created by anilpaudel on 1/6/18.
 */

public class FmBindService extends Service {
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_CLOSE = "ACTION_CLOSE";
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    MediaPlayer mediaPlayer;
    FmPlayingEvent fmPlayingEvent;
    FmErrorEvent fmErrorEvent;
    int fmID;
    String token;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Timber.d("onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("service, oncreate");
        mediaPlayer = new MediaPlayer();
        fmPlayingEvent = new FmPlayingEvent();
        fmErrorEvent = new FmErrorEvent();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        Timber.d("onStartCommand %s", action);
        this.fmID = intent.getIntExtra(FM_ID, 0);
        this.token = intent.getStringExtra(AUTH_TOKEN);

        assert action != null;
        switch (action) {
            case FmBindService.ACTION_PLAY:
                getFmURL(fmID, token);
                break;
            case FmBindService.ACTION_STOP:
                stopPlayer();
                stopSelf();
                break;
            case FmBindService.ACTION_CLOSE:
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                playEvent(false);
                stopSelf();
                break;
        }
        return START_NOT_STICKY;
    }

    public void getFmURL(int fmID, String token) {
        this.fmID = fmID;
        this.token = token;
        Retrofit retrofit = ApiManager.getAdapter();
        final FmApiInterface fmApiInterface = retrofit.create(FmApiInterface.class);
        Observable<Response<TvLink>> observable = fmApiInterface.getFmLink(fmID, token);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<TvLink>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<TvLink> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            startPlayer(value.body().getLink());
                        } else if (responseCode == 403) {
                            errorEvent("403");
                        } else {
                            errorEvent(value.message()); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            errorEvent("No Internet Connection");
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            errorEvent("Couldn't connect to server");
                        } else {
                            errorEvent("Error Occured");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void startPlayer(String channelLink) {
        channelLink= "https://www.ssaurel.com/tmp/mymusic.mp3";
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(this, Uri.parse(channelLink));
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Timber.d("onPrepared");
                    mediaPlayer.start();
                    callReceiver(ACTION_STOP);
                    playEvent(true);
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    Timber.d("onError");
                    mediaPlayer.reset();
                    callReceiver(ACTION_PLAY);
                    return false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resumePlayer() {
        mediaPlayer.start();
        callReceiver(ACTION_STOP);
        playEvent(true);
    }

    public void stopPlayer() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        callReceiver(ACTION_PLAY);
        playEvent(false);
    }

    public void pausePlayer() {
        mediaPlayer.pause();
        callReceiver(ACTION_PLAY);
        playEvent(false);
    }

    public void playEvent(boolean playevent) {
        fmPlayingEvent.setPlaying(playevent);
        EventBus.getDefault().post(fmPlayingEvent);
    }

    public void errorEvent(String message) {
        fmPlayingEvent.setPlaying(false);
        EventBus.getDefault().post(fmPlayingEvent);

        fmErrorEvent.setMessage(message);
        EventBus.getDefault().post(fmErrorEvent);
    }

    public void callReceiver(String state) {
        Intent intent = new Intent(this, FmReceiver.class);
        intent.putExtra(FM_ID, fmID);
        intent.putExtra(AUTH_TOKEN, token);
        intent.setAction(state);
        sendBroadcast(intent);
    }


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        FmBindService getService() {
            // Return this instance of LocalService so clients can call public methods
            return FmBindService.this;
        }
    }


}
