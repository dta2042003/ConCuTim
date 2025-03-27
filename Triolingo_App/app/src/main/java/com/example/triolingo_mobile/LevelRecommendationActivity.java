package com.example.triolingo_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.triolingo_mobile.DAO.CourseDAO;
import com.example.triolingo_mobile.DAO.LessonDAO;
import com.example.triolingo_mobile.DAO.StudentCourseDAO;
import com.example.triolingo_mobile.DAO.StudentLessonDAO;
import com.example.triolingo_mobile.DAO.UserDAO;
import com.example.triolingo_mobile.Model.Course;
import com.example.triolingo_mobile.Model.LessonModel;
import com.example.triolingo_mobile.Model.StudentCourse;
import com.example.triolingo_mobile.Model.StudentLesson;
import com.example.triolingo_mobile.Model.UserEntity;
import com.example.triolingo_mobile.Model.UserNote;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

public class LevelRecommendationActivity extends AppCompatActivity {

    private Button continueButton;
    private ImageView backButton;
    private TextView chatBubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ✅ Cho phép tạm thời thực hiện query trên main thread (CHỈ KHI CẦN)
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_recommendation);

        continueButton = findViewById(R.id.continueButton);
        backButton = findViewById(R.id.backButton);
        chatBubble = findViewById(R.id.chatBubble);

        SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String userJson = prefs.getString("user", null);

        if (userJson != null) {
            UserEntity user = new Gson().fromJson(userJson, UserEntity.class);
            String note = user.getNote();

            if (note != null) {
                try {
                    JsonObject noteObj = new Gson().fromJson(note, JsonObject.class);
                    String level = noteObj.has("level") ? noteObj.get("level").getAsString() : null;
                    String language = noteObj.has("language") ? noteObj.get("language").getAsString() : null;

                    chatBubble.setText(getMessageBasedOnLevel(level));

                    Course matchedCourse = null;
                    for (Course c : CourseDAO.getInstance().getList("Status = 1")) {
                        if (c.getName() != null && c.getName().equalsIgnoreCase(language)) {
                            matchedCourse = c;
                            break;
                        }
                    }

                    if (matchedCourse != null) {
                        boolean assigned = StudentCourseDAO.getInstance()
                                .assignStudentToCourse(user.getId(), matchedCourse.getId());

                        int studentCourseId = -1;
                        List<StudentCourse> scList = StudentCourseDAO.getInstance()
                                .getList("StudentId = " + user.getId() + " AND CourseId = " + matchedCourse.getId());

                        if (!scList.isEmpty()) {
                            studentCourseId = scList.get(0).getId();
                        }

                        if (assigned || studentCourseId != -1) {
                            Log.d("LevelRecommendation", "✅ Đã gán CourseId = " + matchedCourse.getId());

                            int maxLessonsToAssign = 1; // default
                            if ("Tôi biết một vài từ thông dụng".equalsIgnoreCase(level)) {
                                maxLessonsToAssign = 4;
                            } else if ("Tôi có thể nói về nhiều chủ đề".equalsIgnoreCase(level)) {
                                maxLessonsToAssign = 6;
                            }

                            List<LessonModel> lessons = LessonDAO.getInstance().getList(
                                    "Status = 1 AND UnitId IN (SELECT Id FROM Unit WHERE CourseId = " + matchedCourse.getId() + ") ORDER BY Id"
                            );

                            int count = 0;
                            for (LessonModel lesson : lessons) {
                                if (count >= maxLessonsToAssign) break;

                                StudentLesson sl = new StudentLesson();
                                sl.setLessonId(lesson.getId());
                                sl.setStudentCourseId(studentCourseId);
                                sl.setMark(0);

                                boolean created = StudentLessonDAO.getInstance().createStudentLesson(sl);
                                if (created) {
                                    Log.d("LevelRecommendation", "✅ Assigned lesson: " + lesson.getId());
                                }
                                count++;
                            }
                        }

                    } else {
                        Log.e("LevelRecommendation", "❌ Không tìm thấy khóa học phù hợp: " + language);
                    }

                } catch (Exception e) {
                    Log.e("LevelRecommendation", "❌ Lỗi parse note JSON", e);
                    chatBubble.setText("Không thể đọc trình độ người dùng.");
                }
            } else {
                chatBubble.setText("Không tìm thấy trình độ người dùng.");
            }
        }
        continueButton.setOnClickListener(v -> {
            if (userJson != null) {
                Gson gson = new Gson();
                UserEntity user = gson.fromJson(userJson, UserEntity.class);

                // Parse note cũ nếu có
                UserNote note;
                try {
                    note = gson.fromJson(user.getNote(), UserNote.class);
                    if (note == null) note = new UserNote();
                } catch (Exception e) {
                    note = new UserNote(); // fallback nếu lỗi
                }

                // Cập nhật intro = true, giữ lại dữ liệu cũ
                note.setIntro(true);

                String noteJson = gson.toJson(note);
                user.setNote(noteJson);

                new Thread(() -> {
                    UserDAO.getInstance().udpateUser(user);
                    Log.d("IntroDebug", "✅ Đã cập nhật note = " + noteJson);

                    prefs.edit()
                            .putString("user", gson.toJson(user))
                            .apply();

                    runOnUiThread(() -> {
                        Intent intent = new Intent(LevelRecommendationActivity.this, AccountActivity.class);
                        startActivity(intent);
                        finish();
                    });
                }).start();
            }
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(LevelRecommendationActivity.this, SelectLevelActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private String getMessageBasedOnLevel(String level) {
        if (level == null) return "Bạn nên bắt đầu học!";
        if ("Tôi mới học".equals(level)) {
            return "Vì bạn mới bắt đầu, tôi đã mở khóa phần 1 và 2!";
        } else if ("Tôi biết một vài từ thông dụng".equals(level)) {
            return "Bạn biết một chút, tôi đã mở khóa 4 bài đầu!";
        } else if ("Tôi có thể nói về nhiều chủ đề".equals(level)) {
            return "Bạn đã khá tốt, tôi đã mở khóa 6 bài đầu!";
        } else {
            return "Bạn nên bắt đầu học!";
        }
    }
}
