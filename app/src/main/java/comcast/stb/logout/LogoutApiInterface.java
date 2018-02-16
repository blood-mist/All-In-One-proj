package comcast.stb.logout;

/**
 * Created by anilpaudel on 1/9/18.
 */

public interface LogoutApiInterface {
    interface LogoutView{
        void successfulLogout();
    }
    interface LogoutPresenter{
        void logout();
    }
    interface LogoutInteractor{
        void logout();
    }
    interface LogoutListener{
        void successfulLogout();
    }
}
