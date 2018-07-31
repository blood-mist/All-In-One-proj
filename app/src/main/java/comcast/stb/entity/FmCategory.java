package comcast.stb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class FmCategory implements Parcelable {

	@SerializedName("category_name")
	private String categoryName;

	@SerializedName("id")
	private int id;

	@SerializedName("fms")
	private List<FmsItem> fms;

	public void setCategoryName(String categoryName){
		this.categoryName = categoryName;
	}

	public String getCategoryName(){
		return categoryName;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setFms(List<FmsItem> fms){
		this.fms = fms;
	}

	public List<FmsItem> getFms(){
		return fms;
	}

	@Override
 	public String toString(){
		return 
			"FmCategory{" +
			"category_name = '" + categoryName + '\'' + 
			",id = '" + id + '\'' + 
			",fms = '" + fms + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.categoryName);
		dest.writeInt(this.id);
		dest.writeTypedList(this.fms);
	}

	public FmCategory() {
	}

	protected FmCategory(Parcel in) {
		this.categoryName = in.readString();
		this.id = in.readInt();
		this.fms = in.createTypedArrayList(FmsItem.CREATOR);
	}

	public static final Creator<FmCategory> CREATOR = new Creator<FmCategory>() {
		@Override
		public FmCategory createFromParcel(Parcel source) {
			return new FmCategory(source);
		}

		@Override
		public FmCategory[] newArray(int size) {
			return new FmCategory[size];
		}
	};
}