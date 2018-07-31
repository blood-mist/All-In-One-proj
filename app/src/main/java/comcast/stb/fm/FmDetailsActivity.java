package comcast.stb.fm;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cleveroad.audiovisualization.AudioVisualization;
import com.cleveroad.audiovisualization.DbmHandler;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comcast.stb.R;
import comcast.stb.entity.BuyResponse;
import comcast.stb.entity.FmCategory;
import comcast.stb.entity.FmsItem;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.MovieLink;
import comcast.stb.entity.MoviesItem;
import comcast.stb.entity.NewToken;
import comcast.stb.entity.events.FmErrorEvent;
import comcast.stb.entity.events.FmPlayingEvent;
import comcast.stb.movielist.CenterLayoutManager;
import comcast.stb.movielist.MovieDialogFragment;
import comcast.stb.movielist.MovieExoPlay;
import comcast.stb.movielist.MovieListApiInterface;
import comcast.stb.movielist.SingleItemListAdapter;
import comcast.stb.purchase.moviepurchase.BuyMovieDialog;
import comcast.stb.tokenrefresh.TokenPresImpl;
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

import static comcast.stb.StringData.AUTH_TOKEN;
import static comcast.stb.StringData.BUY_ERROR;
import static comcast.stb.StringData.FM_ID;
import static comcast.stb.StringData.MOVIE_CATEGORY;
import static comcast.stb.StringData.MOVIE_ITEM;
import static comcast.stb.StringData.MOVIE_PLAY_ERROR;
import static comcast.stb.StringData.TOKEN_ERROR;
import static comcast.stb.StringData.VIDEO_URL;

public class FmDetailsActivity extends AppCompatActivity {
    @BindView(R.id.fm_picture)
    ImageView fmImage;
    @BindView(R.id.fm_name)
    TextView fmName;

    @BindView(R.id.visualizer_view)
    AudioVisualization audioVisualization;

    @BindView(R.id.fm_details_progress)
    AVLoadingIndicatorView progressbar;


    @BindView(R.id.simiar_fms_recycler)
    RecyclerView similarRecycler;

    @BindView(R.id.icon_fm_play)
    ImageView pausePlay;

    @BindView(R.id.icon_fm_next)
    ImageView nextButton;

    @BindView(R.id.icon_fm_prev)
    ImageView prevButton;


    private FmCategory currentFmCategory;

    private TokenPresImpl tokenPres;
    private FmsItem currentFm;
    private int currentFmPostion;

    private ArrayList<FmsItem> similarFmsList;

