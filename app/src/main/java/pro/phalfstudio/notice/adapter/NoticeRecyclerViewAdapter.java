package pro.phalfstudio.notice.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import pro.phalfstudio.notice.R;

public class NoticeRecyclerViewAdapter extends RecyclerView.Adapter<NoticeRecyclerViewAdapter.ViewHolder>{
    @NonNull
    @Override
    public NoticeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeRecyclerViewAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTextView;
        private TextView timeTextView;
        private TextView numberTextView;
        private TextView partBodyTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.NoticeTitleTextview);
            timeTextView = itemView.findViewById(R.id.NoticeTimeTextView);
            numberTextView = itemView.findViewById(R.id.NoticeNumberTextview);
            partBodyTextView = itemView.findViewById(R.id.NoticePartBodyTextview);
        }
    }
}
