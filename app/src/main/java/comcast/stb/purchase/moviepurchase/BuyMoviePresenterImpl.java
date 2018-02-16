package comcast.stb.purchase.moviepurchase;


import comcast.stb.entity.BuyResponse;

/**
 * Created by blood-mist on 1/27/18.
 */

public class BuyMoviePresenterImpl implements BuyMovieApiInterface.BuyListener,BuyMovieApiInterface.BuyPresenter {
    BuyMovieApiInterface.BuyView buyView;
    BuyMovieApiInterface.BuyInteractor buyInteractor;

    public BuyMoviePresenterImpl(BuyMovieApiInterface.BuyView buyView) {
        this.buyView = buyView;
        buyInteractor = new BuyMovieModel(this);
    }

    @Override
    public void buyMovie(int movieId, int months, String token) {
        buyInteractor.buyMovie(movieId, months, token);
    }

    @Override
    public void setBuyResponse(BuyResponse buyResponse) {
        buyView.setBuyRespone(buyResponse);
    }

    @Override
    public void onError(String message) {
        buyView.onError(message);
    }
}
