package com.example.con_cu_tim.Course;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.con_cu_tim.Model.Course;
import com.example.con_cu_tim.R;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseHolder>{
    private List<Course> list;
    public CourseAdapter(List<Course> _list) {
        list=_list;
    }

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_course, parent, false);
        CourseHolder viewHolder = new CourseHolder(v, parent.getContext());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseHolder holder, int position) {
        Course a = list.get(position);
        holder.setView(a);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
