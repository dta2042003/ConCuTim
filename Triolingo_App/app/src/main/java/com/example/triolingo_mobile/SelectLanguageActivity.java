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
import com.example.triolingo_mobile.Model.UserNote;
import com.google.gson.Gson;

public class SelectLanguageActivity extends AppCompatActivity {

    private Button continueButton;
    private String selectedLanguage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        continueButton = findViewById(R.id.continueButton);
        ImageView backButton = findViewById(R.id.backButton);
        LinearLayout languageList = findViewById(R.id.languageListContainer);

        String[][] languages = {
                {"🇺🇸", "Tiếng Anh"},
                {"🇨🇳", "Tiếng Hoa"},
                {"🇮🇹", "Tiếng Ý"},
                {"🇫🇷", "Tiếng Pháp"},
                {"🇪🇸", "Tiếng Tây Ban Nha"},
                {"🇩🇪", "Tiếng Đức"}
        };

        for (String[] lang : languages) {
            TextView langView = new TextView(this);
            langView.setText(lang[0] + "  " + lang[1]);
            langView.setTextSize(18);
            langView.setTextColor(Color.WHITE);
            langView.setPadding(24, 32, 24, 32);
            langView.setBackgroundResource(R.drawable.language_option_background);
            langView.setOnClickListener(v -> {
                selectedLanguage = lang[1];
                highlightSelection(languageList, v);
                continueButton.setEnabled(true);
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 12, 0, 0);
            langView.setLayoutParams(params);
            languageList.addView(langView);
        }

        continueButton.setOnClickListener(v -> {
            if (selectedLanguage == null) return;

            SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
            String userJson = prefs.getString("user", null);

            if (userJson != null) {
                Gson gson = new Gson();
                UserEntity user = gson.fromJson(userJson, UserEntity.class);

                // Parse note hiện tại (nếu có)
                UserNote note;
                try {
                    note = gson.fromJson(user.getNote(), UserNote.class);
                } catch (Exception e) {
                    note = new UserNote();
                }

                if (note == null) note = new UserNote();

                // ✅ Gán ngôn ngữ và intro = false ngay từ đầu
                note.setLanguage(selectedLanguage);
                note.setIntro(false);

                String noteJson = gson.toJson(note);
                user.setNote(noteJson);

                // Cập nhật DB và SharedPreferences
                new Thread(() -> {
                    UserDAO.getInstance().udpateUser(user);
                    prefs.edit().putString("user", gson.toJson(user)).apply();

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Đã chọn: " + selectedLanguage, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, SelectLevelActivity.class));
                        finish();
                    });
                }).start();
            }
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SelectLanguageActivity.this, IntroStep2Activity.class);
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

