package pro.phalfstudio.notice;

import static android.view.FrameMetrics.ANIMATION_DURATION;

import android.animation.Animator;
import android.animation.TimeInterpolator;
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
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import java.util.List;

import pro.phalfstudio.notice.adapter.NoticeRecyclerViewAdapter;
import pro.phalfstudio.notice.controller.DatabaseController;
import pro.phalfstudio.notice.database.LocalNotices;
import pro.phalfstudio.notice.net.LoadNetNotices;
import pro.phalfstudio.notice.utils.DisplayUtil;

public class AllNoticesFragment extends Fragment {
    private static final TimeInterpolator ANIMATION_INTERPOLATOR = new DecelerateInterpolator();
    NoticeRecyclerViewAdapter adapter;
    List<LocalNotices> localNotices;
    List<LocalNotices> newLocal;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private SearchView searchView;
    private View searchBar;
    private static final int ANIMATION_DURATION = 300; // 持续时间，单位为毫秒
    private NoticeRecyclerView recyclerView;
    int marginTop;
    boolean isSearchNow = false;

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
        searchBar = view.findViewById(R.id.searchLayout);
        searchView = view.findViewById(R.id.AllNoticeSearchView);
        adapter = new NoticeRecyclerViewAdapter(getContext(), localNotices);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(false);
        searchView.clearFocus();
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                refreshNotices(true, query);
                isSearchNow = true;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    isSearchNow = false;
                    refreshNotices(false, "");
                }
                return false;
            }
        });
        refreshNotices(false, "");

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
                if (lastVisibleItemPosition == totalItemCount - 1 && currentPage < totalPages && !isSearchNow) {
                    //加载下一页
                    currentPage++;
                    loadNetNotices.loadNotice(currentPage);
                    refreshNotices(false, "");
                }
            }
        });
    }

    public void refreshNotices(boolean search, String searchString) {
        if (search) {
            newLocal = new DatabaseController(getContext()).searchNotice(searchString);
        } else {
            newLocal = new DatabaseController(getContext()).getAllNotices();
        }
        localNotices.clear();
        localNotices.addAll(newLocal);
        adapter.notifyDataSetChanged();
    }

}