package com.example.triolingo_mobile.Units;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.triolingo_mobile.Course.CourseDescriptionActivity;
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
    private TextView edit_id;
    private TextView edit_name;
    private TextView edit_desc;
    private RecyclerView list_lesson;
    private CardView card_view;
    private Context context;
    private UnitModel unit;

    private boolean isFirst;

    public UnitHolder(@NonNull View itemView, Context context) {
        super(itemView);
        bindingView(itemView);
        bindingAction(itemView);
        this.context = context;
    }

    private void bindingAction(View itemView) {
        card_view.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        list_lesson.setVisibility(list_lesson.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
    }
    private void bindingView(View itemView) {
        edit_id = itemView.findViewById(R.id.unit_id);
        edit_name = itemView.findViewById(R.id.unit_name);
        edit_desc = itemView.findViewById(R.id.unit_description);
        list_lesson=itemView.findViewById(R.id.lesson_list);
        card_view=itemView.findViewById(R.id.unit_card_view);
    }

    public void setView(UnitModel unit,boolean isFirst) {
        this.unit=unit;
        edit_id.setText(unit.getId()+"");
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
        List<StudentCourse> listStudentCourse = StudentCourseDAO.getInstance().getList("StudentId="+userId+" AND CourseId="+unit.getCourseId());
        if(listStudentCourse.size()==0){
            Intent intent = new Intent(context, ListCoursesActivity.class);
            itemView.getContext().startActivity(intent);
            return;
        }
        StudentCourse studentCourse = listStudentCourse.get(0);
        List<StudentLesson> list = StudentLessonDAO.getInstance().getList("LessionId in (select id from [Lesson] Where [UnitId]="+unit.getId()+") AND StudentCourseId ="+studentCourse.getId());
        List<LessonModel> listResult = LessonDAO.getInstance().getList("Status>0 AND UnitId="+unit.getId());
        if(isFirst){
            listResult.get(0).setPreviousActived(true);
        }else{
            listResult.get(0).setPreviousActived(list.size()>0);
        }
        for (int i = 0; i< listResult.size();i++) {
            int index= list.stream().map(StudentLesson::getLessonId).collect(Collectors.toList()).indexOf(listResult.get(i).getId());
            listResult.get(i).setUserMark(index!=-1?list.get(index).getMark():listResult.get(i).isPreviousActived()?-100:-1);
            listResult.get(i).setStudentCourse(studentCourse.getId());
            if(i<listResult.size()-1&&index!=-1){
                listResult.get(i+1).setPreviousActived(true);
            }
        }
        LessonAdapter adapter = new LessonAdapter(listResult,list_lesson);
        list_lesson.setAdapter(adapter);
        list_lesson.setVisibility(View.VISIBLE);
        list_lesson.addItemDecoration(new MarginDecoration(10));
        int column = 4;
        GridLayoutManager layoutManager = new GridLayoutManager(context, column) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                if(getItemCount()==0){
                    return;
                }
                boolean rightToLeft=false;
                int previousWidth = 0,previousHeight = 0;
                View view = getChildAt(0);
                previousWidth = view.getWidth();
                previousHeight = view.getHeight();
                int left = (getWidth()-view.getWidth())/2,
                        top = 0,
                        bottom=view.getHeight(),
                        right=left+view.getWidth();
                view.layout(left, top, right, bottom);
                for (int i = 1; i < getChildCount(); i++) {
                    view = getChildAt(i);
                    int distanceWidth = ((int)Math.round(previousWidth*4/5)),distanceHeight=((int)Math.round(previousHeight*4/5));
                    if((rightToLeft&&left-distanceWidth<=0)||(!rightToLeft&&right+distanceWidth-getWidth()>=0)){
                        rightToLeft=!rightToLeft;
                    }
                    top += distanceHeight;
                    left += distanceWidth*(rightToLeft?-1:1);
                    previousWidth = view.getWidth();
                    previousHeight = view.getHeight();
                    right = left + previousWidth;
                    bottom = top + previousHeight + distanceHeight;
                    view.layout(left, top, right, bottom);
                }
            }
        };
        list_lesson.setLayoutManager(layoutManager);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(list_lesson);
    }
}