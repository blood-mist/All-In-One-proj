package comcast.stb.livetv;



import java.util.List;

import comcast.stb.entity.ChannelCategory;
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
    public void takeChannelsWithCategory(List<ChannelCategory> channelCategoryList) {
        channelWithCategoryView.setChannelsWithCategory(channelCategoryList);
    }

    @Override
    public void onErrorOccured(String message) {
        channelWithCategoryView.onErrorOccured(message);
    }

}
