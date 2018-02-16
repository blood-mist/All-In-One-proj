package comcast.stb.login;


import comcast.stb.entity.LoginData;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginApiInterface {
    @POST("login-stb")
    @FormUrlEncoded
    Observable<Response<LoginData>> loginStatus(@Field("mac_address") String email, @Field("password") String password);//, @Query("app-name") String appName, @Query("platform") String platform

    interface LoginView {

        void onSuccessfullyLoggedIn(LoginData loginData);

        void showProgress();

        void hideProgress();

        void errorMessage(String message);

        void logginFromReal(LoginData loginData);

    }

    interface LoginPresenter {
        void userTryingToLogin(String userName, String password);




        void getCredentials();


    }

    interface LoginInteractor {

        void checkCredentials(String userName, String password);


        void saveCredentials(LoginData loginData);


        void getCredentials();


    }

    interface LoginListener {

        void successfullyLoggedIn(LoginData loginData);


        void logginFromReal(LoginData loginData);


        void onErrorOccured(String message);


    }
}
