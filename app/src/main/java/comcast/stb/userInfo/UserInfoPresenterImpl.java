package comcast.stb.userInfo;


import comcast.stb.entity.UserInfo;
import comcast.stb.logout.LogoutPresImpl;

/**
 * Created by blood-mist on 1/27/18.
 */

public class UserInfoPresenterImpl implements UserInfoApiInterface.SplashPresenter, UserInfoApiInterface.SplashListener {
    UserInfoApiInterface.SplashView splashView;
    UserInfoApiInterface.SplashInteractor splashInteractor;

    public UserInfoPresenterImpl(UserInfoApiInterface.SplashView splashView, LogoutPresImpl logoutPres) {
        this.splashView = splashView;
        splashInteractor = new UserInfoModel(this, logoutPres);
    }

    @Override
    public void getUserInfo(String token) {
        splashView.showProgress();
        splashInteractor.getUserInfo(token);
    }

    @Override
    public void takeUserInfo(UserInfo userInfo) {
        splashView.hideProgress();
        splashView.setUserInfo(userInfo);
    }

    @Override
    public void onErrorOccured(String message) {
        splashView.hideProgress();
        splashView.onErrorOccured(message);

    }
}
