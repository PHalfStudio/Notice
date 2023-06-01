package pro.phalfstudio.notice.database;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;
import java.util.Map;

@Dao
public interface NoticeDao {
    @Query("INSERT INTO localnotices (title, body, url, date, time, notice_id,images,append,appendMap,imagesArray) VALUES (:title, :body, :url, :date, :time, :id,:images,:append,:appendMap,:imagesArray)")
    void insertNotice(String title , String body , String url , String date , String time , int id
            , String[] images, String[] append, Map<String,String> appendMap,String[] imagesArray);

    @Query("DELETE FROM localnotices WHERE notice_id = :id")
    void deleteNoticeById(int id);

    @Query("UPDATE localnotices SET title=:title,body=:body,url=:url,date=:date,time=:time WHERE notice_id = :noticeID")
    void updateNoticeById(int noticeID,String title , String body , String url , String date , String time);

    @Query("SELECT * FROM localnotices WHERE notice_id = :id")
    LocalNotices findNoticeById(int id);

    @Query("SELECT * FROM localnotices WHERE date = :date")
    List<LocalNotices> findNoticeByDate(String date);

    @Query("SELECT * FROM localnotices ORDER BY notice_id DESC")
    List<LocalNotices> showAllNotices();

    @Query("SELECT count(*) FROM localnotices")
    int allNoticeNum();

    @Query("SELECT * FROM localnotices WHERE title LIKE :search")
    List<LocalNotices> searchNotice(String search);

    @Query("SELECT status FROM localnotices WHERE notice_id = :notice_id")
    boolean selectStatusById(String notice_id);

    @Query("UPDATE localnotices SET status=:status WHERE notice_id = :notice_id")
    void chanceStatusById(boolean status,String notice_id);
}
