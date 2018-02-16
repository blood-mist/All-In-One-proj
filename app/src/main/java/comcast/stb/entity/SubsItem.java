package comcast.stb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class SubsItem implements Parcelable{

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("subscribed_type")
	private String subscribedType;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("subscribed_id")
	private int subscribedId;

	@SerializedName("expiry")
	private String expiry;

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setSubscribedType(String subscribedType){
		this.subscribedType = subscribedType;
	}

	public String getSubscribedType(){
		return subscribedType;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setSubscribedId(int subscribedId){
		this.subscribedId = subscribedId;
	}

	public int getSubscribedId(){
		return subscribedId;
	}

	public void setExpiry(String expiry){
		this.expiry = expiry;
	}

	public String getExpiry(){
		return expiry;
	}

	@Override
 	public String toString(){
		return 
			"SubsItem{" +
			"updated_at = '" + updatedAt + '\'' + 
			",user_id = '" + userId + '\'' + 
			",subscribed_type = '" + subscribedType + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",subscribed_id = '" + subscribedId + '\'' + 
			",expiry = '" + expiry + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.updatedAt);
		dest.writeInt(this.userId);
		dest.writeString(this.subscribedType);
		dest.writeString(this.createdAt);
		dest.writeInt(this.id);
		dest.writeInt(this.subscribedId);
		dest.writeString(this.expiry);
	}

	public SubsItem() {
	}

	protected SubsItem(Parcel in) {
		this.updatedAt = in.readString();
		this.userId = in.readInt();
		this.subscribedType = in.readString();
		this.createdAt = in.readString();
		this.id = in.readInt();
		this.subscribedId = in.readInt();
		this.expiry = in.readString();
	}

	public static final Creator<SubsItem> CREATOR = new Creator<SubsItem>() {
		@Override
		public SubsItem createFromParcel(Parcel source) {
			return new SubsItem(source);
		}

		@Override
		public SubsItem[] newArray(int size) {
			return new SubsItem[size];
		}
	};
}