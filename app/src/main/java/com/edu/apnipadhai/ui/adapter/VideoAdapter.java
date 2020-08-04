package com.edu.apnipadhai.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.edu.apnipadhai.R;
import com.edu.apnipadhai.callbacks.ListItemClickListener;
import com.edu.apnipadhai.callbacks.MenuClickListener;
import com.edu.apnipadhai.model.VideoModel;
import com.edu.apnipadhai.utils.GlideApp;
import com.edu.apnipadhai.utils.GlideRequests;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.RecordViewHolder> {

    private final ListItemClickListener<Integer, VideoModel> listener;
    private List<VideoModel> list;
    private MenuClickListener<VideoModel, View> menuListener;
    private GlideRequests glide;

    public VideoAdapter(Context context, ListItemClickListener<Integer, VideoModel> listener, MenuClickListener<VideoModel, View> menuListener) {
        this.list = new ArrayList<>();
        this.listener = listener;
        this.menuListener = menuListener;
        glide = GlideApp.with(context);

    }


    public void setList(List<VideoModel> list, AppCompatTextView textView,RecyclerView rvRecords) {
        this.list = list;
        notifyDataSetChanged();
        if (list.size() <=0) {
            textView.setVisibility(View.VISIBLE);
            rvRecords.setVisibility(View.GONE);
        }
        else{
            textView.setVisibility(View.GONE);
            rvRecords.setVisibility(View.VISIBLE);
        }
    }


    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new RecordViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        VideoModel vo = list.get(position);
        holder.tvTitle.setText(vo.getName());
        //StringBuilder builder = new StringBuilder();
        //builder.append(vo.getChannel()).append(" ").append(vo.getViews()).append(" ").append(vo.getUploaded()).append(" ").append("months ago");
        holder.tvOther.setText(vo.getChannel());
        //holder.tvDuration.setText(vo.getDuration());
        // holder.ivStatus.setImageResource(getStatusImg(vo.fetchStatus()));
        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(-1, vo);
        });

        glide.load(vo.getThumbnailUrl()).placeholder(R.drawable.logo).into(holder.ivThumbnail);

       /* holder.ivMore.setOnClickListener(v -> {
            menuListener.itemClick(vo, v);
        });*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView tvTitle, tvOther;//, tvDuration;
        private AppCompatImageView ivMore, ivThumbnail;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOther = itemView.findViewById(R.id.tvOther);
            // tvDuration = itemView.findViewById(R.id.tvDuration);
            ivMore = itemView.findViewById(R.id.ivMore);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
        }
    }
}
