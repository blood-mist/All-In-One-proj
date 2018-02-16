package comcast.stb.purchase.livetvpurchase;


import comcast.stb.entity.BuyResponse;

/**
 * Created by blood-mist on 1/27/18.
 */

public class BuyChannelPresenterImpl implements BuyChannelApiInterface.BuyListener,BuyChannelApiInterface.BuyPresenter {
    BuyChannelApiInterface.BuyView buyView;
    BuyChannelApiInterface.BuyInteractor buyInteractor;

    public BuyChannelPresenterImpl(BuyChannelApiInterface.BuyView buyView) {
        this.buyView = buyView;
        buyInteractor = new BuyChannelModel(this);
    }

    @Override
    public void buyChannel(int channelId, int months, String token) {
        buyInteractor.buyChannel(channelId, months, token);
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
