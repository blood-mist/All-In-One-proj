package com.crostinotv.stb.purchase.livetvpurchase;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.R;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.UserInfo;
import io.realm.Realm;

public class UserDialog extends DialogFragment {

    private static final String USER_INFO = "user_info";

    private UserInfo userInfo;
    private Activity activity;
    private Realm realm;
    private LoginData loginData;

    @BindView(R.id.txt_purchaseBody)
    TextView purchaseBody;
    @BindView(R.id.txt_purchase_title)
    TextView purchaseTitle;



    public static UserDialog newInstance(UserInfo userInfo) {
        UserDialog fragment = new UserDialog();
        Bundle args = new Bundle();
        args.putParcelable(USER_INFO, userInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
        realm = Realm.getDefaultInstance();
        loginData = realm.where(LoginData.class).findFirst();
    }


    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_info, container,
                false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }
    @Override
    public void onStart() {
        super.onStart();
    }


}
