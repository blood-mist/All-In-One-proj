package comcast.stb.livetv;



import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import comcast.stb.entity.Channel;
import comcast.stb.entity.ChannelCategory;
import comcast.stb.entity.DvrResponse;
import comcast.stb.entity.EventItem;
import comcast.stb.logout.LogoutPresImpl;

/**
 * Created by blood-mist on 1/9/18.
 */

public class LiveTVPresenterImpl implements LiveTVApiInterface.ChannelWithCategoryListener,LiveTVApiInterface.ChannelWithCategoryPresenter {
    LiveTVApiInterface.ChannelWithCategoryView channelWithCategoryView;
    LiveTVApiInterface.ChannelWithCategoryInteractor channelWithCategoryInteractor;
    LogoutPresImpl logoutPres;

    public LiveTVPresenterImpl(LiveTVApiInterface.ChannelWithCategoryView channelWithCategoryView,LogoutPresImpl logoutPres) {
        this.channelWithCategoryView = channelWithCategoryView;
        channelWithCategoryInteractor = new LiveTVModel(this,logoutPres);
    }

    @Override
    public void getChannelsWithCategory(String token) {
        channelWithCategoryInteractor.getChannelsWithCategory(token);
    }

    @Override
    public void getEpg(int channelId,String token) {
        channelWithCategoryInteractor.getEpg(channelId,token);
    }

    @Override
    public void getDvr(String dvrPath, String token) {
        channelWithCategoryInteractor.getDvr(dvrPath,token);
    }

    @Override
    public void takeChannelsWithCategory(List<ChannelCategory> channelCategoryList) {
        channelWithCategoryView.setChannelsWithCategory(channelCategoryList);
    }

    @Override
    public void takeEpgList(LinkedHashMap<String, ArrayList<EventItem>> epgList) {
        channelWithCategoryView.setEpg(epgList);
    }

    @Override
    public void takeDvrList(List<DvrResponse> dvrList) {
        channelWithCategoryView.setDvr(dvrList);
    }

    @Override
    public void onErrorOccured(String message, Channel channel, String errorType) {
        channelWithCategoryView.onErrorOccured(message,channel,errorType);
    }


}
