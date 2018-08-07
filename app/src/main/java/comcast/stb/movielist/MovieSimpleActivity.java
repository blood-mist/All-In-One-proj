package comcast.stb.movielist;

import android.content.Intent;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.R;
import comcast.stb.entity.BuyResponse;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.MovieCategory;
import comcast.stb.entity.MoviesItem;
import comcast.stb.logout.LogoutApiInterface;
import comcast.stb.logout.LogoutPresImpl;
import io.realm.Realm;

import static comcast.stb.StringData.BUY_ERROR;
import static comcast.stb.StringData.LANGUAGE_ENGLISH;
import static comcast.stb.StringData.MOVIE_CATEGORY;
import static comcast.stb.StringData.MOVIE_CATEGORY_ERROR;
import static comcast.stb.StringData.MOVIE_ITEM;
import static comcast.stb.StringData.MOVIE_PLAY_ERROR;
import static comcast.stb.StringData.PREF_LANG;


public class MovieSimpleActivity extends AppCompatActivity implements MovieListApiInterface.MovieWithCategoryView, MovieDialogFragment.OnFragmentInteractionListener, LogoutApiInterface.LogoutView {


    @BindView(R.id.main_recycler_view)
    RecyclerView mainRecycler;

    @BindView(R.id.progressbar_movieCategory)
    AVLoadingIndicatorView progressbar;


    private ComplexRecyclerViewAdapter mAdapter;
    MoviePresImpl moviePresenter;
    private MoviesItem currentMovie;
    private Realm realm;
    LoginData loginData;
    private String language;

    private ArrayList<MovieCategory> movieCategoryList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        language= PreferenceManager.getDefaultSharedPreferences(this).getString(PREF_LANG,LANGUAGE_ENGLISH);
        setContentView(R.layout.activity_movie_simple);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        loginData = realm.where(LoginData.class).findFirst();
        String authToken = loginData.getToken();
        final LogoutPresImpl logoutPres = new LogoutPresImpl(this);
        moviePresenter = new MoviePresImpl(this, logoutPres);
        moviePresenter.getMoviesWithCategory(authToken,language);


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
        moviePresenter.getMoviesWithCategory(loginData.getToken(),language);
    }

    @Override
    public void onDismissBtnInteraction() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment infoDialogFragment = manager.findFragmentByTag("MovieErrorFragment");
        if (infoDialogFragment != null)
            manager.beginTransaction().remove(infoDialogFragment).commit();

    }

    @Override
    public void setMoviesWithCategory(final List<MovieCategory> movieCategoryList) {
        this.movieCategoryList = (ArrayList<MovieCategory>) movieCategoryList;
        mAdapter = new ComplexRecyclerViewAdapter(MovieSimpleActivity.this, this.movieCategoryList);

        mainRecycler.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new CenterLayoutManager(this);
        mainRecycler.setLayoutManager(layoutManager);

        mainRecycler.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new ComplexRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int absolutePosition, int relativePosition, MoviesItem model) {

                //handle item click events here
                Intent intent = new Intent(MovieSimpleActivity.this, MovieDetailsActivity.class);
                intent.putExtra(MOVIE_ITEM, model);
                intent.putExtra(MOVIE_CATEGORY, movieCategoryList.get(absolutePosition));
                startActivity(intent);

            }
        });
        mAdapter.SetOnItemSelectListener(new ComplexRecyclerViewAdapter.OnItemSelectListener() {
            @Override
            public void onItemSelect(View view, int absolutePosition) {
                mainRecycler.smoothScrollToPosition(absolutePosition);
            }
        });
    }

    @Override
    public void onMovieBought(BuyResponse buyResponse) {

    }

    @Override
    public void onErrorOccured(String message, MoviesItem movie, String errorType) {
        stopAnim();
        MovieDialogFragment infoDialogFragment = MovieDialogFragment.newInstance("", message, movie, errorType, true);
        infoDialogFragment.show(getSupportFragmentManager(), "MovieErrorFragment");

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
