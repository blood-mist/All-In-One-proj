package comcast.stb.entity;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class SchedulerData{

	@SerializedName("@type")
	private String type;

	@SerializedName("Channel")
	private EpgChannel channel;

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setChannel(EpgChannel channel){
		this.channel = channel;
	}

	public EpgChannel getChannel(){
		return channel;
	}

	@Override
 	public String toString(){
		return 
			"SchedulerData{" + 
			"@type = '" + type + '\'' + 
			",channel = '" + channel + '\'' + 
			"}";
		}
}