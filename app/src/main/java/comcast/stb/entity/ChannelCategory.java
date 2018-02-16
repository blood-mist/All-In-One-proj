package comcast.stb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class ChannelCategory implements Parcelable {

	@SerializedName("category_title")
	private String categoryTitle;

	@SerializedName("category_id")
	private int categoryId;

	@SerializedName("channels")
	private List<Channel> channels;

	@SerializedName("category_desc")
	private String categoryDesc;

	public void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
	}

	public String getCategoryTitle() {
		return categoryTitle;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.categoryTitle);
		dest.writeInt(this.categoryId);
		dest.writeList(this.channels);
		dest.writeString(this.categoryDesc);
	}

	public ChannelCategory() {
	}

	protected ChannelCategory(Parcel in) {
		this.categoryTitle = in.readString();
		this.categoryId = in.readInt();
		this.channels = new ArrayList<Channel>();
		in.readList(this.channels, Channel.class.getClassLoader());
		this.categoryDesc = in.readString();
	}

	public static final Creator<ChannelCategory> CREATOR = new Creator<ChannelCategory>() {
		@Override
		public ChannelCategory createFromParcel(Parcel source) {
			return new ChannelCategory(source);
		}

		@Override
		public ChannelCategory[] newArray(int size) {
			return new ChannelCategory[size];
		}
	};
}