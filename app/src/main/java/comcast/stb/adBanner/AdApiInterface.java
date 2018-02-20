package comcast.stb.adBanner;

import java.util.List;

import comcast.stb.entity.AdItem;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by blood-mist on 2/21/18.
 */

public interface AdApiInterface {
    @GET("banner-ads")
    Observable<Response<List<AdItem>>>getAdBanners(@Query("token") String token);
    interface AdView{
        void setAdBanners(List<AdItem> bannerList);
        void onErrorOccured(String message);
    }
    interface AdPresenter {
        void getAdBanners(String token);
    }

    interface AdInteractor {
        void getAdBanners(String token);
    }

    interface AdListener {
        void takeAdBanners(List<AdItem> bannerList);

        void onErrorOccured(String message);
    }
}
