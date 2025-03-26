package com.example.triolingo_mobile.Units;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.triolingo_mobile.DAO.ExerciseDAO;
import com.example.triolingo_mobile.Model.Exercise;
import com.example.triolingo_mobile.Model.LessonModel;
import com.example.triolingo_mobile.R;
import com.example.triolingo_mobile.Util.LessonUtil;
import com.example.triolingo_mobile.View.CircularProgressView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LessonHolder extends RecyclerView.ViewHolder {
    private MaterialButton lessonButton;
    private TextView edit_id;
    private TextView edit_name;
    private Context context;
    private LessonModel lesson;
    public LessonHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        bindingView(itemView);
    }

    private void bindingView(View itemView) {
        edit_id = itemView.findViewById(R.id.lesson_id);
        edit_name = itemView.findViewById(R.id.lesson_name);
        lessonButton = itemView.findViewById(R.id.lesson_btn);
    }

    public void onClick(View view) {
        ExerciseDAO exDao = ExerciseDAO.getInstance();
        String lessonId = edit_id.getText().toString();
        ArrayList<Exercise> listExercise = exDao.getExerciseOfLesson( "STATUS>0 AND LessonId = "+lessonId);

        if(listExercise.size()>0){
            LessonUtil.setListExercise(listExercise);
            LessonUtil.setCourseStudentId(lesson.getStudentCourse());
            LessonUtil.nextExercise( 0, 0, 0, 0, itemView.getContext());
        }
    }

    public void setView(LessonModel lesson) {
        this.lesson = lesson;
        edit_id.setText(String.valueOf(lesson.getId()));
        edit_name.setText(lesson.getName());

        // Kiểm tra trạng thái bài học
        if (!lesson.isPreviousActived()) {
            // 🔴 Chưa học (bị khóa)
            lessonButton.setEnabled(false);
            lessonButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gray));
            lessonButton.setIconResource(R.drawable.ic_lock);
        } else if (lesson.getUserMark() < lesson.getTotalMark()) {
            // 🟡 Đang học (chưa hoàn thành)
            lessonButton.setEnabled(true);
            lessonButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.purple_700));
            lessonButton.setIconResource(R.drawable.ic_play);
            lessonButton.setOnClickListener(this::onClick);
        } else {
            // ✅ Đã học xong
            lessonButton.setEnabled(true);
            lessonButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green));
            lessonButton.setIconResource(R.drawable.ic_check);
            lessonButton.setOnClickListener(this::onClick);
        }
    }
}
