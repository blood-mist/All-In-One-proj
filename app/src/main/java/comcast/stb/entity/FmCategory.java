package comcast.stb.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class FmCategory {

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
}