package comcast.stb.valueAddedPackages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


import android.widget.Toast;
import android.os.Handler;

import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.R;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.OrderItem;
import comcast.stb.entity.PackagesInfo;
import comcast.stb.entity.SubsItem;
import comcast.stb.launcher.LauncherModifiedActivity;
import comcast.stb.logout.LogoutApiInterface;
import comcast.stb.logout.LogoutPresImpl;
import comcast.stb.splashscreen.SplashActivity;
import comcast.stb.userutility.UserApiInterface;
import comcast.stb.userutility.UserPresImpl;
import io.realm.Realm;

import static comcast.stb.StringData.CHANNEL_PACKAGE;
import static comcast.stb.StringData.CHANNEL_PCKG;
import static comcast.stb.StringData.MOVIE_PACKAGE;
import static comcast.stb.StringData.MOVIE_PCKG;
import static comcast.stb.StringData.ORDER_LIST;
import static comcast.stb.StringData.SUBSCRIPTION_LIST;
import static comcast.stb.StringData.USER_NAME;


public class PackageActivity extends AppCompatActivity implements UserApiInterface.UserView, LogoutApiInterface.LogoutView {

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


    private PackageListAdapter channelPackageRecyclerAdapter;

    private PackageListAdapter moviePackagerecyclerAdapter;

    private ArrayList<PackageEntity> modelList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        ButterKnife.bind(this);
        realm=Realm.getDefaultInstance();
        loginData=realm.where(LoginData.class).findFirst();
        logoutPres = new LogoutPresImpl(this);
        userPres = new UserPresImpl(this, logoutPres);
        userPres.getPackageInfo(CHANNEL_PACKAGE,loginData.getToken());
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
                Toast.makeText(PackageActivity.this, "Hey " + model.getPackageName(), Toast.LENGTH_SHORT).show();


            }
        });

        moviePackagerecyclerAdapter=new PackageListAdapter(PackageActivity.this,moviesPackagesList);
        movieRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        movieRecycler.setHasFixedSize(true);
        movieRecycler.setAdapter(moviePackagerecyclerAdapter);



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
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onErrorOccured(String error_message, String otherInfo, String errorType) {

    }

    @Override
    public void setOrderHistory(List<OrderItem> orderHistory) {

    }
}
