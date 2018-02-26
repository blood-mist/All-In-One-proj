package comcast.stb.movielist;




import java.util.List;

import comcast.stb.entity.MovieCategory;
import comcast.stb.entity.MoviesItem;
import comcast.stb.logout.LogoutPresImpl;

/**
 * Created by nitv on 12/26/17.
 */

public class MoviePresImpl implements MovieListApiInterface.MovieWithCategoryPresenter, MovieListApiInterface.MovieWithCategoryListener {
    MovieListApiInterface.MovieWithCategoryView movieWithCategoryView;
    MovieListApiInterface.MovieWithCategoryInteractor movieWithCategoryInteractor;

    public MoviePresImpl(MovieListApiInterface.MovieWithCategoryView movieWithCategoryView,LogoutPresImpl logoutPres) {
        this.movieWithCategoryView = movieWithCategoryView;
        movieWithCategoryInteractor = new MovieModel(this,logoutPres);
    }

    @Override
    public void getMoviesWithCategory(String token) {
        movieWithCategoryView.showProgress();
        movieWithCategoryInteractor.getMoviesWithCategory(token);
    }

    @Override
    public void takeMoviesWithCategory(List<MovieCategory> movieCategoryList) {
        movieWithCategoryView.setMoviesWithCategory(movieCategoryList);
        movieWithCategoryView.hideProgress();
    }

    @Override
    public void onErrorOccured(String message,MoviesItem movie,String errorType) {
        movieWithCategoryView.onErrorOccured(message,movie,errorType);
        movieWithCategoryView.hideProgress();
    }
}
