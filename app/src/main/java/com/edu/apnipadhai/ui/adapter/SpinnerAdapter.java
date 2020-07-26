package com.edu.apnipadhai.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.edu.apnipadhai.R;
import com.edu.apnipadhai.model.Course;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<Course> {

    private final List<Course> items;
    private final Context context;

    public SpinnerAdapter(Context context, List<Course> items) {
        super(context, R.layout.support_simple_spinner_dropdown_item, items);
        this.items = items;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);

        //v.setTextColor(Color.BLACK);
        v.setText(items.get(position).getName());
        return v;
    }

    @Override
    public Course getItem(int position) {
        return items.get(position);
    }

    @NotNull
    @Override
    public View getView(int position, View v, @NotNull ViewGroup parent) {
        //View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.support_simple_spinner_dropdown_item, null);
        }
        TextView lbl = (TextView) v.findViewById(android.R.id.text1);
        // lbl.setTextColor(Color.BLACK);
        lbl.setText(items.get(position).getName());
        return v;
    }
}
