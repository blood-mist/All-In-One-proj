package comcast.stb;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;


public class MainApplication extends Application {
    private long lastUsed;
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }
}