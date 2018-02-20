package comcast.stb.userInfo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.R;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.UserInfo;
import comcast.stb.login.LoginActivity;
import comcast.stb.logout.LogoutApiInterface;
import comcast.stb.logout.LogoutPresImpl;
import comcast.stb.utils.AppConfig;
import comcast.stb.utils.DeviceUtils;
import io.realm.Realm;

public class UserDialogFragment extends DialogFragment implements LogoutApiInterface.LogoutView, UserInfoApiInterface.SplashView {
    private LoginData loginData;
    private Realm realm;
    private UserInfoPresenterImpl userInfoPresenter;
    private LogoutPresImpl logoutPres;

    private OnUserFragInteractionListener mListener;
    @BindView(R.id.img_userImage)
    ImageView userImage;

    @BindView(R.id.txt_displayName)
    TextView displayname;

    @BindView(R.id.txt_fullName)
    TextView email;

    @BindView(R.id.txt_macAddress)
    TextView macAddress;

    @BindView(R.id.txt_user_balance)
    TextView currentBalance;

    @BindView(R.id.btn_dismiss)
    Button btnDismiss;

    @BindView(R.id.progressbar_userinfo)
    AVLoadingIndicatorView progressBar;


    public UserDialogFragment() {
        // Required empty public constructor
    }


    public static UserDialogFragment newInstance() {
        return new UserDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.Theme_AppCompat_Dialog);
        realm = Realm.getDefaultInstance();
        loginData = realm.where(LoginData.class).findFirst();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View dialogView = inflater.inflate(R.layout.fragment_user_dialog,container,false);
        ButterKnife.bind(this, dialogView);
        logoutPres = new LogoutPresImpl(this);
        userInfoPresenter = new UserInfoPresenterImpl(this, logoutPres);
        displayname.setText(loginData.getUser().getName());
        return dialogView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userInfoPresenter.getUserInfo(loginData.getToken());
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onUserFragInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserFragInteractionListener) {
            mListener = (OnUserFragInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserFragInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(android.content.DialogInterface dialog,
                                 int keyCode,android.view.KeyEvent event)
            {
                if ((keyCode ==  android.view.KeyEvent.KEYCODE_BACK))
                {
                    // To dismiss the fragment when the back-button is pressed.
                    dismiss();
                    return true;
                }
                // Otherwise, do nothing else
                else return false;
            }
        });
    }

    @Override
    public void successfulLogout() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void setUserInfo(UserInfo userInfo) {
        updateUI(userInfo);

    }

    private void updateUI(UserInfo userInfo) {
        email.setText(userInfo.getUserData().getEmail());
        macAddress.setText(AppConfig.isDevelopment()?AppConfig.getMac(): DeviceUtils.getMac(getActivity()));
        currentBalance.setText(userInfo.getUserData().getBalance());
        btnDismiss.requestFocus();

    }

    @Override
    public void onErrorOccured(String message) {
        mListener.onUserInfoErrorOccured(message);
    }

    @Override
    public void showProgress() {
        startAnim();
    }

    private void startAnim() {
        progressBar.smoothToShow();
    }

    @Override
    public void hideProgress() {
        stopAnim();
    }

    private void stopAnim() {
        progressBar.smoothToHide();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnUserFragInteractionListener {
        // TODO: Update argument type and name
        void onUserFragInteraction();
        void onUserInfoErrorOccured(String message);
    }
}
