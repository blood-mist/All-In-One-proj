package comcast.stb.purchase.moviepurchase;

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
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comcast.stb.R;
import comcast.stb.entity.BuyResponse;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.MoviesItem;

import comcast.stb.entity.events.BuyEvent;
import comcast.stb.movielist.MovieDetailsActivity;
import io.realm.Realm;

import static comcast.stb.StringData.PURCHASE_TYPE_BOUGHT;


public class BuyMovieDialog extends DialogFragment implements BuyMovieApiInterface.BuyView {
    private static final String MOVIE_INFO = "movie";

    private MoviesItem mMovies;
    private Activity activity;
    private Realm realm;
    private LoginData loginData;
    private BuyMoviePresenterImpl buyMoviePresenter;

    @BindView(R.id.txt_purchaseBody)
    TextView purchaseBody;
    @BindView(R.id.txt_purchase_title)
    TextView purchaseTitle;
    @BindView(R.id.btn_purchase)
    Button purchase;
    @BindView(R.id.btn_cancel)
    Button cancel;


    public BuyMovieDialog() {
        // Required empty public constructr
    }

    /**
     * @param movies Parameter 1.
     * @return A new instance of fragment BuyChannelDialog.
     */


    public static BuyMovieDialog newInstance(MoviesItem movies) {
        BuyMovieDialog fragment = new BuyMovieDialog();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_INFO, movies);
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
        buyMoviePresenter = new BuyMoviePresenterImpl(this);
    }

    @OnClick(R.id.btn_purchase)
    public void onPurchaseClick(){
        showProgress();
        buyMoviePresenter.buyMovie(mMovies.getMovieId(), 1, loginData.getToken());
    }

    @OnClick(R.id.btn_cancel)
    public void onCancelClick(){
        hideProgress();
        getDialog().dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_buy_dialog, new LinearLayout(getActivity()), false);
        ButterKnife.bind(this,view);
        mMovies = getArguments().getParcelable(MOVIE_INFO);
        purchaseTitle.setText("Buy "+ mMovies.getMovieName());
        purchaseBody.setText(getString(R.string.confirm_purchase));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_Light_Dialog_Alert);
        showProgress();
        builder.setView(view);
        builder.setCancelable(true);
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
        mMovies.setSubscriptionStatus(PURCHASE_TYPE_BOUGHT);
        mMovies.setExpiry(buyRespone.getNewExpiry());
        mMovies.setExpiryFlag(false);
        Toast.makeText(getActivity(),mMovies.getMovieName()+" has been bought successfully.",Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post(new BuyEvent());
        getDialog().dismiss();

    }

    @Override
    public void onError(String message) {
        hideProgress();
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        if(activity instanceof MovieDetailsActivity)
            ((MovieDetailsActivity) activity).updateProgress(true);


    }

    @Override
    public void hideProgress() {
        if(activity instanceof MovieDetailsActivity)
            ((MovieDetailsActivity) activity).updateProgress(false);
    }
}
