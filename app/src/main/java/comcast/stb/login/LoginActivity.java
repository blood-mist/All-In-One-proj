package comcast.stb.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comcast.stb.R;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.OrderItem;
import comcast.stb.entity.PackagesInfo;
import comcast.stb.entity.SubsItem;
import comcast.stb.launcher.LauncherModifiedActivity;
import comcast.stb.logout.LogoutApiInterface;
import comcast.stb.logout.LogoutPresImpl;
import comcast.stb.userutility.UserApiInterface;
import comcast.stb.userutility.UserPresImpl;
import comcast.stb.utils.AppConfig;
import comcast.stb.utils.DeviceUtils;
import io.realm.Realm;

import static comcast.stb.StringData.CHANNEL_PACKAGE;
import static comcast.stb.StringData.CHANNEL_PCKG;
import static comcast.stb.StringData.MOVIE_PACKAGE;
import static comcast.stb.StringData.MOVIE_PCKG;
import static comcast.stb.StringData.ORDER_LIST;
import static comcast.stb.StringData.SUBSCRIPTION_LIST;
import static comcast.stb.StringData.USER_NAME;


public class LoginActivity extends AppCompatActivity implements LoginApiInterface.LoginView, Validator.ValidationListener,
        LoginInfoFragment.OnFragmentInteractionListener,UserApiInterface.UserView,LogoutApiInterface.LogoutView {
    private LoginPresenterImpl loginPresenter;
    private UserPresImpl userPres;
    private LogoutPresImpl logoutPres;
    private ArrayList<SubsItem> subscriptionList;
    private ArrayList<OrderItem> orderItemArrayList;
    private ArrayList<PackagesInfo> channelPackageslist, moviesPackagesList;

    @BindView(R.id.txt_user_mac)
    TextView userMac;

    @Password(min = 6, scheme = Password.Scheme.ANY)
    @BindView(R.id.edittext_user_password)
    EditText userPassword;
    @BindView(R.id.progressBar2)
    AVLoadingIndicatorView progressBar;
    private Validator validator;
    Realm realm;
    private LoginData loginData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        realm = Realm.getDefaultInstance();
        loginPresenter = new LoginPresenterImpl(this);
        logoutPres = new LogoutPresImpl(this);
        userPres = new UserPresImpl(this, logoutPres);
        userPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }

    @OnClick(R.id.btn_login)
    public void login() {
        validator.validate();

    }

    @Override
    protected void onStart() {
        super.onStart();
        userMac.setText(AppConfig.isDevelopment() ? AppConfig.getMac() : DeviceUtils.getMac(LoginActivity.this));
    }

    @Override
    public void onSuccessfullyLoggedIn(LoginData loginData) {
        updateUI(loginData);
        this.loginData=loginData;
        userPres.getSubsHistory(loginData.getToken());
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
                Intent launcherIntent = new Intent(LoginActivity.this, LauncherModifiedActivity.class);
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
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onErrorOccured(String error_message, String otherInfo, String errorType) {
        errorMessage(error_message);
    }

    @Override
    public void setOrderHistory(List<OrderItem> orderHistory) {
        this.orderItemArrayList = (ArrayList<OrderItem>) orderHistory;
        userPres.getPackageInfo(CHANNEL_PACKAGE, loginData.getToken());
    }

    @Override
    public void errorMessage(String message) {
        LoginInfoFragment loginInfoFragment = LoginInfoFragment.newInstance("", message, true);
        loginInfoFragment.show(getSupportFragmentManager(), "LoginInfoDialog");

    }

    @Override
    public void logginFromReal(LoginData loginData) {

    }

    @Override
    public void onValidationSucceeded() {
        loginPresenter.userTryingToLogin(userMac.getText().toString(), userPassword.getText().toString());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void updateUI(final LoginData loginData) {
        hideProgress();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(loginData);
            }
        });
    }

    @Override
    public void onRetryBtnInteraction() {
        onDismissBtnInteraction();
        String username = userMac.getText().toString();
        String password = userPassword.getText().toString();
        loginPresenter.userTryingToLogin(username, password);
    }

    @Override
    public void onDismissBtnInteraction() {
        Fragment toRemoveFrag = getSupportFragmentManager().findFragmentByTag("LoginInfoDialog");
        getSupportFragmentManager().beginTransaction().remove(toRemoveFrag).commit();
    }

    @Override
    public void successfulLogout() {

    }
}
