package comcast.stb.fm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cleveroad.audiovisualization.AudioVisualization;
import com.cleveroad.audiovisualization.DbmHandler;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comcast.stb.R;
import comcast.stb.entity.FmCategory;
import comcast.stb.entity.FmsItem;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.events.FmErrorEvent;
import comcast.stb.entity.events.FmPlayingEvent;
import comcast.stb.login.LoginActivity;
import comcast.stb.logout.LogoutApiInterface;
import comcast.stb.logout.LogoutPresImpl;
import comcast.stb.utils.EqualSpacingItemDecoration;
import io.realm.Realm;

import static comcast.stb.StringData.AUTH_TOKEN;
import static comcast.stb.StringData.FM_ID;


public class FmListActivity extends AppCompatActivity implements FmApiInterface.FmWithCategoryView, LogoutApiInterface.LogoutView, FmCategoryRecyclerAdapter.OnCategoryListInteractionListener, FmRecyclerAdapter.OnFmListInteractionListener {
    FmPresImpl fmPresenter;
    FmCategoryRecyclerAdapter categoryRecyclerAdapter;
    FmRecyclerAdapter fmRecyclerAdapter;
    private boolean isPlaying = true;
    FmBindService mService;
    boolean mBound = false;
    private ArrayList<FmCategory> fmCategoryList;

    private ArrayList<FmsItem> fmList;
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_CLOSE = "ACTION_CLOSE";

    @BindView(R.id.img_fm_logout)
    ImageView logout;
    @BindView(R.id.txt_fm_username)
    TextView username;
    @BindView(R.id.recycler_fm_category_list)
    RecyclerView fmCategory;
    @BindView(R.id.fm_list_recycler)
    RecyclerView fmlist;
    @BindView(R.id.txt_current_fm)
    TextView fmName;
    @BindView(R.id.current_fm_category)
    TextView currentCategory;
    @BindView(R.id.no_content)
    TextView noContent;
    @BindView(R.id.progressbar_fmCategory)
    AVLoadingIndicatorView progressbar;
    @BindView(R.id.fm_play_layout)
    LinearLayout playLayout;
    @BindView(R.id.img_fm_play)
    ImageView pausePlay;
    @BindView(R.id.visualizer_view)
    AudioVisualization audioVisualization;
    @BindView(R.id.category_container)
    LinearLayout categoryLayout;
    @BindView(R.id.fm_list_container)
    LinearLayout fmListLayout;
    private Realm realm;
    LoginData loginData;
    private FmsItem currentFm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fm_new);
        ButterKnife.bind(this);
        LogoutPresImpl logoutPres = new LogoutPresImpl(this);
        categoryLayout.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                if(categoryLayout.getFocusedChild()==null){
                    categoryLayout.setBackgroundDrawable(ContextCompat.getDrawable(FmListActivity.this,R.drawable.menu_left_bg_unselected));

                }else{
                    categoryLayout.setBackgroundDrawable(ContextCompat.getDrawable(FmListActivity.this,R.drawable.menu_left_bg_selected));
                }

            }
        });
        fmListLayout.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                if(fmListLayout.getFocusedChild()==null){
                    fmListLayout.setBackgroundDrawable(ContextCompat.getDrawable(FmListActivity.this,R.drawable.menu_right_bg_unselected));

                }else{
                    fmListLayout.setBackgroundDrawable(ContextCompat.getDrawable(FmListActivity.this,R.drawable.menu_right_bg_selected));

                }

            }
        });
        fmPresenter = new FmPresImpl(this, logoutPres);
        realm = Realm.getDefaultInstance();
        loginData = realm.where(LoginData.class).findFirst();
        noContent.setVisibility(View.VISIBLE);
        playLayout.setVisibility(View.GONE);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        fmlist.setLayoutManager(manager);
        audioVisualization.linkTo(DbmHandler.Factory.newVisualizerHandler(this, 0));

    }

    @Override
    protected void onResume() {
        super.onResume();
        audioVisualization.onResume();
    }

    @Override
    protected void onPause() {
        audioVisualization.onPause();
        super.onPause();
    }

    @OnClick(R.id.img_fm_play)
    public void onPlayClick() {

        updatePlayPauseButton(currentFm);
    }

    private void updatePlayPauseButton(FmsItem currentFm) {
        int fmID = currentFm.getId();
        if (isPlaying) {
            pausePlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle_outline_black_24dp));

            isPlaying = false;
