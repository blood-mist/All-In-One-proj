package comcast.stb.purchase.channelPckgPurchase;




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

public interface ChannelPckgApiInterface {
    //    http://69.79.26.170:8080/html/stv/public/api/v1/subscribe/channel-package/1?token={token}
    @FormUrlEncoded
    @POST("subscribe/channel_package/{package_id}")
    Observable<Response<BuyResponse>> buyChannelPackages(@Path("package_id") int channelId, @Field("duration") int duration, @Query("token") String token);

    interface ChannelPcgkBuyView {
        void setChannelPcgkBuyRespone(BuyResponse buyRespone);
        void onChannelPckgBuyError(int packageId, String message);
        void showProgress();
        void hideProgress();
    }

    interface ChannelPcgkBuyPresenter {
        void buyChannelPcgk(int packageId, int months, String token);
    }

    interface ChannelPcgkBuyListener {
        void setChannelPcgkBuyResponse(BuyResponse buyResponse);
        void onChannelPckgBuyError(int packageId, String message);
    }

    interface ChannelPcgkBuyInteractor {
        void buyChannelPcgk(int packageId, int months, String token);
    }
}
