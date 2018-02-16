package comcast.stb.entity;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

import io.realm.RealmObject;

@Generated("com.robohorse.robopojogenerator")
public class LoginData extends RealmObject {

    @SerializedName("user")
    private User user;

    @SerializedName("token")
    private String token;

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }

    @Override
    public String toString(){
        return
                "LoginData{" +
                        "user = '" + user + '\'' +
                        ",token = '" + token + '\'' +
                        "}";
    }
}

