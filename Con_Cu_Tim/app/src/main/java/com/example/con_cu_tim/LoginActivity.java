package com.example.con_cu_tim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.con_cu_tim.DAO.UserDAO;
import com.example.con_cu_tim.Model.UserEntity;
import com.example.con_cu_tim.Model.UserModel;
import com.example.con_cu_tim.Model.UserNote;
import com.example.con_cu_tim.Util.UserUtil;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginDebug";
    private static final int RC_SIGN_IN = 100;

    private TextInputEditText inputEmail, inputPassword;
    private MaterialButton loginBtn;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String userJson = prefs.getString("user", null);

        if (userJson != null) {
            UserEntity userEntity = new Gson().fromJson(userJson, UserEntity.class);
            UserNote userNote = null;

            try {
                userNote = new Gson().fromJson(userEntity.getNote(), UserNote.class);
            } catch (Exception e) {
                Log.w(TAG, "❌ Note không hợp lệ, mặc định là chưa xem intro");
            }

            if (userNote == null || !userNote.getIntro()) {
                Log.d(TAG, "➡️ Điều hướng IntroActivity (user đã đăng nhập nhưng chưa xem intro)");
                startActivity(new Intent(this, IntroActivity.class));
            } else {
                Log.d(TAG, "➡️ Điều hướng AccountActivity (user đã xem intro)");
                startActivity(new Intent(this, AccountActivity.class));
            }

            finish();
            return;
        }


        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.editTextEmail);
        inputPassword = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(v -> loginUser());

        findViewById(R.id.registerBtn).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        findViewById(R.id.imgGoToRegister).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.btnGoogleSignIn).setOnClickListener(v -> {
            mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            });
        });
    }

    private void loginUser() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

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
                } else {
                    saveUserAndNavigate(userEntity);
                }
            });
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String name = account.getDisplayName();
                String email = account.getEmail();

                UserEntity user = new UserEntity();
                user.setFullNamel(name);
                user.setEmail(email);
                user.setStatus(1);
                user.setPassword("GOOGLE_LOGIN");

                new Thread(() -> {
                    if (UserDAO.getInstance().IsExistEmail(email)) {
                        UserDAO.getInstance().insertUser(user);
                    }

                    UserEntity userFromDb = UserDAO.getInstance().getUserByEmail(email);

                    runOnUiThread(() -> {
                        if (userFromDb != null) {
                            saveUserAndNavigate(userFromDb);
                            Toast.makeText(this, "Đăng nhập thành công với Google", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Không thể lấy thông tin user từ DB", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();

            } catch (ApiException e) {
                Log.e(TAG, "Google Sign-In failed", e);
                Toast.makeText(this, "Đăng nhập Google thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveUserAndNavigate(UserEntity userEntity) {
        Gson gson = new Gson();
        String userJson = gson.toJson(userEntity);

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        sharedPref.edit()
                .putString("user", userJson)
                .apply();

        // Đọc note từ JSON
        UserNote userNote = null;
        try {
            userNote = gson.fromJson(userEntity.getNote(), UserNote.class);
        } catch (Exception e) {
            Log.w(TAG, "❌ Note không phải định dạng JSON hợp lệ.");
        }

        if (userNote == null || !userNote.getIntro()) {
            Log.d(TAG, "➡️ Điều hướng: IntroActivity (intro=false hoặc lỗi JSON)");
            startActivity(new Intent(this, IntroActivity.class));
        } else {
            Log.d(TAG, "➡️ Điều hướng: AccountActivity (intro=true)");
            startActivity(new Intent(this, AccountActivity.class));
        }

        finish();
    }


}
