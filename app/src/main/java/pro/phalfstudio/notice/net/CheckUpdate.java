package pro.phalfstudio.notice.net;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import pro.phalfstudio.notice.R;
import pro.phalfstudio.notice.bean.Update;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
        Call<Update> checkUpdate = noticeService.checkUpdate();
        checkUpdate.enqueue(new Callback<Update>() {
            @Override
            public void onResponse(Call<Update> call, Response<Update> response) {
                if(response.isSuccessful()){
                    System.out.println(response.body().getData().getVersion());
                }
            }

            @Override
            public void onFailure(Call<Update> call, Throwable t) {

            }
        });
    }
}
