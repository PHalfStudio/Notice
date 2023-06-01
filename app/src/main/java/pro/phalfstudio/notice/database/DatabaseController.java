package pro.phalfstudio.notice.database;

import android.content.Context;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import pro.phalfstudio.notice.bean.NetBackNotices;

public class DatabaseController {
    NoticesDatabase database;

    public DatabaseController(Context context) {
        database = NoticesDatabase.getDatabase(context);
    }

    public boolean addNotices(List<NetBackNotices.Record> records){
        AtomicInteger testID = new AtomicInteger();
        Thread a = new Thread(() -> {
            for (NetBackNotices.Record record : records) {
                String title = record.getTitle();
                String body = record.getBody();
                String url = record.getUrl();
                String date = record.getDate();
                String time = record.getTime();
                int id = record.getId();
                String[] images = record.getImages();
                String[] append = record.getAppend();
                Map<String,String> appendMap = record.getAppendMap();
                String[] imagesArray = record.getImagesArray();
                database.noticeDao().insertNotice(title, body, url, date, time, id ,images, append, appendMap, imagesArray);
                testID.set(id);
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return findNoticeById(testID.get()) != null;
    }

    public List<LocalNotices> showAllNotices() {
        AtomicReference<List<LocalNotices>> cache = null;
        Thread a = new Thread(() -> {
            cache.set(database.noticeDao().showAllNotices());
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return cache.get();
    }

    public int allNoticeNum() {
        AtomicReference<Integer> cache = null;
        Thread a = new Thread(() -> {
            cache.set(database.noticeDao().allNoticeNum());
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return cache.get();
    }

    public List<LocalNotices> searchNotice(String search) {
        AtomicReference<List<LocalNotices>> cache = null;
        Thread a = new Thread(() -> {
            cache.set(database.noticeDao().searchNotice("%"+search+"%"));
        });
        return cache.get();
    }

    public boolean selectStatusById(String notice_id) {
        AtomicBoolean cacheBoolean = null;
        Thread a = new Thread(() -> {
            cacheBoolean.set(database.noticeDao().selectStatusById(notice_id));
        });
        return cacheBoolean.get();
    }

    public void chanceStatusById(boolean status,String notice_id) {
        Thread a = new Thread(() -> {
            database.noticeDao().chanceStatusById(status,notice_id);
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public LocalNotices findNoticeById(int id) {
        AtomicReference<LocalNotices> cache = null;
        Thread a = new Thread(() -> {
            cache.set(database.noticeDao().findNoticeById(id));
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return cache.get();
    }
}
