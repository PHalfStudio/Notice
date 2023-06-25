package pro.phalfstudio.notice.net;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

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
    private String longVersion;
    private String appLongVersion;
    private String appVersion;
    private String date;
    private String dlUrl;
    private String description;

    public CheckUpdate(String url, Context context, boolean userCheck) {
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

    public void checkAppUpdate() {
        Call<Update> checkUpdate = noticeService.checkUpdate();
        checkUpdate.enqueue(new Callback<Update>() {
            @Override
            public void onResponse(Call<Update> call, Response<Update> response) {
                if (response.isSuccessful()) {
                    appVersion = response.body().getData().getVersion();
                    date = response.body().getData().getDate();
                    longVersion = String.valueOf(response.body().getData().getVersions());
                    dlUrl = response.body().getData().getUrl();
                    description = response.body().getData().getDescription();
                } else {
                    date = null;
                    longVersion = null;
                    dlUrl = null;
                    description = null;
                }
                if (date != null && longVersion != null && dlUrl != null && description != null) {
                    if (userCheck) {
                        appLongVersion = context.getString(R.string.number_version_code);
                        if (Integer.parseInt(longVersion) > Integer.parseInt(appLongVersion)) {
                            int i = Integer.parseInt(longVersion.substring(7));
                            System.out.println(i);
                            AlertDialog dialog = new AlertDialog.Builder(context)
                                    .setTitle(appVersion+"版本更新")
                                    .setMessage("检测到新版本！更新信息如下："+description+"\n\n更新时间："+date)
                                    .setCancelable(false)
                                    .setPositiveButton("更新", (dialog1, which) -> {
                                        Intent openIntent = new Intent(Intent.ACTION_VIEW);
                                        openIntent.setData(Uri.parse(dlUrl));
                                        context.startActivity(openIntent);
                                    })
                                    .setNegativeButton("取消", (dialog2, which) -> {
                                        if(i == 0){
                                            dialog2.dismiss();
                                        }else if(i == 1){
                                            Toast.makeText(context, "本版本为强制更新，请更新至最新版本！", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .create();
                            dialog.show();
                        }else{
                            Toast.makeText(context, "应用为最新版本！", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        appLongVersion = context.getString(R.string.number_version_code);
                        if (Integer.parseInt(longVersion) > Integer.parseInt(appLongVersion)) {
                            int i = Integer.parseInt(longVersion.substring(7));
                            AlertDialog dialog = new AlertDialog.Builder(context)
                                    .setTitle(appVersion+"版本更新")
                                    .setMessage("检测到新版本！更新信息如下："+"\n\n"+description+"\n\n更新时间："+date)
                                    .setPositiveButton("更新", (dialog1, which) -> {
                                        Intent openIntent = new Intent(Intent.ACTION_VIEW);
                                        openIntent.setData(Uri.parse(dlUrl));
                                        context.startActivity(openIntent);
                                    })
                                    .setNegativeButton("取消", (dialog2, which) -> {
                                        if(i == 0){
                                            dialog2.dismiss();
                                        }else if(i == 1){
                                            Toast.makeText(context, "本版本为强制更新，请更新至最新版本！", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .create();
                            dialog.show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Update> call, Throwable t) {
                date = null;
                longVersion = null;
                dlUrl = null;
                description = null;
            }
        });
    }
}
