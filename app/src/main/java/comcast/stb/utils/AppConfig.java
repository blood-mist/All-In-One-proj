package comcast.stb.utils;

import comcast.stb.BuildConfig;


/**
 * Created by blood-mist on 1/12/18.
 */

public class AppConfig {
    private static boolean isFromDevelopment = BuildConfig.DEBUG;

    public static boolean isDevelopment() {
        return isFromDevelopment;
    }
     public static String getMac(){

      return "c4fad645719";
     }
}
