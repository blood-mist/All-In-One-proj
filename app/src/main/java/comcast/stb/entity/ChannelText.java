package comcast.stb.entity;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class ChannelText{

	@SerializedName("ChannelName")
	private String channelName;

	@SerializedName("@language")
	private String language;

	public void setChannelName(String channelName){
		this.channelName = channelName;
	}

	public String getChannelName(){
		return channelName;
	}

	public void setLanguage(String language){
		this.language = language;
	}

	public String getLanguage(){
		return language;
	}

	@Override
 	public String toString(){
		return 
			"ChannelText{" + 
			"channelName = '" + channelName + '\'' + 
			",@language = '" + language + '\'' + 
			"}";
		}
}