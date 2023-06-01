package pro.phalfstudio.notice.net;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pro.phalfstudio.notice.bean.NetBackNotices;
import pro.phalfstudio.notice.bean.NetBackNotices.Record;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadNetNotices {
    Retrofit retrofit;
    NoticeService noticeService;
    Call<NetBackNotices> getNotice;

    public LoadNetNotices(String url) {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        noticeService = retrofit.create(NoticeService.class);
    }

    public List<Record> load(int currentPage){
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

            }

            @Override
            public void onFailure(Call<NetBackNotices> call, Throwable t) {

            }
        });
        return null;
    }
}
