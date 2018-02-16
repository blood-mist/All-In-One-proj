package comcast.stb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class FmsItem implements Parcelable{

	@SerializedName("image")
	private String image;

	@SerializedName("category_id")
	private int categoryId;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setCategoryId(int categoryId){
		this.categoryId = categoryId;
	}

	public int getCategoryId(){
		return categoryId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"FmsItem{" + 
			"image = '" + image + '\'' + 
			",category_id = '" + categoryId + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.image);
		dest.writeInt(this.categoryId);
		dest.writeString(this.name);
		dest.writeInt(this.id);
	}

	public FmsItem() {
	}

	protected FmsItem(Parcel in) {
		this.image = in.readString();
		this.categoryId = in.readInt();
		this.name = in.readString();
		this.id = in.readInt();
	}

	public static final Creator<FmsItem> CREATOR = new Creator<FmsItem>() {
		@Override
		public FmsItem createFromParcel(Parcel source) {
			return new FmsItem(source);
		}

		@Override
		public FmsItem[] newArray(int size) {
			return new FmsItem[size];
		}
	};
}