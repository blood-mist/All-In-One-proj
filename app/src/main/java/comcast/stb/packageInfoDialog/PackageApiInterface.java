package comcast.stb.packageInfoDialog;

import java.util.List;

import comcast.stb.entity.ChannelPckgItem;
import comcast.stb.entity.MoviePckgItem;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by blood-mist on 2/21/18.
 */

public interface PackageApiInterface {
    @GET("package/{package-id}/channels")
    Observable<Response<List<ChannelPckgItem>>> getChannelsInAPckg(@Path("package-id")int packageId, @Query("token") String token);

    @GET("package/{package-id}/movies")
    Observable<Response<List<MoviePckgItem>>> getMoviesInAPckg(@Path("package-id")int packageId, @Query("token") String token);
interface PackagesView{
    void setChannelsInaPckg(int packageId,List<ChannelPckgItem> channelsInaPckgList);
    void setMoviesInaPckg(int packageId,List<MoviePckgItem> moviesInaPckgList );
    void onChannelInaPckgError(int packageId,String message);
    void onMoviesInaPckgError(int packageId,String message);
}
interface PackageInteractor{
    void getChannlesInaPckg(int packageId,String token);
    void getMoviesInaPckg(int packageId,String token);
}
interface PackagePresenter{
    void getChannlesInaPckg(int packageId,String token);
    void getMoviesInaPckg(int packageId,String token);
}
interface PackageListener{
    void setChannelsInaPckg(int packageId,List<ChannelPckgItem> channelsInaPckgList);
    void setMoviesInaPckg(int packageId,List<MoviePckgItem> moviesInaPckgList );
    void onChannelInaPckgError(int packageId,String message);
    void onMoviesInaPckgError(int packageId,String message);
}
}
