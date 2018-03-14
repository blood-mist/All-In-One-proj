package comcast.stb.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class EpgResponse{

	@SerializedName("creationTime")
	private String creationTime;

	@SerializedName("channelName")
	private String channelName;

	@SerializedName("events")
	private List<EventItem> events;

	public void setCreationTime(String creationTime){
		this.creationTime = creationTime;
	}

	public String getCreationTime(){
		return creationTime;
	}

	public void setChannelName(String channelName){
		this.channelName = channelName;
	}

	public String getChannelName(){
		return channelName;
	}

	public void setEvents(List<EventItem> events){
		this.events = events;
	}

	public List<EventItem> getEvents(){
		return events;
	}

	@Override
 	public String toString(){
		return 
			"EpgResponse{" + 
			"creationTime = '" + creationTime + '\'' + 
			",channelName = '" + channelName + '\'' + 
			",events = '" + events + '\'' + 
			"}";
		}
}