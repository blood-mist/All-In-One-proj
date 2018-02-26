package comcast.stb.movielist;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.R;
import comcast.stb.entity.MoviesItem;
import comcast.stb.utils.AppConfig;
import comcast.stb.utils.DeviceUtils;

import static android.view.View.GONE;
import static comcast.stb.StringData.MOVIE_ID;
import static comcast.stb.StringData.VIDEO_URL;
import static java.lang.Thread.sleep;

public class MoviePlayActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private static final int FORWARD_REWIND_DURATION = 15000;

    private String videoUrl;
    private MoviesItem currentMovie;
    private MediaPlayer mediaPlayer;
    @BindView(R.id.video_play_view)
    SurfaceView videoPlayView;
    Handler handler = new Handler();
    @BindView(R.id.tvboxID)
    TextView txtRandomDisplayBoxId;
    @BindView(R.id.movie_progressBar)
    AVLoadingIndicatorView loadingView;
    private final Random random = new Random();
    Fragment controllerFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_play);
        ButterKnife.bind(this);
        videoUrl = getIntent().getStringExtra(VIDEO_URL);
        currentMovie = getIntent().getParcelableExtra(MOVIE_ID);
        findViews();
        mediaPlayer = new MediaPlayer();
        playVideo(videoUrl);
        threadToDisplayBoxId.start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 5000);
    }


    @Override
    public void onPause() {
        threadToDisplayBoxId.interrupt();
        stopControllerShow();
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    private void stopControllerShow() {

    }


    private void findViews() {
        loadingView.setVisibility(View.VISIBLE);
        SurfaceHolder holder = videoPlayView.getHolder();
        holder.addCallback(MoviePlayActivity.this);
        VideoControllerFragment controllerFragment = VideoControllerFragment.newInstance(currentMovie, mediaPlayer);
        getSupportFragmentManager().beginTransaction().replace(R.id.controller_fragment, controllerFragment, "controllerFragment").hide(controllerFragment).commit();

    }


    private void playVideo(String videoUrl) {
        try {
            mediaPlayer.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(MoviePlayActivity.this, Uri.parse(videoUrl));
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (mediaPlayer != null) {
                        try {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer.release();
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                loadingView.setVisibility(GONE);

            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Toast.makeText(MoviePlayActivity.this, "Could not play", Toast.LENGTH_SHORT).show();
                Log.d("media_error", i1 + " " + i);
                stopControllerShow();
                if (mediaPlayer != null) {
                    try {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
                finish();
                return false;
            }
        });
    }


    private final Thread threadToDisplayBoxId = new Thread(new Runnable() {
        @Override
        public void run() {
            final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) txtRandomDisplayBoxId.getLayoutParams();
            while (true) {
                if (txtRandomDisplayBoxId.getVisibility() == View.VISIBLE) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtRandomDisplayBoxId.setVisibility(GONE);
                        }
                    });
                    try {
                        sleep(2000 + (new Random().nextInt(2 * 60 * 1000)));
                        Log.d("hcjasdhsa", "here");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    params.setMargins(random.nextInt(500), random.nextInt(500),
                            random.nextInt(200), random.nextInt(200));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtRandomDisplayBoxId.setText((AppConfig.isDevelopment() ? AppConfig.getMac() : DeviceUtils.getMac(MoviePlayActivity.this)));
                            txtRandomDisplayBoxId.bringToFront();
                            txtRandomDisplayBoxId.setLayoutParams(params);
                            txtRandomDisplayBoxId.setVisibility(View.VISIBLE);
                        }
                    });
                    try {
                        sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    });


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mediaPlayer.setDisplay(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 66://key code for enter in keyboard
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                if (controllerNotVisible()) {
                    showFeatures();
                    return true;
                } else {
                    return false;
                }
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                if (controllerNotVisible()) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    } else {
                        mediaPlayer.start();
                    }
                }
                return true;
            case KeyEvent.KEYCODE_MEDIA_STOP:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    finish();

                }
                return true;

            case KeyEvent.KEYCODE_MEDIA_REWIND:
                if (mediaPlayer.isPlaying())
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - FORWARD_REWIND_DURATION);
                return true;
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                if (mediaPlayer.isPlaying())
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + FORWARD_REWIND_DURATION);
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (controllerNotVisible())
                    if (mediaPlayer.isPlaying())
                        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + FORWARD_REWIND_DURATION);
                break;

            case KeyEvent.KEYCODE_DPAD_DOWN_LEFT:
                if (controllerNotVisible())
                    if (mediaPlayer.isPlaying())
                        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - FORWARD_REWIND_DURATION);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private boolean controllerNotVisible() {
        if (controllerFragment.isHidden())
            return true;
        else
            return false;
    }

    private void showFeatures() {
        controllerFragment = getSupportFragmentManager().findFragmentById(R.id.controller_fragment);
        getSupportFragmentManager().beginTransaction().show(controllerFragment).commit();
    }

    private Runnable closeFragmentRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    sleep(1 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!controllerNotVisible()) {
                    try {
                        if (mediaPlayer.isPlaying()) {
                            try {
                                getSupportFragmentManager().beginTransaction().hide(controllerFragment).commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        getSupportFragmentManager().beginTransaction().show(controllerFragment).commit();
        stopHandler();//stop first and then start
        startHandler();
    }

    public void stopHandler() {
        handler.removeCallbacks(closeFragmentRunnable);
    }

    public void startHandler() {
        handler.postDelayed(closeFragmentRunnable, TimeUnit.SECONDS.toMillis(10)); //for 10 secs
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mediaPlayer.stop();

            try {
                mediaPlayer.reset();
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }

            mediaPlayer.release();
            mediaPlayer = null;
        } catch (Exception e) {
            Log.d("Exception", e.getMessage() + "");
        }
        finish();

    }
}
