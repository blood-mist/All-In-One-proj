package comcast.stb.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

import io.realm.RealmObject;

@Generated("com.robohorse.robopojogenerator")
public class User extends RealmObject {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mac_address")
    @Expose
    private String macAddress;
    @SerializedName("user_stb_id")
    @Expose
    private Integer userStbId;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("group_id")
    @Expose
    private Integer groupId;
    @SerializedName("balance")
    @Expose
    private String balance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Integer getUserStbId() {
        return userStbId;
    }

    public void setUserStbId(Integer userStbId) {
        this.userStbId = userStbId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

}
