package comcast.stb.login;


import comcast.stb.entity.LoginData;

/**
 * Created by nitv on 2/28/17.
 */

public class LoginPresenterImpl implements LoginApiInterface.LoginPresenter, LoginApiInterface.LoginListener {
    LoginApiInterface.LoginView loginView;
    LoginApiInterface.LoginInteractor loginModel;

    public LoginPresenterImpl(LoginApiInterface.LoginView loginView) {
        this.loginView = loginView;
        loginModel = new LoginModel(this);
    }


    @Override
    public void userTryingToLogin(String userName, String password) {
        loginView.showProgress();
        loginModel.checkCredentials(userName, password);
    }


    @Override
    public void getCredentials() {
        loginModel.getCredentials();
    }


    @Override
    public void successfullyLoggedIn(LoginData loginData) {
        loginView.onSuccessfullyLoggedIn(loginData);
    }

    @Override
    public void logginFromReal(LoginData loginData) {
        loginView.logginFromReal(loginData);
    }


    @Override
    public void onErrorOccured(String message) {
        loginView.errorMessage(message);
        loginView.hideProgress();
    }


}

