package comcast.stb.purchase.livetvpurchase;


import comcast.stb.entity.BuyResponse;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by blood-mist on 1/27/18.
 */

public interface BuyChannelApiInterface {
    @FormUrlEncoded
    @POST("subscribe/channel/{channel_id}")
    Observable<Response<BuyResponse>> buyChannel(@Path("channel_id") int channelId, @Field("duration") int duration, @Query("token") String token);

    interface BuyView {
        void setBuyRespone(BuyResponse buyRespone);

        void onError(String message);

        void showProgress();

        void hideProgress();
    }

    interface BuyPresenter {
        void buyChannel(int channelId, int months, String token);
    }

    interface BuyListener {
        void setBuyResponse(BuyResponse buyResponse);
        void onError(String message);
    }

    interface BuyInteractor {
        void buyChannel(int channelId, int months, String token);
    }

}
