package comcast.stb.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comcast.stb.R;
import comcast.stb.entity.LoginData;
import comcast.stb.launcher.LauncherActivity;
import comcast.stb.utils.AppConfig;
import comcast.stb.utils.DeviceUtils;
import io.realm.Realm;

import static comcast.stb.StringData.USER_NAME;


public class LoginActivity extends AppCompatActivity implements LoginApiInterface.LoginView ,Validator.ValidationListener{
    private LoginPresenterImpl loginPresenter;

    @BindView(R.id.txt_user_mac)
    TextView userMac;

    @Password(min = 6, scheme = Password.Scheme.ANY)
    @BindView(R.id.edittext_user_password)
    EditText userPassword;
    @BindView(R.id.progressBar2)
    AVLoadingIndicatorView progressBar;
    private Validator validator;
    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        realm = Realm.getDefaultInstance();
        loginPresenter=new LoginPresenterImpl(this);
    }
    @OnClick(R.id.btn_login)
    public void login(){
       validator.validate();

    }

    @Override
    protected void onStart() {
        super.onStart();
        userMac.setText(AppConfig.isDevelopment()?AppConfig.getMac(): DeviceUtils.getMac(LoginActivity.this));
    }

    @Override
    public void onSuccessfullyLoggedIn(LoginData loginData) {
        updateUI(loginData);
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
    public void errorMessage(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String username = userMac.getText().toString();
                        String password = userPassword.getText().toString();
                        loginPresenter.userTryingToLogin(username, password);
                    }
                }).setActionTextColor(getResources().getColor(R.color.white_color));
        snackbar.show();
    }

    @Override
    public void logginFromReal(LoginData loginData) {

    }

    @Override
    public void onValidationSucceeded() {
        loginPresenter.userTryingToLogin(userMac.getText().toString(),userPassword.getText().toString());
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
        Intent intent = new Intent(this, LauncherActivity.class);
        intent.putExtra(USER_NAME,loginData.getUser().getName());
        startActivity(intent);
        finish();
    }
}
