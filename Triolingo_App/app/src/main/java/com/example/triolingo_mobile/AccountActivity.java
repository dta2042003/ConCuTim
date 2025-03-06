package com.example.triolingo_mobile;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.triolingo_mobile.Course.ListCoursesActivity;
import com.example.triolingo_mobile.DAO.StudentCourseDAO;
import com.example.triolingo_mobile.DAO.StudentLessonDAO;
import com.example.triolingo_mobile.DAO.UserDAO;
import com.example.triolingo_mobile.Model.StudentCourse;
import com.example.triolingo_mobile.Model.UserEntity;
import com.example.triolingo_mobile.Model.UserModel;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccountActivity extends AppCompatActivity {
    UserEntity us;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String json = sharedPref.getString("user", null);
        int userId = 0;
        if (json != null) {
            Gson gson = new Gson();
            UserEntity userLogin = gson.fromJson(json, UserEntity.class);
            userId = userLogin.getId();
        }
        us = UserDAO.getInstance().GetUserById(userId);
        TextView username = findViewById(R.id.tv_name);
        TextView email = findViewById(R.id.email);
        email.setText(us.getEmail());
        username.setText(us.getFullNamel());
        int totalCourse = StudentCourseDAO.getInstance().countRows(StudentCourseDAO.DB_TABLE_NAME,"StudentId="+us.getId());
        ((TextView)findViewById(R.id.totalCourse)).setText(totalCourse+"");
        ((TextView)findViewById(R.id.totalMark)).setText(StudentLessonDAO.getInstance().getMarkByUser(us.getId()) +"");
        Bitmap b = convertBase64ToBitMap();
        ((CircleImageView)findViewById(R.id.image_profile)).setImageBitmap(b);

        (findViewById(R.id.btnSetting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingProfile = new  Intent(AccountActivity.this, SettingProfileActivity.class);
                startActivity(settingProfile);
            }
        });
        (findViewById(R.id.btnLogout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent settingProfile = new  Intent(AccountActivity.this, LoginActivity.class);
                startActivity(settingProfile);
            }
        });
        (findViewById(R.id.btnManageCourses)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, ListCoursesActivity.class);
                startActivity(intent);
            }
        });
    }

    public Bitmap convertBase64ToBitMap() {
        try {
            String base64String = us.getAvatarUrl();
//            String base64Image = base64String.split(",")[1];
            byte[] decodedString = Base64.decode(base64String, 0);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return decodedByte;
        } catch (Exception ex) {
            Log.e("Error:", ex.getMessage());
        }
        return null;
    }

}