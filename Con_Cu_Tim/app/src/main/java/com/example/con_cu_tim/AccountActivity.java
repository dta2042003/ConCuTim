package com.example.con_cu_tim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.con_cu_tim.Course.ListCoursesActivity;
import com.example.con_cu_tim.DAO.CourseDAO;
import com.example.con_cu_tim.DAO.StudentCourseDAO;
import com.example.con_cu_tim.DAO.StudentLessonDAO;
import com.example.con_cu_tim.DAO.UserDAO;
import com.example.con_cu_tim.Model.Course;
import com.example.con_cu_tim.Model.UserEntity;
import com.google.gson.Gson;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {
    private UserEntity currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ImageView iconSettings = findViewById(R.id.iconSettings);
        iconSettings.setOnClickListener(view -> {
            Log.d("DEBUG_ICON", "iconSettings clicked"); // ✅ Log để kiểm tra click
            showSettingsMenu(view);
        });

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

        TextView viewAllCourses = findViewById(R.id.viewAllCourses);
        viewAllCourses.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, ListCoursesActivity.class);
            startActivity(intent);
        });


        // Tải dữ liệu người dùng từ DB
        loadUserData(currentUser.getId());
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

            // 🔥 Thêm dòng này để lấy 3 khóa học
            List<Course> topCourses = CourseDAO.getInstance().getTop3CoursesByStudentId(userId);

            runOnUiThread(() -> {
                updateUI(userFromDb, totalCourses, totalMark, profileImage);
                displayTopCourses(topCourses); // Hàm để tạo view từ danh sách
            });
        }).start();
    }


    private void updateUI(UserEntity user, int totalCourses, int totalMark, Bitmap profileImage) {
        ((TextView) findViewById(R.id.tv_name)).setText(user.getFullNamel());
        ((TextView) findViewById(R.id.email)).setText(user.getEmail());
        ((TextView) findViewById(R.id.totalMark)).setText(String.valueOf(totalMark));

        // Cập nhật text "Tổng số khóa học (x)"
        TextView totalCourseLabel = findViewById(R.id.totalCourseLabel);
        if (totalCourseLabel != null) {
            totalCourseLabel.setText("Tổng số khóa học (" + totalCourses + ")");
        }

        if (profileImage != null) {
            ((CircleImageView) findViewById(R.id.image_profile)).setImageBitmap(profileImage);
        }
    }

    private void showSettingsMenu(View anchor) {
        PopupMenu popup = new PopupMenu(AccountActivity.this, anchor);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_settings, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_update_profile) {
                startActivity(new Intent(this, SettingProfileActivity.class));
                return true;
            } else if (id == R.id.menu_change_password) {
                startActivity(new Intent(this, ChangePasswordActivity.class));
                return true;
            } else if (id == R.id.menu_logout) {
                logout();
                return true;
            }

            return false;
        });

        popup.show();
    }

    private void logout() {
        getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                .edit().clear().apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void displayTopCourses(List<Course> topCourses) {
        LinearLayout box1 = findViewById(R.id.courseBox1);
        LinearLayout box2 = findViewById(R.id.courseBox2);
        LinearLayout box3 = findViewById(R.id.courseBox3);

        // Ẩn toàn bộ trước
        box1.setVisibility(View.GONE);
        box2.setVisibility(View.GONE);
        box3.setVisibility(View.GONE);

        if (topCourses.size() > 0) {
            ((TextView) findViewById(R.id.courseName1)).setText(topCourses.get(0).getName());
            box1.setVisibility(View.VISIBLE);
        }

        if (topCourses.size() > 1) {
            ((TextView) findViewById(R.id.courseName2)).setText(topCourses.get(1).getName());
            box2.setVisibility(View.VISIBLE);
        }

        if (topCourses.size() > 2) {
            ((TextView) findViewById(R.id.courseName3)).setText(topCourses.get(2).getName());
            box3.setVisibility(View.VISIBLE);
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
