package comcast.stb.utils;

/**
 * Created by blood-mist on 3/24/18.
 */

public class DurationUtil {
    private String duration;
    public DurationUtil(String duration) {
        this.duration=duration;
    }

    public int getDurationHour(){
       return Integer.parseInt(duration.substring(0,2));

    }
    public int getDurationMinute(){
        return Integer.parseInt(duration.substring(2,4));
    }
    public int getDurationSecond(){
        return Integer.parseInt(duration.substring(4));
    }
}
