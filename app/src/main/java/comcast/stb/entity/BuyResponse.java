package comcast.stb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class BuyResponse implements Parcelable{

	@SerializedName("new_expiry")
	private String newExpiry;

	@SerializedName("success")
	private boolean success;

	@SerializedName("name")
	private String name;

	public void setNewExpiry(String newExpiry){
		this.newExpiry = newExpiry;
	}

	public String getNewExpiry(){
		return newExpiry;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	@Override
 	public String toString(){
		return 
			"BuyResponse{" + 
			"new_expiry = '" + newExpiry + '\'' + 
			",success = '" + success + '\'' + 
			",name = '" + name + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.newExpiry);
		dest.writeByte(this.success ? (byte) 1 : (byte) 0);
		dest.writeString(this.name);
	}

	public BuyResponse() {
	}

	protected BuyResponse(Parcel in) {
		this.newExpiry = in.readString();
		this.success = in.readByte() != 0;
		this.name = in.readString();
	}

	public static final Creator<BuyResponse> CREATOR = new Creator<BuyResponse>() {
		@Override
		public BuyResponse createFromParcel(Parcel source) {
			return new BuyResponse(source);
		}

		@Override
		public BuyResponse[] newArray(int size) {
			return new BuyResponse[size];
		}
	};
}