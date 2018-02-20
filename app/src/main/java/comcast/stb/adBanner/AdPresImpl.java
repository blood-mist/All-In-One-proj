package comcast.stb.adBanner;

import java.util.List;

import comcast.stb.entity.AdItem;
import comcast.stb.logout.LogoutPresImpl;

/**
 * Created by blood-mist on 2/21/18.
 */

public class AdPresImpl implements AdApiInterface.AdPresenter,AdApiInterface.AdListener {
    AdApiInterface.AdView adView;
    AdApiInterface.AdInteractor adInteractor;

    public AdPresImpl(AdApiInterface.AdView adView, LogoutPresImpl logoutPres) {
        this.adView = adView;
        adInteractor = new AdModel(this, logoutPres);
    }
    @Override
    public void getAdBanners(String token) {
        adInteractor.getAdBanners(token);
    }

    @Override
    public void takeAdBanners(List<AdItem> bannerList) {
        adView.setAdBanners(bannerList);
    }

    @Override
    public void onErrorOccured(String message) {

    }
}
