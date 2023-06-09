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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
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
    SwipeRefreshLayout swipeRefreshLayout;
    private NoticeRecyclerView recyclerView;
    private static final TimeInterpolator ANIMATION_INTERPOLATOR = new DecelerateInterpolator();
    private SearchView searchView;
    private View searchBar;
    private static final int ANIMATION_DURATION = 300; // 持续时间，单位为 毫秒
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
        searchBar = view.findViewById(R.id.searchLayout);
        searchView = view.findViewById(R.id.AllNoticeSearchView);
        recyclerView = view.findViewById(R.id.AllNoticeRecyclerView);
        adapter = new NoticeRecyclerViewAdapter(getContext(), localNotices);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(false);
        searchView.clearFocus();
        int noticeNum = databaseController.getAllNoticesNumber();
        searchView.setQueryHint("搜索本地"+noticeNum+"条通知");
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                boolean isNum = query.matches("\\d+");
                if(isNum){
                    int searchNoticeID = Integer.parseInt(query);
                    refreshNotices(true, "",searchNoticeID);
                }else{
                    refreshNotices(true, query,0);
                }
                isSearchNow = true;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    isSearchNow = false;
                    refreshNotices(false, "",0);
                }
                return false;
            }
        });
        refreshNotices(false, "",0);

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
                    loadNetNotices.loadNotice(currentPage,false);
                    refreshNotices(false, "",0);
                }
            }
        });
        swipeRefreshLayout = view.findViewById(R.id.allSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String url = getString(R.string.main_url);
                LoadNetNotices loadNetNotices = new LoadNetNotices(url, getContext());
                loadNetNotices.loadNotice(1,true);
                refreshNotices(false, "",0);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void refreshNotices(boolean search, String searchString , int NoticeID) {
        if (search) {
            if(NoticeID != 0){
                newLocal = new ArrayList<>();
                newLocal.add(new DatabaseController(getContext()).findNoticeById(NoticeID));
            }else{
                newLocal = new DatabaseController(getContext()).searchNotice(searchString);
            }
            if(newLocal.size() == 0){
                Toast.makeText(getContext(), "没有找到相关内容", Toast.LENGTH_SHORT).show();
            }
        } else {
            newLocal = new DatabaseController(getContext()).getAllNotices();
        }
        localNotices.clear();
        localNotices.addAll(newLocal);
        adapter.notifyDataSetChanged();
    }

}