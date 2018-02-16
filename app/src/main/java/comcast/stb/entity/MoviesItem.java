package comcast.stb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class MoviesItem implements Parcelable{

	@SerializedName("movie_picture")
	private String moviePicture;

	@SerializedName("subscription_status")
	private String subscriptionStatus;

	@SerializedName("expiry_flag")
	private boolean expiryFlag;

	@SerializedName("movie_category_id")
	private int movieCategoryId;

	@SerializedName("movie_price")
	private double moviePrice;

	@SerializedName("movie_name")
	private String movieName;

	@SerializedName("expiry")
	private String expiry;

	@SerializedName("movie_id")
	private int movieId;

	public void setMoviePicture(String moviePicture){
		this.moviePicture = moviePicture;
	}

	public String getMoviePicture(){
		return moviePicture;
	}

	public void setSubscriptionStatus(String subscriptionStatus){
		this.subscriptionStatus = subscriptionStatus;
	}

	public String getSubscriptionStatus(){
		return subscriptionStatus;
	}

	public void setExpiryFlag(boolean expiryFlag){
		this.expiryFlag = expiryFlag;
	}

	public boolean getExpiryFlag(){
		return expiryFlag;
	}

	public void setMovieCategoryId(int movieCategoryId){
		this.movieCategoryId = movieCategoryId;
	}

	public int getMovieCategoryId(){
		return movieCategoryId;
	}

	public void setMoviePrice(double moviePrice){
		this.moviePrice = moviePrice;
	}

	public double getMoviePrice(){
		return moviePrice;
	}

	public void setMovieName(String movieName){
		this.movieName = movieName;
	}

	public String getMovieName(){
		return movieName;
	}

	public void setExpiry(String expiry){
		this.expiry = expiry;
	}

	public String getExpiry(){
		return expiry;
	}

	public void setMovieId(int movieId){
		this.movieId = movieId;
	}

	public int getMovieId(){
		return movieId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.moviePicture);
		dest.writeString(this.subscriptionStatus);
		dest.writeByte(this.expiryFlag ? (byte) 1 : (byte) 0);
		dest.writeInt(this.movieCategoryId);
		dest.writeDouble(this.moviePrice);
		dest.writeString(this.movieName);
		dest.writeString(this.expiry);
		dest.writeInt(this.movieId);
	}

	public MoviesItem() {
	}

	protected MoviesItem(Parcel in) {
		this.moviePicture = in.readString();
		this.subscriptionStatus = in.readString();
		this.expiryFlag = in.readByte() != 0;
		this.movieCategoryId = in.readInt();
		this.moviePrice = in.readDouble();
		this.movieName = in.readString();
		this.expiry = in.readString();
		this.movieId = in.readInt();
	}

	public static final Creator<MoviesItem> CREATOR = new Creator<MoviesItem>() {
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