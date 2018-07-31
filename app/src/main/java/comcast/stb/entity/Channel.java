package comcast.stb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

@Generated("com.robohorse.robopojogenerator")
public class Channel extends RealmObject implements Parcelable{

	@SerializedName("channel_name")
	private String channelName;

	@SerializedName("channel_logo")
	private String channelLogo;

	@SerializedName("channel_price")
	private String channelPrice;

	@SerializedName("channel_category_id")
	private int channelCategoryId;

	@SerializedName("subscription_status")
	private String subscriptionStatus;

	@SerializedName("expiry_flag")
	private boolean expiryFlag;

	@SerializedName("dvr_path")
	private String dvrPath;

	@SerializedName("expiry")
	private boolean expiry;

	@SerializedName("channel_id")
	private int channelId;

	public void setChannelName(String channelName){
		this.channelName = channelName;
	}

	public String getChannelName(){
		return channelName;
	}

	public void setChannelLogo(String channelLogo){
		this.channelLogo = channelLogo;
	}

	public String getChannelLogo(){
		return channelLogo;
	}

	public void setChannelPrice(String channelPrice){
		this.channelPrice = channelPrice;
	}

	public String getChannelPrice(){
		return channelPrice;
	}

	public void setChannelCategoryId(int channelCategoryId){
		this.channelCategoryId = channelCategoryId;
	}

	public int getChannelCategoryId(){
		return channelCategoryId;
	}

	public void setSubscriptionStatus(String subscriptionStatus){
		this.subscriptionStatus = subscriptionStatus;
	}

	public String getSubscriptionStatus(){
		return subscriptionStatus;
	}

	public void setExpiryFlag(boolean expiryFlag){
		this.expiryFlag = expiryFlag;
	}

	public Object getExpiryFlag(){
		return expiryFlag;
	}

	public void setDvrPath(String dvrPath){
		this.dvrPath = dvrPath;
	}

	public String getDvrPath(){
		return dvrPath;
	}

	public void setExpiry(boolean expiry){
		this.expiry = expiry;
	}

	public boolean getExpiry(){
		return expiry;
	}

	public void setChannelId(int channelId){
		this.channelId = channelId;
	}

	public int getChannelId(){
		return channelId;
	}

	@Override
 	public String toString(){
		return 
			"Channel{" +
			"channel_name = '" + channelName + '\'' + 
			",channel_logo = '" + channelLogo + '\'' + 
			",channel_price = '" + channelPrice + '\'' + 
			",channel_category_id = '" + channelCategoryId + '\'' + 
			",subscription_status = '" + subscriptionStatus + '\'' + 
			",expiry_flag = '" + expiryFlag + '\'' + 
			",dvr_path = '" + dvrPath + '\'' + 
			",expiry = '" + expiry + '\'' + 
			",channel_id = '" + channelId + '\'' + 
			"}";
		}
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.channelName);
		dest.writeString(this.channelLogo);
		dest.writeString(this.channelPrice);
		dest.writeInt(this.channelId);
//		dest.writeString(this.channelDesc);
	}

	public Channel() {
	}

	protected Channel(Parcel in) {
		this.channelName = in.readString();
		this.channelLogo = in.readString();
		this.channelPrice = in.readString();
		this.channelId = in.readInt();
//		this.channelDesc = in.readString();
	}

	public static final Parcelable.Creator<Channel> CREATOR = new Parcelable.Creator<Channel>() {
		@Override
		public Channel createFromParcel(Parcel source) {
			return new Channel(source);
		}

		@Override
		public Channel[] newArray(int size) {
			return new Channel[size];
		}
	};
}