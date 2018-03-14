package comcast.stb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class MoviesItem implements Parcelable {
	@SerializedName("movie_picture")
	private String moviePicture;

	@SerializedName("movie_category_id")
	private int movieCategoryId;

	@SerializedName("movie_name")
	private String movieName;

	@SerializedName("movie_id")
	private int movieId;

	@SerializedName("is_youtube")
	private int isYoutube;
	@SerializedName("movie_price")
	private String moviePrice;
	@SerializedName("expiry")
	private String expiry;
	@SerializedName("subscription_status")
	private String subscriptionStatus;
	@SerializedName("expiry_flag")
	private boolean expiryFlag;

	public void setMoviePicture(String moviePicture){
		this.moviePicture = moviePicture;
	}

	public String getMoviePicture(){
		return moviePicture;
	}

	public void setMovieCategoryId(int movieCategoryId){
		this.movieCategoryId = movieCategoryId;
	}

	public int getMovieCategoryId(){
		return movieCategoryId;
	}

	public void setMovieName(String movieName){
		this.movieName = movieName;
	}

	public String getMovieName(){
		return movieName;
	}

	public void setMovieId(int movieId){
		this.movieId = movieId;
	}

	public int getMovieId(){
		return movieId;
	}

	public void setIsYoutube(int isYoutube){
		this.isYoutube = isYoutube;
	}

	public int getIsYoutube(){
		return isYoutube;
	}

	public String getMoviePrice() {
		return moviePrice;
	}

	public void setMoviePrice(String moviePrice) {
		this.moviePrice = moviePrice;
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.moviePicture);
		dest.writeInt(this.movieCategoryId);
		dest.writeString(this.movieName);
		dest.writeInt(this.movieId);
		dest.writeInt(this.isYoutube);
		dest.writeString(this.moviePrice);
		dest.writeString(this.expiry);
		dest.writeString(this.subscriptionStatus);
		dest.writeByte(this.expiryFlag ? (byte) 1 : (byte) 0);
	}

	public MoviesItem() {
	}

	protected MoviesItem(Parcel in) {
		this.moviePicture = in.readString();
		this.movieCategoryId = in.readInt();
		this.movieName = in.readString();
		this.movieId = in.readInt();
		this.isYoutube = in.readInt();
		this.moviePrice = in.readString();
		this.expiry = in.readString();
		this.subscriptionStatus = in.readString();
		this.expiryFlag = in.readByte() != 0;
	}

	public static final Parcelable.Creator<MoviesItem> CREATOR = new Parcelable.Creator<MoviesItem>() {
		@Override
		public MoviesItem createFromParcel(Parcel source) {
			return new MoviesItem(source);
		}

		@Override
		public MoviesItem[] newArray(int size) {
			return new MoviesItem[size];
		}
	};
}