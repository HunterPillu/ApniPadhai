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

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.RecordViewHolder> {

    private static final String TAG = "SupplierAdapter";
    private final ListItemClickListener<Video> listener;
    private List<Video> list;

    public VideoAdapter(ListItemClickListener<Video> listener) {
        this.list = new ArrayList<>();
        this.listener = listener;
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
        holder.tvName.setText(vo.getVideoId());
        // holder.ivStatus.setImageResource(getStatusImg(vo.fetchStatus()));
        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(vo);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView tvName;
        private AppCompatImageView ivStatus;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            // ivStatus = itemView.findViewById(R.id.ivStatus);

        }
    }
}
