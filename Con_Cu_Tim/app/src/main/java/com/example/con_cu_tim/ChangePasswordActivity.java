package com.example.con_cu_tim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.con_cu_tim.DAO.UserDAO;
import com.example.con_cu_tim.Model.UserEntity;
import com.example.con_cu_tim.Util.UserUtil;
import com.google.gson.Gson;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText oldPassword, newPassword, confirmPassword;
    UserEntity user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnSavePassword).setOnClickListener(this::onChangePassword);

        SharedPreferences prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String json = prefs.getString("user", null);
        if (json != null) {
            user = new Gson().fromJson(json, UserEntity.class);
        }
    }

    private void onChangePassword(View view) {
        String oldPassInput = oldPassword.getText().toString().trim();
        String newPassInput = newPassword.getText().toString().trim();
        String confirmPassInput = confirmPassword.getText().toString().trim();

        // Kiểm tra nhập đủ
        if (oldPassInput.isEmpty() || newPassInput.isEmpty() || confirmPassInput.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu cũ đúng
        if (!UserUtil.md5(oldPassInput).equals(user.getPassword())) {
            oldPassword.setError("Mật khẩu cũ không đúng");
            return;
        }

        // Validate mật khẩu mới
        if (newPassInput.length() < 6) {
            newPassword.setError("Mật khẩu phải từ 6 ký tự");
            return;
        }
        if (!newPassInput.matches(".*[A-Za-z].*") || !newPassInput.matches(".*\\d.*")) {
            newPassword.setError("Mật khẩu phải chứa cả chữ và số");
            return;
        }

        // Xác nhận mật khẩu
        if (!newPassInput.equals(confirmPassInput)) {
            confirmPassword.setError("Mật khẩu không khớp");
            return;
        }

        // Cập nhật mật khẩu
        user.setPassword(UserUtil.md5(newPassInput));

        new Thread(() -> {
            int result = UserDAO.getInstance().udpateUser(user);
            runOnUiThread(() -> {
                if (result > 0) {
                    Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, AccountActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}

