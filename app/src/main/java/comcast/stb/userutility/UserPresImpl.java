package comcast.stb.userutility;

import java.util.List;

import comcast.stb.entity.ChannelPckgItem;
import comcast.stb.entity.MoviePckgItem;
import comcast.stb.entity.OrderItem;
import comcast.stb.entity.PackagesInfo;
import comcast.stb.entity.SubsItem;
import comcast.stb.entity.UserInfo;
import comcast.stb.logout.LogoutPresImpl;


/**
 * Created by ACER on 2/15/2018.
 */

public class UserPresImpl implements UserApiInterface.UserDataListener, UserApiInterface.UserDataPresenter {
    UserApiInterface.UserView userView;
    UserApiInterface.UserDataInteractor userDataInteractor;

    public UserPresImpl(UserApiInterface.UserView userView, LogoutPresImpl logoutPres) {
        this.userView = userView;
        userDataInteractor = new UserDataModel(this, logoutPres);
    }

    @Override
    public void getSubsHistory(String token) {
        userDataInteractor.getSubsHistory(token);
    }

    @Override
    public void getOrderHistory(String token) {
        userDataInteractor.getOrderHistory(token);
    }

    @Override
    public void getUserInfo(String token) {
        userDataInteractor.getUserInfo(token);
    }

    @Override
    public void getPackageInfo(String packageType, String token) {
        userDataInteractor.getPackageInfo(packageType, token);
    }

    @Override
    public void getChannlesInaPckg(int packageId, String token) {
        userDataInteractor.getChannlesInaPckg(packageId, token);

    }

    @Override
    public void getMoviesInaPckg(int packageId, String token) {
        userDataInteractor.getMoviesInaPckg(packageId, token);
    }

    @Override
    public void takeUserInfo(UserInfo userInfo) {
        userView.setUserInfo(userInfo);
    }

    @Override
    public void takeSubsHistory(List<SubsItem> subsHistory) {
        userView.setSubsHistory(subsHistory);
    }

    @Override
    public void takeOrderHistory(List<OrderItem> orderHistory) {
        userView.setOrderHistory(orderHistory);
    }

    @Override
    public void takePackageInfo(List<PackagesInfo> channelInfoList, String packageType) {
        userView.setPackageInfo(channelInfoList, packageType);
    }

    @Override
    public void setChannelsInaPckg(int packageId, List<ChannelPckgItem> channelsInaPckgList) {
        userView.setChannelsInaPckg(packageId, channelsInaPckgList);
    }

    @Override
    public void setMoviesInaPckg(int packageId, List<MoviePckgItem> moviesInaPckgList) {
        userView.setMoviesInaPckg(packageId, moviesInaPckgList);

    }

    @Override
    public void onErrorOccured(String packageType, String message) {
        userView.onErrorOccured(packageType, message);
    }

    @Override
    public void onChannelInaPckgError(int packageId, String message) {
        userView.onErrorOccured(message);
    }

    @Override
    public void onMoviesInaPckgError(int packageId, String message) {
        userView.onErrorOccured(message);
    }

    @Override
    public void onErrorOccured(String message) {
        userView.onErrorOccured(message);
    }
}
