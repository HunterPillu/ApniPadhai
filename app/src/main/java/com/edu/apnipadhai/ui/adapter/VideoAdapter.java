package com.edu.apnipadhai.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.edu.apnipadhai.R;
import com.edu.apnipadhai.callbacks.ListItemClickListener;
import com.edu.apnipadhai.model.Category;
import com.edu.apnipadhai.model.Video;
import com.google.firebase.database.snapshot.Index;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.RecordViewHolder> {

    private final ListItemClickListener<Video> listener;
    private List<Video> list;
    private OnChildItemClickListener menuListener;

    public VideoAdapter(ListItemClickListener<Video> listener, OnChildItemClickListener menuListener) {
        this.list = new ArrayList<>();
        this.listener = listener;
        this.menuListener = menuListener;
    }


    public void setList(List<Video> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new RecordViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Video vo = list.get(position);
        holder.txtTitle.setText(vo.getVideoId());
        StringBuilder builder = new StringBuilder();
        builder.append(vo.getChannel()).append(" ").append(vo.getViews()).append(" ").append(vo.getUploaded()).append(" ").append("months ago");
        holder.txtOther.setText(builder);
        holder.txtDuration.setText(vo.getDuration());
        // holder.ivStatus.setImageResource(getStatusImg(vo.fetchStatus()));
        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(vo);
        });

        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuListener.onChildItemClick(vo, holder.imgMore);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView txtTitle, txtOther,txtDuration;
        private AppCompatImageView imgMore;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtOther = itemView.findViewById(R.id.txtOther);
            txtDuration = itemView.findViewById(R.id.txtDuration);
            imgMore = itemView.findViewById(R.id.imgMore);
        }
    }

    public interface OnChildItemClickListener {
        void onChildItemClick(Video video, AppCompatImageView view);
    }
}
