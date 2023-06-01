package pro.phalfstudio.notice.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Map;

@Entity
public class LocalNotices {
    @PrimaryKey
    public int id;
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
    private String[] images;
    @ColumnInfo(name = "append")
    private String[] append;
    @ColumnInfo(name = "appendMap")
    private Map<String,String> appendMap;
    @ColumnInfo(name = "imagesArray")
    private String[] imagesArray;
}
