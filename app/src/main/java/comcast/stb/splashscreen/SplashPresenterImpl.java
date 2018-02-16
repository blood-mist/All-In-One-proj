package comcast.stb.splashscreen;


import comcast.stb.entity.UserInfo;
import comcast.stb.logout.LogoutPresImpl;

/**
 * Created by blood-mist on 1/27/18.
 */

public class SplashPresenterImpl implements SplashApiInterface.SplashPresenter, SplashApiInterface.SplashListener {
    SplashApiInterface.SplashView splashView;
    SplashApiInterface.SplashInteractor splashInteractor;

    public SplashPresenterImpl(SplashApiInterface.SplashView splashView, LogoutPresImpl logoutPres) {
        this.splashView = splashView;
        splashInteractor = new SplashModel(this, logoutPres);
    }

    @Override
    public void getUserInfo(String token) {
        splashInteractor.getUserInfo(token);
    }

    @Override
    public void takeUserInfo(UserInfo userInfo) {
        splashView.setUserInfo(userInfo);
    }

    @Override
    public void onErrorOccured(String message) {
        splashView.onErrorOccured(message);

    }
}
