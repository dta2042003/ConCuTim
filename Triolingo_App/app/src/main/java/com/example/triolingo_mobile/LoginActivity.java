package com.example.triolingo_mobile;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.example.triolingo_mobile.DAO.UserDAO;
import com.example.triolingo_mobile.Model.UserEntity;
import com.example.triolingo_mobile.Model.UserModel;
import com.example.triolingo_mobile.Util.UserUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private TextInputEditText inputEmail;
    private TextInputEditText inputPassword;
    private MaterialButton loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.editTextEmail);
        inputPassword = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(v -> {
            loginUser();
        });

        TextView registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void loginUser() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        Log.d(TAG, "Attempting login with email: " + email);

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        loginBtn.setEnabled(false);

        new Thread(() -> {
            String encryptedPassword = UserUtil.md5(password);
            UserModel userLogin = new UserModel(email, encryptedPassword);

            UserEntity userEntity = UserDAO.getInstance().Login(userLogin);

            runOnUiThread(() -> {
                loginBtn.setEnabled(true);

                if (userEntity == null) {
                    Toast.makeText(this, "Email hoặc mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Login failed: Incorrect email/password");
                } else {
                    Gson gson = new Gson();
                    String userJson = gson.toJson(userEntity);
                    SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                    sharedPref.edit().putString("user", userJson).apply();

                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, AccountActivity.class));
                    finish();
                }
            });
        }).start();
    }
}