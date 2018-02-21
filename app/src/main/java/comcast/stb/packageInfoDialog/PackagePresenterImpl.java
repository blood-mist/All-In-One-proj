package comcast.stb.packageInfoDialog;

import java.util.List;

import comcast.stb.entity.ChannelPckgItem;
import comcast.stb.entity.MoviePckgItem;
import comcast.stb.logout.LogoutPresImpl;

/**
 * Created by blood-mist on 2/21/18.
 */

public class PackagePresenterImpl implements PackageApiInterface.PackageListener,PackageApiInterface.PackagePresenter {
    PackageApiInterface.PackagesView packagesView;
    PackageApiInterface.PackageInteractor packageInteractor;
    public PackagePresenterImpl(PackageApiInterface.PackagesView packagesView, LogoutPresImpl logoutPres) {
        this.packagesView = packagesView;
        packageInteractor = new PackageModel(this,logoutPres);
    }
    @Override
    public void getChannlesInaPckg(int packageId, String token) {
        packageInteractor.getChannlesInaPckg(packageId, token);
    }

    @Override
    public void getMoviesInaPckg(int packageId, String token) {
        packageInteractor.getMoviesInaPckg(packageId, token);
    }

    @Override
    public void setChannelsInaPckg(int packageId, List<ChannelPckgItem> channelsInaPckgList) {
        packagesView.setChannelsInaPckg(packageId,channelsInaPckgList);
    }

    @Override
    public void setMoviesInaPckg(int packageId, List<MoviePckgItem> moviesInaPckgList) {
        packagesView.setMoviesInaPckg(packageId,moviesInaPckgList);
    }

    @Override
    public void onChannelInaPckgError(int packageId, String message) {
        packagesView.onChannelInaPckgError(packageId,message);
    }

    @Override
    public void onMoviesInaPckgError(int packageId, String message) {
        packagesView.onMoviesInaPckgError(packageId,message);

    }
}
