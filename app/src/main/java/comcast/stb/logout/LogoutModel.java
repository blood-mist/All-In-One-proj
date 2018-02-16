package comcast.stb.logout;



import comcast.stb.entity.LoginData;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by anilpaudel on 1/9/18.
 */

public class LogoutModel implements LogoutApiInterface.LogoutInteractor {
    LogoutApiInterface.LogoutListener logoutListener;

    public LogoutModel(LogoutApiInterface.LogoutListener logoutListener) {
        this.logoutListener = logoutListener;
    }

    @Override
    public void logout() {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<LoginData> loginDatas = realm.where(LoginData.class).findAll();
                loginDatas.deleteAllFromRealm();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                logoutListener.successfulLogout();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });


    }
}
