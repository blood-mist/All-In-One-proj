package comcast.stb.userutility;

import java.util.List;

import comcast.stb.entity.OrderItem;
import comcast.stb.entity.PackagesInfo;
import comcast.stb.entity.SubsItem;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ACER on 2/15/2018.
 */

public interface UserApiInterface {
    @GET("user/subscriptions")
    Observable<Response<List<SubsItem>>> getSubsHistory(@Query("token") String token);

    @GET("user/orders")
    Observable<Response<List<OrderItem>> >getOrderHistory(@Query("token") String token);

    @GET("user/{choose-package}")
    Observable<Response<List<PackagesInfo>>> getChannelPackage(@Path("choose-package")String packageType, @Query("token") String token);

    interface UserView {
        void setSubsHistory(List<SubsItem> subsHistory);
        void setPackageInfo(List<PackagesInfo> channelListInfo,String packageType);
        void showProgress();
        void hideProgress();
        void onErrorOccured(String message);
        void onErrorOccured(String packageType,String message);
        void setOrderHistory(List<OrderItem> orderHistory);
    }

    interface UserDataPresenter {
        void getSubsHistory(String token);
        void getOrderHistory(String token);
        void getPackageInfo(String packageType,String token);

    }

    interface UserDataInteractor {
        void getSubsHistory(String token);
        void getOrderHistory(String token);
        void getPackageInfo(String packageType,String token);

    }

    interface UserDataListener {
        void takeSubsHistory(List<SubsItem> subsHistory);
        void takeOrderHistory(List<OrderItem>orderHistory);
        void takePackageInfo(List<PackagesInfo> channelInfoList,String packageType);
        void onErrorOccured(String packageType,String message);
        void onChannelInaPckgError(int packageId,String message);
        void onMoviesInaPckgError(int packageId,String message);
        void onErrorOccured(String message);
    }
}
