package com.example.triolingo_mobile.Units;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.triolingo_mobile.Units.LessonHolder;
import com.example.triolingo_mobile.Model.LessonModel;
import com.example.triolingo_mobile.R;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonHolder>{
    private List<LessonModel> list;
    private RecyclerView recyclerView;
    private int itemWidth,firstItemPosition;

    public LessonAdapter(List<LessonModel> _list, RecyclerView _recyclerView) {
        list=_list;
        recyclerView=_recyclerView;
        itemWidth = (int) (recyclerView.getResources().getDisplayMetrics().widthPixels * 0.8);
        firstItemPosition = (recyclerView.getResources().getDisplayMetrics().widthPixels - itemWidth) / 2;
    }

    @NonNull
    @Override
    public LessonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_lesson, parent, false);
        LessonHolder viewHolder = new LessonHolder(v, parent.getContext());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LessonHolder holder, int position) {

        LessonModel a = list.get(position);
        holder.setView(a);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
