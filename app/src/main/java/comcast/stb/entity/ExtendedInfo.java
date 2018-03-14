package comcast.stb.entity;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class ExtendedInfo{

	@SerializedName("@itemcontent")
	private String itemcontent;

	@SerializedName("@itemname")
	private String itemname;

	public void setItemcontent(String itemcontent){
		this.itemcontent = itemcontent;
	}

	public String getItemcontent(){
		return itemcontent;
	}

	public void setItemname(String itemname){
		this.itemname = itemname;
	}

	public String getItemname(){
		return itemname;
	}

	@Override
 	public String toString(){
		return 
			"ExtendedInfo{" + 
			"@itemcontent = '" + itemcontent + '\'' + 
			",@itemname = '" + itemname + '\'' + 
			"}";
		}
}