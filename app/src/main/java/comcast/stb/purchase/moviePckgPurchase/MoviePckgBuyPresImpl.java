package comcast.stb.purchase.moviePckgPurchase;


import comcast.stb.entity.BuyResponse;

/**
 * Created by nitv on 2/1/18.
 */

public class MoviePckgBuyPresImpl implements MoviePckgApiInterface.MoviePcgkBuyPresenter, MoviePckgApiInterface.MoviePcgkBuyListener {
    MoviePckgApiInterface.MoviePcgkBuyView MoviePcgkBuyView;
    MoviePckgApiInterface.MoviePcgkBuyInteractor MoviePcgkBuyInteractor;

    public MoviePckgBuyPresImpl(MoviePckgApiInterface.MoviePcgkBuyView MoviePcgkBuyView) {
        this.MoviePcgkBuyView = MoviePcgkBuyView;
        MoviePcgkBuyInteractor = new MoviePckgBuyModel(this);
    }


    @Override
    public void setMoviePcgkBuyResponse(BuyResponse buyResponse) {
        MoviePcgkBuyView.setMoviePcgkBuyRespone(buyResponse);
    }

    @Override
    public void onMoviePckgBuyError(int packageId,String message) {
        MoviePcgkBuyView.onMoviePckgBuyError(packageId,message);
    }

    @Override
    public void buyMoviePcgk(int packageId, int months, String token) {
        MoviePcgkBuyInteractor.buyMoviePcgk(packageId, months, token);
    }
}
