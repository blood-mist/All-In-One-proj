package comcast.stb.logout;

/**
 * Created by anilpaudel on 1/9/18.
 */

public class LogoutPresImpl implements LogoutApiInterface.LogoutPresenter,LogoutApiInterface.LogoutListener {
    LogoutApiInterface.LogoutView logoutView;
    LogoutApiInterface.LogoutInteractor logoutInteractor;

    public LogoutPresImpl(LogoutApiInterface.LogoutView logoutView) {
        this.logoutView = logoutView;
        logoutInteractor = new LogoutModel(this);
    }

    @Override
    public void logout() {
        logoutInteractor.logout();
    }

    @Override
    public void successfulLogout() {
        logoutView.successfulLogout();
    }
}
