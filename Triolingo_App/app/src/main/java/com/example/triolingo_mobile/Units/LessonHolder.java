package com.example.triolingo_mobile.Units;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LessonHolder extends RecyclerView.ViewHolder {
    private CircularProgressView card;
    private TextView edit_id;
    private TextView edit_name;
    private Context context;
    private LessonModel lesson;
    public LessonHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
    }

    private void bindingAction(View itemView) {
        card.setOnClickListener(this::onClick);
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
    private void bindingView(View itemView) {
        edit_id = itemView.findViewById(R.id.lesson_id);
        edit_name = itemView.findViewById(R.id.lesson_name);
        card = itemView.findViewById(R.id.lesson_btn);
        card.setProgress(lesson.isPreviousActived()&&lesson.getUserMark()>=0?
                (lesson.getUserMark()==lesson.getTotalMark()?100:(((float)lesson.getUserMark()/lesson.getTotalMark())*100))
                :lesson.getUserMark());
    }

    public void setView(LessonModel lesson) {
        this.lesson=lesson;
        bindingView(itemView);
        edit_id.setText(lesson.getId()+"");
        edit_name.setText(lesson.getName());
        if(lesson.isPreviousActived()){
            bindingAction(itemView);
        }
    }
}
