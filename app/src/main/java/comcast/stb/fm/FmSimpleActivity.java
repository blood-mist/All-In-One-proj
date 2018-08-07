package comcast.stb.fm;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.InfoDialog.InfoDialogFragment;
import comcast.stb.R;
import comcast.stb.entity.FmCategory;
import comcast.stb.entity.FmsItem;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.MovieCategory;
import comcast.stb.entity.MoviesItem;
import comcast.stb.logout.LogoutApiInterface;
import comcast.stb.logout.LogoutPresImpl;
import comcast.stb.movielist.CenterLayoutManager;
import comcast.stb.movielist.ComplexRecyclerViewAdapter;
import comcast.stb.movielist.MovieDetailsActivity;
import comcast.stb.movielist.MovieDialogFragment;
import io.realm.Realm;

import static comcast.stb.StringData.LANGUAGE_ENGLISH;
import static comcast.stb.StringData.MOVIE_CATEGORY;
import static comcast.stb.StringData.MOVIE_ITEM;
import static comcast.stb.StringData.PREF_LANG;


public class FmSimpleActivity extends AppCompatActivity implements FmApiInterface.FmWithCategoryView, LogoutApiInterface.LogoutView, MovieDialogFragment.OnFragmentInteractionListener,InfoDialogFragment.OnFragmentInteractionListener {


    @BindView(R.id.main_recycler_view)
    RecyclerView mainRecycler;

    @BindView(R.id.progressbar_movieCategory)
    AVLoadingIndicatorView progressbar;


    private FmComplexRecycler mAdapter;
    FmPresImpl fmPres;
    private MoviesItem currentMovie;
    private Realm realm;
    LoginData loginData;
    String language;

    private ArrayList<FmCategory> fmCategoryList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_simple);
        language= PreferenceManager.getDefaultSharedPreferences(this).getString(PREF_LANG,LANGUAGE_ENGLISH);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        loginData = realm.where(LoginData.class).findFirst();
        String authToken = loginData.getToken();
        final LogoutPresImpl logoutPres = new LogoutPresImpl(this);
        fmPres = new FmPresImpl(this, logoutPres);
        fmPres.getFmsWithCategory(authToken,language);


    }

    private void startAnim() {
        progressbar.smoothToShow();
    }


    @Override
    public void successfulLogout() {

    }


    @Override
    public void onRetryBtnInteraction(String errorType, MoviesItem movie) {
        onDismissBtnInteraction();
        fmPres.getFmsWithCategory(loginData.getToken(),language);
    }

    @Override
    public void onRetryBtnInteraction() {
        onDismissBtnInteraction();
        fmPres.getFmsWithCategory(loginData.getToken(),language);
    }

    @Override
    public void onDismissBtnInteraction() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment infoDialogFragment = manager.findFragmentByTag("MovieErrorFragment");
        if (infoDialogFragment != null)
            manager.beginTransaction().remove(infoDialogFragment).commit();

    }

    @Override
    public void onErrorOccured(String message) {
        stopAnim();
        InfoDialogFragment infoDialogFragment = InfoDialogFragment.newInstance("", message, true);
        infoDialogFragment.show(getSupportFragmentManager(), "MovieErrorFragment");

    }

    @Override
    public void setFmsWithCategory(final List<FmCategory> fmCategoryList) {


        for (Iterator<FmCategory> iterator = fmCategoryList.iterator(); iterator.hasNext(); ) {
            FmCategory value = iterator.next();
            if (value.getFms().size() == 0) {
                iterator.remove();
            }
        }
        this.fmCategoryList = (ArrayList<FmCategory>) fmCategoryList;
        mAdapter = new FmComplexRecycler(FmSimpleActivity.this, this.fmCategoryList);

        mainRecycler.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new CenterLayoutManager(this);
        mainRecycler.setLayoutManager(layoutManager);

        mainRecycler.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new FmComplexRecycler.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int absolutePosition, int relativePosition, FmsItem model) {

                //handle item click events here
                Intent intent = new Intent(FmSimpleActivity.this, FmDetailsActivity.class);
                intent.putExtra(MOVIE_ITEM, model);
                intent.putExtra(MOVIE_CATEGORY, fmCategoryList.get(absolutePosition));
                startActivity(intent);

            }
        });
        mAdapter.SetOnItemSelectListener(new FmComplexRecycler.OnItemSelectListener() {
            @Override
            public void onItemSelect(View view, int absolutePosition) {
                mainRecycler.smoothScrollToPosition(absolutePosition);
            }
        });
    }


    @Override
    public void showProgress() {
        startAnim();
    }

    @Override
    public void hideProgress() {
        stopAnim();
    }

    private void stopAnim() {
        progressbar.smoothToHide();
    }
}
