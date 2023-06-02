package pro.phalfstudio.notice.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.RoomDatabase;

import java.util.Map;

@Entity
public class LocalNotices {
    @PrimaryKey
    public int id;
    @ColumnInfo(name = "status")
    public boolean status;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "body")
    public String body;
    @ColumnInfo(name = "url")
    public String url;
    @ColumnInfo(name = "date")
    public String date;
    @ColumnInfo(name = "time")
    public String time;
    @ColumnInfo(name = "notice_id")
    public int noticeID;
    @ColumnInfo(name = "images")
    private String images;
    @ColumnInfo(name = "append")
    private String append;
    @ColumnInfo(name = "appendMap")
    private String appendMap;
    @ColumnInfo(name = "imagesArray")
    private String imagesArray;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNoticeID() {
        return noticeID;
    }

    public void setNoticeID(int noticeID) {
        this.noticeID = noticeID;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getAppend() {
        return append;
    }

    public void setAppend(String append) {
        this.append = append;
    }

    public String getAppendMap() {
        return appendMap;
    }

    public void setAppendMap(String appendMap) {
        this.appendMap = appendMap;
    }

    public String getImagesArray() {
        return imagesArray;
    }

    public void setImagesArray(String imagesArray) {
        this.imagesArray = imagesArray;
    }
}
