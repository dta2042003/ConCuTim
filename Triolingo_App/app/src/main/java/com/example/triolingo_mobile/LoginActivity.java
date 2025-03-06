package com.example.triolingo_mobile;

import androidx.appcompat.app.AppCompatActivity;

import com.example.triolingo_mobile.DAO.UserDAO;
import com.example.triolingo_mobile.Model.UserEntity;
import com.example.triolingo_mobile.Model.UserModel;
import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triolingo_mobile.Lesson.QuestionChoice.QuestionChoiceActivity;

public class LoginActivity extends AppCompatActivity {
    EditText InputUsername;
    EditText InputPassword;
    Button LoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InputUsername = findViewById(R.id.userNameTxt);
        InputPassword = findViewById(R.id.passwordTxt);
        LoginBtn = findViewById(R.id.loginBtn);

        InputUsername.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String UserName = InputUsername.getText().toString().trim();
                String Password = InputPassword.getText().toString().trim();
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!UserName.isEmpty() && !Password.isEmpty()) {
                        LoginBtn.setEnabled(true);
                        LoginBtn.setBackgroundColor(Color.BLUE);
                        LoginBtn.setTextColor(Color.parseColor("#FFFFFF"));

                    } else {
                        LoginBtn.setEnabled(false);
                        LoginBtn.setBackgroundColor(Color.parseColor(("#5E756969")));
                        LoginBtn.setTextColor(Color.parseColor("#BAEFE2E2"));
                    }
                    return true;
                }
                return false;
            }
        });
        (findViewById(R.id.signInBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        InputPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String UserName = InputUsername.getText().toString().trim();
                String Password = InputPassword.getText().toString().trim();
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!UserName.isEmpty() && !Password.isEmpty()) {
                        LoginBtn.setEnabled(true);
                        LoginBtn.setBackgroundColor(Color.BLUE);
                        LoginBtn.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        LoginBtn.setEnabled(false);
                        LoginBtn.setBackgroundColor(Color.parseColor(("#5E756969")));
                        LoginBtn.setTextColor(Color.parseColor("#BAEFE2E2"));
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void LoginBtn(View v) {
        String Email = InputUsername.getText().toString().trim();
        String Password = InputPassword.getText().toString().trim();
        UserModel userLogin = new UserModel(Email, Password);
        UserEntity us = UserDAO.getInstance().Login(userLogin);
        if (us == null) {
            InputUsername.setError("Email hoặc mật khẩu không khớp");
        } else {
            Gson gson = new Gson();
            String userJson = gson.toJson(us);
            SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("user", userJson);
            editor.apply();
            Intent switchActivityIntent = new Intent(this, AccountActivity.class);
            startActivity(switchActivityIntent);
            Toast.makeText(this, "Login successfully...", Toast.LENGTH_SHORT).show();
        }
    }
}