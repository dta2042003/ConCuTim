package com.example.con_cu_tim.Course;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.con_cu_tim.DAO.StudentCourseDAO;
import com.example.con_cu_tim.Model.UserEntity;
import com.example.con_cu_tim.R;
import com.example.con_cu_tim.Units.ListUnits;
import com.google.gson.Gson;

public class CourseDescriptionActivity extends AppCompatActivity {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_description);
        Intent unit = getIntent();
        int id = unit.getIntExtra("id",0);
        String name =unit.getStringExtra("name");
        boolean isAssigned = unit.getBooleanExtra("isAssigned",false);
        ((TextView)findViewById(R.id.totalCourse)).setText(name);
        String description = unit.getStringExtra("description");
        ((TextView)findViewById(R.id.unitDesc)).setText(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N?
                Html.fromHtml(description,Html.FROM_HTML_MODE_COMPACT):
                Html.fromHtml(description));
        ((ImageView)findViewById(R.id.btn_course_return)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(CourseDescriptionActivity.this, ListCoursesActivity.class);
                startActivity(intent);
            }
        });
        ((ImageView)findViewById(R.id.course_detail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isAssigned==false){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Đăng ký khóa học");
                    builder.setMessage("Bạn có chắc sẽ đăng ký khóa học "+name+" không?");
                    builder.setPositiveButton("Tôi đồng ý",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                                    int userId = 2;
                                    String json = sharedPref.getString("user", null);
                                    if (json != null) {
                                        Gson gson = new Gson();
                                        UserEntity userLogin = gson.fromJson(json, UserEntity.class);
                                        userId = userLogin.getId();
                                    }
                                    if(StudentCourseDAO.getInstance().assignStudentToCourse(userId,id)){
                                        Intent intent = new  Intent(CourseDescriptionActivity.this, ListUnits.class);
                                        intent.putExtra("id", id);
                                        startActivity(intent);
                                    }
                                }
                            });
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    Intent intent = new  Intent(CourseDescriptionActivity.this, ListUnits.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            }
        });
    }
}