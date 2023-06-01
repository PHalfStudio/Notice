package pro.phalfstudio.notice.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {LocalNotices.class},version = 1)
public abstract class NoticesDatabase extends RoomDatabase {
    private static volatile NoticesDatabase INSTANCE;
    public abstract NoticeDao noticeDao();
    public static NoticesDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (NoticesDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context, NoticesDatabase.class, "notices").build();
                }
            }
        }
        return INSTANCE;
    }
}
