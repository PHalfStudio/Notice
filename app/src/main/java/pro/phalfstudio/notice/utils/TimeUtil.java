package pro.phalfstudio.notice.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    private static final long ONE_MINUTE = 60 * 1000;
    private static final long ONE_HOUR = 60 * ONE_MINUTE;
    private static final long ONE_DAY = 24 * ONE_HOUR;

    public static String formatTime(String dateTimeString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd/HH:mm", Locale.getDefault());
        try {
            Calendar calendar = Calendar.getInstance();
            int NoticeYear = Integer.parseInt(dateTimeString.substring(0, 4));
            int NowYear = calendar.get(Calendar.YEAR);
            int NoticeDay = Integer.parseInt(dateTimeString.substring(8, 10));
            int NowDay = calendar.get(Calendar.DAY_OF_MONTH);
            Date date = format.parse(dateTimeString);
            long currentTime = calendar.getTimeInMillis();
            calendar.setTime(date);
            long targetTime = calendar.getTimeInMillis();
            long timeDifference = currentTime - targetTime;
            if (timeDifference < ONE_HOUR) {
                long minutes = timeDifference / ONE_MINUTE;
                return minutes + "分钟前";
            } else if (timeDifference > ONE_HOUR & timeDifference < ONE_DAY & NoticeDay == NowDay) {
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                return formatTimeDigit(hourOfDay) + ":" + formatTimeDigit(minute);
            }  else if (NoticeDay < NowDay) {
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                return "昨天 " + formatTimeDigit(hourOfDay) + ":" + formatTimeDigit(minute);
            } else if (timeDifference > 2 * ONE_DAY & NoticeYear == NowYear) {
                int month = calendar.get(Calendar.MONTH) + 1;
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                return month + "月" + dayOfMonth + "日";
            }else if(NoticeYear < NowYear){
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                return year + "年" + month + "月" + dayOfMonth + "日";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(calendar.getTime());
    }

    private static String formatTimeDigit(int time) {
        return time < 10 ? "0" + time : String.valueOf(time);
    }
}


