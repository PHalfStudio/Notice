package pro.phalfstudio.notice;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class NoticeRecyclerView extends RecyclerView {
    private int mFirstY;
    public NoticeRecyclerView(@NonNull Context context) {
        super(context);
    }

    public NoticeRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoticeRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if(e.getAction()==MotionEvent.ACTION_DOWN){mFirstY=(int)e.getY();}
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public int getTouchPointY() {
        return mFirstY;
    }
}
