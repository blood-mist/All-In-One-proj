package comcast.stb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class PackagesInfo implements Parcelable {

	@SerializedName("recommend_group")
	private int recommendGroup;

	@SerializedName("subscription_status")
	private String subscriptionStatus;

	@SerializedName("expiry_flag")
	private String expiryFlag;

	@SerializedName("package_name")
	private String packageName;

	@SerializedName("package_id")
	private int packageId;

	@SerializedName("expiry")
	private String expiry;

	@SerializedName("package_price")
	private String packagePrice;

	public void setRecommendGroup(int recommendGroup){
		this.recommendGroup = recommendGroup;
	}

	public int getRecommendGroup(){
		return recommendGroup;
	}

	public void setSubscriptionStatus(String subscriptionStatus){
		this.subscriptionStatus = subscriptionStatus;
	}

	public String getSubscriptionStatus(){
		return subscriptionStatus;
	}

	public void setExpiryFlag(String expiryFlag){
		this.expiryFlag = expiryFlag;
	}

	public String getExpiryFlag(){
		return expiryFlag;
	}

	public void setPackageName(String packageName){
		this.packageName = packageName;
	}

	public String getPackageName(){
		return packageName;
	}

	public void setPackageId(int packageId){
		this.packageId = packageId;
	}

	public int getPackageId(){
		return packageId;
	}

	public void setExpiry(String expiry){
		this.expiry = expiry;
	}

	public String getExpiry(){
		return expiry;
	}

	public void setPackagePrice(String packagePrice){
		this.packagePrice = packagePrice;
	}

	public String getPackagePrice(){
		return packagePrice;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.recommendGroup);
		dest.writeString(this.subscriptionStatus);
		dest.writeString(this.expiryFlag);
		dest.writeString(this.packageName);
		dest.writeInt(this.packageId);
		dest.writeString(this.expiry);
		dest.writeString(this.packagePrice);
	}

	public PackagesInfo() {
	}

	protected PackagesInfo(Parcel in) {
		this.recommendGroup = in.readInt();
		this.subscriptionStatus = in.readString();
		this.expiryFlag = in.readString();
		this.packageName = in.readString();
		this.packageId = in.readInt();
		this.expiry = in.readString();
		this.packagePrice = in.readString();
	}

	public static final Parcelable.Creator<PackagesInfo> CREATOR = new Parcelable.Creator<PackagesInfo>() {
		@Override
		public PackagesInfo createFromParcel(Parcel source) {
			return new PackagesInfo(source);
		}

		@Override
		public PackagesInfo[] newArray(int size) {
			return new PackagesInfo[size];
		}
	};
}