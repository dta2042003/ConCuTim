package com.example.con_cu_tim.Units;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.con_cu_tim.Course.CourseDescriptionActivity;
import com.example.con_cu_tim.Course.ListCoursesActivity;
import com.example.con_cu_tim.DAO.CourseDAO;
import com.example.con_cu_tim.DAO.UnitDAO;
import com.example.con_cu_tim.Model.Course;
import com.example.con_cu_tim.Model.UnitModel;
import com.example.con_cu_tim.R;

import java.util.List;

public class ListUnits extends AppCompatActivity {
    Course course;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_units);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);

        // Gọi dữ liệu bất đồng bộ
        CourseDAO.getInstance().getDetailAsync(id, new CourseDAO.OnCourseDetailListener() {
            @Override
            public void onCourseLoaded(Course cour) {
                if (cour != null) {
                    ((TextView) findViewById(R.id.course_name)).setText(cour.getName());
                    handleCourseData(cour, id);
                }
            }
        });
    }

    private void handleCourseData(Course cour, int id) {
        Button btn = findViewById(R.id.btn_backToAccount);
        ((ImageView) findViewById(R.id.course_return)).setOnClickListener(view -> {
            Intent returnCourses = new Intent(ListUnits.this, ListCoursesActivity.class);
            startActivity(returnCourses);
        });

        List<UnitModel> listResult = UnitDAO.getInstance().getList("Status>0" + (id == 0 ? "" : " AND CourseId=" + id));
        RecyclerView recyclerView = findViewById(R.id.list_unit);
        UnitAdapter adapter = new UnitAdapter(listResult);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListUnits.this));
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void onClickGuide(View view) {
        if (course != null) {
            Intent intent = new Intent(ListUnits.this, CourseDescriptionActivity.class);
            intent.putExtra("courseId", course.getId());
            startActivity(intent);
        }
    }

}