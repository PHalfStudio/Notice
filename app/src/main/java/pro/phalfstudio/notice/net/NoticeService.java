package pro.phalfstudio.notice.net;

import okhttp3.RequestBody;

import pro.phalfstudio.notice.bean.NetBackNotices;
import pro.phalfstudio.notice.bean.NetBackOneNotice;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NoticeService {
    @POST("api/notices")
    Call<NetBackNotices> getNotice(@Body RequestBody request);

    @GET("api/notices")
    Call<NetBackOneNotice> getNewOneNotice();

    @POST("api/notices")
    Call<NetBackNotices> searchNotice(@Body RequestBody request);

    @POST("api/update/android")
    Call<NetBackOneNotice> checkUpdate();
}
