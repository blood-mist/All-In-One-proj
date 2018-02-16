package comcast.stb.purchase.channelPckgPurchase;


import comcast.stb.entity.BuyResponse;

/**
 * Created by nitv on 2/1/18.
 */

public class ChannelPckgBuyPresImpl implements ChannelPckgApiInterface.ChannelPcgkBuyPresenter, ChannelPckgApiInterface.ChannelPcgkBuyListener {
    ChannelPckgApiInterface.ChannelPcgkBuyView channelPcgkBuyView;
    ChannelPckgApiInterface.ChannelPcgkBuyInteractor channelPcgkBuyInteractor;

    public ChannelPckgBuyPresImpl(ChannelPckgApiInterface.ChannelPcgkBuyView channelPcgkBuyView) {
        this.channelPcgkBuyView = channelPcgkBuyView;
        channelPcgkBuyInteractor = new ChannelPckgBuyModel(this);
    }


    @Override
    public void setChannelPcgkBuyResponse(BuyResponse buyResponse) {
        channelPcgkBuyView.setChannelPcgkBuyRespone(buyResponse);
    }

    @Override
    public void onChannelPckgBuyError(int packageId,String message) {
        channelPcgkBuyView.onChannelPckgBuyError(packageId,message);
    }

    @Override
    public void buyChannelPcgk(int packageId, int months, String token) {
        channelPcgkBuyInteractor.buyChannelPcgk(packageId, months, token);
    }
}
