package comcast.stb.entity.events;

public class ChannelSwitch {
    private boolean upChannel;
    public ChannelSwitch(boolean upChannel) {
        this.upChannel=upChannel;
    }

    public boolean isUpChannel() {
        return upChannel;
    }

    public void setUpChannel(boolean upChannel) {
        this.upChannel = upChannel;
    }
}
