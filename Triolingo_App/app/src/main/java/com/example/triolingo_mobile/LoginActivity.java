package com.example.triolingo_mobile;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.example.triolingo_mobile.DAO.UserDAO;
import com.example.triolingo_mobile.Model.UserEntity;
import com.example.triolingo_mobile.Model.UserModel;
import com.example.triolingo_mobile.Util.UserUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
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
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 100;

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

        findViewById(R.id.imgGoToRegister).setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.btnGoogleSignIn).setOnClickListener(v -> {
            mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
                // Sau khi sign out thành công, hiển thị giao diện chọn tài khoản
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            });
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                // Sau khi đăng nhập thành công
                String name = account.getDisplayName();
                String email = account.getEmail();

                // Gửi vào DB
                UserEntity user = new UserEntity();
                user.setFullNamel(name);
                user.setEmail(email);
                user.setStatus(1);
                user.setPassword("GOOGLE_LOGIN"); // ✅ add dòng này


                new Thread(() -> {
                    Log.d("GoogleLogin", "⏳ Bắt đầu kiểm tra email: " + email);

                    if (UserDAO.getInstance().IsExistEmail(email)) {
                        Log.d("GoogleLogin", "✅ Email chưa tồn tại, tiến hành insert user");
                        UserDAO.getInstance().insertUser(user);
                    } else {
                        Log.d("GoogleLogin", "⚠️ Email đã tồn tại, bỏ qua insert");
                    }

                    // Lấy lại user từ DB
                    UserEntity userFromDb = UserDAO.getInstance().getUserByEmail(email);

                    if (userFromDb != null) {
                        Log.d("GoogleLogin", "✅ Lấy user từ DB thành công: ID = " + userFromDb.getId());

                        // Lưu vào SharedPreferences
                        Gson gson = new Gson();
                        String userJson = gson.toJson(userFromDb);
                        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                        sharedPref.edit().putString("user", userJson).apply();

                        runOnUiThread(() -> {
                            Log.d("GoogleLogin", "🚀 Chuyển sang màn AccountActivity");
                            Toast.makeText(this, "Đăng nhập thành công với Google", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, AccountActivity.class));
                            finish();
                        });
                    } else {
                        Log.e("GoogleLogin", "❌ Không thể lấy thông tin user từ DB với email: " + email);
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Lỗi: Không thể lấy thông tin user sau đăng nhập Google", Toast.LENGTH_SHORT).show();
                        });
                    }
                }).start();



            } catch (ApiException e) {
                Log.e(TAG, "Google Sign-In failed: code=" + e.getStatusCode(), e);
                Toast.makeText(this, "Google Sign-In failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
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