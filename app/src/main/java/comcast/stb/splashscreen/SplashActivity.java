package comcast.stb.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

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
import comcast.stb.launcher.LauncherActivity;
import comcast.stb.launcher.LauncherModifiedActivity;
import comcast.stb.login.LoginActivity;
import comcast.stb.logout.LogoutApiInterface;
import comcast.stb.logout.LogoutPresImpl;
import comcast.stb.userutility.UserApiInterface;
import comcast.stb.userutility.UserPresImpl;
import io.realm.Realm;

import static comcast.stb.StringData.CHANNEL_PACKAGE;
import static comcast.stb.StringData.CHANNEL_PCKG;
import static comcast.stb.StringData.MOVIE_PACKAGE;
import static comcast.stb.StringData.MOVIE_PCKG;
import static comcast.stb.StringData.ORDER_ERROR;
import static comcast.stb.StringData.ORDER_LIST;
import static comcast.stb.StringData.PACKAGE_ERROR;
import static comcast.stb.StringData.SUBSCRIPTION_ERROR;
import static comcast.stb.StringData.SUBSCRIPTION_LIST;
import static comcast.stb.StringData.TOKEN_ERROR;
import static comcast.stb.StringData.USER_NAME;


public class SplashActivity extends AppCompatActivity implements UserApiInterface.UserView, LogoutApiInterface.LogoutView
        , SplashInfoFragment.OnFragmentInteractionListener {
    private LoginData loginData;
    private Realm realm;
    private LogoutPresImpl logoutPres;
    private UserPresImpl userPres;
    private ArrayList<SubsItem> subscriptionList;
    private ArrayList<OrderItem> orderItemArrayList;
    private ArrayList<PackagesInfo> channelPackageslist, moviesPackagesList;
    @BindView(R.id.avi)
    AVLoadingIndicatorView loadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        realm = Realm.getDefaultInstance();
        logoutPres = new LogoutPresImpl(this);
        userPres = new UserPresImpl(this, logoutPres);
        ButterKnife.bind(this);
        startAnim();

    }

    @Override
    protected void onStart() {
        super.onStart();
        loginData = realm.where(LoginData.class).findFirst();
        if (loginData != null) {
            userPres.getSubsHistory(loginData.getToken());

        } else {
            showLogin();
        }
    }

    private void showLogin() {
        Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void startAnim() {
        loadingIndicatorView.smoothToShow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAnim();
    }

    private void stopAnim() {
        loadingIndicatorView.smoothToHide();
    }

    @Override
    public void successfulLogout() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void setSubsHistory(List<SubsItem> subsHistory) {
        this.subscriptionList = (ArrayList<SubsItem>) subsHistory;
        userPres.getOrderHistory(loginData.getToken());
    }

    @Override
    public void setPackageInfo(List<PackagesInfo> channelListInfo, String packageType) {
        switch (packageType) {
            case CHANNEL_PACKAGE:
                this.channelPackageslist = (ArrayList<PackagesInfo>) channelListInfo;
                userPres.getPackageInfo(MOVIE_PACKAGE, loginData.getToken());
                break;
            case MOVIE_PACKAGE:
                this.moviesPackagesList = (ArrayList<PackagesInfo>) channelListInfo;
                Intent launcherIntent = new Intent(SplashActivity.this, LauncherModifiedActivity.class);
                launcherIntent.putExtra(USER_NAME, loginData.getUser().getName());
                launcherIntent.putParcelableArrayListExtra(SUBSCRIPTION_LIST, subscriptionList);
                launcherIntent.putParcelableArrayListExtra(ORDER_LIST, orderItemArrayList);
                launcherIntent.putParcelableArrayListExtra(CHANNEL_PCKG, channelPackageslist);
                launcherIntent.putParcelableArrayListExtra(MOVIE_PCKG, moviesPackagesList);
                launcherIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(launcherIntent);
                finish();
                break;
        }

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }


    @Override
    public void onErrorOccured(String errorMessage, String otherData, String errorType) {
        showErrorDialog(errorMessage, otherData, errorType);

    }

    private void showErrorDialog(String errorMessage, String otherData, String errorType) {
        SplashInfoFragment infoFragment = SplashInfoFragment.newInstance("", errorMessage, true, otherData, errorType);
        infoFragment.show(getSupportFragmentManager(), "SplashErrorFragment");
    }

    @Override
    public void setOrderHistory(List<OrderItem> orderHistory) {
        this.orderItemArrayList = (ArrayList<OrderItem>) orderHistory;
        userPres.getPackageInfo(CHANNEL_PACKAGE, loginData.getToken());
    }

    @Override
    public void onRetryBtnInteraction(String errorType, String otherData) {
        onDismissBtnInteraction();
        switch (errorType) {
            case SUBSCRIPTION_ERROR:
                userPres.getSubsHistory(loginData.getToken());
                break;
            case ORDER_ERROR:
                userPres.getOrderHistory(loginData.getToken());
                break;
            case PACKAGE_ERROR:
                if (otherData.equals(CHANNEL_PACKAGE))
                    userPres.getPackageInfo(CHANNEL_PACKAGE, loginData.getToken());
                else
                    userPres.getPackageInfo(MOVIE_PACKAGE, loginData.getToken());
                break;
            case TOKEN_ERROR:
                userPres.getSubsHistory(loginData.getToken());

        }

    }

    @Override
    public void onDismissBtnInteraction() {
        Fragment toRemoveFrag = getSupportFragmentManager().findFragmentByTag("SplashErrorFragment");
        getSupportFragmentManager().beginTransaction().remove(toRemoveFrag).commit();

    }
}
