package com.example.con_cu_tim.Course;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.con_cu_tim.AccountActivity;
import com.example.con_cu_tim.DAO.CourseDAO;
import com.example.con_cu_tim.DAO.StudentCourseDAO;
import com.example.con_cu_tim.Model.Course;
import com.example.con_cu_tim.Model.StudentCourse;
import com.example.con_cu_tim.Model.UserEntity;
import com.example.con_cu_tim.R;
import com.google.gson.Gson;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

public class ListCoursesActivity extends AppCompatActivity {
    Connection connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_courses);
        GetText();
    }
    public void GetText()
    {
        try {
            SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            int userId = 2;
            String json = sharedPref.getString("user", null);
            if (json != null) {
                Gson gson = new Gson();
                UserEntity userLogin = gson.fromJson(json, UserEntity.class);
                userId = userLogin.getId();
            }
            (findViewById(R.id.btn_backToAccount)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ListCoursesActivity.this, AccountActivity.class);
                    startActivity(intent);
                }
            });
            List<Integer> list = StudentCourseDAO.getInstance().getList("StudentId="+userId).stream().map(StudentCourse::getCourseId).collect(Collectors.toList());
            List<Course> listResult = CourseDAO.getInstance().getList("Status>0");
            for (Course cour: listResult) {
                cour.setAssign(list.contains(cour.getId()));
            }
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_courses);
            CourseAdapter adapter = new CourseAdapter(listResult);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(ListCoursesActivity.this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(manager);
            recyclerView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}