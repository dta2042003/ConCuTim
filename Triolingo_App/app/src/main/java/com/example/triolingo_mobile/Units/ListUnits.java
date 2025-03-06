package com.example.triolingo_mobile.Units;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.triolingo_mobile.Course.CourseDescriptionActivity;
import com.example.triolingo_mobile.Course.ListCoursesActivity;
import com.example.triolingo_mobile.DAO.CourseDAO;
import com.example.triolingo_mobile.DAO.UnitDAO;
import com.example.triolingo_mobile.Model.Course;
import com.example.triolingo_mobile.Model.UnitModel;
import com.example.triolingo_mobile.R;

import java.util.List;

public class ListUnits extends AppCompatActivity {

    Course course;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_units);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);
        Course cour = CourseDAO.getInstance().getDetail(id);
        ((TextView)findViewById(R.id.course_name)).setText(cour.getName());
        Button btn = findViewById(R.id.btn_backToAccount);
        ((ImageView)findViewById(R.id.course_return)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnCourses = new  Intent(ListUnits.this, ListCoursesActivity.class);
                startActivity(returnCourses);
            }
        });
        if (cour==null||cour.getDescription() == null || cour.getDescription().equals("") || cour.getDescription().trim().equals("")){
            btn.setVisibility(View.INVISIBLE);
        }else{
            course=cour;
            btn.setOnClickListener(this::onClickGuide);
        }
        List<UnitModel> listResult = UnitDAO.getInstance().getList("Status>0"+(id==0?"":" AND CourseId="+id));
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_unit);
        UnitAdapter adapter = new UnitAdapter(listResult);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(ListUnits.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setVisibility(View.VISIBLE);
    }
    public void onClickGuide(View v) {
        Intent intent = new  Intent(this, CourseDescriptionActivity.class);
        intent.putExtra("id", course.getId());
        intent.putExtra("name", course.getName());
        intent.putExtra("description", course.getDescription());
        startActivity(intent);
    }
}