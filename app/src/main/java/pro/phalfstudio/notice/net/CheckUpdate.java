package pro.phalfstudio.notice.net;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import pro.phalfstudio.notice.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckUpdate {
    Retrofit retrofit;
    NoticeService noticeService;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private boolean userCheck;
    private String url;
    private Context context;

    public CheckUpdate(String url , Context context , boolean userCheck) {
        this.userCheck = userCheck;
        this.url = url;
        this.context = context;
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        noticeService = retrofit.create(NoticeService.class);
        sharedPreferences = context.getSharedPreferences("notice", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void checkAppUpdate(){
    }
}
