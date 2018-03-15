package comcast.stb.entity.events;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class EpgResponse{

	@SerializedName("creationTime")
	private String creationTime;

	@SerializedName("channelName")
	private String channelName;

	@SerializedName("events")
	private List<EventsItem> events;

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

	public void setEvents(List<EventsItem> events){
		this.events = events;
	}

	public List<EventsItem> getEvents(){
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