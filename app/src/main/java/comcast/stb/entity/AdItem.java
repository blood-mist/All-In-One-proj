package comcast.stb.entity;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class AdItem{

	@SerializedName("channel_logo")
	private String channelLogo;

	@SerializedName("link")
	private String link;

	@SerializedName("id")
	private int id;

	@SerializedName("label")
	private String label;

	public void setChannelLogo(String channelLogo){
		this.channelLogo = channelLogo;
	}

	public String getChannelLogo(){
		return channelLogo;
	}

	public void setLink(String link){
		this.link = link;
	}

	public String getLink(){
		return link;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setLabel(String label){
		this.label = label;
	}

	public String getLabel(){
		return label;
	}

	@Override
 	public String toString(){
		return 
			"AdItem{" + 
			"channel_logo = '" + channelLogo + '\'' + 
			",link = '" + link + '\'' + 
			",id = '" + id + '\'' + 
			",label = '" + label + '\'' + 
			"}";
		}
}