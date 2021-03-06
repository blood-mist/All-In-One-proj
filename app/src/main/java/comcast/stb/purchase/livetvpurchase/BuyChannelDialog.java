package comcast.stb.purchase.livetvpurchase;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.R;
import comcast.stb.entity.BuyResponse;
import comcast.stb.entity.Channel;
import comcast.stb.entity.LoginData;
import comcast.stb.livetv.LiveTVActivity;
import io.realm.Realm;

import static comcast.stb.StringData.PURCHASE_TYPE_BOUGHT;


public class BuyChannelDialog extends DialogFragment implements BuyChannelApiInterface.BuyView {

    private static final String CHANNEL_INFO = "channel";

    private Channel mChannel;
    private Activity activity;
    private Realm realm;
    private LoginData loginData;
    private BuyChannelPresenterImpl buyChannelPresenter;

    @BindView(R.id.txt_purchaseBody)
    TextView purchaseBody;
    @BindView(R.id.txt_purchase_title)
    TextView purchaseTitle;
    @BindView(R.id.btn_purchase)
    Button purchase;
    @BindView(R.id.btn_cancel)
    Button cancel;


    public BuyChannelDialog() {
        // Required empty public constructr
    }

    /**
     * @param channel Parameter 1.
     * @return A new instance of fragment BuyChannelDialog.
     */


    public static BuyChannelDialog newInstance(Channel channel) {
        BuyChannelDialog fragment = new BuyChannelDialog();
        Bundle args = new Bundle();
        args.putParcelable(CHANNEL_INFO, channel);
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
        buyChannelPresenter = new BuyChannelPresenterImpl(this);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_buy_dialog, new LinearLayout(getActivity()), false);
        ButterKnife.bind(view);
        mChannel = getArguments().getParcelable(CHANNEL_INFO);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_Light_DarkActionBar);
        showProgress();
        return builder.create();
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

    @Override
    public void setBuyRespone(BuyResponse buyRespone) {
        hideProgress();
        mChannel.setSubscriptionStatus(PURCHASE_TYPE_BOUGHT);
        mChannel.setExpiry(buyRespone.getNewExpiry());
        final AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            purchaseTitle.setText(getString(R.string.buy_success));
            purchaseBody.setText(mChannel.getChannelName() + " bought successfully");
            purchase.setVisibility(View.GONE);
        }


    }

    @Override
    public void onError(String message) {
        hideProgress();
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        buyChannelPresenter.buyChannel(mChannel.getChannelId(),1,loginData.getToken());
                    }
                }).setActionTextColor(getResources().getColor(R.color.white_color));
        snackbar.show();
    }

    @Override
    public void showProgress() {
        if(activity instanceof LiveTVActivity)
            ((LiveTVActivity) activity).updateProgress(true);


    }

    @Override
    public void hideProgress() {
        if(activity instanceof LiveTVActivity)
            ((LiveTVActivity) activity).updateProgress(false);
    }
}
