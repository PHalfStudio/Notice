package pro.phalfstudio.notice;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import pro.phalfstudio.notice.adapter.NoticeRecyclerViewAdapter;
import pro.phalfstudio.notice.controller.DatabaseController;
import pro.phalfstudio.notice.database.LocalNotices;
import pro.phalfstudio.notice.net.LoadNetNotices;
import pro.phalfstudio.notice.utils.TimeUtil;

public class TodayNoticesFragment extends Fragment {
    List<LocalNotices> newLocal;
    private List<LocalNotices> localNotices;
    NoticeRecyclerViewAdapter adapter;
    private NoticeRecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    public static TodayNoticesFragment newInstance() {
        return new TodayNoticesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notices_today, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseController databaseController = new DatabaseController(getContext());
        localNotices = databaseController.findNoticeByDate(TimeUtil.getTodayDate());
        adapter = new NoticeRecyclerViewAdapter(getContext(), localNotices);
        recyclerView = view.findViewById(R.id.TodayNoticeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout = view.findViewById(R.id.todaySwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String url = getString(R.string.main_url);
                LoadNetNotices loadNetNotices = new LoadNetNotices(url, getContext());
                loadNetNotices.loadNotice(1,true);
                refreshNotices(false, "");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    public void refreshNotices(boolean search, String searchString) {
        if (search) {
            newLocal = new DatabaseController(getContext()).searchNotice(searchString);
            if(newLocal.size() == 0){
                Toast.makeText(getContext(), "没有找到相关内容", Toast.LENGTH_SHORT).show();
            }
        } else {
            newLocal = new DatabaseController(getContext()).findNoticeByDate(TimeUtil.getTodayDate());
        }
        localNotices.clear();
        localNotices.addAll(newLocal);
        adapter.notifyDataSetChanged();
    }
}