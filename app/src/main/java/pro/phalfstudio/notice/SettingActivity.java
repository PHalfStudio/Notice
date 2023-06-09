package pro.phalfstudio.notice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sharedPreferences = getSharedPreferences("notice_settings", MODE_PRIVATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        Switch openDeviceNotice = findViewById(R.id.OpenDeviceNotice);
        boolean isNoticeServiceOn = sharedPreferences.getBoolean("NoticeService", false);
        openDeviceNotice.setChecked(isNoticeServiceOn);

        openDeviceNotice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setSwitchAnimation(event,openDeviceNotice);
                return false;
            }
        });
        openDeviceNotice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("NoticeService", isChecked);
                editor.apply();
            }
        });
        Switch runToAmerica = findViewById(R.id.RunToAmerica);
        runToAmerica.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setSwitchAnimation(event,runToAmerica);
                return false;
            }
        });
        //装饰性代码部分--end
        LinearLayout userPrivate = findViewById(R.id.userPrivate);
        userPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,WebActivity.class);
                startActivity(intent);
            }
        });
        ImageButton back = findViewById(R.id.SettingBack);
        back.setOnClickListener((v)->{finish();});


    }
    final float thumbScale = 1.2f; // thumb 放大的比例
    final int trackDarkColor = Color.parseColor("#0c6799"); // track 加深的颜色
    //装饰性代码部分--start
    public void setSwitchAnimation(MotionEvent event,Switch switchButton) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 开始按下时，设置 thumb 放大和 track 颜色加深效果
                ScaleAnimation scaleAnimation = new ScaleAnimation(1f, thumbScale, 1f, thumbScale,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(200);
                switchButton.getThumbDrawable().setAlpha(255);
                switchButton.startAnimation(scaleAnimation);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 手指抬起或取消时，恢复原始状态
                ScaleAnimation reverseAnimation = new ScaleAnimation(thumbScale, 1f, thumbScale, 1f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                reverseAnimation.setDuration(200);
                switchButton.getThumbDrawable().setAlpha(255);
                switchButton.startAnimation(reverseAnimation);
                break;
        }
    }
}
