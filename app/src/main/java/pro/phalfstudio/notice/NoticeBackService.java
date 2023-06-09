package pro.phalfstudio.notice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

import pro.phalfstudio.notice.net.LoadNetNotices;

public class NoticeBackService extends Service {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    NotificationManager notificationManager;
    Notification notification;

    LoadNetNotices loadNetNotices;

    public NoticeBackService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String url = getString(R.string.main_url);
        loadNetNotices = new LoadNetNotices(url,getBaseContext());
        sharedPreferences = getApplication().getSharedPreferences("notice_settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notice 「Notice」 Channel";
            String description = "Coder在敲下这行代码时有点Sad";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("noticeChannel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        notification = new NotificationCompat.Builder(this, "noticeChannel")
                .setContentTitle("学校发布了新消息")
                .setContentText("点击通知查看详情")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timer time = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                boolean isNotification = sharedPreferences.getBoolean("isNotification", false);
                if (!isNotification & loadNetNotices.getNetAndCompare()) {
                    notificationManager.notify(1, notification);
                    editor.putBoolean("isNotification", true);
                    editor.apply();
                }
            }
        };
        time.schedule(task, 0, 30*60*1000);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}