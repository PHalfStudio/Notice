package pro.phalfstudio.notice;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pro.phalfstudio.notice.adapter.NoticeRecyclerViewAdapter;
import pro.phalfstudio.notice.controller.DatabaseController;
import pro.phalfstudio.notice.database.LocalNotices;
import pro.phalfstudio.notice.utils.TimeUtil;

public class TodayNoticesFragment extends Fragment {
    private List<LocalNotices> localNotices;
    NoticeRecyclerViewAdapter adapter;
    private NoticeRecyclerView recyclerView;

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
    }
}