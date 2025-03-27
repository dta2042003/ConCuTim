package com.example.con_cu_tim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.con_cu_tim.Model.UserEntity;
import com.google.gson.Gson;

public class IntroActivity extends AppCompatActivity {

    private static final String TAG = "IntroDebug";

    private Button continueButton;
    private ImageView backButton;
    private TextView chatBubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Ánh xạ View
        continueButton = findViewById(R.id.continueButton);
        backButton = findViewById(R.id.backButton);
        chatBubble = findViewById(R.id.chatBubble);

        // Lấy SharedPreferences
        SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String userJson = prefs.getString("user", null);

        // Hiển thị tên người dùng nếu có
        if (userJson != null) {
            try {
                UserEntity user = new Gson().fromJson(userJson, UserEntity.class);
                if (user != null && user.getFullNamel() != null) {
                    String welcomeText = "Chào mừng bạn, " + user.getFullNamel() + "! Mình là Cú Tím!";
                    chatBubble.setText(welcomeText);
                }
            } catch (Exception e) {
                Log.e(TAG, "Lỗi parse user từ JSON", e);
            }
        }

        // Sự kiện "Tiếp tục"
        continueButton.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, IntroStep2Activity.class);
            startActivity(intent);
            finish();
        });

        // Sự kiện "Quay lại" (logout)
        backButton.setOnClickListener(v -> {
            prefs.edit()
                    .clear()
                    .apply();

            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
