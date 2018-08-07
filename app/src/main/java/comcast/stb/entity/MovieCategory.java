package comcast.stb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class MovieCategory implements Parcelable {

	@SerializedName("movies")
	private List<MoviesItem> movies;

	@SerializedName("category_title")
	private String categoryTitle;

	@SerializedName("category_logo")
	private String categoryLogo;

	@SerializedName("category_id")
	private int categoryId;

	@SerializedName("category_desc")
	private String categoryDesc;

	@SerializedName("parent_cat")
	private int parentCat;

	public void setMovies(List<MoviesItem> movies){
		this.movies = movies;
	}

	public List<MoviesItem> getMovies(){
		return movies;
	}

	public void setCategoryTitle(String categoryTitle){
		this.categoryTitle = categoryTitle;
	}

	public String getCategoryTitle(){
		return categoryTitle;
	}

	public void setCategoryLogo(String categoryLogo){
		this.categoryLogo = categoryLogo;
	}

	public String getCategoryLogo(){
		return categoryLogo;
	}

	public void setCategoryId(int categoryId){
		this.categoryId = categoryId;
	}

	public int getCategoryId(){
		return categoryId;
	}

	public void setCategoryDesc(String categoryDesc){
		this.categoryDesc = categoryDesc;
	}

	public String getCategoryDesc(){
		return categoryDesc;
	}

	public void setParentCat(int parentCat){
		this.parentCat = parentCat;
	}

	public int getParentCat(){
		return parentCat;
	}

	@Override
 	public String toString(){
		return 
			"MovieCategory{" + 
			"movies = '" + movies + '\'' + 
			",category_title = '" + categoryTitle + '\'' + 
			",category_logo = '" + categoryLogo + '\'' + 
			",category_id = '" + categoryId + '\'' + 
			",category_desc = '" + categoryDesc + '\'' + 
			",parent_cat = '" + parentCat + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(this.movies);
		dest.writeString(this.categoryTitle);
		dest.writeString(this.categoryLogo);
		dest.writeInt(this.categoryId);
		dest.writeString(this.categoryDesc);
		dest.writeInt(this.parentCat);
	}

	public MovieCategory() {
	}

	protected MovieCategory(Parcel in) {
		this.movies = new ArrayList<MoviesItem>();
		in.readList(this.movies, MoviesItem.class.getClassLoader());
		this.categoryTitle = in.readString();
		this.categoryLogo = in.readString();
		this.categoryId = in.readInt();
		this.categoryDesc = in.readString();
		this.parentCat = in.readInt();
	}

	public static final Parcelable.Creator<MovieCategory> CREATOR = new Parcelable.Creator<MovieCategory>() {
		@Override
		public MovieCategory createFromParcel(Parcel source) {
			return new MovieCategory(source);
		}

		@Override
		public MovieCategory[] newArray(int size) {
			return new MovieCategory[size];
		}
	};
}