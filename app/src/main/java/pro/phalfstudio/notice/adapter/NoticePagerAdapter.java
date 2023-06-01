package pro.phalfstudio.notice.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import pro.phalfstudio.notice.AllNoticesFragment;
import pro.phalfstudio.notice.TodayNoticesFragment;

public class NoticePagerAdapter extends FragmentStateAdapter {
    public NoticePagerAdapter(FragmentActivity fragmentActivity, Lifecycle lifecycle) {
        super(fragmentActivity.getSupportFragmentManager(), lifecycle);
    }
    private TodayNoticesFragment fragment1;
    private AllNoticesFragment fragment2;

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                if (fragment1 == null) {
                    fragment1 = new TodayNoticesFragment();
                }
                return fragment1;
            case 1:
                if (fragment2 == null) {
                    fragment2 = new AllNoticesFragment();
                }
                return fragment2;
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2; // 总共有两个页面
    }
}
