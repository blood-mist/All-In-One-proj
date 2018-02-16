package comcast.stb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class OrderItem implements Parcelable{

	@SerializedName("duration")
	private String duration;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("total_amount")
	private String totalAmount;

	@SerializedName("price")
	private String price;

	@SerializedName("subscribed_type")
	private String subscribedType;

	@SerializedName("name")
	private String name;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("expiry")
	private String expiry;

	@SerializedName("subscribed_id")
	private String subscribedId;

	public void setDuration(String duration){
		this.duration = duration;
	}

	public String getDuration(){
		return duration;
	}

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

	public void setTotalAmount(String totalAmount){
		this.totalAmount = totalAmount;
	}

	public String getTotalAmount(){
		return totalAmount;
	}

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return price;
	}

	public void setSubscribedType(String subscribedType){
		this.subscribedType = subscribedType;
	}

	public String getSubscribedType(){
		return subscribedType;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
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

	public void setExpiry(String expiry){
		this.expiry = expiry;
	}

	public String getExpiry(){
		return expiry;
	}

	public void setSubscribedId(String subscribedId){
		this.subscribedId = subscribedId;
	}

	public String getSubscribedId(){
		return subscribedId;
	}

	@Override
 	public String toString(){
		return 
			"OrderItem{" + 
			"duration = '" + duration + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",user_id = '" + userId + '\'' + 
			",total_amount = '" + totalAmount + '\'' + 
			",price = '" + price + '\'' + 
			",subscribed_type = '" + subscribedType + '\'' + 
			",name = '" + name + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",expiry = '" + expiry + '\'' + 
			",subscribed_id = '" + subscribedId + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.duration);
		dest.writeString(this.updatedAt);
		dest.writeInt(this.userId);
		dest.writeString(this.totalAmount);
		dest.writeString(this.price);
		dest.writeString(this.subscribedType);
		dest.writeString(this.name);
		dest.writeString(this.createdAt);
		dest.writeInt(this.id);
		dest.writeString(this.expiry);
		dest.writeString(this.subscribedId);
	}

	public OrderItem() {
	}

	protected OrderItem(Parcel in) {
		this.duration = in.readString();
		this.updatedAt = in.readString();
		this.userId = in.readInt();
		this.totalAmount = in.readString();
		this.price = in.readString();
		this.subscribedType = in.readString();
		this.name = in.readString();
		this.createdAt = in.readString();
		this.id = in.readInt();
		this.expiry = in.readString();
		this.subscribedId = in.readString();
	}

	public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
		@Override
		public OrderItem createFromParcel(Parcel source) {
			return new OrderItem(source);
		}

		@Override
		public OrderItem[] newArray(int size) {
			return new OrderItem[size];
		}
	};
}