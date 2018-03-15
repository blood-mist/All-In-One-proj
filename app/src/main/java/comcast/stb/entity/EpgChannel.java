package comcast.stb.entity;

/**
 * Created by blood-mist on 3/8/18.
 */


import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class EpgChannel{

    @SerializedName("ChannelText")
    private ChannelText channelText;

    @SerializedName("Event")
    private List<EventItem> event;

    public void setChannelText(ChannelText channelText){
        this.channelText = channelText;
    }

    public ChannelText getChannelText(){
        return channelText;
    }

    public void setEvent(List<EventItem> event){
        this.event = event;
    }

    public List<EventItem> getEvent(){
        return event;
    }

    @Override
    public String toString(){
        return
                "Channel{" +
                        "channelText = '" + channelText + '\'' +
                        ",event = '" + event + '\'' +
                        "}";
    }
}
