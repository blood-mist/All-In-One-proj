package comcast.stb.userutility;

import java.util.List;

import comcast.stb.entity.OrderItem;
import comcast.stb.entity.PackagesInfo;
import comcast.stb.entity.SubsItem;
import comcast.stb.logout.LogoutPresImpl;

import static comcast.stb.StringData.PACKAGE_ERROR;


/**
 * Created by ACER on 2/15/2018.
 */

public class UserPresImpl implements UserApiInterface.UserDataListener, UserApiInterface.UserDataPresenter {
    UserApiInterface.UserView userView;
    UserApiInterface.UserDataInteractor userDataInteractor;

    public UserPresImpl(UserApiInterface.UserView userView, LogoutPresImpl logoutPres) {
        this.userView = userView;
        userDataInteractor = new UserDataModel(this,logoutPres);
    }

    @Override
    public void getSubsHistory(String token) {
        userView.showProgress();
        userDataInteractor.getSubsHistory(token);
    }

    @Override
    public void getOrderHistory(String token) {
        userView.showProgress();
        userDataInteractor.getOrderHistory(token);
    }

    @Override
    public void getPackageInfo(String packageType, String token) {
        userView.showProgress();
        userDataInteractor.getPackageInfo(packageType, token);
    }


    @Override
    public void takeSubsHistory(List<SubsItem> subsHistory) {
        userView.setSubsHistory(subsHistory);
        userView.hideProgress();
    }

    @Override
    public void takeOrderHistory(List<OrderItem> orderHistory) {
        userView.setOrderHistory(orderHistory);
        userView.hideProgress();
    }

    @Override
    public void takePackageInfo(List<PackagesInfo> channelInfoList, String packageType) {
        userView.setPackageInfo(channelInfoList, packageType);
        userView.hideProgress();
    }


    @Override
    public void onErrorOccured( String message,String packageType,String errorType) {
        userView.onErrorOccured(message,packageType,PACKAGE_ERROR);
        userView.hideProgress();
    }


}
