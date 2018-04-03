package comcast.stb.launcher;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comcast.stb.R;
import comcast.stb.adBanner.AdApiInterface;
import comcast.stb.adBanner.AdPresImpl;
import comcast.stb.entity.AdItem;
import comcast.stb.entity.AppData;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.events.FmLauncherEvent;
import comcast.stb.login.LoginActivity;
import comcast.stb.logout.LogoutApiInterface;
import comcast.stb.logout.LogoutPresImpl;
import comcast.stb.movielist.MovieNewActivity;
import comcast.stb.utils.AppConfig;
import comcast.stb.utils.Connectivity;
import comcast.stb.utils.DeviceUtils;
import io.realm.Realm;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

import static comcast.stb.StringData.USER_NAME;
import static comcast.stb.utils.StringData.LIVE_TV;
import static comcast.stb.utils.StringData.MOVIE;
import static comcast.stb.utils.StringData.PACKAGES;
import static comcast.stb.utils.StringData.RADIO_SERVICE;
import static comcast.stb.utils.StringData.SETTINGS;
import static comcast.stb.utils.StringData.SUBSCRIPTIONS;

public class LauncherModifiedActivity extends AppCompatActivity implements AdApiInterface.AdView, LogoutApiInterface.LogoutView {
    private ArrayList<AppData> appDataList;
    @BindView(R.id.app_recycler_list)
    RecyclerView appRecyclerList;
    @BindView(R.id.receiver_layout)
    LinearLayout fmLayout;
    @BindView(R.id.txt_mediaTitle)
    TextView fmTitle;
    @BindView(R.id.txt_currentMedia)
    TextView fmName;
    @BindView(R.id.img_connectivity)
    ImageView imgNetwork;
    @BindView(R.id.txt_date)
    TextView txtDate;
    @BindView(R.id.txt_username)
    TextView userName;
    @BindView(R.id.txt_boxId)
    TextView boxId;

    @BindView(R.id.banner_slider1)
    BannerSlider bannerSlider;
    private PendingIntent resultPendingIntent;
    private BroadcastReceiver mNetworkReceiver;
    AdPresImpl adPres;
    private LoginData loginData;
    LogoutPresImpl logoutPres;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_modified);
        EventBus.getDefault().register(this);
        mNetworkReceiver = new NetworkChangeReceiver();
        ButterKnife.bind(this);
        registerNetworkBroadcast();
        realm = Realm.getDefaultInstance();
        loginData = realm.where(LoginData.class).findFirst();
        String user = getIntent().getStringExtra(USER_NAME);
        logoutPres = new LogoutPresImpl(this);
        adPres = new AdPresImpl(this, null);
        setTime();
        checkNetwork();
        userName.setText(user);
        boxId.setText(AppConfig.isDevelopment()?AppConfig.getMac(): DeviceUtils.getMac(this));
        addItemsToList();
        populateList();
        getAdBanners();

    }

    private void getAdBanners() {
        adPres.getAdBanners(loginData.getToken());

    }

    private void registerNetworkBroadcast() {
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void checkNetwork() {
        if (Connectivity.isConnected(this)) {
            if (Connectivity.isConnectedWifi(this))
                imgNetwork.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.wifi_connected));
            else if (Connectivity.isEthernetConnected(this))
                imgNetwork.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.lan_connected));


        } else {
            imgNetwork.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.wifi_disconnected));
        }
    }

    private void setTime() {
        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE,dd MMM yyyy");
        txtDate.setText(dateFormat.format(time));


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FmLauncherEvent event) {
        if (event.isFmPlaying()) {
            fmTitle.setText(event.getFmTitle());
            fmName.setText(event.getFmName());
            resultPendingIntent = event.getIntent();
            fmLayout.setVisibility(View.VISIBLE);
        } else {
            fmLayout.setVisibility(View.INVISIBLE);
            resultPendingIntent = event.getIntent();
        }

    }

    @OnClick(R.id.btn_pause)
    public void onClick() {
        try {
            resultPendingIntent.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateList() {
        AppListRecyclerAdapter appListRecyclerAdapter = new AppListRecyclerAdapter(LauncherModifiedActivity.this, appDataList, appRecyclerList);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        appRecyclerList.setLayoutManager(layoutManager);
        appRecyclerList.setAdapter(appListRecyclerAdapter);
        appRecyclerList.setNestedScrollingEnabled(false);
        appRecyclerList.setHasFixedSize(true);
    }

    private void addItemsToList() {
        appDataList = new ArrayList<>();
        appDataList = new ArrayList<>();
        appDataList.add(new AppData(LIVE_TV, ContextCompat.getDrawable(this, R.drawable.live)));
        appDataList.add(new AppData(SUBSCRIPTIONS, ContextCompat.getDrawable(this, R.drawable.subscribe)));
        appDataList.add(new AppData(MOVIE, ContextCompat.getDrawable(this, R.drawable.video)));
        appDataList.add(new AppData(SETTINGS, ContextCompat.getDrawable(this, R.drawable.settings)));
        appDataList.add(new AppData(RADIO_SERVICE, ContextCompat.getDrawable(this, R.drawable.radio)));
        appDataList.add(new AppData(PACKAGES, ContextCompat.getDrawable(this, R.drawable.package_icon)));

    }

    @OnClick(R.id.img_logout)
    public void logout() {
        logoutPres.logout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setAdBanners(List<AdItem> bannerList) {
        List<Banner> banners = new ArrayList<>();
        //add banner using image url
        for (AdItem item : bannerList) {
            banners.add(new RemoteBanner(item.getChannelLogo()).setScaleType(ImageView.ScaleType.FIT_XY));
        }

        //add banner using resource drawable
//        banners.add(new DrawableBanner(R.drawable.yourDrawable));
        bannerSlider.setBanners(banners);
    }

    @Override
    public void onErrorOccured(String message) {

    }

    @Override
    public void successfulLogout() {
        Toast.makeText(LauncherModifiedActivity.this,"User successfully logged out",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LauncherModifiedActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkNetwork();
        }
    }
}
