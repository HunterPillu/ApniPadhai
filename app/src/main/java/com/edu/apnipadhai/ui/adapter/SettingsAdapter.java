package com.edu.apnipadhai.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.edu.apnipadhai.R;
import com.edu.apnipadhai.callbacks.ListItemClickListener;
import com.edu.apnipadhai.model.Category;
import com.edu.apnipadhai.model.Setting;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ChildHolder> {

    private ArrayList<Setting> list;


    public SettingsAdapter(ArrayList<Setting> list) {
        this.list = list;
    }

    @NotNull
    @Override
    public ChildHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting, parent, false);
        return new ChildHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsAdapter.ChildHolder holder, int position) {

       Setting setting = list.get(position);
        holder.tvName.setText(setting.getName());
        holder.img_icon.setImageResource(setting.getId());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ChildHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvName;
        ImageView img_icon;

        public ChildHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            img_icon = itemView.findViewById(R.id.img_icon);
        }

    }

}