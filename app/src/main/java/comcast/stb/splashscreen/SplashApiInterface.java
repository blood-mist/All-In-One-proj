package comcast.stb.splashscreen;



import comcast.stb.entity.UserInfo;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by blood-mist on 1/27/18.
 */

public interface SplashApiInterface {
    @GET("user/info")
    Observable<Response<UserInfo>> getUserInfo(@Query("token") String token);

    interface SplashView {
        void setUserInfo(UserInfo userInfo);

        void onErrorOccured(String message);
    }

    interface SplashPresenter {
        void getUserInfo(String token);
    }

    interface SplashInteractor {
        void getUserInfo(String token);
    }

    interface SplashListener {
        void takeUserInfo(UserInfo userInfo);

        void onErrorOccured(String message);
    }
}
