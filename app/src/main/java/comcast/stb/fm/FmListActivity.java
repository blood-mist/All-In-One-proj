package comcast.stb.fm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.R;
import comcast.stb.entity.FmCategory;
import comcast.stb.entity.FmsItem;
import comcast.stb.entity.LoginData;
import comcast.stb.login.LoginActivity;
import comcast.stb.logout.LogoutApiInterface;
import comcast.stb.logout.LogoutPresImpl;
import comcast.stb.utils.EqualSpacingItemDecoration;
import io.realm.Realm;

import static comcast.stb.StringData.FM_ID;


public class FmListActivity extends AppCompatActivity implements FmApiInterface.FmWithCategoryView, LogoutApiInterface.LogoutView, FmCategoryRecyclerAdapter.OnCategoryListInteractionListener, FmRecyclerAdapter.OnChannelListInteractionListener {
    FmPresImpl fmPresenter;
    FmCategoryRecyclerAdapter categoryRecyclerAdapter;
    FmRecyclerAdapter fmRecyclerAdapter;
    private ArrayList<FmCategory> fmCategoryList;

    private ArrayList<FmsItem> fmList;
    @BindView(R.id.fm_recycler_category)
    RecyclerView fmCategory;
    @BindView(R.id.recycler_fm_list)
    RecyclerView fmlist;
    @BindView(R.id.current_fm)
    TextView fmName;
    private Realm realm;
    LoginData loginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fm_list);
        ButterKnife.bind(this);
        LogoutPresImpl logoutPres = new LogoutPresImpl(this);
        fmPresenter = new FmPresImpl(this, logoutPres);
        realm = Realm.getDefaultInstance();
        loginData = realm.where(LoginData.class).findFirst();

    }

    @Override
    protected void onStart() {
        super.onStart();
        String authToken = loginData.getToken();
        fmPresenter.getFmsWithCategory(authToken);
    }

    @Override
    public void setFmsWithCategory(List<FmCategory> fmCategoryList) {
        this.fmCategoryList = (ArrayList<FmCategory>) fmCategoryList;
        fmCategory.setLayoutManager(new LinearLayoutManager(FmListActivity.this, LinearLayoutManager.VERTICAL, false));
        categoryRecyclerAdapter = new FmCategoryRecyclerAdapter(FmListActivity.this, this.fmCategoryList, FmListActivity.this);
        fmCategory.setAdapter(categoryRecyclerAdapter);
        fmCategory.addItemDecoration(new EqualSpacingItemDecoration(10, EqualSpacingItemDecoration.HORIZONTAL));

    }

    @Override
    public void onErrorOccured(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fmPresenter.getFmsWithCategory(loginData.getToken());
                    }
                }).setActionTextColor(getResources().getColor(R.color.white_color));
        snackbar.show();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void successfulLogout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onCategoryListClickInteraction(List<FmsItem> fmList) {
        this.fmList = (ArrayList<FmsItem>) fmList;
        fmRecyclerAdapter = new FmRecyclerAdapter(this, this.fmList, FmListActivity.this);
        GridLayoutManager manager = new GridLayoutManager(this, 5);
        fmlist.setLayoutManager(manager);
        fmlist.setAdapter(fmRecyclerAdapter);
    }

    @Override
    public void onChannelClickInteraction(FmsItem fmsItem) {
        Intent intent = new Intent(this, FmPlayActivity.class);
        intent.putExtra(FM_ID, fmsItem.getId());
        startActivity(intent);
    }

    @Override
    public void onChannelSelected(FmsItem fmsItem) {
        fmName.setText(fmsItem.getName());
    }
}
