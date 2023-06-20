package pro.phalfstudio.notice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;

import pro.phalfstudio.notice.adapter.NoticePagerAdapter;
import pro.phalfstudio.notice.bean.Update;
import pro.phalfstudio.notice.controller.DatabaseController;
import pro.phalfstudio.notice.net.CheckUpdate;
import pro.phalfstudio.notice.net.LoadNetNotices;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Boolean isDialogShow;
    private Boolean isDialogShowing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 设置状态栏为白色背景和白色字体
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        sharedPreferences = getApplication().getSharedPreferences("notice_settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("isNotification",false);
        editor.apply();
        //初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), "5fe8557d90", false);
        //隐私政策
        isDialogShow = sharedPreferences.getBoolean("isDialogShow",true);
        isDialogShowing = false;
        showUserAgreeDialog();
        //检查更新
        if(sharedPreferences.getBoolean("NoticeService",false)){
            Intent intent = new Intent(this, NoticeBackService.class);
            startService(intent);
        }
        //
        String url = getString(R.string.main_url);
        CheckUpdate check = new CheckUpdate(url,this,false);
        check.checkAppUpdate();
        DatabaseController databaseController = new DatabaseController(getBaseContext());
        LoadNetNotices loadNetNotices = new LoadNetNotices(url, getBaseContext());
        if(databaseController.getAllNotices().size() == 0){
            loadNetNotices.loadNotice(1,false);
        }else{
            loadNetNotices.loadNotice(1,true);
        }
        TextView textView1 = findViewById(R.id.TodayNoticeBtn);
        TextView textView2 = findViewById(R.id.AllNoticeBtn);
        View redLine = findViewById(R.id.red_line);
        ViewPager2 viewPager = findViewById(R.id.MainViewPager);
        viewPager.setAdapter(new NoticePagerAdapter(this,getLifecycle()));
        textView1.setOnClickListener(v -> {
            moveRedLineAnimation(textView1, redLine, textView2);
            viewPager.setCurrentItem(0);
        });
        textView2.setOnClickListener(v -> {
            moveRedLineAnimation(textView2, redLine, textView1);
            viewPager.setCurrentItem(1);
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position == 0){
                    moveRedLineAnimation(textView1, redLine, textView2);
                }else{
                    moveRedLineAnimation(textView2, redLine, textView1);
                }
            }
        });
        ImageButton setting = findViewById(R.id.AppSetting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting = new Intent(getApplicationContext(),SettingActivity.class);
                startActivity(setting);
            }
        });
    }

    private void moveRedLineAnimation(final TextView targetView, final View redLine, final TextView noActiveView) {
        final int startColor = Color.parseColor("#707070");
        final int endColor = Color.parseColor("#000000");
        final float startX = redLine.getX();
        final float endX = targetView.getX() + (targetView.getWidth() - redLine.getWidth()) / 2;

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(300);
        animator.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            int currentColor = (int) new ArgbEvaluator().evaluate(fraction, startColor, endColor);
            targetView.setTextColor(currentColor);
            noActiveView.setTextColor((int) new ArgbEvaluator().evaluate(fraction, endColor, startColor));
            float currentX = startX + fraction * (endX - startX);
            redLine.setX(currentX);
        });
        animator.start();
    }
    private void showUserAgreeDialog(){
        if(isDialogShow & !isDialogShowing){
            isDialogShowing = true;
            Intent intent = new Intent(this, WebActivity.class);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("用户协议")
                    .setMessage("使用本应用前，请阅读并同意我们的用户协议")
                    .setCancelable(false)
                    .setNeutralButton("用户协议" , (dialog3, which) -> {
                        startActivity(intent);
                        isDialogShowing = false;
                        dialog3.dismiss();
                    })
                    .setPositiveButton("同意", (dialog1, which) -> {
                        editor.putBoolean("isDialogShow",false);
                        editor.apply();
                        isDialogShow = false;
                        dialog1.dismiss();
                    })
                    .setNegativeButton("退出", (dialog2, which) -> {
                        finish();
                    })
                    .create();
            dialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showUserAgreeDialog();
    }
}