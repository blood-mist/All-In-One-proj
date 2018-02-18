package comcast.stb.launcher;

import android.app.PendingIntent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comcast.stb.R;
import comcast.stb.entity.AppData;
import comcast.stb.entity.OrderItem;
import comcast.stb.entity.PackagesInfo;
import comcast.stb.entity.SubsItem;
import comcast.stb.entity.events.FmLauncherEvent;

import static comcast.stb.StringData.CHANNEL_PACKAGE;
import static comcast.stb.StringData.CHANNEL_PCKG;
import static comcast.stb.StringData.MOVIE_PCKG;
import static comcast.stb.StringData.ORDER_LIST;
import static comcast.stb.StringData.SUBSCRIPTION_LIST;
import static comcast.stb.StringData.USER_NAME;
import static comcast.stb.utils.StringData.LIVE_TV;
import static comcast.stb.utils.StringData.MOVIE;
import static comcast.stb.utils.StringData.RADIO_SERVICE;


public class LauncherActivity extends AppCompatActivity implements MainPckgRecyclerAdapter.OnPackageListInteraction,MainSubsRecyclerAdapter.OnSubsListInteraction,MainOrderRecyclerAdapter.OnOrderInteractionListener {
    private ArrayList<AppData> appDataList;
    @BindView(R.id.app_recycler_list)
    RecyclerView appRecyclerList;
    @BindView(R.id.receiver_layout)
    LinearLayout fmLayout;
    @BindView(R.id.txt_mediaTitle)
    TextView fmTitle;
    @BindView(R.id.txt_currentMedia)
    TextView fmName;
    @BindView(R.id.txt_username)
    TextView userName;
    @BindView(R.id.img_logout)
    ImageView logout;
    @BindView(R.id.subscription_recycler_list)
    RecyclerView subscriptionRecycler;
    @BindView(R.id.order_recycler_list)
    RecyclerView orderRecyclerlist;
    @BindView(R.id.img_ad)
    ImageView ad_banner;
    @BindView(R.id.channel_pckg_recycler)
    RecyclerView channelPckgRecycler;
    @BindView(R.id.movie_pckg_recycler)
    RecyclerView moviePckgRecycler;
    @BindView(R.id.txt_sub_no_content)
    TextView noSubscription;
    @BindView(R.id.txt_order_no_content)
    TextView noOrders;
    @BindView(R.id.txt_chPkg_no_content)
    TextView noChanelpkg;
    @BindView(R.id.txt_movPkg_no_content)
    TextView noMoviePkg;

    private PendingIntent resultPendingIntent;
    private ArrayList<SubsItem>subscriptionList;
    private ArrayList<OrderItem> orderItemArrayList;
    private ArrayList<PackagesInfo>channelPackageslist,moviesPackagesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        String user=getIntent().getStringExtra(USER_NAME);
        subscriptionList=getIntent().getParcelableArrayListExtra(SUBSCRIPTION_LIST);
        orderItemArrayList=getIntent().getParcelableArrayListExtra(ORDER_LIST);
        channelPackageslist=getIntent().getParcelableArrayListExtra(CHANNEL_PCKG);
        moviesPackagesList=getIntent().getParcelableArrayListExtra(MOVIE_PCKG);
        userName.setText(user);
        addItemsToList();
        populatePackages();
        poplateSubscriptions();
        populateList();

    }

    private void poplateSubscriptions() {
        if(subscriptionList.size()>0) {
            MainSubsRecyclerAdapter subsRecyclerAdapter = new MainSubsRecyclerAdapter(this, subscriptionList);
            LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            subscriptionRecycler.setLayoutManager(manager);
            subscriptionRecycler.setAdapter(subsRecyclerAdapter);
            noSubscription.setVisibility(View.GONE);
            subscriptionRecycler.setVisibility(View.VISIBLE);

        }else{
            subscriptionRecycler.setVisibility(View.GONE);
            noSubscription.setVisibility(View.VISIBLE);
        }
        if(orderItemArrayList.size()>0) {
            MainOrderRecyclerAdapter orderRecyclerAdapter = new MainOrderRecyclerAdapter(this, orderItemArrayList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            orderRecyclerlist.setLayoutManager(layoutManager);
            orderRecyclerlist.setAdapter(orderRecyclerAdapter);
            noOrders.setVisibility(View.GONE);
            orderRecyclerlist.setVisibility(View.VISIBLE);
        }else{
            orderRecyclerlist.setVisibility(View.GONE);
            noOrders.setVisibility(View.VISIBLE);
        }
    }

    private void populatePackages() {
        if(channelPackageslist.size()>0) {
            MainPckgRecyclerAdapter channelPckgAdapter = new MainPckgRecyclerAdapter(LauncherActivity.this, channelPackageslist, CHANNEL_PACKAGE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            channelPckgRecycler.setLayoutManager(layoutManager);
            channelPckgRecycler.setAdapter(channelPckgAdapter);
            noChanelpkg.setVisibility(View.GONE);
            channelPckgRecycler.setVisibility(View.VISIBLE);
        }else{
            channelPckgRecycler.setVisibility(View.GONE);
            noChanelpkg.setVisibility(View.VISIBLE);
        }
        if(moviesPackagesList.size()>0) {
            MainPckgRecyclerAdapter moviePckgAdapter = new MainPckgRecyclerAdapter(LauncherActivity.this, channelPackageslist, CHANNEL_PACKAGE);
            LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            moviePckgRecycler.setLayoutManager(manager);
            moviePckgRecycler.setAdapter(moviePckgAdapter);
            noMoviePkg.setVisibility(View.GONE);
            moviePckgRecycler.setVisibility(View.VISIBLE);
        }else{
            moviePckgRecycler.setVisibility(View.GONE);
            noMoviePkg.setVisibility(View.VISIBLE);
        }
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FmLauncherEvent event) {
        if (event.isFmPlaying()) {
            fmTitle.setText(event.getFmTitle());
            fmName.setText(event.getFmName());
            resultPendingIntent=event.getIntent();
            fmLayout.setVisibility(View.VISIBLE);
        }else{
            fmLayout.setVisibility(View.INVISIBLE);
            resultPendingIntent=event.getIntent();
        }

    }

    @OnClick(R.id.btn_pause)
    public void onClick() {
        try {
            resultPendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    private void populateList() {
        AppListRecyclerAdapter appListRecyclerAdapter = new AppListRecyclerAdapter(LauncherActivity.this, appDataList, appRecyclerList);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        appRecyclerList.setLayoutManager(layoutManager);
        appRecyclerList.setAdapter(appListRecyclerAdapter);
        appRecyclerList.requestFocus();
    }

    private void addItemsToList() {
        appDataList = new ArrayList<>();
        appDataList.add(new AppData(LIVE_TV,  ContextCompat.getDrawable(this, R.drawable.live)));
        appDataList.add(new AppData(MOVIE, ContextCompat.getDrawable(this, R.drawable.video)));
        appDataList.add(new AppData(RADIO_SERVICE, ContextCompat.getDrawable(this, R.drawable.radio)));
        appDataList.add(new AppData("settings", ContextCompat.getDrawable(this, R.drawable.settings)));
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    @Override
    public void onSubscriptionInfoClicked(SubsItem subsInfo) {

    }

    @Override
    public void onOrderInfoClicked(OrderItem orderItem) {

    }

    @Override
    public void onPackageInfoClicked(PackagesInfo packagesInfo, String packageType) {

    }

    @Override
    public void onPackageBuyClicked(PackagesInfo packagesInfo, String packageType) {

    }
}
