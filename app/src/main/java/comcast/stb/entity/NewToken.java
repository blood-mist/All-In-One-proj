package comcast.stb.entity;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class NewToken{

	@SerializedName("token")
	private String token;

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}
}