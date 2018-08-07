package comcast.stb.movielist;

import java.util.List;

import comcast.stb.entity.BuyResponse;
import comcast.stb.entity.MovieCategory;
import comcast.stb.entity.MovieLink;
import comcast.stb.entity.MoviesItem;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by blood-mist on 1/15/18.
 */

public interface MovieListApiInterface {
    @GET("user/movies")//?with=Movies && movie-categories?with=movies
    Observable<Response<List<MovieCategory>>> getMoviesWithCategory(@Query("token") String token,@Query("lang") String language);
    @GET("movies/{movie-id}/playable")
    Observable<Response<MovieLink>> getChannelLink(@Path("movie-id") int movieId, @Query("token") String token,@Query("lang") String language);

    @FormUrlEncoded
    @POST("subscribe/movie/{movie-id}")
    Observable<Response<BuyResponse>> buyMovie( @Field("duration") int duration,@Path("movie-id") int movieId,@Query("token") String token,@Query("lang") String language);

    interface MovieWithCategoryView{
        void setMoviesWithCategory(List<MovieCategory> movieCategoryList);
        void onMovieBought(BuyResponse buyResponse);
        void onErrorOccured(String message, MoviesItem movie, String errorType);
        void showProgress();
        void hideProgress();
    }
    interface MovieWithCategoryPresenter{
        void getMoviesWithCategory(String token,String language);
        void buyMovie(int duration,int movieId,String token,String language);

    }
    interface MovieWithCategoryInteractor{
        void getMoviesWithCategory(String token,String language);
        void buyMovie(int duration,int movieId,String token,String language);
    }
    interface MovieWithCategoryListener{
        void takeMoviesWithCategory(List<MovieCategory> movieCategoryList);
        void onErrorOccured(String message,MoviesItem movie,String errorType);
        void onMovieBought(BuyResponse buyResponse);
    }
}
