package comcast.stb.subscriptions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.R;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.OrderItem;
import comcast.stb.entity.PackagesInfo;
import comcast.stb.entity.SubsItem;
import comcast.stb.entity.UserInfo;
import comcast.stb.logout.LogoutApiInterface;
import comcast.stb.logout.LogoutPresImpl;
import comcast.stb.userInfo.UserInfoApiInterface;
import comcast.stb.userInfo.UserInfoPresenterImpl;
import comcast.stb.userutility.UserApiInterface;
import comcast.stb.userutility.UserPresImpl;
import io.realm.Realm;

public class SubscriptionActivity extends AppCompatActivity implements UserInfoApiInterface.SplashView, UserApiInterface.UserView, LogoutApiInterface.LogoutView {
    private LoginData loginData;
    private Realm realm;
    private LogoutPresImpl logoutPres;
    private UserPresImpl userPres;
    private UserInfoPresenterImpl userInfoPresenter;
    private ArrayList<SubsItem> subscriptionList;
    private ArrayList<OrderItem> orderItemArrayList;


    @BindView(R.id.txt_userName)
    TextView username;

    @BindView(R.id.txt_userEmail)
    TextView userEmail;

    @BindView(R.id.txt_userBalance)
    TextView userBalance;

    @BindView(R.id.recycler_subscription)
    RecyclerView subscriptionRecycler;

    @BindView(R.id.recycler_orderHistory)
    RecyclerView orderHistoryRecycler;

    @BindView(R.id.progress_subscription)
    AVLoadingIndicatorView progressbar;

    @BindView(R.id.txt_noSubscription)
    TextView noSubscription;

    @BindView(R.id.txt_noOrder)
    TextView noOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription2);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        loginData = realm.where(LoginData.class).findFirst();
        logoutPres = new LogoutPresImpl(this);
        userInfoPresenter = new UserInfoPresenterImpl(this, logoutPres);
        userPres = new UserPresImpl(this, logoutPres);
        userInfoPresenter.getUserInfo(loginData.getToken());
        userPres.getSubsHistory(loginData.getToken());
        userPres.getOrderHistory(loginData.getToken());

    }

    @Override
    public void setUserInfo(UserInfo userInfo) {
        username.setText(userInfo.getUserData().getName());
        userEmail.setText(userInfo.getUserData().getEmail());
        userBalance.setText("You have $" + userInfo.getUserData().getBalance());

    }

    @Override
    public void onErrorOccured(String message) {

    }

    @Override
    public void setSubsHistory(List<SubsItem> subsHistory) {
        if(subsHistory!=null && subsHistory.size()>0) {
            MainSubsRecyclerAdapter subsRecyclerAdapter = new MainSubsRecyclerAdapter(SubscriptionActivity.this, (ArrayList<SubsItem>) subsHistory);
            LinearLayoutManager manager = new LinearLayoutManager(SubscriptionActivity.this, LinearLayoutManager.HORIZONTAL, false);
            subscriptionRecycler.setLayoutManager(manager);
            subscriptionRecycler.setAdapter(subsRecyclerAdapter);
        }else{
            subscriptionRecycler.setVisibility(View.GONE);
            noSubscription.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void setPackageInfo(List<PackagesInfo> channelListInfo, String packageType) {

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
    public void onErrorOccured(String error_message, String otherInfo, String errorType) {

    }

    @Override
    public void setOrderHistory(List<OrderItem> orderHistory) {
        if(orderHistory!=null && orderHistory.size()>0) {
            MainOrderRecyclerAdapter orderRecyclerAdapter = new MainOrderRecyclerAdapter(SubscriptionActivity.this, (ArrayList<OrderItem>) orderHistory);
            LinearLayoutManager manager = new LinearLayoutManager(SubscriptionActivity.this, LinearLayoutManager.HORIZONTAL, false);
            orderHistoryRecycler.setLayoutManager(manager);
            orderHistoryRecycler.setAdapter(orderRecyclerAdapter);
        }else{
            orderHistoryRecycler.setVisibility(View.GONE);
            noOrders.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void successfulLogout() {
        logoutPres.logout();
    }
}
