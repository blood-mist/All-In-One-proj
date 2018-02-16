package comcast.stb.tokenrefresh;



import comcast.stb.entity.NewToken;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by anilpaudel on 1/9/18.
 */

public interface TokenRefreshApiInterface {
    @GET("re-login")
    Observable<Response<NewToken>> getNewToken(@Query("token") String oldToken);

    interface TokenRefreshView{
        void newToken(NewToken newToken);
        void onError(String message);
        void logout();
    }
    interface TokenRefreshPresenter{
        void refreshTheToken(String oldToken);
    }
    interface TokenRefreshInteractor{
        void generateNewToken(String oldToken);
    }
    interface TokenRefreshListener{
        void tokenIsRefreshed(NewToken newToken);
        void onError(String message);
        void logout();
    }
}
