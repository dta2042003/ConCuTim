package com.example.triolingo_mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.triolingo_mobile.Course.ListCoursesActivity;
import com.example.triolingo_mobile.DAO.StudentCourseDAO;
import com.example.triolingo_mobile.DAO.StudentLessonDAO;
import com.example.triolingo_mobile.DAO.UserDAO;
import com.example.triolingo_mobile.Model.UserEntity;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {
    private UserEntity currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String json = sharedPref.getString("user", null);

        if (json == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        currentUser = new Gson().fromJson(json, UserEntity.class);
        if (currentUser == null || currentUser.getId() == 0) {
            Toast.makeText(this, "Dữ liệu người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Tải dữ liệu người dùng từ DB
        loadUserData(currentUser.getId());

        // Các sự kiện click
        findViewById(R.id.btnSetting).setOnClickListener(v -> {
            startActivity(new Intent(this, SettingProfileActivity.class));
        });

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            sharedPref.edit()
                    .clear()
                    .apply();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear stack
            startActivity(intent);
            finish();
        });

        findViewById(R.id.btnManageCourses).setOnClickListener(v -> {
            startActivity(new Intent(this, ListCoursesActivity.class));
        });
    }

    private void loadUserData(int userId) {
        new Thread(() -> {
            UserEntity userFromDb = UserDAO.getInstance().GetUserById(userId);

            if (userFromDb == null) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Không tải được thông tin người dùng", Toast.LENGTH_SHORT).show();
                    finish();
                });
                return;
            }

            int totalCourses = StudentCourseDAO.getInstance().countRows(
                    StudentCourseDAO.DB_TABLE_NAME, "StudentId=" + userId);

            int totalMark = StudentLessonDAO.getInstance().getMarkByUser(userId);
            Bitmap profileImage = convertBase64ToBitmap(userFromDb.getAvatarUrl());

            runOnUiThread(() -> updateUI(userFromDb, totalCourses, totalMark, profileImage));
        }).start();
    }

    private void updateUI(UserEntity user, int totalCourses, int totalMark, Bitmap profileImage) {
        ((TextView) findViewById(R.id.tv_name)).setText(user.getFullNamel());
        ((TextView) findViewById(R.id.email)).setText(user.getEmail());
        ((TextView) findViewById(R.id.totalCourse)).setText(String.valueOf(totalCourses));
        ((TextView) findViewById(R.id.totalMark)).setText(String.valueOf(totalMark));

        if (profileImage != null) {
            ((CircleImageView) findViewById(R.id.image_profile)).setImageBitmap(profileImage);
        }
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
