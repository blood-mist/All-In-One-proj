package comcast.stb.entity.events;

import android.app.PendingIntent;

/**
 * Created by blood-mist on 1/27/18.
 */

public class FmLauncherEvent {
    private String fmTitle, fmName;
    private boolean isFmPlaying;
    private PendingIntent intent;

    public FmLauncherEvent(String fmTitle, String fmName,boolean isFmPlaying, PendingIntent intent) {
        this.fmTitle = fmTitle;
        this.fmName = fmName;
        this.intent = intent;
        this.isFmPlaying=isFmPlaying;
    }
    public String getFmTitle() {
        return fmTitle;
    }

    public void setFmTitle(String fmTitle) {
        this.fmTitle = fmTitle;
    }

    public String getFmName() {
        return fmName;
    }

    public void setFmName(String fmName) {
        this.fmName = fmName;
    }

    public PendingIntent getIntent() {
        return intent;
    }

    public void setIntent(PendingIntent intent) {
        this.intent = intent;
    }
    public boolean isFmPlaying() {
        return isFmPlaying;
    }

    public void setFmPlaying(boolean fmPlaying) {
        isFmPlaying = fmPlaying;
    }


}
