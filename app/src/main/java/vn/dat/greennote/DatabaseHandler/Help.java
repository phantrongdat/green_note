package vn.dat.greennote.DatabaseHandler;

/**
 * Created by Alone on 04/25/2016.
 */
public class Help {
    String title;
    String content;
    byte[] image;
    String imgResource;

    public Help(String imgResource, String title, String content) {
        this.imgResource = imgResource;
        this.title = title;
        this.content = content;
    }

    public String getImgResource() {
        return imgResource;
    }

    public void setImgResource(String imgResource) {
        this.imgResource = imgResource;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

}