    private LoginData loginData;
    private Realm realm;
    private static final int REQUEST_CODE = 1;
    private boolean isPlaying = true;
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_CLOSE = "ACTION_CLOSE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fm_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        if (getIntent().getExtras() == null) {
            Toast.makeText(this, "Something went Wrong, Please try again", Toast.LENGTH_LONG).show();
            finish();

        } else {
            currentFm = getIntent().getParcelableExtra(MOVIE_ITEM);
            currentFmCategory = getIntent().getParcelableExtra(MOVIE_CATEGORY);
        }
        realm = Realm.getDefaultInstance();
        loginData = realm.where(LoginData.class).findFirst();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
            audioVisualization.linkTo(DbmHandler.Factory.newVisualizerHandler(this, 0));
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.MODIFY_AUDIO_SETTINGS)) {
                AlertDialog.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            requestPermissions();
                        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                            permissionsNotGranted();
                        }
                    }
                };
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.title_permissions))
                        .setMessage(Html.fromHtml(getString(R.string.message_permissions)))
                        .setPositiveButton(getString(R.string.allow), onClickListener)
                        .setNegativeButton(getString(R.string.btn_cancel), onClickListener)
                        .show();
            } else {
                requestPermissions();
            }
        }
        setFmDetails();
        setSimilarMovies();

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS},
                REQUEST_CODE
        );
    }

    private void permissionsNotGranted() {
        Toast.makeText(this, R.string.toast_permissions_not_granted, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            boolean bothGranted = true;
            for (int i = 0; i < permissions.length; i++) {
                if (Manifest.permission.RECORD_AUDIO.equals(permissions[i]) || Manifest.permission.MODIFY_AUDIO_SETTINGS.equals(permissions[i])) {
                    bothGranted &= grantResults[i] == PackageManager.PERMISSION_GRANTED;
                }
            }
            if (bothGranted) {
                audioVisualization.linkTo(DbmHandler.Factory.newVisualizerHandler(this, 0));
            } else {
                permissionsNotGranted();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        audioVisualization.onResume();
    }

    private void setFmDetails() {
        Picasso.with(FmDetailsActivity.this)
                .load(currentFm.getImage())
                .resize(250, 350)
                .placeholder(R.drawable.placeholder)
                .into(fmImage);


        fmName.setText(currentFm.getName());


    }

    @Override
    protected void onPause() {
        audioVisualization.onPause();
        super.onPause();
    }

    @OnClick(R.id.icon_fm_play)
    public void onWatchClick() {
        updatePlayPauseButton(currentFm);
    }

    @OnClick(R.id.icon_fm_prev)
    public void onPrevClicked() {
        if (currentFmPostion >0) {
            currentFmPostion--;
            currentFm =similarFmsList.get(currentFmPostion);
            setFmDetails();
        }
    }

    @OnClick(R.id.icon_fm_next)
    public void onNextClicked() {
        if (currentFmPostion < currentFmCategory.getFms().size() - 1) {
            currentFmPostion++;
            currentFm = similarFmsList.get(currentFmPostion);
            setFmDetails();
    }
    }

    private void updatePlayPauseButton(FmsItem currentFm) {
        int fmID = currentFm.getId();
        if (isPlaying) {
            updateUI(true);
//            mService.stopPlayer();

        } else {
            updateUI(false);
            //            mService.resumePlayer();
        }
        Intent intent = new Intent(this, FmBindService.class);
        intent.putExtra(FM_ID, fmID);
        intent.putExtra(AUTH_TOKEN, loginData.getToken());
        if (isPlaying) {
            intent.setAction(ACTION_STOP);

        } else {
            intent.setAction(ACTION_PLAY);
        }
        startService(intent);
    }

    private void updateUI(boolean b) {
        if (b) {
            pausePlay.setImageDrawable(getResources().getDrawable(R.drawable.exo_controls_play));
            isPlaying = false;
        } else {
            pausePlay.setImageDrawable(getResources().getDrawable(R.drawable.exo_controls_pause));
            isPlaying = true;
        }
    }


    private void onErrorOccured(String message) {
        stopAnim();
        Toast.makeText(FmDetailsActivity.this, "FM Couldn't be played", Toast.LENGTH_SHORT).show();
    }

    private void startAnim() {
        progressbar.smoothToShow();
    }

    private void stopAnim() {
        progressbar.smoothToHide();
    }

    private void setSimilarMovies() {
        similarFmsList = (ArrayList<FmsItem>) currentFmCategory.getFms();
        for(int i=0;i<currentFmCategory.getFms().size();i++){
            if(currentFm.getId()==currentFmCategory.getFms().get(i).getId()){
                similarFmsList.remove(i);
                break;
            }
        }
        FmItemListAdapter itemListAdapter = new FmItemListAdapter(FmDetailsActivity.this, similarFmsList);
        LinearLayoutManager manager = new CenterLayoutManager(FmDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        similarRecycler.setLayoutManager(manager);
        similarRecycler.setHasFixedSize(true);
        similarRecycler.setAdapter(itemListAdapter);
        itemListAdapter.SetOnItemFocusedListener(new FmItemListAdapter.OnItemSelectListener() {
            @Override
            public void onItemSelected(View view, int position) {
                similarRecycler.smoothScrollToPosition(position);
            }
        });

        itemListAdapter.SetOnItemClickListener(new FmItemListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int itemPosition, FmsItem model) {
                Intent intent = new Intent(FmDetailsActivity.this, FmDetailsActivity.class);
                intent.putExtra(MOVIE_ITEM, model);
                intent.putExtra(MOVIE_CATEGORY, currentFmCategory);
                startActivity(intent);
                FmDetailsActivity.this.finish();
            }
        });

        itemListAdapter.notifyDataSetChanged();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FmPlayingEvent event) {
        if (event.isPlaying()) {
            updateUI(false);

        } else {
            updateUI(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FmErrorEvent event) {
        isPlaying = false;
        onErrorOccured(event.getMessage());
    }


    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    public void onError(String message) {
        onErrorOccured(message);

    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void updateProgress(boolean b) {
        if (b) {
            startAnim();
        } else {
            stopAnim();
        }
    }
}
