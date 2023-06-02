package pro.phalfstudio.notice;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import java.util.List;

import pro.phalfstudio.notice.adapter.NoticeRecyclerViewAdapter;
import pro.phalfstudio.notice.controller.DatabaseController;
import pro.phalfstudio.notice.database.LocalNotices;
import pro.phalfstudio.notice.net.LoadNetNotices;
import pro.phalfstudio.notice.utils.DisplayUtil;

public class AllNoticesFragment extends Fragment {
    NoticeRecyclerViewAdapter adapter;
    List<LocalNotices> localNotices;
    List<LocalNotices> newLocal;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private SearchView searchView;
    private View searchBar;
    private NoticeRecyclerView recyclerView;
    int mFirstY, mCurrentY, marginTop;
    boolean direction;
    public static AllNoticesFragment newInstance() {
        return new AllNoticesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_notices, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences("notice", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String url = getString(R.string.main_url);
        DatabaseController databaseController = new DatabaseController(getContext());
        LoadNetNotices loadNetNotices = new LoadNetNotices(url, getContext());
        localNotices = databaseController.getAllNotices();
        recyclerView = view.findViewById(R.id.AllNoticeRecyclerView);
        searchView = view.findViewById(R.id.AllNoticeSearchView);
        adapter = new NoticeRecyclerViewAdapter(getContext(), localNotices);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        refreshNotices();
        initView();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                //获取item的总数
                int totalItemCount = layoutManager.getItemCount();
                int currentPage = databaseController.getCurrentNumber();
                int totalPages = sharedPreferences.getInt("totalPages", 0);
                if (lastVisibleItemPosition == totalItemCount - 1 && currentPage < totalPages) {
                    //加载下一页
                    currentPage++;
                    loadNetNotices.loadNotice(currentPage);
                    refreshNotices();
                }
            }
        });
    }

    public void refreshNotices() {
        newLocal = new DatabaseController(getContext()).getAllNotices();
        localNotices.clear();
        localNotices.addAll(newLocal);
        adapter.notifyDataSetChanged();
    }
    private void initView() {
        marginTop = DisplayUtil.dip2px(getContext(), 50);
        searchBar = getView().findViewById(R.id.searchLayout);

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            private float mFirstY;
            private float mCurrentY;
            private boolean direction;
            private boolean isAnimateRunning = false;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mFirstY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurrentY = motionEvent.getY();
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) searchBar.getLayoutParams();

                        if (mCurrentY - mFirstY > 0) {
                            direction = false; // 向下滑动
                        } else {
                            direction = true; // 向上滑动
                        }

                        if (direction) {
                            if (layoutParams.topMargin > -marginTop) {
                                layoutParams.topMargin += mCurrentY - mFirstY;
                                if (layoutParams.topMargin < -marginTop) {
                                    layoutParams.topMargin = -marginTop;
                                }
                                searchBar.requestLayout();
                            }
                        } else {
                            if (layoutParams.topMargin < 0) {
                                layoutParams.topMargin += mCurrentY - mFirstY;
                                if (layoutParams.topMargin > 0) {
                                    layoutParams.topMargin = 0;
                                }
                                searchBar.requestLayout();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (direction && !isAnimateRunning) {
                            // 向上滑动时，执行动画将搜索框显示出来
                            animateSearchBar(0);
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void animateSearchBar(final int targetMargin) {
        final boolean[] isAnimateRunning = new boolean[1];
        final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) searchBar.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(layoutParams.topMargin, targetMargin);
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                layoutParams.topMargin = (int) valueAnimator.getAnimatedValue();
                searchBar.requestLayout();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isAnimateRunning[0] = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimateRunning[0] = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                isAnimateRunning[0] = false;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.start();
    }

}