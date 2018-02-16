package comcast.stb.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by blood-mist on 1/6/18.
 */

public class AppData {
    private String appName;
    private Drawable appIcon;

    public Drawable getAppImage() {
        return appIcon;
    }

    public void setAppImage(Drawable appImage) {
        this.appIcon = appImage;
    }


    public AppData(String appName,Drawable appIcon) {
        this.appName = appName;
        this.appIcon=appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

}