//            mService.stopPlayer();

        } else {
            pausePlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_outline_black_24dp));
            isPlaying = true;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FmPlayingEvent event) {
        if (event.isPlaying()) {
            pausePlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_outline_black_24dp));
            isPlaying = false;

        } else {
            pausePlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle_outline_black_24dp));
            isPlaying = true;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FmErrorEvent event) {
        isPlaying = false;
        onPlayErrorOccured(event.getMessage());
    }

    public void onPlayErrorOccured(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mService.getFmURL(currentFm.getId(), loginData.getToken());
                    }
                }).setActionTextColor(getResources().getColor(R.color.white_color));
        snackbar.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        String authToken = loginData.getToken();
        fmPresenter.getFmsWithCategory(authToken);
    }

    @Override
    protected void onDestroy() {
        audioVisualization.release();
        super.onDestroy();
    }

    @Override
    public void setFmsWithCategory(List<FmCategory> fmCategoryList) {
        this.fmCategoryList = (ArrayList<FmCategory>) fmCategoryList;
        fmCategory.setLayoutManager(new LinearLayoutManager(FmListActivity.this, LinearLayoutManager.VERTICAL, false));
        if (categoryRecyclerAdapter == null)
            categoryRecyclerAdapter = new FmCategoryRecyclerAdapter(FmListActivity.this, this.fmCategoryList, FmListActivity.this);
        fmCategory.setAdapter(categoryRecyclerAdapter);
        fmCategory.addItemDecoration(new EqualSpacingItemDecoration(10, EqualSpacingItemDecoration.HORIZONTAL));
        fmCategory.requestFocus();

    }

    @Override
    public void onErrorOccured(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fmPresenter.getFmsWithCategory(loginData.getToken());
                    }
                }).setActionTextColor(getResources().getColor(R.color.white_color));
        snackbar.show();
    }

    @Override
    public void showProgress() {
        startAnim();
    }

    private void startAnim() {
        progressbar.smoothToShow();
    }

    @Override
    public void hideProgress() {
        stopAnim();
    }

    private void stopAnim() {
        progressbar.smoothToHide();
    }

    @Override
    public void successfulLogout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onCategoryListClickInteraction(FmCategory fmCategory) {
        this.fmList = (ArrayList<FmsItem>) fmCategory.getFms();
        currentCategory.setText(fmCategory.getCategoryName());
        fmRecyclerAdapter = new FmRecyclerAdapter(this, this.fmList, FmListActivity.this);
        fmlist.setAdapter(fmRecyclerAdapter);
        fmRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFmClickInteraction(FmsItem fmsItem) {
        playFm(fmsItem);
    }

    private void playFm(FmsItem fmsItem) {
        int fmID = fmsItem.getId();
        Intent intent = new Intent(this, FmBindService.class);
        intent.putExtra(FM_ID, fmID);
        intent.putExtra(AUTH_TOKEN, loginData.getToken());
        if (isPlaying) {
            intent.setAction(ACTION_PLAY);
        } else {
            intent.setAction(ACTION_STOP);
        }
        startService(intent);
    }

    @Override
    public void onFmSelected(FmsItem fmsItem) {
        fmName.setText(fmsItem.getName());
        this.currentFm = fmsItem;
        noContent.setVisibility(View.GONE);
        playLayout.setVisibility(View.VISIBLE);
    }
}
