package comcast.stb.entity;

public class DvrResponse{
	private String startTime;
	private String url;
	private String startDate;

	public void setStartTime(String startTime){
		this.startTime = startTime;
	}

	public String getStartTime(){
		return startTime;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	public String getStartDate(){
		return startDate;
	}

	@Override
 	public String toString(){
		return 
			"DvrResponse{" + 
			"startTime = '" + startTime + '\'' + 
			",url = '" + url + '\'' + 
			",startDate = '" + startDate + '\'' + 
			"}";
		}
}
