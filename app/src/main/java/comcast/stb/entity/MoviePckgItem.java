package comcast.stb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class MoviePckgItem implements Parcelable{

    @SerializedName("movies")
    private List<MoviesItem> movies;

    @SerializedName("package_name")
    private String packageName;

    @SerializedName("package_id")
    private int packageId;

    @SerializedName("package_price")
    private String packagePrice;

    @SerializedName("package_description")
    private String packageDescription;

    public void setMovies(List<MoviesItem> channels){
        this.movies = channels;
    }

    public List<MoviesItem> getMovies(){
        return movies;
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

    public void setPackagePrice(String packagePrice){
        this.packagePrice = packagePrice;
    }

    public String getPackagePrice(){
        return packagePrice;
    }

    public void setPackageDescription(String packageDescription){
        this.packageDescription = packageDescription;
    }

    public String getPackageDescription(){
        return packageDescription;
    }

    @Override
    public String toString(){
        return
                "MoviePckgItem{" +
                        "movies = '" + movies + '\'' +
                        ",package_name = '" + packageName + '\'' +
                        ",package_id = '" + packageId + '\'' +
                        ",package_price = '" + packagePrice + '\'' +
                        ",package_description = '" + packageDescription + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.movies);
        dest.writeString(this.packageName);
        dest.writeInt(this.packageId);
        dest.writeString(this.packagePrice);
        dest.writeString(this.packageDescription);
    }

    public MoviePckgItem() {
    }

    protected MoviePckgItem(Parcel in) {
        this.movies = in.createTypedArrayList(MoviesItem.CREATOR);
        this.packageName = in.readString();
        this.packageId = in.readInt();
        this.packagePrice = in.readString();
        this.packageDescription = in.readString();
    }

    public static final Creator<MoviePckgItem> CREATOR = new Creator<MoviePckgItem>() {
        @Override
        public MoviePckgItem createFromParcel(Parcel source) {
            return new MoviePckgItem(source);
        }

        @Override
        public MoviePckgItem[] newArray(int size) {
            return new MoviePckgItem[size];
        }
    };
}