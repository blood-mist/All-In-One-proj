package comcast.stb.valueAddedPackages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.R;
import comcast.stb.entity.BuyResponse;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.OrderItem;
import comcast.stb.entity.PackagesInfo;
import comcast.stb.entity.SubsItem;
import comcast.stb.logout.LogoutApiInterface;
import comcast.stb.logout.LogoutPresImpl;
import comcast.stb.packageInfoDialog.PackageDialogFragment;
import comcast.stb.packageInfoDialog.PackageInfo;
import comcast.stb.purchase.channelPckgPurchase.ChannelPckgApiInterface;
import comcast.stb.purchase.channelPckgPurchase.ChannelPckgBuyPresImpl;
import comcast.stb.purchase.moviePckgPurchase.MoviePckgApiInterface;
import comcast.stb.purchase.moviePckgPurchase.MoviePckgBuyPresImpl;
import comcast.stb.userutility.UserApiInterface;
import comcast.stb.userutility.UserPresImpl;
import io.realm.Realm;

import static comcast.stb.StringData.CHANNEL_PACKAGE;
import static comcast.stb.StringData.MOVIE_PACKAGE;


public class PackageActivity extends AppCompatActivity implements UserApiInterface.UserView, LogoutApiInterface.LogoutView, PackageInfo.OnFragmentInteractionListener,
        PackageDialogFragment.OnFragmentInteractionListener, ChannelPckgApiInterface.ChannelPcgkBuyView, MoviePckgApiInterface.MoviePcgkBuyView {

    private static final String PACKAGE_INFO_DIALOG = "package_info_fragment";
    private static final String PACKAGE_DETAILS_FRAGMENT = "package_details_fragment";
    private Realm realm;
    private LogoutPresImpl logoutPres;
    private UserPresImpl userPres;
    private LoginData loginData;
    private ArrayList<SubsItem> subscriptionList;
    private ArrayList<OrderItem> orderItemArrayList;
    private ArrayList<PackagesInfo> channelPackageslist, moviesPackagesList;
    @BindView(R.id.channel_package_recycler)
    RecyclerView channelRecycler;
    @BindView(R.id.movie_package_recycler)
    RecyclerView movieRecycler;
    @BindView(R.id.progressbar_packageActivity)
    AVLoadingIndicatorView progressbar;


    private PackageListAdapter channelPackageRecyclerAdapter;

    private PackageListAdapter moviePackagerecyclerAdapter;

    private ArrayList<PackageEntity> modelList = new ArrayList<>();
    private ChannelPckgBuyPresImpl channelPckgBuyPres;
    private MoviePckgBuyPresImpl moviePckgBuyPres;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        loginData = realm.where(LoginData.class).findFirst();
        logoutPres = new LogoutPresImpl(this);
        userPres = new UserPresImpl(this, logoutPres);
        channelPckgBuyPres = new ChannelPckgBuyPresImpl(this);
        moviePckgBuyPres = new MoviePckgBuyPresImpl(this);
        userPres.getPackageInfo(CHANNEL_PACKAGE, loginData.getToken());
        // ButterKnife.bind(this);


    }

    private void setAdapter() {


        channelPackageRecyclerAdapter = new PackageListAdapter(PackageActivity.this, channelPackageslist);

        channelRecycler.setHasFixedSize(true);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        channelRecycler.setLayoutManager(layoutManager);


        channelRecycler.setAdapter(channelPackageRecyclerAdapter);


        channelPackageRecyclerAdapter.SetOnItemClickListener(new PackageListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position, PackagesInfo model) {

                //handle item click events here

                PackageInfo packagesInfo = PackageInfo.newInstance(model, CHANNEL_PACKAGE);
                packagesInfo.show(getSupportFragmentManager(), PACKAGE_INFO_DIALOG);


            }
        });

        moviePackagerecyclerAdapter = new PackageListAdapter(PackageActivity.this, moviesPackagesList);
        movieRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        movieRecycler.setHasFixedSize(true);
        movieRecycler.setAdapter(moviePackagerecyclerAdapter);
        moviePackagerecyclerAdapter.SetOnItemClickListener(new PackageListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, PackagesInfo model) {
                PackageInfo packagesInfo = PackageInfo.newInstance(model, MOVIE_PACKAGE);
                packagesInfo.show(getSupportFragmentManager(), PACKAGE_INFO_DIALOG);
            }
        });


    }


    @Override
    public void successfulLogout() {

    }

    @Override
    public void setSubsHistory(List<SubsItem> subsHistory) {

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
                setAdapter();
                break;
        }

    }

    @Override
    public void setChannelPcgkBuyRespone(BuyResponse buyRespone) {
        Toast.makeText(PackageActivity.this, buyRespone.getName() + "successfully bought", Toast.LENGTH_SHORT).show();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(PACKAGE_INFO_DIALOG);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }

    }

    @Override
    public void onChannelPckgBuyError(int packageId, String message) {
        Toast.makeText(PackageActivity.this, message + ", package_icon purchase failed!", Toast.LENGTH_SHORT).show();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(PACKAGE_INFO_DIALOG);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void setMoviePcgkBuyRespone(BuyResponse buyRespone) {
        Toast.makeText(PackageActivity.this, buyRespone.getName() + "successfully bought", Toast.LENGTH_SHORT).show();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(PACKAGE_INFO_DIALOG);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void onMoviePckgBuyError(int packageId, String message) {
        Toast.makeText(PackageActivity.this, message + ", package_icon purchase failed!", Toast.LENGTH_SHORT).show();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(PACKAGE_INFO_DIALOG);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void showProgress() {
        startAnim();
    }

    private void startAnim() {
        progressbar.smoothToShow();
    }

    @Override
    public void hideProgress() {
        stopAnim();
    }

    private void stopAnim() {
        progressbar.smoothToHide();
    }

    @Override
    public void onErrorOccured(String error_message, String otherInfo, String errorType) {

    }

    @Override
    public void setOrderHistory(List<OrderItem> orderHistory) {

    }

    @Override
    public void onPurchaseInteraction(PackagesInfo packagesInfo, String packageType) {
        PackageDialogFragment packageDialogFragment = PackageDialogFragment.newInstance(packageType, packagesInfo);
        packageDialogFragment.show(getSupportFragmentManager(), PACKAGE_INFO_DIALOG);
    }

    @Override
    public void onBuyClick(String packageType, int packageId, int duration) {
        switch (packageType) {
            case CHANNEL_PACKAGE:
                channelPckgBuyPres.buyChannelPcgk(packageId, duration, loginData.getToken());
                break;
            case MOVIE_PACKAGE:
                moviePckgBuyPres.buyMoviePcgk(packageId, duration, loginData.getToken());
                break;

        }
    }
}
