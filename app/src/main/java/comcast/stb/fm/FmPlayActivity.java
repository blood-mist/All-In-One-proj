package comcast.stb.fm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;



import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comcast.stb.R;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.events.FmErrorEvent;
import comcast.stb.entity.events.FmPlayingEvent;
import io.realm.Realm;

import static comcast.stb.StringData.AUTH_TOKEN;
import static comcast.stb.StringData.FM_ID;

public class FmPlayActivity extends AppCompatActivity {
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_CLOSE = "ACTION_CLOSE";
    private boolean isPlaying = true;

    FmBindService mService;
    boolean mBound = false;
    @BindView(R.id.playPause)
    ImageView playPause;
    LoginData loginDatas;
    int fmID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fm_play);
        ButterKnife.bind(this);
        Realm realm = Realm.getDefaultInstance();
        loginDatas = realm.where(LoginData.class).findFirst();

        fmID = getIntent().getIntExtra(FM_ID, 0);
        Intent intent = new Intent(this, FmBindService.class);
        intent.putExtra(FM_ID, fmID);
        intent.putExtra(AUTH_TOKEN, loginDatas.getToken());
        if (isPlaying) {
            intent.setAction(ACTION_PLAY);
        } else {
            intent.setAction(ACTION_STOP);
        }
        startService(intent);
    }

    @OnClick(R.id.playPause)
    public void onPlayClick() {

        updatePlayPauseButton();
    }

    public void updatePlayPauseButton() {
        if (isPlaying) {
            playPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle_outline_black_24dp));

            isPlaying = false;
//            mService.stopPlayer();

        } else {
            playPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_outline_black_24dp));
            isPlaying = true;
//            mService.resumePlayer();
        }
        Intent intent = new Intent(this, FmBindService.class);
        intent.putExtra(FM_ID, fmID);
        intent.putExtra(AUTH_TOKEN, loginDatas.getToken());
        if (isPlaying) {
            intent.setAction(ACTION_STOP);

        } else {
            intent.setAction(ACTION_PLAY);
        }
        startService(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FmPlayingEvent event) {
        if (event.isPlaying()) {
            playPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_outline_black_24dp));
            isPlaying = false;

        } else {
            playPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle_outline_black_24dp));
            isPlaying = true;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FmErrorEvent event) {
        isPlaying = false;
        onErrorOccured(event.getMessage());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);


    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbindService(mConnection);
//        mBound = false;
    }
    public void onErrorOccured(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mService.getFmURL(fmID, loginDatas.getToken());
                    }
                }).setActionTextColor(getResources().getColor(R.color.white_color));
        snackbar.show();
    }

}
