package comcast.stb.livetv;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import comcast.stb.entity.Channel;
import comcast.stb.entity.ChannelCategory;
import comcast.stb.entity.EpgResponse;
import comcast.stb.entity.EventItem;
import comcast.stb.entity.TvLink;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by blood-mist on 1/9/18.
 */

public interface LiveTVApiInterface {
    @GET("user/channels")//?with=channels && channel-categories?with=channels
    Observable<Response<List<ChannelCategory>>> getChannelsWithCategory(@Query("token") String token);//, @Query("app-name") String appName, @Query("platform") String platform
    @GET("channels/{channel-id}/playable")
    Observable<Response<TvLink>> getChannelLink(@Path("channel-id") int channelID, @Query("token") String token);
    @GET("epg/channel/{channel-id}")
    Observable<Response<EpgResponse>> getEpg(@Path("channel-id") int channelID, @Query("token")String token);


    interface ChannelWithCategoryView{
        void setChannelsWithCategory(List<ChannelCategory> channelCategoryList);
        void setEpg(LinkedHashMap<String, ArrayList<EventItem>> epgChannelList);
        void onErrorOccured(String message,Channel channel,String errorType);
    }
    interface ChannelWithCategoryPresenter{
        void getChannelsWithCategory(String token);
        void getEpg(int channelId,String token);
    }
    interface ChannelWithCategoryInteractor{
        void getChannelsWithCategory(String token);
        void getEpg(int channelId,String token);
    }
    interface ChannelWithCategoryListener{
        void takeChannelsWithCategory(List<ChannelCategory> channelCategoryList);
        void takeEpgList(LinkedHashMap<String, ArrayList<EventItem>> epgList);
        void onErrorOccured(String message, Channel channel, String errorType);
    }
}
