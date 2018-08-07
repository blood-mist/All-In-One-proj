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


public class SplashActivity extends AppCompatActivity {
    private LoginData loginData;
    private Realm realm;

    @BindView(R.id.avi)
    AVLoadingIndicatorView loadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        realm = Realm.getDefaultInstance();
        ButterKnife.bind(this);
        startAnim();

    }

    @Override
    protected void onStart() {
        super.onStart();
        loginData = realm.where(LoginData.class).findFirst();
        if (loginData != null) {
//            userPres.getSubsHistory(loginData.getToken());
            Intent launcherIntent = new Intent(SplashActivity.this, LauncherModifiedActivity.class);
            launcherIntent.putExtra(USER_NAME, loginData.getUser().getName());
            launcherIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(launcherIntent);
            finish();

        } else {
            showLogin();
        }
    }

    private void showLogin() {
        Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

}
