package comcast.stb.entity.events;

/**
 * Created by nitv on 1/5/18.
 */

public class FmPlayingEvent {
    boolean isPlaying;
    boolean onError;

    public boolean isOnError() {
        return onError;
    }

    public void setOnError(boolean onError) {
        this.onError = onError;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
