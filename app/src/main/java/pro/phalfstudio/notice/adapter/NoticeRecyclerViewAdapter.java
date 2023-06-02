package pro.phalfstudio.notice.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pro.phalfstudio.notice.DetailsActivity;
import pro.phalfstudio.notice.MainActivity;
import pro.phalfstudio.notice.R;
import pro.phalfstudio.notice.database.LocalNotices;
import pro.phalfstudio.notice.utils.TimeUtil;

public class NoticeRecyclerViewAdapter extends RecyclerView.Adapter<NoticeRecyclerViewAdapter.ViewHolder>{
    Context context;
    List<LocalNotices> localNotices;

    public NoticeRecyclerViewAdapter(Context context, List<LocalNotices> localNotices) {
        this.context = context;
        this.localNotices = localNotices;
    }

    @NonNull
    @Override
    public NoticeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeRecyclerViewAdapter.ViewHolder holder, int position) {
        LocalNotices notice = localNotices.get(position);
        if(notice!=null) {
            String title = notice.title;
            int id = notice.noticeID;
            String url = notice.url;
            String dateAndTime = notice.date + "/" + notice.time;
            holder.numberTextView.setText(String.valueOf(position + 1));
            holder.numberTextView.setText(String.valueOf(position + 1));
            holder.titleTextView.setText(title);
            holder.partBodyTextView.setText(notice.body);
            holder.timeTextView.setText(TimeUtil.formatTime(dateAndTime));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("noticeID",id);
                    bundle.putString("noticeUrl",url);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return localNotices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTextView;
        private TextView timeTextView;
        private TextView numberTextView;
        private TextView partBodyTextView;
        private View pointView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.NoticeTitleTextview);
            timeTextView = itemView.findViewById(R.id.NoticeTimeTextView);
            numberTextView = itemView.findViewById(R.id.NoticeNumberTextview);
            partBodyTextView = itemView.findViewById(R.id.NoticePartBodyTextview);
            pointView = itemView.findViewById(R.id.newNoticeDot);
        }
    }
}
