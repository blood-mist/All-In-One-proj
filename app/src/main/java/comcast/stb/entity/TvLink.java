package comcast.stb.entity;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class TvLink{

    @SerializedName("link")
    private String link;

    public void setLink(String link){
        this.link = link;
    }

    public String getLink(){
        return link;
    }
}