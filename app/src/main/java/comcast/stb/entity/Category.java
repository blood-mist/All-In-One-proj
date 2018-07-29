package comcast.stb.entity;

import java.util.List;

public class Category {
	private String categoryTitle;
	private Object categoryLogo;
	private int categoryId;
	private List<Channel> channels;
	private String categoryDesc;
	private Object otherLanguagesName;

	public void setCategoryTitle(String categoryTitle){
		this.categoryTitle = categoryTitle;
	}

	public String getCategoryTitle(){
		return categoryTitle;
	}

	public void setCategoryLogo(Object categoryLogo){
		this.categoryLogo = categoryLogo;
	}

	public Object getCategoryLogo(){
		return categoryLogo;
	}

	public void setCategoryId(int categoryId){
		this.categoryId = categoryId;
	}

	public int getCategoryId(){
		return categoryId;
	}

	public void setChannels(List<Channel> channels){
		this.channels = channels;
	}

	public List<Channel> getChannels(){
		return channels;
	}

	public void setCategoryDesc(String categoryDesc){
		this.categoryDesc = categoryDesc;
	}

	public String getCategoryDesc(){
		return categoryDesc;
	}

	public void setOtherLanguagesName(Object otherLanguagesName){
		this.otherLanguagesName = otherLanguagesName;
	}

	public Object getOtherLanguagesName(){
		return otherLanguagesName;
	}

	@Override
 	public String toString(){
		return 
			"Category{" +
			"category_title = '" + categoryTitle + '\'' + 
			",category_logo = '" + categoryLogo + '\'' + 
			",category_id = '" + categoryId + '\'' + 
			",channels = '" + channels + '\'' + 
			",category_desc = '" + categoryDesc + '\'' + 
			",other_languages_name = '" + otherLanguagesName + '\'' + 
			"}";
		}
}