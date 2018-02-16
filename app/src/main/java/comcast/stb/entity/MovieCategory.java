package comcast.stb.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class MovieCategory{

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
}