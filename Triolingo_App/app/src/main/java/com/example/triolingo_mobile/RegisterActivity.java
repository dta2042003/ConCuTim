package com.example.triolingo_mobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.*;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.example.triolingo_mobile.DAO.AccountDAO;
import com.example.triolingo_mobile.DAO.UserDAO;
import com.example.triolingo_mobile.Model.AccountModel;
import com.example.triolingo_mobile.Util.UserUtil;

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends AppCompatActivity {
    EditText email_txt;
    EditText name_txt;
    EditText password_txt;
    EditText confirm_pw_txt;
    Button registerBtn;
    ImageButton uploadImgBtn;
    ImageView avatarView;
    String avatarUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email_txt = findViewById(R.id.editTextEmail);
        name_txt = findViewById(R.id.editTextName);
        password_txt = findViewById(R.id.editTextPassword);
        confirm_pw_txt = findViewById(R.id.editTextPassword2);
        registerBtn = findViewById(R.id.registerBtn);
        uploadImgBtn = findViewById(R.id.uploadImageButton);
        avatarView = findViewById(R.id.avatarView);

        (findViewById(R.id.backBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkRegisterInfo()) {
                    String email = email_txt.getText().toString().trim();
                    String name = name_txt.getText().toString().trim();
                    String password = password_txt.getText().toString().trim();

                    // 🔒 Mã hóa mật khẩu bằng MD5
                    String hashedPassword = UserUtil.md5(password);

                    AccountModel newAccount = new AccountModel(
                            name,
                            email,
                            hashedPassword,
                            avatarUrl,
                            1,
                            null
                    );

                    if (AccountDAO.getInstance().registerAccount(newAccount)) {
                        Toast.makeText(view.getContext(), "Tạo tài khoản thành công! Vui lòng đăng nhập lại", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(view.getContext(), "Tạo tài khoản thất bại! Vui lòng thử lại sau", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        uploadImgBtn.setOnClickListener(this::onChangeImage);
        avatarView.setOnClickListener(this::onChangeImage);
    }

    void onChangeImage(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Chọn ảnh từ");
        builder.setItems(new CharSequence[]{"Chụp ảnh", "Chọn ảnh", "Xoá ảnh đã chọn"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 0);
                        break;
                    case 1:
                        Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent2, 1);
                        break;
                    case 2:
                        avatarUrl = null;
                        avatarView.setImageResource(R.mipmap.ic_launcher_round);
                        break;
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    avatarView.setImageBitmap(bitmap);
                    saveBase64Str(bitmap);
                    break;
                case 1:
                    Uri imageUri = data.getData();
                    avatarView.setImageURI(imageUri);
                    try {
                        saveBase64Str(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
                    } catch (Exception e) {
                        Toast.makeText(this, getString(R.string.upload_image_fail_message), Toast.LENGTH_SHORT).show();
                        avatarUrl = null;
                        avatarView.setImageResource(R.mipmap.ic_launcher_round);
                    }
                    break;
            }
        }
    }

    boolean checkRegisterInfo() {
        String email = email_txt.getText().toString().trim();
        String name = name_txt.getText().toString().trim();
        String password = password_txt.getText().toString();
        String confirmPw = confirm_pw_txt.getText().toString();

        // Email
        if (email.isEmpty()) {
            email_txt.setError("Email không được để trống");
            return false;
        }
        if (!UserDAO.getInstance().IsValidEmail(email)) {
            email_txt.setError("Email không hợp lệ");
            return false;
        }
        if (!UserDAO.getInstance().IsExistEmail(email)) {
            email_txt.setError("Email đã tồn tại");
            return false;
        }

        // Tên
        if (name.isEmpty()) {
            name_txt.setError("Tên không được để trống");
            return false;
        }
        if (!name.matches("^[\\p{L} ]+$")) { // chỉ chữ và khoảng trắng
            name_txt.setError("Tên chỉ được chứa chữ");
            return false;
        }

        // Mật khẩu
        if (password.isEmpty()) {
            password_txt.setError("Mật khẩu không được để trống");
            return false;
        }
        if (password.length() < 6) {
            password_txt.setError("Mật khẩu phải từ 6 ký tự");
            return false;
        }
        if (!password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*")) {
            password_txt.setError("Mật khẩu phải chứa cả chữ và số");
            return false;
        }

        // Xác nhận mật khẩu
        if (!confirmPw.equals(password)) {
            confirm_pw_txt.setError("Mật khẩu không khớp");
            return false;
        }

        return true;
    }


    void saveBase64Str(Bitmap bm) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        avatarUrl = Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }
}