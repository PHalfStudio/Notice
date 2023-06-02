package pro.phalfstudio.notice.net;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import pro.phalfstudio.notice.bean.NetBackNotices;
import pro.phalfstudio.notice.bean.NetBackNotices.Record;
import pro.phalfstudio.notice.database.DatabaseController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadNetNotices {
    Retrofit retrofit;
    NoticeService noticeService;
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
    }

    public void loadNotice(int currentPage){
        String current = String.valueOf(currentPage);
        JSONObject json = new JSONObject();
        try {
            JSONObject page = new JSONObject();
            page.put("current", current);
            page.put("size", "100");
            json.put("page", page);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json.toString());
        Call<NetBackNotices> getNotice = noticeService.getNotice(requestBody);
        getNotice.enqueue(new Callback<NetBackNotices>() {
            @Override
            public void onResponse(Call<NetBackNotices> call, Response<NetBackNotices> response) {
                if(response.isSuccessful()){
                    NetBackNotices notices = response.body();
                    records = notices.getData().getRecords();
                    if(databaseController.addNotices(records)){
                        Toast.makeText(Allcontext, "数据加载成功", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<NetBackNotices> call, Throwable t) {
                System.out.println(t.getMessage());
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
}
