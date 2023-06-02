package pro.phalfstudio.notice;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pro.phalfstudio.notice.adapter.NoticeRecyclerViewAdapter;
import pro.phalfstudio.notice.database.DatabaseController;
import pro.phalfstudio.notice.database.LocalNotices;
import pro.phalfstudio.notice.net.LoadNetNotices;

public class AllNoticesFragment extends Fragment {
    NoticeRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    List<LocalNotices> localNotices;

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
        String url = getString(R.string.main_url);
        LoadNetNotices loadNetNotices = new LoadNetNotices(url, getContext());
        DatabaseController databaseController = new DatabaseController(getContext());
        loadNetNotices.loadNotice(1);
        localNotices = databaseController.showAllNotices();
        recyclerView = view.findViewById(R.id.AllNoticeRecyclerView);
        adapter = new NoticeRecyclerViewAdapter(getContext(), localNotices);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}