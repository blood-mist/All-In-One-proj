package comcast.stb.entity.events;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class EventsItem{

	@SerializedName("duration")
	private String duration;

	@SerializedName("programName")
	private String programName;

	@SerializedName("description")
	private String description;

	@SerializedName("beginTime")
	private String beginTime;

	public void setDuration(String duration){
		this.duration = duration;
	}

	public String getDuration(){
		return duration;
	}

	public void setProgramName(String programName){
		this.programName = programName;
	}

	public String getProgramName(){
		return programName;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setBeginTime(String beginTime){
		this.beginTime = beginTime;
	}

	public String getBeginTime(){
		return beginTime;
	}

	@Override
 	public String toString(){
		return 
			"DvrResponse{" +
			"duration = '" + duration + '\'' + 
			",programName = '" + programName + '\'' + 
			",description = '" + description + '\'' + 
			",beginTime = '" + beginTime + '\'' + 
			"}";
		}
}