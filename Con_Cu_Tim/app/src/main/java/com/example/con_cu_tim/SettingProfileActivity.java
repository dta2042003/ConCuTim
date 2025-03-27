package com.example.con_cu_tim;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.con_cu_tim.DAO.UserDAO;
import com.example.con_cu_tim.Model.UserEntity;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingProfileActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    EditText name, email;
    CircleImageView imageView;
    Bitmap imageBitmap;
    UserEntity user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);

        imageView = findViewById(R.id.imageview_account_profile);
        name = findViewById(R.id.editTextTextPersonName);
        email = findViewById(R.id.editTextTextEmailAddress);

        findViewById(R.id.btnClose).setOnClickListener(this::onQuit);
        findViewById(R.id.btnSave).setOnClickListener(this::onSave);
        findViewById(R.id.floatingActionButton).setOnClickListener(this::onEditImage);

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String json = sharedPref.getString("user", null);
        int userId = 0;

        if (json != null) {
            Gson gson = new Gson();
            UserEntity userLogin = gson.fromJson(json, UserEntity.class);
            userId = userLogin.getId();
        }

        int finalUserId = userId;
        new Thread(() -> {
            user = UserDAO.getInstance().GetUserById(finalUserId);

            if (user == null) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Không thể tải dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                    finish();
                });
                return;
            }

            Bitmap avatar = convertBase64ToBitmap(user.getAvatarUrl());

            runOnUiThread(() -> {
                name.setText(user.getFullNamel());
                email.setText(user.getEmail());
                imageView.setImageBitmap(avatar);
            });
        }).start();
    }

    private void onEditImage(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh từ");
        builder.setItems(new CharSequence[]{"Chụp ảnh", "Chọn ảnh"}, (dialog, which) -> {
            Intent intent;
            if (which == 0) {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            } else {
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });
        builder.show();
    }

    private void onSave(View view) {
        user.setFullNamel(name.getText().toString());
        user.setEmail(email.getText().toString());

        if (imageBitmap != null) {
            user.setAvatarUrl(convertToBase64(imageBitmap));
        }

        new Thread(() -> {
            int updated = UserDAO.getInstance().udpateUser(user);
            runOnUiThread(() -> {
                if (updated > 0) {
                    Toast.makeText(this, R.string.update_profile_user_sucess, Toast.LENGTH_SHORT).show();
                    onQuit(view);
                } else {
                    name.setError(getString(R.string.update_profile_user_fail));
                }
            });
        }).start();
    }

    private void onQuit(View view) {
        startActivity(new Intent(this, AccountActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data.getExtras() != null) {
                imageBitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(imageBitmap);
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                Uri imageUri = data.getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    imageView.setImageBitmap(imageBitmap);
                } catch (IOException e) {
                    Log.e("ImagePickError", "Failed to load image", e);
                }
            }
        }
    }

    private String convertToBase64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    private Bitmap convertBase64ToBitmap(String base64Str) {
        try {
            byte[] decoded = Base64.decode(base64Str, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
        } catch (Exception e) {
            Log.e("Base64ToBitmap", "Error: ", e);
            return null;
        }
    }
}
