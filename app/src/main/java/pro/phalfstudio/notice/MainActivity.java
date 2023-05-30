package pro.phalfstudio.notice;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.TextView;

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

/*        SearchView searchView = findViewById(R.id.NoticeSearchView);
        searchView.setIconifiedByDefault(false); // 设置为false，使SearchView一开始就展开
        searchView.setFocusable(false); // 设置为false，使SearchView不可获取焦点
        searchView.clearFocus(); // 清除SearchView的焦点
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });*/
        TextView textView1 = findViewById(R.id.TodayNoticeBtn);
        TextView textView2 = findViewById(R.id.AllNoticeBtn);
        View redLine = findViewById(R.id.red_line);
        textView1.setOnClickListener(v -> moveRedLineAnimation(textView1, redLine, textView2));
        textView2.setOnClickListener(v -> moveRedLineAnimation(textView2, redLine, textView1));
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