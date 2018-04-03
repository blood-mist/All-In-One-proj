package comcast.stb.valueAddedPackages;

import java.util.ArrayList;

public class PackageEntity {

    private String title;

    private String message;


    public PackageEntity(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public PackageEntity() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
