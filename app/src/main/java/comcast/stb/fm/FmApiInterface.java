package comcast.stb.fm;


import java.util.List;

import comcast.stb.entity.FmCategory;
import comcast.stb.entity.TvLink;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface FmApiInterface {
    @GET("fms")
    Observable<Response<List<FmCategory>>> getFmsWithCategory(@Query("token") String token);

    @GET("fms/{fm-id}/playable")
    Observable<Response<TvLink>> getFmLink(@Path("fm-id") int fmID, @Query("token") String token);

    interface FmWithCategoryView{
        void setFmsWithCategory(List<FmCategory> FmCategoryList);
        void onErrorOccured(String message);
        void showProgress();
        void hideProgress();
    }
    interface FmWithCategoryPresenter{
        void getFmsWithCategory(String token);


    }
    interface FmWithCategoryInteractor{
        void getFmsWithCategory(String token);
    }
    interface FmWithCategoryListener{
        void takeFmsWithCategory(List<FmCategory> FmCategoryList);
        void onErrorOccured(String message);
    }
}
