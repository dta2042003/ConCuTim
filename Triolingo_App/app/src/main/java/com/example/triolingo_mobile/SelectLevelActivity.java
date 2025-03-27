package com.example.triolingo_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.triolingo_mobile.DAO.UserDAO;
import com.example.triolingo_mobile.Model.UserEntity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SelectLevelActivity extends AppCompatActivity {

    private Button continueButton;
    private String selectedLevel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_level);

        continueButton = findViewById(R.id.continueButton);
        ImageView backButton = findViewById(R.id.backButton);
        LinearLayout levelList = findViewById(R.id.levelListContainer);

        String[] levels = {
                "Tôi mới học",
                "Tôi biết một vài từ thông dụng",
                "Tôi có thể nói về nhiều chủ đề",
        };

        for (String level : levels) {
            TextView levelView = new TextView(this);
            levelView.setText(level);
            levelView.setTextSize(16);
            levelView.setTextColor(Color.WHITE);
            levelView.setPadding(24, 32, 24, 32);
            levelView.setBackgroundResource(R.drawable.language_option_background);
            levelView.setOnClickListener(v -> {
                selectedLevel = level;
                highlightSelection(levelList, v);
                continueButton.setEnabled(true);
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 12, 0, 0);
            levelView.setLayoutParams(params);
            levelList.addView(levelView);
        }

        continueButton.setOnClickListener(v -> {
            // lưu selectedLevel vào note JSON của User
            SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
            String userJson = prefs.getString("user", null);
            if (userJson != null) {
                Gson gson = new Gson();
                UserEntity user = gson.fromJson(userJson, UserEntity.class);

                JsonObject noteObj = new JsonObject();
                try {
                    noteObj = gson.fromJson(user.getNote(), JsonObject.class);
                } catch (Exception ignored) {}

                noteObj.addProperty("level", selectedLevel);
                user.setNote(noteObj.toString());

                new Thread(() -> {
                    UserDAO.getInstance().udpateUser(user);
                    prefs.edit().putString("user", gson.toJson(user)).apply();

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Bạn chọn: " + selectedLevel, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, LevelRecommendationActivity.class));
                        finish();
                    });
                }).start();
            }
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SelectLevelActivity.this, SelectLanguageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void highlightSelection(LinearLayout parent, View selected) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            child.setBackgroundResource(R.drawable.language_option_background);
        }
        selected.setBackgroundResource(R.drawable.language_option_selected);
    }
}

