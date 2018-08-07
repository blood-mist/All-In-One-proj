package comcast.stb.livetv;



import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import comcast.stb.entity.Channel;
import comcast.stb.entity.ChannelCategory;
import comcast.stb.entity.DvrLink;
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
    public void getChannelsWithCategory(String token,String language) {
        channelWithCategoryInteractor.getChannelsWithCategory(token,language);
    }


    @Override
    public void getDvr(Channel channel, String token,String language) {
        channelWithCategoryInteractor.getDvr(channel,token,language);
    }


    @Override
    public void getDvrLink(Channel channel, String DvrName, String token,String language) {
        channelWithCategoryInteractor.getDvrLink(channel,DvrName,token,language);

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
    public void takeDvrList(List<DvrResponse> dvrList,Channel channel) {
        channelWithCategoryView.setDvr(dvrList,channel);
    }

    @Override
    public void takeDvrLink(DvrLink link, Channel channel) {
        channelWithCategoryView.setDvrLink(link.getLink(),channel);
    }

    @Override
    public void onErrorOccured(String message, Channel channel, String errorType) {
        channelWithCategoryView.onErrorOccured(message,channel,errorType);
    }
    @Override
    public void getEpg(int channelId,String token,String language) {
        channelWithCategoryInteractor.getEpg(channelId,token,language);
    }


}
