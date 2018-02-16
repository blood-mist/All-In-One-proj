package comcast.stb.purchase.moviePckgPurchase;



import comcast.stb.entity.BuyResponse;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by nitv on 2/1/18.
 */

public interface MoviePckgApiInterface {
    //    http://69.79.26.170:8080/html/stv/public/api/v1/subscribe/movie-package/1?token={token}token
    @FormUrlEncoded
    @POST("subscribe/movie-package/{package_id}")
    Observable<Response<BuyResponse>> buyMoviePackages(@Path("package_id") int packageId, @Field("duration") int duration, @Query("token") String token);

    interface MoviePcgkBuyView {
        void setMoviePcgkBuyRespone(BuyResponse buyRespone);

        void onMoviePckgBuyError(int packageId, String message);

        void showProgress();

        void hideProgress();
    }

    interface MoviePcgkBuyPresenter {
        void buyMoviePcgk(int packageId, int months, String token);
    }

    interface MoviePcgkBuyListener {
        void setMoviePcgkBuyResponse(BuyResponse buyResponse);

        void onMoviePckgBuyError(int packageId, String message);
    }

    interface MoviePcgkBuyInteractor {
        void buyMoviePcgk(int packageId, int months, String token);
    }
}
