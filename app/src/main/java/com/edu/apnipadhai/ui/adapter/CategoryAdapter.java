package com.edu.apnipadhai.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.edu.apnipadhai.R;
import com.edu.apnipadhai.callbacks.ListItemClickListener;
import com.edu.apnipadhai.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.RecordViewHolder> {

    private static final String TAG = CategoryAdapter.class.getSimpleName();
    private final ListItemClickListener<Category> listener;
    private List<Category> list;

    public CategoryAdapter(ListItemClickListener<Category> listener) {
        this.list = new ArrayList<>();
        this.listener = listener;
    }


    public void setList(List<Category> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new RecordViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Category vo = list.get(position);
        holder.tvName.setText(vo.getName());
        holder.tvCount.setText("" + vo.getVideoCount());
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
        private AppCompatTextView tvCount;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCount = itemView.findViewById(R.id.tvCount);

        }
    }
}
