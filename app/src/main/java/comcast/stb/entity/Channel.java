package comcast.stb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

import io.realm.RealmObject;

@Generated("com.robohorse.robopojogenerator")
public class Channel extends RealmObject implements Parcelable  {
	@SerializedName("channel_name")
	private String channelName;

	@SerializedName("channel_logo")
	private String channelLogo;

	@SerializedName("channel_category_id")
	private int channelCategoryId;

	@SerializedName("parental_lock")
	private int parentalLock;

	@SerializedName("channel_id")
	private int channelId;

	@SerializedName("channel_price")
	private String channelPrice;
	@SerializedName("expiry")
	private String expiry;
	@SerializedName("subscription_status")
	private String subscriptionStatus;
	@SerializedName("expiry_flag")
	private boolean expiryFlag;

	public String getChannelPrice() {
		return channelPrice;
	}

	public void setChannelPrice(String channelPrice) {
		this.channelPrice = channelPrice;
	}

	public String getExpiry() {
		return expiry;
	}

	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}

	public String getSubscriptionStatus() {
		return subscriptionStatus;
	}

	public void setSubscriptionStatus(String subscriptionStatus) {
		this.subscriptionStatus = subscriptionStatus;
	}

	public boolean isExpiryFlag() {
		return expiryFlag;
	}

	public void setExpiryFlag(boolean expiryFlag) {
		this.expiryFlag = expiryFlag;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelLogo(String channelLogo) {
		this.channelLogo = channelLogo;
	}

	public String getChannelLogo() {
		return channelLogo;
	}

	public void setChannelCategoryId(int channelCategoryId) {
		this.channelCategoryId = channelCategoryId;
	}

	public int getChannelCategoryId() {
		return channelCategoryId;
	}

	public void setParentalLock(int parentalLock) {
		this.parentalLock = parentalLock;
	}

	public int getParentalLock() {
		return parentalLock;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public int getChannelId() {
		return channelId;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.channelName);
		dest.writeString(this.channelLogo);
		dest.writeInt(this.channelCategoryId);
		dest.writeInt(this.parentalLock);
		dest.writeInt(this.channelId);
		dest.writeString(this.channelPrice);
		dest.writeString(this.expiry);
		dest.writeString(this.subscriptionStatus);
		dest.writeByte(this.expiryFlag ? (byte) 1 : (byte) 0);
	}

	public Channel() {
	}

	protected Channel(Parcel in) {
		this.channelName = in.readString();
		this.channelLogo = in.readString();
		this.channelCategoryId = in.readInt();
		this.parentalLock = in.readInt();
		this.channelId = in.readInt();
		this.channelPrice = in.readString();
		this.expiry = in.readString();
		this.subscriptionStatus = in.readString();
		this.expiryFlag = in.readByte() != 0;
	}

	public static final Creator<Channel> CREATOR = new Creator<Channel>() {
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