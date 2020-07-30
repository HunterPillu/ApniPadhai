package com.edu.apnipadhai.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.edu.apnipadhai.R;
import com.edu.apnipadhai.model.Course;
import com.edu.apnipadhai.utils.Const;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Course> list;
    private int selectedPosition = -1;
    private int cardSelected, cardUnselected;

    public CourseAdapter(Context context) {
        this.list = new ArrayList<>();
        cardSelected = context.getResources().getColor(R.color.colorPrimary);
        cardUnselected = context.getResources().getColor(R.color.card_color_category);
    }


    public void setList(List<Course> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getCourseType();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (Const.COURSE_HEADER == viewType) {
            View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_header, parent, false);
            return new HeaderVH(listItem);
        } else {
            View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
            return new ItemVH(listItem);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            Course vo = list.get(position);

            if (vo.getCourseType() == Const.COURSE_ITEM) {
                ItemVH vh = (ItemVH) holder;
                vh.cvMain.setCardBackgroundColor(vo.getSelected() ? cardSelected : cardUnselected);
                vh.tvName.setText(vo.getName());
                // holder.ivStatus.setImageResource(getStatusImg(vo.fetchStatus()));
                vh.itemView.setOnClickListener(v -> {
                    updatePreviousItem(holder.getAdapterPosition());
                });
            } else {
                HeaderVH vh = (HeaderVH) holder;
                vh.tvName.setText(vo.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePreviousItem(int pos) {
        if (selectedPosition == -1) {
            list.get(pos).setSelected(true);
            selectedPosition = pos;
            notifyItemChanged(selectedPosition);
        } else if (pos == selectedPosition) {
            selectedPosition = -1;
            list.get(pos).setSelected(false);
            notifyItemChanged(pos);
        } else {
            list.get(selectedPosition).setSelected(false);
            notifyItemChanged(selectedPosition);
            list.get(pos).setSelected(true);
            selectedPosition = pos;
            notifyItemChanged(selectedPosition);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int selectedCourseId() {
        if (selectedPosition == -1) {
            return selectedPosition;
        }
        return list.get(selectedPosition).getId();
    }


    public static class ItemVH extends RecyclerView.ViewHolder {
        private AppCompatTextView tvName;
        private CardView cvMain;

        public ItemVH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            cvMain = itemView.findViewById(R.id.cvMain);

        }
    }

    public static class HeaderVH extends RecyclerView.ViewHolder {
        private AppCompatTextView tvName;

        public HeaderVH(@NonNull View itemView) {
            super(itemView);
            //StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //layoutParams.setFullSpan(true);
            ((StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams()).setFullSpan(true);
            //itemView.setLayoutParams(layoutParams);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
