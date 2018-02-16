package comcast.stb.entity;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class UserInfo{

	@SerializedName("user")
	private User userData;

	public void setUserData(User userData){
		this.userData = userData;
	}

	public User getUserData(){
		return userData;
	}

	@Override
 	public String toString(){
		return 
			"UserInfo{" + 
			"userData = '" + userData + '\'' +
			"}";
		}
}