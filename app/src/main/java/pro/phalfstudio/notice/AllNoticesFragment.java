package pro.phalfstudio.notice;

import android.animation.ValueAnimator;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import java.util.List;

import pro.phalfstudio.notice.adapter.NoticeRecyclerViewAdapter;
import pro.phalfstudio.notice.database.DatabaseController;
import pro.phalfstudio.notice.database.LocalNotices;
import pro.phalfstudio.notice.net.LoadNetNotices;

public class AllNoticesFragment extends Fragment {
    NoticeRecyclerViewAdapter adapter;
    List<LocalNotices> localNotices;
    List<LocalNotices> newLocal;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private SearchView searchView;
    private RecyclerView recyclerView;
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
}