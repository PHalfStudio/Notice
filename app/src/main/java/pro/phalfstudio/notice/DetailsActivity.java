package pro.phalfstudio.notice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hutool.json.JSONUtil;
import pro.phalfstudio.notice.controller.DatabaseController;
import pro.phalfstudio.notice.database.LocalNotices;
import pro.phalfstudio.notice.utils.StringUtil;

public class DetailsActivity extends AppCompatActivity {
    TextView title, id, date;
    ImageButton backUP, share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detials);
        title = findViewById(R.id.DetailTitleTextview);
        id = findViewById(R.id.DetailIDTextview);
        date = findViewById(R.id.DetailDateTextview);
        // 设置状态栏为白色背景和白色字体
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int id = bundle.getInt("noticeID");
        String url = bundle.getString("noticeUrl");
        backUP = findViewById(R.id.BackUP);
        backUP.setOnClickListener(v -> finish());
        share = findViewById(R.id.Share);
        share.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("更多选项");
            builder.setItems(new CharSequence[]{"在浏览器打开链接", "分享链接给朋友"}, (dialog, which) -> {
                switch (which) {
                    case 0:
                        // 打开链接
                        Intent openIntent = new Intent(Intent.ACTION_VIEW);
                        openIntent.setData(Uri.parse(url));
                        startActivity(openIntent);
                        break;
                    case 1:
                        // 分享链接
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
                        startActivity(Intent.createChooser(shareIntent, "分享到"));
                        break;
                }
            });
            builder.show();
        });
        DatabaseController databaseController = new DatabaseController(getBaseContext());
        LocalNotices localNotices = databaseController.findNoticeById(id);
        title.setText(localNotices.getTitle());
        this.id.setText(String.valueOf(localNotices.getNoticeID()));
        date.setText(localNotices.getDate());
        ArrayList<String> imagesUrls = StringUtil.string2arraylist(localNotices.getImagesArray());
        String bodyText = localNotices.getBody();
        LinearLayout layout = findViewById(R.id.body_layout);
        for (String imageUrl : imagesUrls) {
            int startIndex = bodyText.indexOf("<images>");
            if (startIndex != -1) {
                TextView textView = new TextView(this);
                textView.setText(bodyText.substring(0, startIndex));
                textView.setTextColor(Color.parseColor("#000000")); // 设置字体颜色
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17); // 设置字体大小
                layout.addView(textView);
            }
            ImageView imageView = new ImageView(this);
            Glide.with(this).load(imageUrl).into(imageView);
            layout.addView(imageView);
            if (startIndex != -1) {
                bodyText = bodyText.substring(startIndex + "<images>".length());
            }
        }
        TextView textView = new TextView(this);
        textView.setText(bodyText);
        textView.setTextColor(Color.parseColor("#000000")); // 设置字体颜色
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17); // 设置字体大小
        layout.addView(textView);
    }
}