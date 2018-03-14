package comcast.stb;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;


public class MainApplication extends Application {
    private long lastUsed;
    protected String userAgent;
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
        
        userAgent = Util.getUserAgent(this, "ComcastExoPlayer");
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread
                    , Throwable e) {
                handleUncaughtException(thread, e);
            }
        });
    }

    private void handleUncaughtException(Thread thread, Throwable e) {
        try {
            e.printStackTrace();
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            String s = writer.toString();
            Log.d("CheckingErrorStatus", s);
            // String fpath = "/sdcard/.Movies_wod/"+fname+".txt";
            File file = new File(getExternalFilesDir(null), "crash_report");
            Log.d("File Stored in", getExternalFilesDir(null).getPath()
                    +"crash_report");
            file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(s);
            bw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
            Toast.makeText(this, "failed to save datas", Toast.LENGTH_LONG).show();
        }
    }

    public DataSource.Factory buildDataSourceFactory(TransferListener<? super DataSource> listener) {
        return new DefaultDataSourceFactory(this, listener, buildHttpDataSourceFactory(listener));
    }

    /** Returns a {@link HttpDataSource.Factory}. */
    public HttpDataSource.Factory buildHttpDataSourceFactory(
            TransferListener<? super DataSource> listener) {
        return new DefaultHttpDataSourceFactory(userAgent, listener);
    }

    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }

}