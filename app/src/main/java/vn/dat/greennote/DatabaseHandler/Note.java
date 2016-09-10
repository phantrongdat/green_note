package vn.dat.greennote.DatabaseHandler;

/**
 * Created by Alone on 04/11/2016.
 */
public class Note {
    private int id;
    private String title;
    private String content;
    private String date;
    private String location;
    private String imgResource;


    public Note(int id, String title, String content, String date, String location, String imgResource) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.location = location;
        this.imgResource = imgResource;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getImgResource() {
        return imgResource;
    }

    public void setImgResource(String imgResource) {
        this.imgResource = imgResource;
    }
}
