package comcast.stb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ChannelsItem implements Parcelable {

	@SerializedName("channel_name")
	private String channelName;

	@SerializedName("channel_logo")
	private String channelLogo;

	@SerializedName("channel_price")
	private String channelPrice;

	@SerializedName("channel_id")
	private int channelId;

	@SerializedName("channel_desc")
	private String channelDesc;

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

	public void setChannelId(int channelId){
		this.channelId = channelId;
	}

	public int getChannelId(){
		return channelId;
	}

	public void setChannelDesc(String channelDesc){
		this.channelDesc = channelDesc;
	}

	public String getChannelDesc(){
		return channelDesc;
	}

	@Override
 	public String toString(){
		return 
			"ChannelsItem{" + 
			"channel_name = '" + channelName + '\'' + 
			",channel_logo = '" + channelLogo + '\'' + 
			",channel_price = '" + channelPrice + '\'' + 
			",channel_id = '" + channelId + '\'' + 
			",channel_desc = '" + channelDesc + '\'' + 
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
		dest.writeString(this.channelDesc);
	}

	public ChannelsItem() {
	}

	protected ChannelsItem(Parcel in) {
		this.channelName = in.readString();
		this.channelLogo = in.readString();
		this.channelPrice = in.readString();
		this.channelId = in.readInt();
		this.channelDesc = in.readString();
	}

	public static final Creator<ChannelsItem> CREATOR = new Creator<ChannelsItem>() {
		@Override
		public ChannelsItem createFromParcel(Parcel source) {
			return new ChannelsItem(source);
		}

		@Override
		public ChannelsItem[] newArray(int size) {
			return new ChannelsItem[size];
		}
	};
}