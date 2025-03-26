package com.example.triolingo_mobile.Units;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import com.example.triolingo_mobile.Course.ListCoursesActivity;
import com.example.triolingo_mobile.DAO.LessonDAO;
import com.example.triolingo_mobile.DAO.StudentCourseDAO;
import com.example.triolingo_mobile.DAO.StudentLessonDAO;
import com.example.triolingo_mobile.Model.LessonModel;
import com.example.triolingo_mobile.Model.StudentCourse;
import com.example.triolingo_mobile.Model.StudentLesson;
import com.example.triolingo_mobile.Model.UnitModel;
import com.example.triolingo_mobile.Model.UserEntity;
import com.example.triolingo_mobile.R;
import com.google.gson.Gson;

import java.util.List;
import java.util.stream.Collectors;

public class UnitHolder extends RecyclerView.ViewHolder {
    private TextView edit_id, edit_name, edit_desc;
    private RecyclerView list_lesson;
    private CardView card_view, card_view_not;
    private Context context;
    private UnitModel unit;

    public UnitHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        bindingView(itemView);
        bindingAction();
    }

    private void bindingView(View itemView) {
        edit_id = itemView.findViewById(R.id.unit_id);
        edit_name = itemView.findViewById(R.id.unit_name);
        edit_desc = itemView.findViewById(R.id.unit_description);
        list_lesson = itemView.findViewById(R.id.lesson_list);
        card_view = itemView.findViewById(R.id.unit_card_view);
        card_view_not = itemView.findViewById(R.id.unit_card_view_not);
    }

    private void bindingAction() {
        card_view.setOnClickListener(view -> toggleLessonList());
    }

    private void toggleLessonList() {
        list_lesson.setVisibility(list_lesson.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    public void setView(UnitModel unit, boolean isFirst) {
        this.unit = unit;
        edit_id.setText(String.valueOf(unit.getId()));
        edit_desc.setText(unit.getDescription());
        edit_name.setText(unit.getName());

        int userId = 2;
        SharedPreferences sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String json = sharedPref.getString("user", null);
        if (json != null) {
            Gson gson = new Gson();
            UserEntity userLogin = gson.fromJson(json, UserEntity.class);
            userId = userLogin.getId();
        }

        List<StudentCourse> listStudentCourse = StudentCourseDAO.getInstance()
                .getList("StudentId=" + userId + " AND CourseId=" + unit.getCourseId());

        if (listStudentCourse.isEmpty()) {
            context.startActivity(new Intent(context, ListCoursesActivity.class));
            return;
        }

        StudentCourse studentCourse = listStudentCourse.get(0);
        List<StudentLesson> completedLessons = StudentLessonDAO.getInstance()
                .getList("LessionId in (select id from [Lesson] Where [UnitId]=" + unit.getId() + ") AND StudentCourseId =" + studentCourse.getId());

        List<LessonModel> lessons = LessonDAO.getInstance()
                .getList("Status>0 AND UnitId=" + unit.getId());

        if (!lessons.isEmpty()) {
            lessons.get(0).setPreviousActived(isFirst || !completedLessons.isEmpty());
        }

        for (int i = 0; i < lessons.size(); i++) {
            int index = completedLessons.stream()
                    .map(StudentLesson::getLessonId)
                    .collect(Collectors.toList())
                    .indexOf(lessons.get(i).getId());

            lessons.get(i).setUserMark(index != -1 ? completedLessons.get(index).getMark() : lessons.get(i).isPreviousActived() ? -100 : -1);
            lessons.get(i).setStudentCourse(studentCourse.getId());

            if (i < lessons.size() - 1 && index != -1) {
                lessons.get(i + 1).setPreviousActived(true);
            }
        }

        LessonAdapter adapter = new LessonAdapter(lessons, list_lesson);
        list_lesson.setAdapter(adapter);
        list_lesson.setLayoutManager(new LinearLayoutManager(context));
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(list_lesson);

        // Xử lý hiển thị đúng card
        if (lessons.isEmpty()) {
            card_view.setVisibility(View.GONE);
            card_view_not.setVisibility(View.VISIBLE);
        } else {
            card_view.setVisibility(View.VISIBLE);
            card_view_not.setVisibility(View.GONE);
        }
    }
}
