package pro.phalfstudio.notice.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import pro.phalfstudio.notice.bean.NetBackNotices;
import pro.phalfstudio.notice.bean.NetBackOneNotice;
import pro.phalfstudio.notice.controller.DatabaseController;
import pro.phalfstudio.notice.database.LocalNotices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadNetNotices {
    Retrofit retrofit;
    NoticeService noticeService;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Call<NetBackNotices> getNotice;
    List<NetBackNotices.Record> records;
    DatabaseController databaseController;
    Context Allcontext;

    public LoadNetNotices(String url, Context context) {
        Allcontext = context;
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        noticeService = retrofit.create(NoticeService.class);
        databaseController = new DatabaseController(context);
        sharedPreferences = context.getSharedPreferences("notice", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void loadNotice(int currentPage , boolean isRefresh){
        String current = String.valueOf(currentPage);
        JSONObject json = new JSONObject();
        if(isRefresh){
            try {
                JSONObject page = new JSONObject();
                page.put("current", "1");
                page.put("size", "100");
                json.put("page", page);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                JSONObject page = new JSONObject();
                page.put("current", current);
                page.put("size", "100");
                json.put("page", page);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json.toString());
        Call<NetBackNotices> getNotice = noticeService.getNotice(requestBody);
        getNotice.enqueue(new Callback<NetBackNotices>() {
            @Override
            public void onResponse(Call<NetBackNotices> call, Response<NetBackNotices> response) {
                if(response.isSuccessful()){
                    NetBackNotices notices = response.body();
                    editor.putInt("totalPages", notices.getData().getPages());
                    editor.commit();
                    records = notices.getData().getRecords();
                    if(isRefresh){
                        int refreshNum = 0;
                        List<LocalNotices> localNotices = databaseController.getAllNotices();
                        for (int i = 0;; i++) {
                            if(records.get(i).getId() > localNotices.get(0).noticeID){
                                databaseController.insertNotice(records.get(i));
                                refreshNum++;
                            }else{
                                break;
                            }
                        }
                        if(refreshNum > 0){
                            Toast.makeText(Allcontext, "奇怪的通知增加了"+refreshNum+"条", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if(databaseController.addNotices(records)){
                            Toast.makeText(Allcontext, "数据加载成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<NetBackNotices> call, Throwable t) {
                System.out.println(t);
                Pattern pattern = Pattern.compile("^.{6}");
                Matcher matcher = pattern.matcher(t.getMessage());
                if (matcher.find()) {
                    String back = matcher.group();
                    if (back.equals("Failed")) {
                        Toast.makeText(Allcontext, "小水管服务器炸了，正在修复，请稍后尝试...", Toast.LENGTH_LONG).show();
                    } else if (back.equals("Unable")) {
                        Toast.makeText(Allcontext, "加载失败！请检查您的网络连接后重试...", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public boolean getNetAndCompare(){
        final Boolean[] cache = {false};
        noticeService.getNewOneNotice().enqueue(new Callback<NetBackOneNotice>() {
            @Override
            public void onResponse(Call<NetBackOneNotice> call, Response<NetBackOneNotice> response) {
                NetBackOneNotice back = response.body();
                int newID = back.getData().getLatestNotice().getId();
                int lastID = databaseController.getLastNoticeID();
                if (newID > lastID) {
                    cache[0] = true;
                }
            }

            @Override
            public void onFailure(Call<NetBackOneNotice> call, Throwable t) {
                System.out.println("fail");
            }
        });
        return cache[0];
    }
}
