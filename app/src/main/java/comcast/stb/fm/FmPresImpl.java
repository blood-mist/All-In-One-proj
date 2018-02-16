package comcast.stb.fm;



import java.util.List;

import comcast.stb.entity.FmCategory;
import comcast.stb.logout.LogoutPresImpl;

public class FmPresImpl implements FmApiInterface.FmWithCategoryPresenter,FmApiInterface.FmWithCategoryListener {
    FmApiInterface.FmWithCategoryView fmWithCategoryView;
    FmApiInterface.FmWithCategoryInteractor fmWithCategoryInteractor;

    public FmPresImpl(FmApiInterface.FmWithCategoryView fmWithCategoryView,LogoutPresImpl logoutPres) {
        this.fmWithCategoryView = fmWithCategoryView;
        fmWithCategoryInteractor = new FmModel(this,logoutPres);
    }

    @Override
    public void getFmsWithCategory(String token) {
        fmWithCategoryView.showProgress();
        fmWithCategoryInteractor.getFmsWithCategory(token);
    }

    @Override
    public void takeFmsWithCategory(List<FmCategory> fmCategoryList) {
        fmWithCategoryView.setFmsWithCategory(fmCategoryList);
        fmWithCategoryView.hideProgress();
    }

    @Override
    public void onErrorOccured(String message) {
        fmWithCategoryView.onErrorOccured(message);
        fmWithCategoryView.hideProgress();
    }
}
