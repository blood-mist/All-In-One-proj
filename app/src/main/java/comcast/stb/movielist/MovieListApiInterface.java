package comcast.stb.movielist;

;

import java.util.List;

import comcast.stb.entity.MovieCategory;
import comcast.stb.entity.MovieLink;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by blood-mist on 1/15/18.
 */

public interface MovieListApiInterface {
    @GET("user/movies")//?with=Movies && movie-categories?with=movies
    Observable<Response<List<MovieCategory>>> getMoviesWithCategory(@Query("token") String token);
    @GET("movies/{movie-id}/playable")
    Observable<Response<MovieLink>> getChannelLink(@Path("movie-id") int movieId, @Query("token") String token);

    interface MovieWithCategoryView{
        void setMoviesWithCategory(List<MovieCategory> movieCategoryList);
        void onErrorOccured(String message);
        void showProgress();
        void hideProgress();
    }
    interface MovieWithCategoryPresenter{
        void getMoviesWithCategory(String token);

    }
    interface MovieWithCategoryInteractor{
        void getMoviesWithCategory(String token);
    }
    interface MovieWithCategoryListener{
        void takeMoviesWithCategory(List<MovieCategory> movieCategoryList);
        void onErrorOccured(String message);
    }
}
