package pro.phalfstudio.notice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import pro.phalfstudio.notice.adapter.NoticePagerAdapter;
import pro.phalfstudio.notice.controller.DatabaseController;
import pro.phalfstudio.notice.net.LoadNetNotices;

public class MainActivity extends AppCompatActivity {

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
        String url = getString(R.string.main_url);
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
}