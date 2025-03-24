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
import android.widget.TextView;
import android.widget.Toast;

import com.example.triolingo_mobile.Course.ListCoursesActivity;
import com.example.triolingo_mobile.DAO.StudentCourseDAO;
import com.example.triolingo_mobile.DAO.StudentLessonDAO;
import com.example.triolingo_mobile.DAO.UserDAO;
import com.example.triolingo_mobile.Model.UserEntity;
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

        if (json == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Gson gson = new Gson();
        UserEntity userLogin = gson.fromJson(json, UserEntity.class);
        int userId = userLogin.getId();

        new Thread(() -> {
            us = UserDAO.getInstance().GetUserById(userId);

            if (us == null) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Không tải được thông tin người dùng", Toast.LENGTH_SHORT).show();
                    finish();
                });
                return;
            }

            int totalCourse = StudentCourseDAO.getInstance().countRows(StudentCourseDAO.DB_TABLE_NAME, "StudentId=" + us.getId());
            int totalMark = StudentLessonDAO.getInstance().getMarkByUser(us.getId());
            Bitmap profileImage = convertBase64ToBitmap(us.getAvatarUrl());

            runOnUiThread(() -> {
                ((TextView) findViewById(R.id.tv_name)).setText(us.getFullNamel());
                ((TextView) findViewById(R.id.email)).setText(us.getEmail());
                ((TextView) findViewById(R.id.totalCourse)).setText(String.valueOf(totalCourse));
                ((TextView) findViewById(R.id.totalMark)).setText(String.valueOf(totalMark));

                if (profileImage != null) {
                    ((CircleImageView) findViewById(R.id.image_profile)).setImageBitmap(profileImage);
                }
            });
        }).start();

        findViewById(R.id.btnSetting).setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, SettingProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            sharedPref.edit().clear().apply();
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.btnManageCourses).setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, ListCoursesActivity.class);
            startActivity(intent);
        });
    }

    private Bitmap convertBase64ToBitmap(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            Log.e("Convert Image Error", "Base64 string is null or empty");
            return null;
        }
        try {
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception ex) {
            Log.e("Convert Image Error", "Error decoding base64 image", ex);
            return null;
        }
    }

}