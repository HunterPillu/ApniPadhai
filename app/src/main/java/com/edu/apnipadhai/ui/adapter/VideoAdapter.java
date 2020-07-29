package com.edu.apnipadhai.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.edu.apnipadhai.R;
import com.edu.apnipadhai.callbacks.ListItemClickListener;
import com.edu.apnipadhai.model.VideoModel;
import com.edu.apnipadhai.utils.Const;
import com.edu.apnipadhai.utils.GlideApp;
import com.edu.apnipadhai.utils.GlideRequests;

import java.util.ArrayList;

public class VideoAdapter extends BaseAdapter<VideoModel> {

    private GlideRequests glide;

    public VideoAdapter(Context context, ListItemClickListener<Integer, VideoModel> listener) {
        this.list = new ArrayList<>();
        this.listener = listener;
        glide = GlideApp.with(context);

    }

   /* public void setList(ArrayList<VideoModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }*/


    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Const.VT_EMPTY) {
            View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);
            return new BaseHolder(listItem);
        } else {

            View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
            return new RecordViewHolder(listItem);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        VideoModel vo = list.get(position);
        if (vo.getType() == Const.VT_EMPTY) {
            super.onBindViewHolder(holder, position);
            return;
        }
        RecordViewHolder vh = (RecordViewHolder) holder;

        vh.tvTitle.setText(vo.getName());
        //StringBuilder builder = new StringBuilder();
        //builder.append(vo.getChannel()).append(" ").append(vo.getViews()).append(" ").append(vo.getUploaded()).append(" ").append("months ago");
        vh.tvOther.setText(vo.getChannel());
        //holder.tvDuration.setText(vo.getDuration());
        // holder.ivStatus.setImageResource(getStatusImg(vo.fetchStatus()));
        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(-1, vo);
        });

        glide.load(vo.getThumbnailUrl()).placeholder(R.drawable.logo).into(vh.ivThumbnail);

       /* holder.ivMore.setOnClickListener(v -> {
            menuListener.itemClick(vo, v);
        });*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class RecordViewHolder extends BaseHolder {
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
