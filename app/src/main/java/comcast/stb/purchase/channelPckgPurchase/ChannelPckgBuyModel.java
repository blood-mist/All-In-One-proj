package comcast.stb.purchase.channelPckgPurchase;





import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import comcast.stb.entity.BuyResponse;
import comcast.stb.utils.ApiManager;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by nitv on 2/1/18.
 */

public class ChannelPckgBuyModel implements ChannelPckgApiInterface.ChannelPcgkBuyInteractor {
    ChannelPckgApiInterface.ChannelPcgkBuyListener channelPcgkBuyListener;

    public ChannelPckgBuyModel(ChannelPckgApiInterface.ChannelPcgkBuyListener channelPcgkBuyListener) {
        this.channelPcgkBuyListener = channelPcgkBuyListener;
    }

    @Override
    public void buyChannelPcgk(final int packageId, int months, String token) {
        Retrofit retrofit = ApiManager.getAdapter();
        final ChannelPckgApiInterface channelPckgApiInterface = retrofit.create(ChannelPckgApiInterface.class);


        Observable<Response<BuyResponse>> observable = channelPckgApiInterface.buyChannelPackages(packageId, months, token);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<BuyResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<BuyResponse> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            channelPcgkBuyListener.setChannelPcgkBuyResponse(value.body());
                        } else if (responseCode == 403) {
                            channelPcgkBuyListener.onChannelPckgBuyError(packageId,"403");
                        } else if (responseCode == 401) {
//                            tokenPres.refreshTheToken(token);
                        } else {
                            channelPcgkBuyListener.onChannelPckgBuyError(packageId,value.message()); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            channelPcgkBuyListener.onChannelPckgBuyError(packageId,"No Internet Connection");
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            channelPcgkBuyListener.onChannelPckgBuyError(packageId,"Couldn't connect to server");
                        } else {
                            channelPcgkBuyListener.onChannelPckgBuyError(packageId,"Error Occured");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
